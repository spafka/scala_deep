package org.spafka.streaming.kafka

import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

import com.typesafe.scalalogging.Logger
import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.clients.consumer.ConsumerRecord
import scalikejdbc._


class EosMysql


/**
  * http://shzhangji.com/cnblogs/2017/08/01/how-to-achieve-exactly-once-semantics-in-spark-streaming/
  */
object EosMysql {

  private val logger: Logger = Logger[EosMysql]
  def main(args: Array[String]): Unit = {


    StreamingExamples.setStreamingLogLevels()

    val brokers = "localhost:9092,localhost:9093,localhost:9094"
    val topic = "alog"

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "exactly-once",
      "enable.auto.commit" -> (false: java.lang.Boolean),
      "auto.offset.reset" -> "none")

    ConnectionPool.singleton("jdbc:mysql://localhost:3306/spark", "root", "root")

    val conf = new SparkConf().setAppName("ExactlyOnce").setIfMissing("spark.master", "local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))

    val fromOffsets: Map[TopicPartition, Long] = DB.readOnly { implicit session =>
      sql"""
      select `partition`, offset from kafka_offset
      where topic = ${topic}
      """.map { rs =>
        new TopicPartition(topic, rs.int("partition")) -> rs.long("offset")
      }.list.apply().toMap
    }

    println(s"offset =>${Thread.currentThread().getName} ${fromOffsets}")

    val messages = KafkaUtils.createDirectStream[String, String](ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Assign[String, String](fromOffsets.keys, kafkaParams, fromOffsets))

    messages.foreachRDD { rdd =>
      val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

      val result = processLogs(rdd).collect()

      println(s"result=> ${Thread.currentThread().getName} ${result}")

      DB.localTx { implicit session =>
        result.foreach { case (time, count) =>
          sql"""
          insert into error_log (log_time, log_count)
          value (${time}, ${count})
          on duplicate key update log_count = log_count + values(log_count)
          """.update.apply()
        }

        offsetRanges.foreach { offsetRange =>
          val value =
            sql"""
          update kafka_offset set offset = ${offsetRange.untilOffset}
          where topic = ${topic} and `partition` = ${offsetRange.partition}
          and offset = ${offsetRange.fromOffset}
          """
          val affectedRows = value
            .update.apply()

          if (affectedRows != 1) {
            throw new SparkException("",null);
          }
        }
      }
    }

    ssc.start()

    //start() 将在幕后启动 JobScheduler, 进而启动 JobGenerator 和 ReceiverTracker
    // ssc.start()
    //    -> JobScheduler.start()
    //        -> JobGenerator.start();    开始不断生成一个一个 batch
    //        -> ReceiverTracker.start(); 开始往 executor 上分布 ReceiverSupervisor 了，也会进一步创建和启动 Receiver
    ssc.awaitTermination()

    // 然后用户 code 主线程就 block 在下面这行代码了
    // block 的后果就是，后台的 JobScheduler 线程周而复始的产生一个一个 batch 而不停息
    // 也就是在这里，我们前面静态定义的 DStreamGraph 的 print()，才一次一次被在 RDD 实例上调用，一次一次打印出当前 batch 的结果
    ssc.awaitTermination()
  }

  def processLogs(messages: RDD[ConsumerRecord[String, String]]): RDD[(LocalDateTime, Int)] = {
    messages.map(x=>{

     // println(x)
      x.value
    })
      .flatMap(parseLog)
      .filter(_.level == "ERROR")
      .map(log => log.time.truncatedTo(ChronoUnit.MINUTES) -> 1)
      .reduceByKey(_ + _)
  }

  case class Log(time: LocalDateTime, level: String)

  val logPattern = "^(.{19}) ([A-Z]+).*".r
  val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  def parseLog(line: String): Option[Log] = {
    line match {
      case logPattern(timeString, level) => {
        val timeOption = try {
          Some(LocalDateTime.parse(timeString, dateTimeFormatter))
        } catch {
          case _: DateTimeParseException => None
        }
        timeOption.map(Log(_, level))
      }
      case _ => {
        None
      }
    }
  }
}
