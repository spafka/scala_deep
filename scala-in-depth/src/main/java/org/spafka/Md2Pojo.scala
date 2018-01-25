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

object Md2Pojo {


  def md2pojo(md:String) = {
     val pojo: Array[String] = md.split("\n").map(_.split("\t")).map(x=>{

     var str:String= x(1)
     var uped= str.replace(str.substring(0, 1), str.substring(0, 1).toUpperCase())

       var _a=""
       if(x(2).equals("是")){
         _a="@NotNull\n"
       }

       if (uped.equals("String") && _a.contains("@")){
         _a="@NotBlank\n"
       }


       val commect="/**\n"+"*"+x(4)+" - "+x(5)+"\n"+"*/"

       if(uped.contains("St")) uped="String"
       if(uped.contains("In")) uped="Integer"
       if(uped.contains("Lo")) uped="Long"

     val ret= commect+ _a + "private " +uped+" "+(x(0))+";"
     ret
    })

    pojo.foreach(println(_))

  }

  def main(args: Array[String]): Unit = {
    md2pojo("idName\tString\t是\t-\t姓名\t仇楚云\nacctId\tString\t是\t-\t银行卡号\t14567656789909\nbankId\tint\t是\t-\t银行唯一ID\t1231231\nbankName\tint\t是\t-\t银行名称\t中国银行\nphone\tString\t是\t-\t银行卡预留手机号\t15757176754\ntoken\tstring\t是\t-\t验证码token\tdfasdfdasfdas324132434324\ncode\tstring\t是\t-\t短信验证码\t232398")
  }

}
