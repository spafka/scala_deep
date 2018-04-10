package org.spafka.sql.streaming.kafka010

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object pdfDemo {


  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("strucedStreaming").setMaster("local[2]")
    val spark = SparkSession.builder.config(conf).getOrCreate

    import spark.implicits._


   //@See KafkaSourceProvider
   // Extract
    val records = spark.
      readStream.
      format("kafka").
      option("subscribe", "kafka"). // <-- topics with a digit at the end
      option("kafka.bootstrap.servers", "localhost:9093,localhost:9094").
      option("startingoffsets", "earliest"). // 读取的offset位置 也可以指定offset "topicA":{"part":offset,"p1":-1},"topicB":{"0":-2}}
      option("maxOffsetsPerTrigger", 10). // 每次拉取得最大数量
     load

    records.printSchema()
    // Transform
    val result = records.
      select(
        $"key" cast "string",   // deserialize keys
        $"value" cast "string", // deserialize values
        $"topic",
        $"partition",
        $"offset")
    // Load
    import org.apache.spark.sql.streaming.{OutputMode, Trigger}



    import scala.concurrent.duration._
    val sq = result.
      writeStream.
      format("console").
      option("checkpointLocation","/tmp/ck").
      // option("truncate", false).
      trigger(Trigger.ProcessingTime(10.seconds)).
      outputMode(OutputMode.Append).
      queryName("from-kafka-to-console").
      start


    // In the end, stop the streaming query

    sq.awaitTermination()
    sq.stop
  }
}
