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

package org.spafka.scala

object Pojo2Md {
  def main(args: Array[String]): Unit = {


    val s="   /**\n     * 创建时间\n     */\n    private Date createTime;\n    /**\n     * 身份证name\n     */\n    private String idname;\n    /**\n     * 商户Id\n     */\n    private Long mid;\n    /**\n     * 1：个人用户；2：企业\n     */\n    private Integer type;\n    /**\n     * 个人审核 身份证\n     */\n    private String idcard;\n    /**\n     * 审核状态 1认证中/待认证，2认证成功，3认证失败\n     */\n    private Integer authStatus;\n    /**\n     * 营业执照编号\n     */\n    private String licenseid;\n    /**\n     * 营业执照名称（公司名称）\n     */\n    private String licensename;\n    /**\n     * 代理商\n     */\n    private String company;\n    /**\n     * 商户名称\n     */\n    private String title;\n    /**\n     * 商户联系方式\n     */\n    private String contact;"

    val tuples = s.
      split(";")
      .map(x => x.replaceAll("\\*", "").replaceAll("/", "").replaceAll("\n", ""))
      .map(x => x.split("private"))
      .map(x => {
        val y = x(1).split(" ")
        (x(0), y(2), y(1))
      })
    tuples


    val strings = tuples.map(x => {
      x._2 + " | " + x._3 + " | " + "是" + " | " + "-" +" | "+ x._1+  " | "
    })
    strings.foreach(println(_))

  }
}
