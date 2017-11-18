package org.apache.spark.rpc

import org.apache.spark.RpcConf
import org.apache.spark.rpc.netty.NettyRpcEnvFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


object HelloworldClient {

  def main(args: Array[String]): Unit = {
    //asyncCall()
    syncCall()
  }

  def asyncCall() = {
    val rpcConf = new RpcConf()
    val config = RpcEnvClientConfig(rpcConf, "hello-client")
    val rpcEnv: RpcEnv = NettyRpcEnvFactory.create(config)
    val endPointRef: RpcEndpointRef = rpcEnv.setupEndpointRef(RpcAddress("localhost", 52345), "hello-service")
    val future: Future[String] = endPointRef.ask[String](SayHi("spafka"))
    future.onComplete {
      case scala.util.Success(value) => println(s"Got the result = $value")
      case scala.util.Failure(e) => println(s"Got error: $e")
    }
    Await.result(future, Duration.apply("30s"))

    //  val eventualBoolean = endPointRef.ask[Boolean](RemoteProcessDisconnected(RpcAddress("localhost", 52345)))
    //    print(eventualBoolean)

  }

  def syncCall() = {
    val rpcConf = new RpcConf()
    val config = RpcEnvClientConfig(rpcConf, "hello-client")
    val rpcEnv: RpcEnv = NettyRpcEnvFactory.create(config)
    val endPointRef: RpcEndpointRef = rpcEnv.setupEndpointRef(RpcAddress("localhost", 52345), "hello-service")
    val result = endPointRef.askWithRetry[String](SayBye("neo"))
    println(result)


    //    val eventualBoolean = endPointRef.ask[Boolean](RemoteProcessDisconnected(RpcAddress("localhost", 52345)))
    //    print(eventualBoolean)
  }
}