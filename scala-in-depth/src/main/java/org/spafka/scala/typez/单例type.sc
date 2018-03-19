object O

// error
// classOf[O.type ]
import scala.reflect.runtime.universe._
O.getClass

typeOf[O.type ]

// 单例类型
//@see http://hongjiang.info/scala-type-system-singleton-type/

class A {def method1: A = this }
class B extends A {def method2: B = this}

val b = new B
b.method2.method1  // 可以工作
b.method1.method2  // 不行，提示：error: value method2 is not a member of A


class C{ def method1: this.type = this }
class D extends C { def method2 : this.type = this }

val c = new D
c.method1.method2  // ok