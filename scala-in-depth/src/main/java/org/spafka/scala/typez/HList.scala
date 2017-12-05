package org.spafka.scala.typez

import org.spafka.scala.typez.HList.HNil

sealed trait HList {}

final case class HCons[H, T <: HList](head : H, tail : T) extends HList {
  def ::[T](v : T) = HCons(v,this)
  override def toString = head + " :: " + tail
}
class HNil extends HList {
  def ::[T](v : T) = HCons(v,this)
  override def toString = "Nil"
}




object HList {
  type ::[H, T <: HList] = HCons[H,T]
  val :: = HCons
  val HNil = new HNil


}

object appType {

  def main(args: Array[String]): Unit = {

    val x = "1" :: 2 :: 3 :: 5.0 :: HNil

    println(x)
    val a = new A

     print(a.foo)

  }

}


class A {
  a =>  //this别名
  val x=2
  def foo =a.x + this.x
}