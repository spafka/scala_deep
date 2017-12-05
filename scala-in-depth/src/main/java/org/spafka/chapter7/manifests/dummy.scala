package org.spafka.chapter7.manifests

class Test {
  def first[A : ClassManifest](x : Array[A]) = Array(x(0))
}
