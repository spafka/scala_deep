import scala.reflect.runtime.universe._

class A

val a=new A

// == A.getclass
val classOfA=classOf[A]

val value = typeOf[A]

val Aclazz = a.getClass

// 两个是一样的
classOfA==Aclazz

// 注意，typeOf 和 classOf 方法接收的都是类型符号(symbol)，并不是对象实例

object O
// combiler error
// classOf[O]

val o=O.getClass


