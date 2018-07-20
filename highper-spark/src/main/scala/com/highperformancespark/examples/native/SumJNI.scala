package com.highperformancespark.examples.native

//import ch.jodersky.jni.nativeLoader

//tag::sumJNIDecorator[]
//@nativeLoader("high-performance-spark0")
//end::sumJNIDecorator[]
// tag::sumJNI[]
class SumJNI {
  @native def sum(n: Array[Int]): Int
}
// end::sumJNI[]
