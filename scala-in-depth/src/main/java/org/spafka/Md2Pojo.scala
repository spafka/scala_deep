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
    md2pojo("oemName\tstring\t是\t-\toem名称\t衫德\nbrandName\tstring\t是\t-\t品牌名称\t我去\nwelcomeWord\tstring\t是\t-\t欢迎语\t你好啊\nadminLogo\tstring\t是\t-\t总后台logo\twww.baidu.com(图片链接)\nsubDomain\tstring\t是\t-\t附属总后台域名\twww.heh.com\nwebLogo\tstring\t是\t-\t网页标题logo\twww.baidu.com(图片链接)\nqrcodeUrl\tstring\t是\t-\t二维码海报文案\t收钱？\nqrCodeLogo\tstring\t是\t-\t二维码logo\twww.baidu.com(图片链接)\noemLoginOutDomain\tstring\t否\t-\toem退出链接\twww.oo.com\naddeliveryPhone\tstring\t否\t-\t广告投放联系方式\t17682341111\nagentLogo\tstring\t是\t-\t代理商后台logo\twww.baidu.com(图片链接)\nagentLoginOutDomain\tstring\t否\t-\t代理商退出链接\twww.oo.com\nservicePhone\tstring\t是\t-\t服务热线\t15270065283\nuserLogo\tstring\t是\t-\t商后台logo\twww.baidu.com(图片链接)\nbottomMsg\tstring\t是\t-\t网页底部文案\t我去 呵呵\nvoiceRemind\tstring\t否\t-\t收款提示文案\t收我钱？ 呵呵\nvideoOnOff\tint\t否\t-\t帮助视频 0\t0关闭 1开启\nvoiceOnOff\tint\t否\t-\t收款语音 0\t0关闭 1开启\nredPacketMsg\tstring\t是\t-\t红包内容文案\t打开有红包\nredPacketNotice\tstring\t否\t-\t红包推送文案\t打开有红包啊\norderSuccessNotice\tstring\t否\t-\t订单支付成功通知\t成功支付了\nqrcodeBackground\tstring\t否\t-\t二维码自定义背景\twww.baidu.com(图片链接)\nmerchantLoginOutDomain\tstring\t否\t-\t商户后台退出链接\twww.oo.com\nwapIndexTitle\tstring\t否\t-\th5名称\t付呗copy1\nwapDefaultHeader\tstring\t否\t-\t商家会员头像\twww.oo.com\nwapDefaultNickname\tstring\t是\t-\t顾客默认昵称\t吃货\nwapUserHeader\tstring\t否\t-\t顾客默认头像\twww.oo.com\nwxMpAppid\tstring\t否\t-\t微信公众号appID\t4321423\nwxMpAppsecret\tstring\t否\t-\t微信公众号appSecret\t4321423\nwapActiveBand\tstring\t否\t-\t短信设置（品牌名称）\t大保健\nwxOfficialAccount\tstring\t否\t-\t公众号名称\t爱生活啊\nappCustomerMobile\tstring\t否\t-\tapp客服电话\t18709098888\nappBindText\tstring\t否\t-\t绑卡提示文案\t欢迎绑卡\nprotocolTitle\tstring\t否\t-\t绑卡协议名称\t词穷了\nprotocolContent\tstring\t否\t-\t绑卡协议文案\t词穷了啊\nmanagerUsername\tstring\t否\t-\t管理员账号\t词穷了啊\nmanagerPassword\tstring\t否\t-\t管理员密码\tqwert123")
  }

}
