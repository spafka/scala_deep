
docker ps -a
cd  /Users/spafka/Desktop/flink/spark_deep/docker/src/main/resources/kafka/docker-cluster

docker-compose up -d

docker exec -it kafka0 /bin/bash

bin/kafka-topics.sh --zookeeper zookeeper0:2181 --create --topic normal-topic --partitions 2 --replication-factor 1
bin/kafka-topics.sh  --list  --zookeeper zookeeper0:2181