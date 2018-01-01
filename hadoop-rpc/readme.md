
[server 分析](https://github.com/spafka/spark_deep/blob/master/hadoop-rpc/src/main/md/server.md)

[client 分析](https://github.com/spafka/spark_deep/blob/master/hadoop-rpc/src/main/md/client.md)

##总结

![](https://github.com/spafka/spark_deep/blob/master/hadoop-rpc/src/main/md/reactor.png)


### server

```txt
这是一个典型的reactor nio通信框架
一个server dispatch 线程监听端口的链接，dispatch 轮询分发给reader线程,reader 线程读取buf，反射buf为call（call in Server ），把call加入到callQueue，
hander 线程调用call，invoke执行call的方法，最终加到responQueue，返回给客户端。
```

### client
```txt

  client 主要使用的是动态代理技术，使用jdk自带的proxy，执行方法封装为call，发送给server ，server接收call，返回response

```
