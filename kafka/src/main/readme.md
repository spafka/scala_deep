[producer]
当acks=-1时，Kafka发送端的TPS受限于topic的副本数量（ISR中），副本越多TPS越低；
acks=0时，TPS最高，其次为1，最差为-1，即TPS：acks_0 > acks_1 > ack_-1；
min.insync.replicas参数不影响TPS；
partition的不同会影响TPS，随着partition的个数的增长TPS会有所增长，但并不是一直成正比关系，到达一定临界值时，partition数量的增加反而会使TPS略微降低；
Kafka在acks=-1,min.insync.replicas>=1(server 配置)时，具有高可靠性，所有成功返回的消息都可以落盘。
[consumer]

1. AtMostOnce
至多一次，先保存消费进度，然后消费。如果消费挂了，下次再从offset读取，则未处理的消息就被忽略了，方法为开启autoCommit，并且自动提交的时间较短。
2. AtLestOnce
至少一次，先消费，然后提交消费进度，可能提交挂了，则下次读取还会处理这条消息，而被处理2次，方法为关闭autoCommit，自己手动提交 commitSync
3. exactlyOnce
处理消息与提交offset原子性，kafka的做法是，消费者不提交偏移量，订阅主题时设置自定义的监听器，读取offset
自己较好的做法是，在comsumer端去重或2阶段提交