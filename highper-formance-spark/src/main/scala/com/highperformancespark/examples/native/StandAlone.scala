package com.highperformancespark.examples.native

object StandAlone {
  def main(args: Array[String]) {
    //tag::systemLoadLibrary[]
    System.loadLibrary("highPerformanceSpark0")
    //end::systemLoadLibrary[]
    println(new SumJNI().sum(Array(1,2,3)))
  }
}
