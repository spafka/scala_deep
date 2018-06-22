package org.spafka.scala.typez

import scala.collection.generic.CanBuildFrom

object func {

  class A{}
  class B extends A{}
  class C extends B{}
  def main(args: Array[String]): Unit = {
    var fun: B=> B=(b:B)=> new B
    fun=(a:A)=>new C
    //fun=(c:C)=>new B
    //@see http://hongjiang.info/scala-type-system-dependent-types/
    implicitly[CanBuildFrom[List[Int], String, List[String]]]
  }
}
