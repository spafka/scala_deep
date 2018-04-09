package org.spafka.sql.chapter3

import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.spafka.sql.Common

object IdentifyingDataOutliers {


  // 极端值分析
  def main(args: Array[String]): Unit = {


    val spark = Common.spark

    val age = StructField("age", DataTypes.IntegerType)
    val job = StructField("job", DataTypes.StringType)
    val marital = StructField("marital", DataTypes.StringType)
    val edu = StructField("edu", DataTypes.StringType)
    val credit_default = StructField("credit_default", DataTypes.StringType)
    val housing = StructField("housing", DataTypes.StringType)
    val loan = StructField("loan", DataTypes.StringType)
    val contact = StructField("contact", DataTypes.StringType)
    val month = StructField("month", DataTypes.StringType)
    val day = StructField("day", DataTypes.StringType)
    val dur = StructField("dur", DataTypes.DoubleType)
    val campaign = StructField("campaign", DataTypes.DoubleType)
    val pdays = StructField("pdays", DataTypes.DoubleType)
    val prev = StructField("prev", DataTypes.DoubleType)
    val pout = StructField("pout", DataTypes.StringType)
    val emp_var_rate = StructField("emp_var_rate", DataTypes.DoubleType)
    val cons_price_idx = StructField("cons_price_idx", DataTypes.DoubleType)
    val cons_conf_idx = StructField("cons_conf_idx", DataTypes.DoubleType)
    val euribor3m = StructField("euribor3m", DataTypes.DoubleType)
    val nr_employed = StructField("nr_employed", DataTypes.DoubleType)
    val deposit = StructField("deposit", DataTypes.StringType)

    val fields = Array(age, job, marital, edu, credit_default, housing, loan, contact, month, day, dur, campaign, pdays, prev, pout, emp_var_rate, cons_price_idx, cons_conf_idx, euribor3m, nr_employed, deposit)
    val schema = StructType(fields)
    val df = spark
      .read
      .schema(schema)
      .option("sep", ";")
      .option("header", true)
      .csv("spark-sql/src/main/resources/bank-additional/bank-additional-full.csv")

    import org.apache.spark.mllib.clustering.KMeans
    import org.apache.spark.mllib.linalg.Vectors

    // 密集矩阵
    val vectors = df.rdd.map(r => Vectors.dense(if (r.get(10) != null) r.getDouble(10) else 0,
      if (r.get(11) != null) r.getDouble(11) else 0,
      if (r.get(12) != null) r.getDouble(12) else 0,
      if (r.get(13) != null) r.getDouble(13) else 0))
    vectors.cache()
    val kMeansModel = KMeans.train(vectors, 2, 20)
    kMeansModel.clusterCenters.foreach(println)

  }

}
