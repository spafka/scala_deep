package org.spafka.LearnSparkSql.chapter3

import org.spafka.LearnSparkSql.Common

object ExploatoryDataAnalysis {

  case class Call(age: Int,
                  job: String,
                  marital: String,
                  edu: String,
                  credit_default: String,
                  housing: String,
                  loan: String,
                  contact: String,
                  month: String,
                  day: String,
                  dur: Double,
                  campaign: Double,
                  pdays: Double,
                  prev: Double,
                  pout: String,
                  emp_var_rate: Double,
                  cons_price_idx: Double,
                  cons_conf_idx: Double,
                  euribor3m: Double,
                  nr_employed: Double,
                  deposit: String)

  def main(args: Array[String]): Unit = {

    val spark = Common.spark
    import org.apache.spark.sql.types._
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


    val count = df.count()

    println(count)






    val ds = df.as[Call]
    ds.printSchema()

    ds.groupBy($"age").count().orderBy($"age").show()
    ds.groupBy($"job").count().show()
    ds.groupBy($"edu").count().show()
    ds.groupBy($"credit_default").count().show()
    ds.groupBy($"housing").count().show()
    ds.groupBy($"contact").count().show()
    ds.groupBy($"campaign").count().show()
    ds.groupBy($"pdays").count().show()
    ds.groupBy($"prev").count().show()
    ds.groupBy($"pout").count().show()
    ds.groupBy($"emp_var_rate").count().show()
    ds.groupBy($"cons_price_idx").count().show()
    ds.groupBy($"cons_conf_idx").count().show()
    ds.groupBy($"euribor3m").count().show()
    ds.groupBy($"nr_employed").count().show()
    ds.groupBy($"deposit").count().show()








  }

}
