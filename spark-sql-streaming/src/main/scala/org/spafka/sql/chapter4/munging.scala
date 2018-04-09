package org.spafka.sql.chapter4

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.spafka.sql.Common

case class HouseholdEPC(date: String, time: String, gap: Double, grp: Double, voltage: Double, gi: Double, sm_1: Double, sm_2: Double, sm_3: Double)
case class HouseholdEPCDTmDay(date: String, day: String, month: String, year: String, dgap: Double, dgrp: Double, dvoltage: Double, dgi: Double, dsm_1: Double, dsm_2: Double, dsm_3: Double)

//数据整理Data
object munging {


  def main(args: Array[String]): Unit = {
    val spark: SparkSession = Common.spark
    val sc: SparkContext = spark.sparkContext

    //Code for Chapter 4 to be executed in Spark spark-shell
    import org.apache.spark.sql.functions.{from_unixtime, unix_timestamp, _}
    import spark.implicits._

    //Code for Pre-processing of the household electric consumption dataset sectoion
   val hhEPCRdd = sc.textFile("spark-sql/src/main/scala/org/spafka/LearnSparkSql/chapter4/household_power_consumption.txt")
    hhEPCRdd.count()
    val header = hhEPCRdd.first()
    val data = hhEPCRdd.filter(row => row != header).filter(rows => !rows.contains("?"))
    val hhEPCClassRdd = data
      .map(_.split(";"))
      .map(p => HouseholdEPC(p(0).trim(), p(1).trim(), p(2).toDouble, p(3).toDouble, p(4).toDouble, p(5).toDouble, p(6).toDouble, p(7).toDouble, p(8).toDouble))
    val hhEPCDF = hhEPCClassRdd.toDF()
    hhEPCDF.printSchema()
    hhEPCDF.show(5)
    hhEPCDF.count()

    //Code Computing basic statistics and aggregations section
    hhEPCDF.describe().show()
    hhEPCDF.describe().select($"summary", $"gap", $"grp", $"voltage", $"gi", $"sm_1", $"sm_2", $"sm_3",
      round($"gap", 4).name("rgap"),
      round($"grp", 4).name("rgrp"),
      round($"voltage", 4).name("rvoltage"),
      round($"gi", 4).name("rgi"),
      round($"sm_1", 4).name("rsm_1"),
      round($"sm_2", 4).name("rsm_2"),
      round($"sm_3", 4).name("rsm_3"))
      .drop("gap", "grp", "voltage", "gi", "sm_1", "sm_2", "sm_3").show()
    val numDates = hhEPCDF.groupBy("date").agg(countDistinct("date")).count()

    //Code for Augmenting the dataset section
    val hhEPCDatesDf =
      hhEPCDF
        .withColumn("dow", from_unixtime(unix_timestamp($"date", "dd/MM/yyyy"), "EEEEE"))
        .withColumn("day", dayofmonth(to_date(unix_timestamp($"date", "dd/MM/yyyy").cast("timestamp"))))
        .withColumn("month", month(to_date(unix_timestamp($"date", "dd/MM/yyyy").cast("timestamp"))))
        .withColumn("year", year(to_date(unix_timestamp($"date", "dd/MM/yyyy").cast("timestamp"))))
    hhEPCDatesDf.show(5)

    //Code for Executing other miscellaneous processing steps section
    val delTmDF = hhEPCDF.drop("time")
    val finalDayDf1 = delTmDF.groupBy($"date").agg(sum($"gap").name("A"), sum($"grp").name("B"), avg($"voltage").name("C"), sum($"gi").name("D"), sum($"sm_1").name("E"), sum($"sm_2").name("F"), sum($"sm_3").name("G")).select($"date", round($"A", 2).name("dgap"), round($"B", 2).name("dgrp"), round($"C", 2).name("dvoltage"), round($"C", 2).name("dgi"), round($"E", 2).name("dsm_1"), round($"F", 2).name("dsm_2"), round($"G", 2).name("dsm_3")).withColumn("day", dayofmonth(to_date(unix_timestamp($"date", "dd/MM/yyyy").cast("timestamp")))).withColumn("month", month(to_date(unix_timestamp($"date", "dd/MM/yyyy").cast("timestamp")))).withColumn("year", year(to_date(unix_timestamp($"date", "dd/MM/yyyy").cast("timestamp"))))
    finalDayDf1.show(5)
    val readingsByMonthDf = hhEPCDatesDf.groupBy($"year", $"month").count().orderBy($"year", $"month")
    readingsByMonthDf.count()
    readingsByMonthDf.show(5)

    val finalDayDs1 = finalDayDf1.as[HouseholdEPCDTmDay]

  }

}