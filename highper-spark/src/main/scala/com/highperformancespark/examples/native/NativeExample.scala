package com.highperformancespark.examples.native

import org.apache.spark.rdd.RDD

object NativeExample {
  def jniSum(input: RDD[(String, Array[Int])]): RDD[(String, Int)] = {
    input.mapValues(values => new SumJNI().sum(values))
  }
}
