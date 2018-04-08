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

package org.kafka.v010.consumer;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class Consumer {
    private static final String GROUP = "MsgConsumer";
    private static final List TOPICS = Arrays.asList("normal-topic");

    /**
     * 自动提交offset
     */
    public void autoCommit() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());//key反序列化方式
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());//value反系列化方式
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);//自动提交
       // properties.propertyNames(ConsumerConfig)
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");//指定broker地址，来找到group的coordinator
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.GROUP);//指定用户组

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(TOPICS);

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);//100ms 拉取一次数据
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("topic: " + record.topic() + " key: " + record.key() + " value: " + record.value() + " partition: " + record.partition());
            }



        }
    }

    /**
     * 手动提交offset
     */
    public void consumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());//key反序列化方式
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());//value反系列化方式
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//手动提交
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");//指定broker地址，来找到group的coordinator
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.GROUP);//指定用户组

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(TOPICS);//指定topic消费

        long i = 0;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);//100ms 拉取一次数据
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("topic: " + record.topic() + " key: " + record.key() + " value: " + record.value() + " partition: " + record.partition());
                i++;
            }

            if (i >= 100) {
                consumer.commitAsync();//手动commit
                i = 0;
            }
        }
    }

    public static void main(String[] args) {
        new Consumer().autoCommit();
    }
}