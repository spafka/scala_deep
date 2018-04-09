package org.spafka.sql.chapter2

import java.util.Properties

import org.apache.spark.sql.{Column, DataFrame, Row}
import org.apache.spark.sql.types._
import org.spafka.sql.Common

object Rmdbs {


  def main(args: Array[String]): Unit = {


    //    CREATE DATABASE retailDB;
    //
    //    USE retailDB;
    //
    //    CREATE TABLE transactions (
    //      transactionID INTEGER NOT NULL
    //        AUTO_INCREMENT,
    //      invoiceNo     VARCHAR(20),
    //      stockCode     VARCHAR(20),
    //      description   VARCHAR(255),
    //      quantity      INTEGER,
    //      unitPrice     DOUBLE,
    //      customerID    VARCHAR(20),
    //      country       VARCHAR(100),
    //      invoiceDate
    //        TIMESTAMP,
    //      PRIMARY KEY (transactionID)
    //    );


    val spark = Common.spark

    spark.sqlContext
    import spark.implicits._
    import org.apache.spark.sql.functions._

    val inFileRDD = spark.sparkContext.textFile("spark-sql/src/main/resources/Online Retail.txt")

    val allRowsRDD = inFileRDD.map(line => line.split("\t").map(_.trim))
    val header = allRowsRDD.first
    val data = allRowsRDD.filter(_ (0) != header(0))

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


    val schema = StructType(fields)
    val rowRDD = data.map(attributes => Row(attributes(0), attributes(1), attributes(2), attributes(3).toInt, attributes(4), attributes(5).toDouble, attributes(6), attributes(7)))
    val r1DF = spark.createDataFrame(rowRDD, schema)

    // Add a new colume
    val ts: Column = unix_timestamp($"invoiceDate","yyyy/MM/dd HH:mm").cast("timestamp")
    val r2DF = r1DF.withColumn("ts", ts)
    r2DF.show()

//    +---------+---------+--------------------+--------+--------------+---------+----------+--------------+-------------------+
//    |invoiceNo|stockCode|         description|quantity|   invoiceDate|unitPrice|customerID|       country|                 ts|
//    +---------+---------+--------------------+--------+--------------+---------+----------+--------------+-------------------+
//    |   536365|   85123A|WHITE HANGING HEA...|       6|2010/12/1 8:26|     2.55|     17850|United Kingdom|2010-12-01 08:26:00|

    r2DF.createOrReplaceTempView("retailTable")
    val r3DF = spark.sql("select * from retailTable where ts< '2011-12-01'")
    val r4DF = spark.sql("select * from retailTable where ts>= '2011-12-01'")
    //val selectData = r4DF.select("invoiceNo", "stockCode", "description", "quantity", "unitPrice", "customerID", "country", "ts")
    val selectData: DataFrame = r4DF.drop("invoiceDate")
    val writeData = selectData.withColumnRenamed("ts", "invoiceDate")
    writeData.show()


    val dbUrl = "jdbc:mysql://localhost:3306/retailDB"
    val prop = new Properties()
    prop.setProperty("user", "root")
    prop.setProperty("password", "root")
    prop.setProperty("driver","com.mysql.jdbc.Driver")
    writeData.write.mode("append").jdbc(dbUrl, "transactions", prop)

    spark.stop()

  }
}
