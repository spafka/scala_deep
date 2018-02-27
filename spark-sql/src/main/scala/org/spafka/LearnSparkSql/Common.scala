package org.spafka.LearnSparkSql

import org.apache.spark.sql.SparkSession
import org.spafka.LearnSparkSql.charter1.UdfExample.spark

object Common {

  implicit val master = "local[*]"

  val spark = {
    val master = Common.master

    val spark = SparkSession
      .builder()
      .master(master)
      .appName("Spark SQL DataFrame example")
      .getOrCreate()
    spark.conf.set("spark.executor.cores", "2")
    spark.conf.set("spark.executor.memory", "4g")
    // Must import this 2 implict rdd 2 df(s)
    import spark.implicits._
    spark
  }

}
