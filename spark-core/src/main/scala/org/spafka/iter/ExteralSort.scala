package org.spafka.iter

import java.util.Comparator

import scala.collection.mutable
import scala.math.Ordering
import scala.util.Random

/***
  *
  * spark ExternalSort测试，reduce端排序时一定要保证，每个排序的iter已经是排序了的
  * 然后使用PriorityQueue，从每个iter头部取出最小的值加入到heap，进行排序，这样全局就有序了
  * @param ordering
  * @tparam K
  * @tparam V
  * @tparam C
  */
class ExternalSorter[K, V, C](implicit ordering: Option[Ordering[K]] = None) {

  /**
    * Merge-sort a sequence of (K, C) iterators using a given a comparator for the keys.
    */
  def mergeSort(iterators: Seq[Iterator[Product2[K, C]]], comparator: Comparator[K])
  : Iterator[Product2[K, C]] = {
    val bufferedIters = iterators.filter(_.hasNext).map(_.buffered)
    type Iter = BufferedIterator[Product2[K, C]]
    val heap = new mutable.PriorityQueue[Iter]()(new Ordering[Iter] {
      // Use the reverse of comparator.compare because PriorityQueue dequeues the max
      override def compare(x: Iter, y: Iter): Int = -comparator.compare(x.head._1, y.head._1)
    })
    heap.enqueue(bufferedIters: _*) // Will contain only the iterators with hasNext = true
    new Iterator[Product2[K, C]] {
      override def hasNext: Boolean = !heap.isEmpty

      override def next(): Product2[K, C] = {
        if (!hasNext) {
          throw new NoSuchElementException
        }
        val firstBuf = heap.dequeue()
        val firstPair = firstBuf.next()
        if (firstBuf.hasNext) {
          heap.enqueue(firstBuf)
        }
        firstPair
      }
    }
  }
}

object ExternalSorter {
  def main(args: Array[String]): Unit = {

    // @SEE http://blog.csdn.net/qq_35326718/article/details/72866180
    val pq = collection.mutable.PriorityQueue(1, 2, 5, 3, 7)
    println(pq) // elements probably not in order
    println(pq.clone.dequeueAll) // prints Vector(7, 5, 3, 2, 1)

    val testData: Iterator[(Int, Int)] = Array(1,2,3,4,5,6,7,8,9,10).map((_,1)).iterator
    val testData2: Iterator[(Int, Int)] = Array(1,2,3,4,5,6,7,8,9,10).map((_,1)).iterator

    val sorter = new ExternalSorter[Int, Int, Int]()

    trait IntOrdering extends Ordering[Int] {
      def compare(x: Int, y: Int) = if (x < y) -1
      else if ((x == y)) {
        0
      }
      else {
        1
      };
    }

    val ordering = new IntOrdering {}
    sorter.mergeSort(Seq(testData, testData2), ordering).foreach(println)


  }

}


