package org.spafka.scala.typez


class MyList {

}

trait List[+A] {

  // 添加 一个泛型参数B以解决协变做用于参数，并且限定B的下限以解决参数的限定
  def ++[B>:A](b: List[B]): List[B]
}

class EmptyList[A] extends List[A]{
  override def ++[B >: A](b: List[B]): List[B] = b
}


