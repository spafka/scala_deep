package org.spafka.sql.chapter4

import org.apache.spark.rdd.RDD
import org.spafka.sql.Common

object s {


  def main(args: Array[String]): Unit = {


    val spark = Common.spark

    val sc = spark.sparkContext

    val path = "spark-sql/src/main/scala/org/spafka/LearnSparkSql/chapter4/20_newsgroups/comp.graphics"
    val data: RDD[(String, String)] = sc.wholeTextFiles(path)
    var output : org.apache.spark.rdd.RDD[(String, String, Int)] = sc.emptyRDD
    val files = data.map { case (filename, content) => filename}
    def Process(filename: String): org.apache.spark.rdd.RDD[(String, String, Int)]= {

      val fpath = filename.split('/').last;
      val lines = sc.textFile(filename);
      val counts = lines.flatMap(line => line.split(" ")).map(word => word).map(word => (word, 1)).reduceByKey(_ + _);
      val word_counts = counts.map( x => (fpath,x._1, x._2));
      println("Processing:" + filename+word_counts)
      word_counts
    }
    val buf = scala.collection.mutable.ArrayBuffer.empty[org.apache.spark.rdd.RDD[(String, String, Int)]]
    output = sc.union(buf.toList);
    files.collect.foreach( filename => { output.union(Process(filename))})
    output.cache()

    output.take(5).foreach(println(_))




  }
}
