import scala.collection.generic.CanBuildFrom

class A
class B extends A
class C extends B

var fun: B=> B=(b:B)=> new B
fun=(a:A)=>new C

fun=(c:C)=>new B