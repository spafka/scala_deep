package org.spafka.scala.implicitz

class defaultValue {

}


import java.util.Date
import java.util.concurrent.{Callable, Executors}

import collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

/** This class defines an interface for how to execute functions. */
trait ThreadStrategy {
  // pass in a function that returns a value and receive a function
  // that returns the value.   The actual function may be executed on another thread.
  def execute[A<:Unit](func: Function0[A]): Function0[A]
}

/** This class stores a dense two-dimensional matrix of finite size. */
class Matrix(private val repr: Array[Array[Double]]) {
  /** Access the row at idx (0-based).  Returns a column-ordered Seq of the values in the row. */
  def row(idx: Int): Seq[Double] = {
    repr(idx)
  }

  /** Access the column at idx (0-based).  Returns a row-ordered Seq of the values in the column. */
  def col(idx: Int): Seq[Double] = {
    repr.foldLeft(new ArrayBuffer[Double]()) {
      (buffer, currentRow) =>
        buffer.append(currentRow(idx))
        buffer
    } toArray
  }

  /** The number of rows in the matrix. */
  lazy val rowRank = repr.size
  /** The number of columns in the matrix. */
  lazy val colRank = if (rowRank > 0) repr(0).size else 0

  /** Pretty-prints the matrix */
  override def toString = "Matrix" + repr.foldLeft("") { (msg, row) => msg + row.mkString("\n|", " | ", "|") }
}

/** This defines a service to multiply two matrices together while swapping out a threading strategy. */
object MatrixService {
  /** This method will multiple two matrices.  It takes an implicit parameter that allows you to change the
    * threading strategy.
    */
  def multiply(a: Matrix, b: Matrix)(implicit threading: ThreadStrategy = ThreadStrategy.SameThreadStrategy): Matrix = {
    // Ensure the columns-rows line up for proper multipication.
    assert(a.colRank == b.rowRank)
    // Create a buffer we can use to store the results.  The size is determined by the row rank in a and
    // column rank in b.
    val buffer = new Array[Array[Double]](a.rowRank)
    for (i <- 0 until a.rowRank) {
      buffer(i) = new Array[Double](b.colRank)
    }

    // This helper function will compute the value stored at index (row,col) in the resulting matrix and place
    // that value in the buffer.
    def computeValue(row: Int, col: Int): Unit = {
      // Constructs a List of pairs of elements from the two matricies.
      val pairwiseElements =
        a.row(row).zip(b.col(col))
      // multiplies every row value by every column value.  The sum of products is the resulting value on the matrix.
      val products =
        for ((x, y) <- pairwiseElements)
          yield x * y
      val result = products.sum
      buffer(row)(col) = result
    }

    // Create a list of computations for every (row,col) result of the matrix.
    val computations = for {
      i <- 0 until a.rowRank
      j <- 0 until b.colRank
    } yield threading.execute { () => computeValue(i, j) } //此处传的是一个匿名内部类
    // Execute all computations *or* wait for threading to finish.
    computations.foreach(_ ())
    new Matrix(buffer)
  }
}

/** This is the companion object of the ThreadStrategy trait.
  * This defines various thread pool strategies that one can uses. */
object ThreadStrategy {

  /** This is a ThreadStrategy that will execute all functions on the local thread. */
  object SameThreadStrategy extends ThreadStrategy {
    def execute[A](func: Function0[A]) = {

      print(func)
      func
    }
  }

  /** This is a strategy that will execute all functions within a thread pool. */
  object ThreadPoolStrategy extends ThreadStrategy {
    val pool = Executors.newFixedThreadPool(java.lang.Runtime.getRuntime.availableProcessors)

    def execute[A](func: Function0[A]) = {
      // Submit a callable class to the thread bool.
      val future = pool.submit(new Callable[A] {
        def call(): A = {

          Console.println("Executing function on thread: " + Thread.currentThread.getName)
          println(func.getClass)
          func()
        }
      })
      // Create a function that will block when called and wait for the defered thread to finish execution.
      () => future.get() //仍然返回一个Function0[unit]
    }
  }

}

// for test
object testMartix {


  def main(args: Array[String]): Unit = {


    val a1: Array[Double] = Array(1, 2)
    val a2: Array[Double] = Array(2, 3)

    val azz = Array(a1, a2)
    val azz2 = Array(a1, a1)

    var matrix: Matrix = new Matrix(azz)

    println(matrix)
    println(matrix.row(1))
    println(matrix.col(1))
    println(matrix.colRank)
    println(matrix.rowRank)

    val mul = MatrixService.multiply(matrix, matrix)


    print(mul)
    import  ThreadStrategy.ThreadPoolStrategy

    val mul2 = MatrixService.multiply(matrix, matrix)(ThreadStrategy.ThreadPoolStrategy)
    println(mul2)

  }

}

object Main extends App {
  val javaVersion = () => sys.props("java.version")

  val anonfun0 = new Function0[String] {
    def apply(): String = sys.props("java.version")
  }

  //var f: C => C = (c: C) => new C
  println(javaVersion() == anonfun0())

}