package org.kafka.v010.producer;

import org.apache.kafka.clients.producer.*;
import org.joda.time.DateTime;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

public class ProducerExample {

    public static void main(String[] str) throws InterruptedException, IOException {

        System.out.println("Starting ProducerExample ...");

        sendMessages();

    }

    private static void sendMessages() throws InterruptedException, IOException {

        Producer<String, String> producer = createProducer();

        sendMessages(producer);

        // Allow the producer to complete the sending of the records before existing the program.
        Thread.sleep(1000);

    }

    private static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        props.put("acks", "all");
        props.put("retries", 1);
        // This property controls how much bytes the sender would wait to batch up the content before publishing to Kafka.
        props.put("batch.size", 10);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        return new KafkaProducer(props);
    }

    private static void sendMessages(Producer<String, String> producer) throws InterruptedException {
        String topic = "alog";
//        int partition = 0;
        long record = 1;
        for (int i = 1; true; i++) {
            producer.send(new ProducerRecord<String, String>(topic, Long.toString(i),new DateTime().toString("yyyy-MM-dd HH:mm:ss")+" ERROR some message"),
                    new DemoCallBack(System.currentTimeMillis(),i,new DateTime().toString("yyyy-MM-dd HH:mm:ss")+" ERROR some message"));
            Thread.sleep(100);
        }


    }

  static   class DemoCallBack implements Callback {

        private final long startTime;
        private final int key;
        private final String message;

        public DemoCallBack(long startTime, int key, String message) {
            this.startTime = startTime;
            this.key = key;
            this.message = message;
        }

        /**
         * A callback method the user can implement to provide asynchronous handling of request completion. This method will
         * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
         * non-null.
         *
         * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
         *                  occurred.
         * @param exception The exception thrown during processing of this record. Null if no error occurred.
         */
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (metadata != null) {
                System.out.println(

                        new Timestamp(System.currentTimeMillis()) +
                                "  message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
                                "), " +
                                "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
            } else {
                exception.printStackTrace();
            }
        }
    }
}
