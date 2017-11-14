//package org.spafka.scala.implicitz
//
//class cov_con {
//
//
//}
//
//// con协变用于消费者
//trait Array[-A] {
//  def update(a: A): Unit
//}
//
//// cov逆变用于生产者
//trait ArrayB[+A] {
//  def update(a: Seq[_]): A
//}
//
//object FunN {
//
//  val f1: (Any) => String = new Function1[Any, String] {
//    override def apply(v1: Any): String = {
//      v1.toString
//    }
//  }
//
//  var f2: Function1[AnyRef, Any] = new Function1[AnyRef, Any] {
//    override def apply(v1: AnyRef): Int = {
//      1
//    }
//  }
//
//  def fun(f: AnyRef => AnyVal) = f
//
//
//  f2 = (x: Any) => 1
//
//  f2 = (x: Any) => 2.0d
//
////  f2 = (x: AnyRef) =>
//
//  //fun(f2)
//
//
//}