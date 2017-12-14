import com.twitter.bijection.Injection
import com.twitter.bijection.avro.GenericAvroCodecs
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}

/*
 * Copyright 2017 Saxon State and University Library Dresden (SLUB)
 *
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

object Avroz {

  val USER_SCHEMA: String = "{" + "\"type\":\"record\"," + "\"name\":\"Iteblog\"," + "\"fields\":[" + "  { \"name\":\"str1\", \"type\":\"string\" }," + "  { \"name\":\"str2\", \"type\":\"string\" }," + "  { \"name\":\"int1\", \"type\":\"int\" }" + "]}"

  def main(args: Array[String]): Unit = {


    val parser = new Schema.Parser()
    val schema = parser.parse(USER_SCHEMA)
    val injection: Injection[GenericRecord, Array[Byte]] = GenericAvroCodecs.toBinary(schema)

    val avroRecord = new GenericData.Record(schema)
    avroRecord.put("str1", "Str 1-" )
    avroRecord.put("str2", "Str 2-")
    avroRecord.put("int1", 1)

    val injected=  injection.apply(avroRecord)

    val ori=  injection.invert(injected)

    ori.get


  }

}
