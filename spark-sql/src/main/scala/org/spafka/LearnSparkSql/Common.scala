package org.spafka.LearnSparkSql

import org.apache.spark.sql.SparkSession

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
    spark
  }

  def benchmark(name: String)(f: => Unit) {
    val startTime = System.nanoTime
    f
    val endTime = System.nanoTime
    println(s"Time taken in $name: " + (endTime - startTime).toDouble / 1000000000 + "seconds")
  }


}
