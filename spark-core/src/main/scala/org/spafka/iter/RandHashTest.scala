package org.spafka.iter

import com.google.common.hash.Hashing

import scala.util.control.Breaks

object RandHashTest {


  def main(args: Array[String]): Unit = {


    val mask = 1
    var nullValue = null

    var data = new Array[AnyRef](2 * 10)

    val loop = new Breaks;

    for (key <- Seq(1,2,3,3)) {

      val k = key.asInstanceOf[AnyRef]
      if (k.eq(null)) {
        return nullValue
      }
      var pos = rehash(k.hashCode) & mask
      var i = 1


      loop.breakable {
        while (true) {
          val curKey = data(2 * pos)
          if (curKey.eq(null)) {
            val newValue = curKey
            data(2 * pos) = k


            loop.break
          } else if (k.eq(curKey) || k.equals(curKey)) {
            val newValue = curKey
            data(2 * pos) = k

            loop.break()
          } else {
            val delta = i
            pos = (pos + delta) & mask
            i += 1
          }
        }
      }
    }


    println(data.mkString("\n"))
  }

  def rehash(h: Int): Int = Hashing.murmur3_32().hashInt(h).asInt()


}
