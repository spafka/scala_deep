package org.spafka.LearnSparkSql.chapter2

import org.apache.spark
import org.spafka.LearnSparkSql.Common

object json {

  def main(args: Array[String]): Unit = {

    val spark = Common.spark

    import spark.implicits._
    val reviewsDF = spark.read.json("spark-sql/src/main/scala/org/spafka/LearnSparkSql/chapter2/people.json")
    reviewsDF.printSchema()
    reviewsDF.createOrReplaceTempView("people")
    val selectedDF = spark.sql("select * from people where people.age >3")
    selectedDF.show()

   // selectedDF.coalesce(1).write.save("spark-sql/src/main/scala/org/spafka/LearnSparkSql/chapter2/people")


    val parquetDf = spark.read.parquet("spark-sql/src/main/scala/org/spafka/LearnSparkSql/chapter2/people")

    parquetDf.printSchema()

    spark.stop()
  }
}
