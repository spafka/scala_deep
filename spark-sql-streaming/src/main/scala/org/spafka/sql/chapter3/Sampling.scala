package org.spafka.sql.chapter3

import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.spafka.sql.Common

import scala.collection.immutable.Map

object Sampling {

  def main(args: Array[String]): Unit = {

    val spark = Common.spark

    import spark.implicits._
    val age = StructField("age", DataTypes.IntegerType)
    val job  = StructField("job", DataTypes.StringType)
    val marital  = StructField("marital", DataTypes.StringType)
    val edu  = StructField("edu", DataTypes.StringType)
    val credit_default  = StructField("credit_default", DataTypes.StringType)
    val housing  = StructField("housing", DataTypes.StringType)
    val loan  = StructField("loan", DataTypes.StringType)
    val contact  = StructField("contact", DataTypes.StringType)
    val month  = StructField("month", DataTypes.StringType)
    val day  = StructField("day", DataTypes.StringType)
    val dur  = StructField("dur", DataTypes.DoubleType)
    val campaign  = StructField("campaign", DataTypes.DoubleType)
    val pdays  = StructField("pdays", DataTypes.DoubleType)
    val prev  = StructField("prev", DataTypes.DoubleType)
    val pout  = StructField("pout", DataTypes.StringType)
    val emp_var_rate  = StructField("emp_var_rate", DataTypes.DoubleType)
    val cons_price_idx  = StructField("cons_price_idx", DataTypes.DoubleType)
    val cons_conf_idx  = StructField("cons_conf_idx", DataTypes.DoubleType)
    val euribor3m  = StructField("euribor3m", DataTypes.DoubleType)
    val nr_employed  = StructField("nr_employed", DataTypes.DoubleType)
    val deposit  = StructField("deposit", DataTypes.StringType)

    val fields = Array(age, job, marital, edu, credit_default, housing, loan, contact, month, day, dur, campaign, pdays, prev, pout, emp_var_rate, cons_price_idx, cons_conf_idx, euribor3m, nr_employed, deposit)
    val schema = StructType(fields)
    val df = spark
      .read
      .schema(schema)
      .option("sep", ";")
      .option("header", true)
      .csv("spark-sql/src/main/resources/bank-additional/bank-additional-full.csv")


    df.groupBy($"marital").count().show(100000)
    //Code for Sampling with RDD API section
    import org.apache.spark.mllib.linalg.Vector
    val rowsRDD = df.rdd.map(r => (if(r.getAs[String](2)!=null) r.getAs[String](2) else "unknown","1"))
    rowsRDD.take(2).foreach(println)
    val fractions = Map("unknown" -> .10, "divorced" -> .15, "married" -> 0.5, "single" -> .25)
    val rowsSampleRDD = rowsRDD.sampleByKey(true, fractions, 3600)
    val rowsSampleRDDExact = rowsRDD.sampleByKeyExact(true, fractions, 3600)
    println(rowsRDD.countByKey)
    println(rowsSampleRDD.countByKey)
    println(rowsSampleRDDExact.countByKey)

  }

}
