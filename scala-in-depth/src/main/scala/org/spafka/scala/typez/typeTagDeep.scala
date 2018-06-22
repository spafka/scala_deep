package org.spafka.scala.typez
import scala.reflect._
import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._


object typeTagDeep {

  def getTypeTag[T: TypeTag](obj: T) = {

   val aBoolean= typeOf[T]  <:< typeOf[Product]

    println(typeOf[T])
    println(aBoolean)
    val value: universe.TypeTag[T] = typeTag[T]

    value

  }
  def getClassTag[T: ClassTag](obj: T) = {

    val value: ClassTag[T] = classTag[T]

    value.runtimeClass.getSimpleName

  }

  def main(args: Array[String]): Unit = {

    val l = Seq(1, 2, 3)


    val theType = getTypeTag(l)
    val tpe: universe.Type = theType.tpe

    println(tpe)
    // List[Int]
    val decls = tpe.decls.take(10)

    val a=1

    val dealias = tpe.dealias



    val str = getClassTag(l)

    println(str)
    // Seq

  }

}
