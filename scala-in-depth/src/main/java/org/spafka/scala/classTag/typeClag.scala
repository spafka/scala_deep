package org.spafka.scala.classTag


import scala.collection.immutable
import scala.reflect.runtime.universe
import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeTag

object typeClag {

  def getTypeTag[T: TypeTag](obj: T): universe.TypeTag[T] = typeTag[T]

  def main(args: Array[String]): Unit = {

    val l:List[Int] =List(1,2,3,4)

    val tag: universe.TypeTag[List[Int]] = getTypeTag(l)
    val tpe: universe.Type = tag.tpe

    l

  }
}
