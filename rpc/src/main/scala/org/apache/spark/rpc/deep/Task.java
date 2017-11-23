package org.apache.spark.rpc.deep;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public abstract class Task implements Serializable {

    public void run() {
        throw new RuntimeException("not implement!");
    }

    public static void main(String[] args) {

        System.out.println(Task.class.getClassLoader());

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.rangeClosed(1,100).forEach((i)->{
            executorService.execute(()->{
                System.out.println(Thread.currentThread().getContextClassLoader());
            });
        });

    }
}
