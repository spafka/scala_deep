package org.apache.spafka.sql

import org.apache.spark.sql.Dataset

object PeopleDemo {

  // $example on:create_ds$
  case class Person(name: String, age: Int)
  // $example off:create_ds$

  def main(args: Array[String]): Unit = {

    import org.apache.spark.sql.SparkSession

    val spark = SparkSession
      .builder()
      .appName("Spark SQL Example")
      .master("local[*]")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    // For implicit conversions like converting RDDs to DataFrames
    import spark.implicits._
    val ds: Dataset[Person] = spark.sparkContext
      .textFile("spark-sql/src/main/resources/people.txt")
      .map(_.split(","))
      .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
      .toDS()
    ds.createOrReplaceTempView("people")
    val teenagersDF = spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19")

    //    import org.apache.spark.sql.execution.debug._
    //    teenagersDF.debug()
    //    teenagersDF.debugCodegen()

    teenagersDF.show()


    Thread.sleep(Int.MaxValue)
  }



}
