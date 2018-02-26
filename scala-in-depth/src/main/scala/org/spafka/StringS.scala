package org.spafka

object StringS {


  def main(args: Array[String]): Unit = {

  val s=  new String(
      s"""
         | s
         |ss
         |ss
         |s
       """.stripMargin)

    println(s)
  }

}
