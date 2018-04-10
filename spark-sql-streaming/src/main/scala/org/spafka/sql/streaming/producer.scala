package org.spafka.sql.streaming

import java.util.Properties
import java.util.concurrent.ExecutionException

import com.twitter.bijection.Injection
import com.twitter.bijection.avro.GenericAvroCodecs
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.clients.producer.internals.DefaultPartitioner
import org.slf4j.LoggerFactory

object producer {
  private val logger = LoggerFactory.getLogger("AvroKafkaProducter")
  val USER_SCHEMA: String = "{" + "\"type\":\"record\"," + "\"name\":\"test\"," + "\"fields\":[" + "  { \"name\":\"str1\", \"type\":\"string\" }," + "  { \"name\":\"str2\", \"type\":\"string\" }," + "  { \"name\":\"int1\", \"type\":\"int\" }" + "]}"

  implicit def Int2Intger(i:Int)=new Integer(i)

  @throws[InterruptedException]
  @throws[ExecutionException]
  def main(args: Array[String]): Unit = {

    val props = new Properties
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9093")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
    //“所有”设置将导致记录的完整提交阻塞，最慢的，但最持久的设置。
    props.put(ProducerConfig.ACKS_CONFIG, "1")
    //如果请求失败，生产者也会自动重试，即使设置成０ the producer can automatically retry.
    props.put(ProducerConfig.RETRIES_CONFIG, Int2Intger(2))
    // 指定分区策略 如果 key 不为空,则使用 hash 如果为空,则使用 round robin 轮训
    props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, classOf[DefaultPartitioner])
    //The producer maintains buffers of unsent records for each partition.
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, Int2Intger(500000))
    // 默认立即发送，这里这是延时毫秒数 可以提高吞吐量
    props.put(ProducerConfig.LINGER_MS_CONFIG, Int2Intger(1000))
    // 生产者缓冲大小，当缓冲区耗尽后，额外的发送调用将被阻塞。时间超过max.block.ms将抛出TimeoutException
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, Int2Intger(33554432))
    val parser = new Schema.Parser
    val schema = parser.parse(USER_SCHEMA)
    val recordInjection: Injection[GenericRecord, Array[Byte]] = GenericAvroCodecs.toBinary(schema)
    val producer = new KafkaProducer[String, Array[Byte]](props)
    var i = 0
    for (i <- 1 to 1000) {
      val avroRecord = new GenericData.Record(schema)
      avroRecord.put("str1", "Str 1-" + i)
      avroRecord.put("str2", "Str 2-" + i)
      avroRecord.put("int1", i)
      val bytes = recordInjection(avroRecord)
      val record = new ProducerRecord[String, Array[Byte]]("spafka", null, bytes)
      val recordMetadata = producer.send(record).get
      System.out.println(recordMetadata.offset)
    }
    producer.close()
  }

}
