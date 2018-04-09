package org.spafka.sql.chapter1

import org.spafka.sql.Common

object Streaming {

  def main(args: Array[String]): Unit = {


    val spark = Common.spark
    import org.apache.spark.sql.types._
    import org.apache.spark.sql.functions._
    import scala.concurrent.duration._
    import org.apache.spark.sql.streaming.ProcessingTime
    import org.apache.spark.sql.streaming.OutputMode.Complete
    import spark.implicits._

    val bidSchema = new StructType()
      .add("bidid", StringType)
      .add("timestamp", StringType)
      .add("ipinyouid", StringType)
      .add("useragent", StringType)
      .add("IP", StringType)
      .add("region", IntegerType)
      .add("city", IntegerType)
      .add("adexchange", StringType)
      .add("domain", StringType)
      .add("url:String", StringType)
      .add("urlid: String", StringType)
      .add("slotid: String", StringType)
      .add("slotwidth", StringType)
      .add("slotheight", StringType)
      .add("slotvisibility", StringType)
      .add("slotformat", StringType)
      .add("slotprice", StringType)
      .add("creative", StringType)
      .add("bidprice", StringType)


    val streamingInputDF = spark
      .readStream.format("csv")
      .schema(bidSchema)
      .option("header", false)
      .option("inferSchema", true)
      .option("sep", "\t")
      .option("maxFilesPerTrigger", 1)
      .load("file:////Users/spafka/Downloads/leaderboard.test.data.20130318_20.txt")
    val streamingCountsDF = streamingInputDF.groupBy($"city").count()
    val query = streamingCountsDF.writeStream.format("console")
      .trigger(ProcessingTime(20.seconds))
      .queryName("counts")
      .outputMode(Complete).start()
    spark
      .streams
      .active
      .foreach(println)
    //Execute the following stop() method after you have executed the code in the next section (otherwise you will not see results of the code in the next section)
    query.stop()

    //Code for Understanding Structured Streaming Internals section
    spark.streams.active(0).explain

  }
}
