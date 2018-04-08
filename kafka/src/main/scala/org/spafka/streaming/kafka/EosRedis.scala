package org.spafka.streaming.kafka

import com.twitter.bijection.JavaSerializationInjection
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.{Jedis, JedisPool}

object EosRedis {

  def main(args: Array[String]): Unit = {


    val brokers = "localhost:9092"
    val topic = "alog"


    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "exactly-once",
      "enable.auto.commit" -> (false: java.lang.Boolean),
      "auto.offset.reset" -> "none")

    val conf = new SparkConf().setAppName("ExactlyOnce").setIfMissing("spark.master", "local[2]")
    val ssc = new StreamingContext(conf, Seconds(5))


    /**
      * Internal Redis client for managing Redis connection {@link Jedis} based on {@link RedisPool}
      */
    object InternalRedisClient extends Serializable {

      @transient private var pool: JedisPool = null

      def makePool(redisHost: String, redisPort: Int, redisTimeout: Int,
                   maxTotal: Int, maxIdle: Int, minIdle: Int): Unit = {
        makePool(redisHost, redisPort, redisTimeout, maxTotal, maxIdle, minIdle, true, false, 10000)
      }

      def makePool(redisHost: String, redisPort: Int, redisTimeout: Int,
                   maxTotal: Int, maxIdle: Int, minIdle: Int, testOnBorrow: Boolean,
                   testOnReturn: Boolean, maxWaitMillis: Long): Unit = {
        if (pool == null) {
          val poolConfig = new GenericObjectPoolConfig()
          poolConfig.setMaxTotal(maxTotal)
          poolConfig.setMaxIdle(maxIdle)
          poolConfig.setMinIdle(minIdle)
          poolConfig.setTestOnBorrow(testOnBorrow)
          poolConfig.setTestOnReturn(testOnReturn)
          poolConfig.setMaxWaitMillis(maxWaitMillis)
          pool = new JedisPool(poolConfig, redisHost, redisPort, redisTimeout)

          val hook = new Thread {

            override def run = pool.destroy()
          }
          sys.addShutdownHook(hook.run)
        }
      }

      def getPool: JedisPool = {
        assert(pool != null)
        pool
      }
    }

    // Redis configurations
    val maxTotal = 10
    val maxIdle = 10
    val minIdle = 1
    val redisHost = "10.10.4.130"
    val redisPort = 6379
    val redisTimeout = 30000
    val dbIndex = 1
    InternalRedisClient.makePool(redisHost, redisPort, redisTimeout, maxTotal, maxIdle, minIdle)


    InternalRedisClient.makePool("localhost", 6000, 5, 5, 5, 5)
    val pool = InternalRedisClient.getPool

    val resource: Jedis = pool.getResource


    case class PartOffSetTopic(topic: String, part: Int, offset: Long)
    case class info(storeId: Long, account: BigDecimal)


    val bytes = resource.get("").getBytes()
    val partOffSetTopic: PartOffSetTopic = JavaSerializationInjection[PartOffSetTopic].invert(bytes).getOrElse(null)


    val messages = KafkaUtils.createDirectStream[String, String](ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Assign[String, String](partOffSetTopic.keys, kafkaParams, fromOffsets))

  }

}

