package org.spafka.scala.typez


class MyList {

}

trait Listq[+A] {

  // 添加 一个泛型参数B以解决协变做用于参数，并且限定B的下限以解决参数的限定
  def ++[B>:A](b: Listq[B]): Listq[B]
}

class EmptyList[A] extends Listq[A]{
  override def ++[B >: A](b: Listq[B]): Listq[B] = b
}



