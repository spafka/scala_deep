package org.spafka.boot

import org.spafka.kafka.broker.Kserver.startUp


object Server {

  def main(args: Array[String]): Unit = {
    startUp()
  }
}
