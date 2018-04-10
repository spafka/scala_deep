package org.spafka.sql.streaming

import com.databricks.spark.avro.SchemaConverters
import com.twitter.bijection.Injection
import com.twitter.bijection.avro.GenericAvroCodecs
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.log4j.{Level, LogManager, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.api.java.UDF1
import org.apache.spark.sql.streaming.StreamingQueryException
import org.apache.spark.sql.types.{DataTypes, StructType}
import java.lang.{Double => Jd, Integer => Ji, Long => Jl}


object strucedStreaming {



  val USER_SCHEMA: String = "{" + "\"type\":\"record\"," + "\"name\":\"orderDetail\"," + "\"fields\":[" + "  { \"name\":\"orderId\", \"type\":\"long\" }," + "  { \"name\":\"mid\", \"type\":\"long\" }," + "  { \"name\":\"amount\", \"type\":\"double\" }," + "  { \"name\":\"time\", \"type\":\"long\" }" + "]}"
  private val parser = new Schema.Parser()
  private val schema = parser.parse(USER_SCHEMA)

  //once per VM, lazily
  val recordInjection: Injection[GenericRecord, Array[Byte]] = GenericAvroCodecs.toBinary(schema)
  val `type` = SchemaConverters.toSqlType(schema).dataType.asInstanceOf[StructType]



  @throws[StreamingQueryException]
  def main(args: Array[String]): Unit = {
    //set log4j programmatically

    Logger.getRootLogger.setLevel(Level.WARN)
    //configure Spark
    val conf = new SparkConf().setAppName("kafka-structured").setMaster("local[*]")
    //initialize spark session
    val spark = SparkSession.builder.config(conf).getOrCreate
    //reduce task number
    spark.sqlContext.setConf("spark.sql.shuffle.partitions", "3")
    //data stream from kafka
    val ds1 = spark.
      readStream.
      format("kafka").
      option("kafka.bootstrap.servers", "localhost:9092,localhost:9093").
      option("subscribe", "order").
      option("startingOffsets", "earliest").
      option("maxOffsetsPerTrigger", 10). // 每次拉取得最大数量
      load
    //start the streaming query
    val structType = DataTypes.createStructType(`type`.fields)

    class myudf2 extends UDF1[Array[Byte], Row] {
      override def call(data: Array[Byte]): Row = {
        val record = recordInjection.invert(data).get
        RowFactory.create(record.get("orderDetail").asInstanceOf[Ji],
          record.get("mid").asInstanceOf[Jl],
          record.get("amount").asInstanceOf[Jd],
          record.get("time").asInstanceOf[Jl])
      }
    }

    spark.udf.register("deserialize", new myudf2, DataTypes.createStructType(`type`.fields))

    val ds2: DataFrame = ds1.select("value").as(Encoders.BINARY).selectExpr("deserialize(value) as rows").select("rows.*")


    ds2.registerTempTable("temp")

    ds2.printSchema()



    val query = spark.sql("select count(*)  from temp")
      .writeStream
      .queryName("query")
      .outputMode("complete")
      .format("console").start

    spark.sql("select mid,sum(amount) from temp group by mid ").writeStream
      .queryName("groupby")
      .outputMode("complete")
      .format("console").start.awaitTermination()


    query.awaitTermination
  }


}
