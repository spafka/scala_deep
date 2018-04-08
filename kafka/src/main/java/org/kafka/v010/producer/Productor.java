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
package org.kafka.v010.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author kafka
 */
public class Productor {

  static   final Logger logger = LoggerFactory.getLogger(Productor.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        boolean Async = false;

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //“所有”设置将导致记录的完整提交阻塞，最慢的，但最持久的设置。
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        //如果请求失败，生产者也会自动重试，即使设置成０ the producer can automatically retry.
        props.put(ProducerConfig.RETRIES_CONFIG, 0);

        // 指定分区策略 如果 key 不为空,则使用 hash 如果为空,则使用 round robin 轮训
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartitioner.class);

        //The producer maintains buffers of unsent records for each partition.
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 500);
        // 默认立即发送，这里这是延时毫秒数 可以提高吞吐量
        props.put(ProducerConfig.LINGER_MS_CONFIG, 100);
        // 生产者缓冲大小，当缓冲区耗尽后，额外的发送调用将被阻塞。时间超过max.block.ms将抛出TimeoutException
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        //The key.serializer and value.serializer instruct how to turn the key and value objects the user provides with their ProducerRecord into bytes.
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        //创建kafka的生产者类
        Producer<String, String> producer = new KafkaProducer<>(props);
        //生产者的主要方法
        // close();//Close this producer.
        //   close(long timeout, TimeUnit timeUnit); //This method waits up to timeout for the producer to complete the sending of all incomplete requests.
        //  flush() ;所有缓存记录被立刻发送
        for (int i = 0; i < 10000000; i++) {


            // 添加 callback 表示异步处理,废除 producer.type 字段
            if (Async) {

                producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), Integer.toString(i)), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (null != metadata) {
                            logger.info("the offset is {},partition is {},timeStamp is {}",
                                    metadata.offset(), metadata.partition(), metadata.timestamp());
                        }
                    }
                });

            } else {

                Future<RecordMetadata> test = producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), Integer.toString(i)));
                RecordMetadata recordMetadata = test.get();
                logger.info("the offset is {},partition is {},timeStamp is {}",
                        recordMetadata.offset(), recordMetadata.partition(), recordMetadata.timestamp());

                Thread.sleep(1);


            }

        }

        producer.close();
    }

}
