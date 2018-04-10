package org.spafka.sql.chapter1

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Encoders, SparkSession}
import org.spafka.sql.Common

object DfExample {

  case class Canser(sample: String, cThick: Int, uCSize: Int, uCShape: Int,
                    mAdhes: Int, sECSize: Int, bNuc: Int, bChrom: Int,
                    nNuc: Int, mitosis: Int, clas: Int)

  def main(args: Array[String]): Unit = {


    val spark = SparkSession
      .builder()
//      .master("local[*]")
      .appName("Spark SQL DataFrame example")
      .getOrCreate()
    spark.conf.set("spark.executor.cores", "2")
    spark.conf.set("spark.executor.memory", "4g")


    // 手动获取schema
    val recordSchema =  Encoders.product[Canser].schema
    Encoders.product[(Long, Long)]

    //Replace directory for the input file with location of the file on your machine.
    val df = spark
      .read
      .format("csv")
      .option("header", false)
      .schema(recordSchema)
      .load("file:///Users/spafka/Desktop/flink/spark_deep/spark-sql-streaming/src/main/resources/breast-cancer-wisconsin.data")
    df.show(20)

    df.createOrReplaceTempView("cancerTable")
    val sqlDF = spark.sql("SELECT sample, bNuc from cancerTable")
    sqlDF.show()

    spark.stop()

  }
}
