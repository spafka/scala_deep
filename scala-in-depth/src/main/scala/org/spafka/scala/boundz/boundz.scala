package org.spafka.scala.boundz

class boundz {

}

object boundz {

  // T<:Comparable[T] 相当于java里的extends ,限定了上界， 这里int并没有实现Comparable<T> 接口
  def min[T <: Comparable[T]](a: T, b: T) = {

    if (a.compareTo(b) <= 0) a else b
  }


  // 上下文界定   // int -> RichInt  RichInt里引用了 protected def ord = scala.math.Ordering.Int
  // 下面含有 IntOrdering 的一个实例隐式对象  implicit object Int
  // scalac 就对代码进行了增强编译，是代码能顺利编译通过

  def minner[T](a: T, b: T)(implicit ordering: Ordering[T]) = {

    val min = ordering.min(a, b)

    if (ordering.gt(a, b)) b else a
  }

  trait IntOrdering extends Ordering[Int] {
    def compare(x: Int, y: Int) =
      if (x > y) -1
      else if (x == y) 0
      else 1
  }

  // implicit object Int extends IntOrdering
  // 注调这段代码，scalac按就近原则，找到了RichInt下的隐式转换，而不再使用这个了
  // 这里对IntOrdering逻辑进行了修改


  //又因为在Predef预定义了从Int到RichInt的隐式转换，而RichInt是Ordered[Int]的子类型；
  // 所以在Predef定义的implicit Int => RichInt的隐式转换函数可作为隐式参数implicit order: T => Ordered[T]的隐式值。
  def minnerViewBound[T <% Ordered[T]](first: T, second: T) {
    if (first < second) first else second
  }

  // 视图界定就是下面这个的简化版本 隐士转换函数转化为 <% 本身可看为 T可以被视作Order[T] ,所以从上下文找一个符合T=>Order[T]的隐式转换函数
  def minnerViewBoundInDepth[T](first: T, second: T)(implicit order: T => Ordered[T]) {
    if (order(first) < second) first else second
    // 进一步简化为
    if ((first) < second) first else second

  }

  def main(args: Array[String]): Unit = {

    val stringMin = min("1", "2")
    println(stringMin)
    //  val intmin=min(1,2)
    //  println(intmin)
    println(minner(1, 2))


  }
}