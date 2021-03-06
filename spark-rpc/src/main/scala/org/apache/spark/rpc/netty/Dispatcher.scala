package org.apache.spark.rpc.netty

import java.util.concurrent._

import javax.annotation.concurrent.GuardedBy
import org.apache.spark.RpcException
import org.apache.spark.network.client.RpcResponseCallback
import org.apache.spark.rpc.{RpcEndpoint, RpcEndpointAddress, RpcEndpointRef, RpcEnvStoppedException}
import org.apache.spark.util.ThreadUtils
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.concurrent.Promise
import scala.util.control.NonFatal

/**
  * A message dispatcher, responsible for routing RPC messages to the appropriate endpoint(s).
  *
  * serve才有  消息调度器，负责将RPC消息路由到适当的RpcEndpoint。
  */
private[netty] class Dispatcher(nettyEnv: NettyRpcEnv) {

  private val log = LoggerFactory.getLogger(classOf[Dispatcher])

  private class EndpointData(val name: String,
                              val endpoint: RpcEndpoint,
                              val ref: NettyRpcEndpointRef) {
    val inbox = new Inbox(ref, endpoint)
  }

  // 维护一个HaskMap，保存Name与EndpointData的关系
  private val endpoints = new ConcurrentHashMap[String, EndpointData]
  // 维护一个HaskMap，保存RpcEndpoint与RpcEndpointRef的关系
  private val endpointRefs = new ConcurrentHashMap[RpcEndpoint, RpcEndpointRef]

  // Track the receivers whose inboxes may contain messages.
  //维护一个BlockingQueue的队列，用于保存拥有消息的EndpointData，注册Endpoint、
  //发送消息时、停止RpcEnv时、取消注册的Endpoint时，会在receivers中添加相应的EndpointData
  private val receivers = new LinkedBlockingQueue[EndpointData]

  /**
    * True if the dispatcher has been stopped. Once stopped, all messages posted will be bounced
    * immediately.
    */
  @GuardedBy("this")
  private var stopped = false

  def registerRpcEndpoint(name: String, endpoint: RpcEndpoint): NettyRpcEndpointRef = {
    val addr = RpcEndpointAddress(nettyEnv.address, name)
    log trace (s"Server registerRpcEndpoint  an endpoint ${nettyEnv.address} -> ${name}")
    val endpointRef = new NettyRpcEndpointRef(nettyEnv.conf, addr, nettyEnv)
    synchronized {
      if (stopped) {
        throw new IllegalStateException("RpcEnv has been stopped")
      }
      if (endpoints.putIfAbsent(name, new EndpointData(name, endpoint, endpointRef)) != null) {
        throw new IllegalArgumentException(s"There is already an RpcEndpoint called $name")
      }
      val data: EndpointData = endpoints.get(name)
      endpointRefs.put(data.endpoint, data.ref)

      receivers.offer(data) // for the OnStart message
      log.trace("recieves queen add an endpoint data {},{},{},{}",data,data.name,data.ref.client,data.inbox)
    }
    endpointRef
  }

  def getRpcEndpointRef(endpoint: RpcEndpoint): RpcEndpointRef = endpointRefs.get(endpoint)

  def removeRpcEndpointRef(endpoint: RpcEndpoint): Unit = endpointRefs.remove(endpoint)

  // Should be idempotent
  private def unregisterRpcEndpoint(name: String): Unit = {
    val data = endpoints.remove(name)
    if (data != null) {
      data.inbox.stop()
      receivers.offer(data) // for the OnStop message
    }
    // Don't clean `endpointRefs` here because it's possible that some messages are being processed
    // now and they can use `getRpcEndpointRef`. So `endpointRefs` will be cleaned in Inbox via
    // `removeRpcEndpointRef`.
  }

  def stop(rpcEndpointRef: RpcEndpointRef): Unit = {
    synchronized {
      if (stopped) {
        // This endpoint will be stopped by Dispatcher.stop() method.
        return
      }
      unregisterRpcEndpoint(rpcEndpointRef.name)
    }
  }

  /**
    * Send a message to all registered [[RpcEndpoint]]s in this process.
    *
    * This can be used to make network events known to all end points (e.g. "a new node connected").
    */
  def postToAll(message: InboxMessage): Unit = {
    val iter = endpoints.keySet().iterator()
    while (iter.hasNext) {
      val name = iter.next
      postMessage(name, message, (e) => log.warn(s"Message $message dropped. ${e.getMessage}"))
    }
  }

  /** Posts a message sent by a remote endpoint. */
  // todo 发布一个由远端endpoint发送的消息
  def postRemoteMessage(message: RequestMessage, callback: RpcResponseCallback): Unit = {
    val rpcCallContext =
      new RemoteNettyRpcCallContext(nettyEnv, callback, message.senderAddress)
    val rpcMessage = RpcMessage(message.senderAddress, message.content, rpcCallContext)
    postMessage(message.receiver.name, rpcMessage, (e) => callback.onFailure(e))
  }

  /** Posts a message sent by a local endpoint. */
  def postLocalMessage(message: RequestMessage, p: Promise[Any]): Unit = {
    val rpcCallContext =
      new LocalNettyRpcCallContext(message.senderAddress, p)
    val rpcMessage = RpcMessage(message.senderAddress, message.content, rpcCallContext)
    postMessage(message.receiver.name, rpcMessage, (e) => p.tryFailure(e))
  }

  /** Posts a one-way message. */
  def postOneWayMessage(message: RequestMessage): Unit = {
    postMessage(message.receiver.name, OneWayMessage(message.senderAddress, message.content),
      (e) => throw e)
  }

  /**
    * Posts a message to a specific endpoint.
    *
    * @param endpointName      name of the endpoint.
    * @param message           the message to post
    * @param callbackIfStopped callback java.java.util.function if the endpoint is stopped.
    *
    *                          根据上面的代码可以看出，Dispatcher在进行Message分发到相应的Endpoint进行处理时，实际上是将Message分发到endpointData中进行处理了
    *                          ，而EndpointData类中最重要的成员就是inbox，下面介绍Inbox。
    *
    */
  private def postMessage(
                           endpointName: String,
                           message: InboxMessage,
                           callbackIfStopped: (Exception) => Unit): Unit = {
    val error = synchronized {
      val data : EndpointData = endpoints.get(endpointName)
      if (stopped) {
        Some(new RpcEnvStoppedException())
      } else if (data == null) {
        Some(new RpcException(s"Could not find $endpointName."))
      } else {
        data.inbox.post(message)
        receivers.offer(data)
        None
      }
    }
    // We don't need to call `onStop` in the `synchronized` block
    error.foreach(callbackIfStopped)
  }

  def stop(): Unit = {
    synchronized {
      if (stopped) {
        return
      }
      stopped = true
    }
    // Stop all endpoints. This will queue all endpoints for processing by the message loops.
    endpoints.keySet().asScala.foreach(unregisterRpcEndpoint)
    // Enqueue a message that tells the message loops to stop.
    receivers.offer(PoisonPill)
    threadpool.shutdown()
  }

  def awaitTermination(): Unit = {
    threadpool.awaitTermination(Long.MaxValue, TimeUnit.MILLISECONDS)
  }

  /**
    * Return if the endpoint exists
    */
  def verify(name: String): Boolean = {
    endpoints.containsKey(name)
  }

  /** Thread pool used for dispatching messages. */
  private val threadpool: ThreadPoolExecutor = {
    val numThreads = nettyEnv.conf.getInt("spark.rpc.netty.dispatcher.numThreads",
      math.max(2, Runtime.getRuntime.availableProcessors()))
    val pool = ThreadUtils.newDaemonFixedThreadPool(numThreads, "dispatcher-event-loop")
    for (i <- 0 until numThreads) {
      pool.execute(new MessageLoop)
    }
    log trace (s"1. server's dispatcher start a daemon threadpool with ${numThreads} thread to process messageloop ")
    pool
  }

  /** Message loop used for dispatching messages. */
  private class MessageLoop extends Runnable {
    override def run(): Unit = {
      try {
        while (true) {
          try {
            val data = receivers.take()
            log.trace("endpointdata queen take and data is {},{},{},{}",data.name,data.endpoint,data.ref,data.inbox)

            if (data == PoisonPill) {
              // Put PoisonPill back so that other MessageLoops can see it.
              log.trace(s"before stop call the posionHook ${data} ")
              receivers.offer(PoisonPill)
              return
            }

            data.inbox.process(Dispatcher.this)
          } catch {
            case NonFatal(e) => log.error(e.getMessage, e)
          }
        }
      } catch {
        case ie: InterruptedException => // exit
      }
    }
  }

  /** A poison endpoint that indicates MessageLoop should exit its message loop. */
  private val PoisonPill = new EndpointData(null, null, null)
}
