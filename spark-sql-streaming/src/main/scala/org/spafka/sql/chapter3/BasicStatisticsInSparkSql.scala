package org.spafka.sql.chapter3

import org.apache.spark.sql.DataFrame
import org.spafka.sql.Common
import org.spafka.sql.chapter3.ExploatoryDataAnalysis.Call
import java.lang

object BasicStatisticsInSparkSql {

  //Code for Computing Basic Statistics section
  case class CallStats(age: Integer, dur: Option[Double], campaign: Option[Double], prev: Option[Double], deposit: String)
  def main(args: Array[String]): Unit = {


    val spark = Common.spark
    import org.apache.spark.sql.types._
    import spark.implicits._
    import org.apache.spark.sql.functions._


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


    val ds = df.as[Call]

    ds.groupBy($"age").count().show()
    ds.groupBy($"dur").count().show()
    ds.groupBy($"campaign").count().show()
    ds.groupBy($"prev").count().show()
    ds.groupBy($"deposit").count().show()


    val dsSubset = ds.select($"age", $"dur", $"campaign", $"prev", $"deposit")
    dsSubset.show(5)
    val dsCallStats = dsSubset.as[CallStats]
    dsSubset.describe().show()

    // 协方差
    val cov = dsSubset.stat.cov("age","dur")
    println("age to call duration : Covariance = %.4f".format(cov))
    val corr = dsSubset.stat.corr("age","dur")
    // pearson 相关系数
    println(s"age to call duration : Correlation = %.4f".format(corr))

    // 制作图表
    ds.stat.crosstab("age", "marital").orderBy("age_marital").show(10)
    val freq = df.stat.freqItems(Seq("edu"), 0.3)
    freq.collect()(0)
    val quantiles = df.stat.approxQuantile("age", Array(0.25,0.5,0.75),0.0)
    dsCallStats.cache()


    // aggerate more data
    import org.apache.spark.sql.expressions.scalalang.typed.{count => typedCount, avg => typedAvg, sum => typedSum}
    (dsCallStats
      .groupByKey(callstats => callstats.deposit)
      .agg(
        typedCount[CallStats](_.age).name("A"),
        typedAvg[CallStats](_.campaign.getOrElse(-1)).name("B"),
        typedAvg[CallStats](_.dur.getOrElse(-1)).name("C"),
        typedAvg[CallStats](_.prev.getOrElse(-1)).name("D")).withColumnRenamed("value", "E"))
      .select(
        $"E".name("TD Subscribed?"),
        $"A".name("Total Customers"),
        round($"B", 2).name("Avg calls(curr)"),
        round($"C", 2).name("Avg dur"),
        round($"D", 2).name("Avg calls(prev)"))
      .show()
    (dsCallStats
      .groupByKey(callstats => callstats.age)
      .agg(typedCount[CallStats](_.age).name("A"),
        typedAvg[CallStats](_.campaign.getOrElse(-1)).name("B"),
        typedAvg[CallStats](_.dur.getOrElse(-1)).name("C"),
        typedAvg[CallStats](_.prev.getOrElse(-1)).name("D"))
      .withColumnRenamed("value", "E"))
      .select(
        $"E".name("Age"),
        $"A".name("Total Customers"),
        round($"B", 2).name("Avg calls(curr)"),
        round($"C", 2).name("Avg dur"),
        round($"D", 2).name("Avg calls(prev)"))
      .orderBy($"age").show(5)




  }
}
