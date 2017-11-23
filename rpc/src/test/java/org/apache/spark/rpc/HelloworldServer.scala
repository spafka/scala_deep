package org.apache.spark.rpc

import org.apache.spark.RpcConf
import org.apache.spark.rpc.netty.NettyRpcEnvFactory

object HelloworldServer {

  def main(args: Array[String]): Unit = {
    //val host = args(0)
    val host = "localhost"
    val config = RpcEnvServerConfig(new RpcConf(), "hello-server", host, 52345)
    // 服务端绑定端口，开启服务
    val rpcEnv: RpcEnv = NettyRpcEnvFactory.create(config)

    println("successful start server ")

    val helloEndpoint: RpcEndpoint = new HelloEndpoint(rpcEnv)
    rpcEnv.setupEndpoint("hello-service", helloEndpoint)
    rpcEnv.awaitTermination()
  }
}

class HelloEndpoint(override val rpcEnv: RpcEnv) extends RpcEndpoint {

  override def onStart(): Unit = {
    println("start hello endpoint")
  }

  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case SayHi(msg) => {
      println(s"receive $msg")
      context.reply(s"$msg")
    }
    case SayBye(msg) => {
      println(s"receive $msg")
      context.reply(s"bye, $msg")
    }
  }

  override def onStop(): Unit = {
    println("stop hello endpoint")

  }

  override def onDisconnected(remoteAddress: RpcAddress): Unit = {
    print("onDisconnected")
  }

}


case class SayHi(msg: String)

case class SayBye(msg: String)