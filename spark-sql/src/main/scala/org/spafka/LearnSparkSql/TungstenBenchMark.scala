package org.spafka.LearnSparkSql

import org.apache.spark.internal.Logging
import org.apache.spark.sql.internal.SQLConf.buildConf

object TungstenBenchMark extends Logging{

  import Common.benchmark
  def main(args: Array[String]): Unit = {


    val spark = Common.spark




    logInfo(
      s"""
         | SQLConf.scala in SPARK2.*
         |  val WHOLESTAGE_CODEGEN_ENABLED = buildConf("spark.sql.codegen.wholeStage")
         |    .internal()
         |    .doc("When true, the whole stage (of multiple operators) will be compiled into single java" +
         |      " method.")
         |    .booleanConf
         |    .createWithDefault(true)
         |
       """.stripMargin)






//    benchmark("Spark 1.6 sum") {
//
//      spark.conf.set("spark.sql.codegen.wholeStage", false)
//      spark.range(1000L * 1000 * 1000).selectExpr("sum(id)").show()
//
//    }
//
//    benchmark("Spark 2.* sum") {
//
//      spark.conf.set("spark.sql.codegen.wholeStage", true)
//      spark.range(1000L * 1000 * 1000).selectExpr("sum(id)").show()
//
//    }
//
    benchmark("Spark 1.6 join ") {

      spark.conf.set("spark.sql.codegen.wholeStage", false)
      spark.range(1000L * 1000 *1000).join(spark.range(1000L).toDF(), "id").count()


//      spark.range(1000L * 1000 * 1000).join(spark.range(1000L).toDF(), "id").selectExpr("count(*)").explain(true)

    }

    benchmark("Spark 2.* join ") {

      spark.conf.set("spark.sql.codegen.wholeStage", true)


      spark.range(1000L * 1000 * 1005).join(spark.range(1040L).toDF(), "id").count()

  //    spark.range(1000L * 1000 * 1000).join(spark.range(1000L).toDF(), "id").selectExpr("count(*)").explain(true)

    }


  }
}
