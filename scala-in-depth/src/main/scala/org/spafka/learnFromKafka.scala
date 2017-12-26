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

package org.spafka

import java.util.Date


object learnFromKafka {


  def createObject[T <: AnyRef](className: String, args: AnyRef*): T = {
    val klass = Class.forName(className, true, Thread.currentThread().getContextClassLoader).asInstanceOf[Class[T]]
    val constructor = klass.getConstructor(args.map(_.getClass): _*)
    constructor.newInstance(args: _*)
  }

  def main(args: Array[String]): Unit = {


    val date: Date = createObject[Date]("java.util.Date")
    date

  }


}
