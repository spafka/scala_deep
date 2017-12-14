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

import java.util.regex.Pattern

object Sql2Md {


  def sql2Md(sql:String) = {

    val sql2 =sql.replaceAll("\n  ","").split(",").map(x => x.split(" "));

    val regEx = "[^0-9]" //匹配指定范围内的数字

    //Pattern是一个正则表达式经编译后的表现模式
    val p = Pattern.compile(regEx)

    sql2.map(
      x=> {
        val name=camelCaseName(x.head.replaceAll("`",""))
        val _intz= p.matcher(x(1)).replaceAll(" ").trim()

        var  _type = ""
        x.foreach( x=> {
          if (x.contains("char")) _type = "string"
          if (x.contains("int")) _type = "int"
        })

        val allString =x.mkString("")
        var _NULL="否"
        if (allString.contains("NOTNULL")) _NULL="是"

        var _COMMENT=""
        for ( i <- 0 until x.length){
          if (x(i).equals("COMMENT"))  _COMMENT=x(i+1).replaceAll("'","")
        }

        name+" | "+_type+" | "+_NULL+" | "+_intz+" | "+_COMMENT+" | "
      }
    ).foreach(println(_))





    /**
      * 转换为下划线
      *
      * @param camelCaseName
      * @return
      */
    def underscoreName(camelCaseName: String) = {
      val result = new StringBuilder
      if (camelCaseName != null && camelCaseName.length > 0) {
        result.append(camelCaseName.substring(0, 1).toLowerCase)
        var i = 1
        while ( {
          i < camelCaseName.length
        }) {
          val ch = camelCaseName.charAt(i)
          if (Character.isUpperCase(ch)) {
            result.append("_")
            result.append(Character.toLowerCase(ch))
          }
          else result.append(ch)

          {
            i += 1; i - 1
          }
        }
      }
      result.toString
    }

    /**
      * 转换为驼峰
      *
      * @param underscoreName
      * @return
      */
    def camelCaseName(underscoreName: String) = {
      val result = new StringBuilder
      if (underscoreName != null && underscoreName.length > 0) {
        var flag = false
        var i = 0
        while ( {
          i < underscoreName.length
        }) {
          val ch = underscoreName.charAt(i)
          if ("_".charAt(0) == ch) flag = true
          else if (flag) {
            result.append(Character.toUpperCase(ch))
            flag = false
          }
          else result.append(ch)

          {
            i += 1; i - 1
          }
        }
      }
      result.toString
    }

  }


  def main(args: Array[String]): Unit = {

    sql2Md("`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',\n  `o_menu_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '旧菜单节点ID',\n  `menu_id` bigint(20) unsigned NOT NULL COMMENT '节点ID',\n  `action` varchar(64) NOT NULL DEFAULT '' COMMENT '操作（action）',\n  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单名称',\n  `is_display` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '显示类型 0隐藏  1显示',\n  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注说明',\n  `pid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '父ID',\n  `level` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '节点等级',\n  `sort` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排序权重',\n  `is_self_support` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否自营专属 1是 0否',\n  `is_del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除 0否 1是',\n  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'")
  }

}
