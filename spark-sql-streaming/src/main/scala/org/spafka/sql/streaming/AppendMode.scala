package org.spafka.sql.streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.streaming.Trigger

object AppendMode {

  def main(args: Array[String]): Unit = {

    Logger.getRootLogger.setLevel(Level.WARN)

    import org.apache.spark.sql.SparkSession

    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()


    import spark.implicits._

    // Create DataFrame representing the stream of input lines from connection to localhost:9999
    val lines = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()

    // Split the lines into words
    val words = lines.as[String].flatMap(_.split(" "))

    // Generate running word count
    val wordCounts: DataFrame = words.groupBy("value").count()

    /**
      * Specifies how data of a streaming DataFrame/Dataset is written to a streaming sink.
      *   - `append`:   only the new rows in the streaming DataFrame/Dataset will be written to
      * the sink
      *   - `complete`: all the rows in the streaming DataFrame/Dataset will be written to the sink
      * every time these is some updates
      *   - `update`:   only the rows that were updated in the streaming DataFrame/Dataset will
      * be written to the sink every time there are some updates. If the query doesn't
      * contain aggregations, it will be equivalent to `append` mode.
      *
      * @since 2.0.0
      */
    // Start running the query that prints the running counts to the consolee
    //    val query = wordCounts.writeStream
    //      .outputMode("append")
    //      .format("console")
    //      .start()

    // Default trigger (runs micro-batch as soon as it can)
    //    wordCounts.writeStream
    //      .format("console")
    //      .start()
    //
    //    // ProcessingTime trigger with two-seconds micro-batch interval
    //    val query = wordCounts.writeStream
    //      .format("console")
    //        .outputMode("complete")
    //      .trigger(Trigger.ProcessingTime("2 seconds"))
    //      .start()
    //
    //    // One-time trigger 只处理一次
    //    val query = wordCounts.writeStream
    //      .format("console")
    //      .outputMode("complete")
    //      .trigger(Trigger.Once())
    //      .start()

    // Continuous trigger with one-second checkpointing interval
    val query = wordCounts.writeStream
      .format("console")
      .outputMode("update")
      .trigger(Trigger.Continuous("1 second"))
      .start()

    query.awaitTermination()

    query.stop()
  }

}
