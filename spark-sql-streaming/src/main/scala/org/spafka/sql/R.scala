package org.spafka.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

object R {

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("example")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext
    import spark.implicits._

    val pairsDF: DataFrame = Seq((1, 0), (2, 0), (0, 0), (3, 0)).toDF("a", "b")

    val sortedDF = pairsDF.sort('a)
    val rows = sortedDF.collect()

    rows

    Thread.sleep(Long.MaxValue)

  }

}
