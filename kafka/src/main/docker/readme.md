
docker ps -a
cd  /Users/spafka/Desktop/flink/spark_deep/docker/src/main/resources/kafka/docker-cluster

docker-compose up -d

docker exec -it kafka0 /bin/bash

bin/kafka-topics.sh --zookeeper zookeeper0:2181 --create --topic test --partitions 3 --replication-factor 3
bin/kafka-topics.sh  --list  --zookeeper zookeeper0:2181