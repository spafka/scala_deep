package org.spafka.LearnSparkSql.chapter1

import org.spafka.LearnSparkSql.Common


/**
  *  Tungsten test using join
  */
object Tungsten extends App {


  case class PinTrans(bidid: String, timestamp: String, ipinyouid: String,
                      useragent: String, IP: String, region: String, city: String, adexchange: String,
                      domain: String, url: String, urlid: String, slotid: String, slotwidth: String,
                      slotheight: String, slotvisibility: String, slotformat: String, slotprice: String,
                      creative: String, bidprice: String)

  case class PinRegion(region: String, regionName: String)

  val spark = Common.spark

  import spark.implicits._

  val pintransDF =
    spark.sparkContext.textFile("file:////Users/spafka/Downloads/leaderboard.test.data.20130318_20.txt").map(_.split("\t")).map(attributes
    => PinTrans(attributes(0).trim, attributes(1).trim, attributes(2).trim,
        attributes(3).trim, attributes(4).trim, attributes(5).trim, attributes(6).trim,
        attributes(7).trim, attributes(8).trim, attributes(9).trim, attributes(10).trim,
        attributes(11).trim, attributes(12).trim, attributes(13).trim, attributes(14).trim,
        attributes(15).trim, attributes(16).trim, attributes(17).trim,
        attributes(18).trim)).toDF()


  val pinregionDF =
    spark.sparkContext.textFile("file:///Users/spafka/Downloads/region.en.txt").map(_.split("\t")).map(attributes
    =>
      PinRegion(attributes(0).trim, attributes(1).trim)).toDF()


  spark.conf.set("spark.sql.codegen.wholeStage", false)

  Common.benchmark("Spark 1.6") {
    pintransDF.join(pinregionDF, "region").count()
    pintransDF.join(pinregionDF, "region").selectExpr("count(*)").explain(true)
    // Time taken in Spark 1.6: 9.030012286seconds
  }

  spark.conf.set("spark.memory.offHeap.enabled","true")
  spark.conf.set("spark.sql.codegen.wholeStage", true)
  spark.conf.set("spark.memory.offHeap.size ","48m")


  Common.benchmark("Spark 2.*") {
    pintransDF.join(pinregionDF, "region").count()
    pintransDF.join(pinregionDF, "region").selectExpr("count(*)").explain(true)
    // Time taken in Spark 2.*: 3.929619014seconds
  }



  spark.stop()
}
