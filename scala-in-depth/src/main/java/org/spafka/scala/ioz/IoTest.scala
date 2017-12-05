package org.spafka.scala.ioz

class IoTest {

}

object IoTest {

  def main(args: Array[String]): Unit = {

    import scala.io.Source
    val source = Source.fromFile("src\\main\\resources\\批导文件.txt")


    val stringses = source.getLines().map(_.split(",").toList)

    def fun(x: Seq[_]) = x.foreach(println)

    val fun2: (Seq[_]) => Any = (y: Seq[_]) => {
       y.mkString("|")
    }

   val ints= 1 to 10000
  }


}
