package org.spafka.streaming.kafka.sql

import com.databricks.spark.avro.SchemaConverters
import com.twitter.bijection.Injection
import com.twitter.bijection.avro.GenericAvroCodecs
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.SparkConf
import org.apache.spark.sql.api.java.UDF1
import org.apache.spark.sql.streaming.StreamingQueryException
import org.apache.spark.sql.types.{DataTypes, StructType}
import org.apache.spark.sql.{Encoders, Row, RowFactory, SparkSession}


object strucedStreaming {

  private val USER_SCHEMA = "{" + "\"type\":\"record\"," + "\"name\":\"myrecord\"," + "\"fields\":[" + "  { \"name\":\"str1\", \"type\":\"string\" }," + "  { \"name\":\"str2\", \"type\":\"string\" }," + "  { \"name\":\"int1\", \"type\":\"int\" }" + "]}"
  private val parser = new Schema.Parser()
  private val schema = parser.parse(USER_SCHEMA)

  //once per VM, lazily
  val recordInjection: Injection[GenericRecord, Array[Byte]] = GenericAvroCodecs.toBinary(schema)
  val `type` = SchemaConverters.toSqlType(schema).dataType.asInstanceOf[StructType]


  @throws[StreamingQueryException]
  def main(args: Array[String]): Unit = {
    //set log4j programmatically
    LogManager.getLogger("org.apache.spark").setLevel(Level.WARN)
    LogManager.getLogger("akka").setLevel(Level.ERROR)
    //configure Spark
    val conf = new SparkConf().setAppName("kafka-structured").setMaster("local[*]")
    //initialize spark session
    val sparkSession = SparkSession.builder.config(conf).getOrCreate
    //reduce task number
    sparkSession.sqlContext.setConf("spark.sql.shuffle.partitions", "3")
    //data stream from kafka
    val ds1 = sparkSession.readStream.format("kafka").option("kafka.bootstrap.servers", "localhost:9092").option("subscribe", "test").option("startingOffsets", "earliest").load
    //start the streaming query
    val structType = DataTypes.createStructType(`type`.fields)

    class myudf2 extends UDF1[Array[Byte], Row] {
      override def call(data: Array[Byte]): Row = {
        val record = recordInjection.invert(data).get
        RowFactory.create(record.get("str1").toString, record.get("str2").toString, record.get("int1"))
      }
    }



    sparkSession.udf.register("deserialize", new myudf2, DataTypes.createStructType(`type`.fields))
    ds1.printSchema
    val ds2 = ds1.select("value").as(Encoders.BINARY).selectExpr("deserialize(value) as rows").select("rows.*")
    ds2.printSchema
    val query1 = ds2.groupBy("str1").count.writeStream.queryName("Test query").outputMode("complete").format("console").start
    query1.awaitTermination
  }


}
