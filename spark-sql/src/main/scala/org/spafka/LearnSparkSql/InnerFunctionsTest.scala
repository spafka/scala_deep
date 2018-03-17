package org.spafka.LearnSparkSql


import org.apache.spark.sql.types._


// spark 内置sql函数 test
object InnerFunctionsTest {


  def main(args: Array[String]): Unit = {

    val spark = Common.spark

    import spark.implicits._
    import org.apache.spark.sql.functions._

    import Common.benchmark
    import org.apache.spark.sql.types._



    //InvoiceNo,StockCode,Description,Quantity,InvoiceDate,UnitPrice,CustomerID,Country
    val fields = Seq(
      StructField("invoiceNo", StringType, true),
      StructField("stockCode", StringType, true),
      StructField("description", StringType, true),
      StructField("quantity", IntegerType, true),
      StructField("invoiceDate", StringType, true),
      StructField("unitPrice", DoubleType, true),
      StructField("customerID", StringType, true),
      StructField("country", StringType, true)
    )

    val df = spark
      .read
      .option("sep", "\t")
      .option("header",true)
      .schema(StructType(fields))
      .csv("/Users/spafka/Desktop/flink/spark_deep/spark-sql/src/main/resources/Online Retail.txt")

    val row = df.first()

    df.createOrReplaceTempView("retail")

    // groupby //

//    benchmark("groupBy dsl"){
//      df.groupBy($"stockCode").count.show(5)}
//
//    benchmark(("groupBy sql")){
//      spark.sql("select stockCode,count(*) from retail group by stockCode ").show(5)
//    }


    // sort //

//    benchmark("sort dsl "){
//      df.sort($"stockCode",$"quantity".desc).show(5)
//    }
//
//
//    benchmark("sort sql "){
//      spark.sql("select * from retail order by stockCode, quantity desc ").show(5)
//    }







    spark.stop()

  }

}


