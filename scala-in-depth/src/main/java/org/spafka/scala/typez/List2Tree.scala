/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spafka.scala.typez

import scala.collection.convert.

abstract class Node[T <: Node[_]] {
  var child: java.util.List[T];
}


case class myNode(var id: Long, var pid: Long, var list: java.util.List[Node] = null) extends Node {

  override var child: java.util.List[Node] = list
}

object List2Tree {

  def mkTree(p:Node[_],list:java.util.List[Node],function2: Function2[Node[_],Node[_],Boolean])={

    list.

  }

  val p = myNode(1, 0)
  val s = myNode(2, 1)



}

