package org.spafka.chapter5.scope
package object foo {
  implicit def foo = new Foo
}


class Foo {
    override def toString = "FOO!"
  }
