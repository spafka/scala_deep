##1. 总结

org/spafka/scala/implicitz/defaultValue.scala
巧妙的使用了隐式转换，func这一Function0 接口，可直接运行，或传入另一个Function0接口直接运行，而这里另一个Function0就是线程池的实现格式了，在必要的时候
可引入适当的隐士转换，让编译器去适配合适的执行方法

##2. 界定 http://www.jianshu.com/p/c8835e27adba for more detail讲的非常棒

##3. classTag 对于构建泛型数组是必须的，必须捕获运行时的泛型实例。而对于普通的泛型，classtag也能根据运行时的状态，做一些额外的'事'

