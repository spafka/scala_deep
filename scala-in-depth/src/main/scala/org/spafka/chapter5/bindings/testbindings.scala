package org.spafka.chapter5.bindings

// This object contains the bindings/scope tests from Chapter 5.   See
// externalbindings.scala for other defintions used by this file.
object Test {

  def main(arg : Array[String]) : Unit = {
    testSamePackage()
    testWildcardImport()
    testExplicitImport()
    testInlineDefinition()
  }

  // This looks for a binding 'x' within the same package (test) as this scope.
  def testSamePackage() {
     println(x)  // prints: Externally bound x object in package test
  }

  // This defines a new scope with an 'x' binding that we can import with a wildcard.
  object Wildcard {
    def x = "Wildcard Import x"
  }

  // This java.java.util.function will print the value in the binding 'x' after importing from the Wildcard object
  // using a wildcard import.
  def testWildcardImport() {
    println(x)  // prints: Wildcard Import x
  }

  // This defines another binding of 'x' that we can import explicitly.
  object Explicit {
    def x = "Explicit Import x"
  }

  def testExplicitImport() {
    println(x)  // prints: Explicit Import x
  }

  // This defines an inline binding for x.  Note that with all the imports, there are no ambiguous naming conflicts.
  def testInlineDefinition() {
    val x = "Inline definition x"
    println(x)  // prints:  Inline definition x
  }
}
