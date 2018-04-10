package org.spafka.sql.chapter1

import org.apache.spark.SparkContext
import org.apache.spark.sql.{Encoders, SparkSession}
import org.apache.spark.sql.types.StructType
import org.spafka.sql.Common
import org.spafka.sql.chapter1.DfExample.Canser
import org.spafka.sql.chapter1.UdfExample.{CancerClass, spark}

object rdd extends App {


  private val spark: SparkSession = Common.spark

  // Must import this 2 implict rdd 2 df(s)


  private val sc: SparkContext = spark.sparkContext

  val cancerRDD = sc.textFile("spark-sql-streaming/src/main/resources/breast-cancer-wisconsin.data", 4)
  cancerRDD.partitions.size


  import spark.implicits._
  val cancerDF = cancerRDD.toDF()

  cancerDF.show()

  import org.apache.spark.sql.Row
  def row(line: List[String]): Row = { Row(line(0).toLong, line(1).toInt, line(2).toInt, line(3).toInt, line(4).toInt, line(5).toInt, line(6).toInt, line(7).toInt, line(8).toInt, line(9).toInt, line(10).toInt) }
  val data = cancerRDD.map(_.split(",").to[List]).map(row)


  val recordSchema =  Encoders.product[Canser].schema

  val cancerDF2 = spark.createDataFrame(data, recordSchema)

  cancerDF2.show()


  val cancerDS = cancerDF2.as[CancerClass]
  cancerDS.show()


  spark.stop()
}
