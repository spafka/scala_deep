package org.apache.spark.future;

import com.google.common.util.concurrent.*;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// netty 异步回调基础，添加一个监听器
public class listenAbleFuture {

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    private ListeningExecutorService service = MoreExecutors.listeningDecorator(executor);

    public static void main(String[] args) {

        listenAbleFuture test = new listenAbleFuture();

        ListenableFuture<Date> future = test.service.submit(() -> {
            Thread.sleep(1000 * 5);
            return new Date();
        });

        Futures.addCallback(future, new FutureCallback<Date>() {

            @Override
            public void onSuccess(Date result) {
                System.out.println("执行结果" + result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("error=============");
            }
        });

        System.out.println("abcdd");
    }
}
