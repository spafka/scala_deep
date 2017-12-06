package org.spafka.kafka.broker

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean


/**
  * Created by shadow on 2017/6/25 0025.
  */
case class EndPoint(val host: String, val port: Int) {}

class Kserver {

  private val startupComplete = new AtomicBoolean(false)
  private val isShuttingDown = new AtomicBoolean(false)
  private val isStartingUp = new AtomicBoolean(false)
  private var shutdownLatch = new CountDownLatch(1)

}

object Kserver {

  private val endPoint = EndPoint("127.0.0.1", 9200)

  private val processors = new Array[Processor](3)
  for (i <- 1 to 3) {
    val processor = new Processor(i)
    processors(i-1) = processor
  }

  private val acceptor = new Acceptor(endPoint, 1024, 1024, processors)

  def startUp(): Unit = {

    new Thread(acceptor, "acceptor").start()
    acceptor.awaitStartup()

  }

  def main(args: Array[String]): Unit = {
    startUp()
  }

}
