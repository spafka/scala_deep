/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.kafka.v010.avro;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * @see https://www.iteblog.com/archives/2263.html
 */
public class AvroProducer {


    Logger logger = LoggerFactory.getLogger("AvroKafkaProducter");
    public static final String USER_SCHEMA = "{"
            + "\"type\":\"record\","
            + "\"name\":\"orderDetail\","
            + "\"fields\":["
            + "  { \"name\":\"orderId\", \"type\":\"int\" },"
            + "  { \"name\":\"mid\", \"type\":\"int\" },"
            + "  { \"name\":\"amount\", \"type\":\"double\" },"
            + "  { \"name\":\"time\", \"type\":\"int\" }"
            + "]}";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9093");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        //“所有”设置将导致记录的完整提交阻塞，最慢的，但最持久的设置。
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        //如果请求失败，生产者也会自动重试，即使设置成０ the producer can automatically retry.
        props.put(ProducerConfig.RETRIES_CONFIG, 0);

        // 指定分区策略 如果 key 不为空,则使用 hash 如果为空,则使用 round robin 轮训
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartitioner.class);

        //The producer maintains buffers of unsent records for each partition.
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 500);
        // 默认立即发送，这里这是延时毫秒数 可以提高吞吐量
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // 生产者缓冲大小，当缓冲区耗尽后，额外的发送调用将被阻塞。时间超过max.block.ms将抛出TimeoutException
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);


        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(USER_SCHEMA);
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);

        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(props);

        long time = new Date().getTime();
        for (int i = 0; i < 10000000; i++) {
            GenericData.Record avroRecord = new GenericData.Record(schema);
            avroRecord.put("orderId", RandomUtils.nextLong(Long.MAX_VALUE >>1, Long.MAX_VALUE));
            avroRecord.put("mid", RandomUtils.nextLong(201701010000L,201701010099L));
            avroRecord.put("amount", RandomUtils.nextDouble(0.0d,10000d));
            avroRecord.put("time", time+RandomUtils.nextLong(0,360000000L));

            byte[] bytes = recordInjection.apply(avroRecord);

            ProducerRecord<String, byte[]> record = new ProducerRecord<>("order", null, bytes);
            producer.send(record);
            TimeUnit.MILLISECONDS.sleep(10);

            //System.out.println(recordMetadata.offset());


        }

        producer.close();
    }
}