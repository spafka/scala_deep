#!/usr/bin/env bash

spark-submit \
  --class org.spafka.sql.chapter1.DfExample \
  --master  yarn-cluster \
  --executor-memory 2G \
  --total-executor-cores 2\
  --deploy-mode  cluster \
  /Users/spafka/Desktop/flink/spark_deep/spark-sql-streaming/target/spark-sql-streaming-1.0-releases.jar