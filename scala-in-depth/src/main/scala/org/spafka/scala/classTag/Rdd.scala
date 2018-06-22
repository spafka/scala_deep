package org.spafka.scala.classTag

import scala.reflect.ClassTag


class Rdd[T: ClassTag] {

  def mkArray(elems: T*): Array[T] = {

    // 获取T的实际类型

    var clazz: ClassTag[T] = implicitly[ClassTag[T]]
        if (classOf[String] == clazz.runtimeClass) {
          print("RunTime Type T is java.lang.String")
        }
        if (classOf[Integer] == clazz.runtimeClass) {
          print("RunTime Type T is java.lang.Integer")
        }

        println(clazz)
    Array[T](elems: _*)
  }

  // 这个方法和上面那个方法实际上是一样的,而构建泛型数组是必须要这样一个显示的证明的，而对于普通的类泛型则不需要
  // 但ClassTag也能捕获运行时泛型的实例
  //  def mkArray[T](elems: T*)(implicit evidence$1: scala.reflect.ClassTag[T]) = {
  //    var clazz: ClassTag[T] = implicitly[ClassTag[T]] //evidence
  //    if (classOf[String] == clazz.runtimeClass) {
  //      println("string")
  //    }
  //    print(evidence$1)
  //    Array[T](elems: _*)
  //  }

  def mkList(elems: T*) = {

        var clazz: ClassTag[T] = implicitly[ClassTag[T]] //evidence
        if (classOf[String] == clazz.runtimeClass) {
          println("string")
        }

    elems
  }

  def mkSet(elems: T*): Set[T] = {

    Set(elems: _*)
  }
}

object ClassTagApp extends App {

  private val stringRdd: Rdd[String] = new Rdd[String]
  private val stringArrz = stringRdd.mkArray("1", "2", "3")
  private val stringListz = stringRdd.mkList("1", "2", "3")
  private val stringSet = stringRdd.mkSet("1", "2", "3")


  private val intRdd = new Rdd[Int]

  private val intArrayz = intRdd.mkArray(1, 2, 3)
  private val intListz: Seq[Int] = intRdd.mkList(1, 2, 3)
  private val intSetz = intRdd.mkSet(1, 2, 3)
}