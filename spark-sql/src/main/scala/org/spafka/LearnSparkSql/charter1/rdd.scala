package org.spafka.LearnSparkSql.charter1

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.spafka.LearnSparkSql.Common
import org.spafka.LearnSparkSql.charter1.UdfExample.{CancerClass, spark}

object rdd extends App {


  private val spark: SparkSession = Common.spark

  // Must import this 2 implict rdd 2 df(s)


  private val sc: SparkContext = spark.sparkContext

  val cancerRDD = sc.textFile("spark-sql/src/main/resources/breast-cancer-wisconsin.data", 4)
  cancerRDD.partitions.size


  import spark.implicits._
  val cancerDF = cancerRDD.toDF()

  cancerDF.show()

  import org.apache.spark.sql.Row
  def row(line: List[String]): Row = { Row(line(0).toLong, line(1).toInt, line(2).toInt, line(3).toInt, line(4).toInt, line(5).toInt, line(6).toInt, line(7).toInt, line(8).toInt, line(9).toInt, line(10).toInt) }
  val data = cancerRDD.map(_.split(",").to[List]).map(row)


  val recordSchema = new StructType()
    .add("sample", "long")
    .add("cThick", "integer")
    .add("uCSize", "integer")
    .add("uCShape", "integer")
    .add("mAdhes", "integer")
    .add("sECSize", "integer")
    .add("bNuc", "integer").add("bChrom", "integer")
    .add("nNuc", "integer").add("mitosis", "integer")
    .add("clas", "integer")

  val cancerDF2 = spark.createDataFrame(data, recordSchema)

  cancerDF2.show()


  val cancerDS = cancerDF2.as[CancerClass]
  cancerDS.show()


  spark.stop()
}
