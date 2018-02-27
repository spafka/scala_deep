package org.spafka.LearnSparkSql.charter1

import org.apache.spark.sql.SparkSession
import org.spafka.LearnSparkSql.Common

object DfExample {


  def main(args: Array[String]): Unit = {


    val master = Common.master

    val spark = SparkSession
      .builder()
      .master(master)
      .appName("Spark SQL DataFrame example")
      .getOrCreate()

    import org.apache.spark.sql.types._
    spark.conf.set("spark.executor.cores", "2")
    spark.conf.set("spark.executor.memory", "4g")

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
    //Replace directory for the input file with location of the file on your machine.
    val df = spark
      .read
      .format("csv")
      .option("header", false)
      .schema(recordSchema)
      .load("spark-sql/src/main/resources/breast-cancer-wisconsin.data")
    df.show(20)

    df.createOrReplaceTempView("cancerTable")
    val sqlDF = spark.sql("SELECT sample, bNuc from cancerTable")
    sqlDF.show()

    spark.stop()

  }
}
