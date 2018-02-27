package org.spafka.LearnSparkSql.charter1

import org.apache.spark.sql.SparkSession
import org.spafka.LearnSparkSql.Common

object UdfExample extends App{


  val master = Common.master


  val spark = SparkSession
    .builder()
    .master(master)
    .appName("Spark SQL UDF example")
    .getOrCreate()


  // Must import this 2 implict rdd 2 df(s)
  import spark.implicits._

  spark.conf.set("spark.executor.cores", "2")
  spark.conf.set("spark.executor.memory", "4g")

  case class CancerClass(sample: Long, cThick: Int, uCSize: Int, uCShape: Int, mAdhes: Int, sECSize: Int, bNuc: Int, bChrom: Int, nNuc: Int, mitosis: Int, clas: Int)
  //Replace directory for the input file with location of the file on your machine.
  val cancerDS = spark
    .sparkContext
    .textFile("spark-sql/src/main/resources/breast-cancer-wisconsin.data")
    .map(_.split(","))
    .map(attributes => CancerClass(attributes(0).trim.toLong, attributes(1).trim.toInt, attributes(2).trim.toInt, attributes(3).trim.toInt, attributes(4).trim.toInt, attributes(5).trim.toInt, attributes(6).trim.toInt, attributes(7).trim.toInt, attributes(8).trim.toInt, attributes(9).trim.toInt, attributes(10).trim.toInt))
    .toDS()

  cancerDS.registerTempTable("cancerTable")

  // define an udf
  def binarize(s: Int): Int = s match {case 2 => 0 case 4 => 1 }
  spark.udf.register("udfValueToCategory", (arg: Int) => binarize(arg))
  val sqlUDF = spark.sql("SELECT *, udfValueToCategory(clas) from cancerTable")
  sqlUDF.show()



  spark.catalog.listDatabases.show()


  spark.stop()


}
