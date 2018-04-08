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
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AvroConsumer {
        Logger logger = LoggerFactory.getLogger(AvroConsumer.class);
        private final ConsumerConnector consumer;
        private final String topic;

        public AvroConsumer(String zookeeper, String groupId, String topic) {
            Properties props = new Properties();
            props.put("zookeeper.connect", zookeeper);
            props.put("group.id", groupId);
            props.put("zookeeper.session.timeout.ms", "500");
            props.put("zookeeper.sync.time.ms", "250");
            props.put("auto.commit.interval.ms", "1000");

            consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
            this.topic = topic;
        }

       Schema.Parser parser = new Schema.Parser();
       Schema schema = parser.parse(AvroProducer.USER_SCHEMA);

        public void testConsumer() {
            Map<String, Integer> topicCount = new HashMap<>();
            topicCount.put(topic, 1);

            Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
            List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
            for (final KafkaStream stream : streams) {
                ConsumerIterator it = stream.iterator();
                while (it.hasNext()) {
                    MessageAndMetadata messageAndMetadata = it.next();
                    String key = new String((byte[])messageAndMetadata.key());
                    byte[] message = (byte[]) messageAndMetadata.message();


                    Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
                    GenericRecord record = recordInjection.invert(message).get();
                    logger.info("key=" + key + ", str1= " + record.get("str1")
                            + ", str2= " + record.get("str2")
                            + ", int1=" + record.get("int1"));
                }
            }
            consumer.shutdown();
        }

        public static void main(String[] args) {

           new AvroConsumer("localhost:2181","spafka","spafka").testConsumer();
        }
    }