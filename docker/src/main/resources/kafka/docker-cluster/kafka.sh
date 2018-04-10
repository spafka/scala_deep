#!/usr/bin/env bash

rm -rf /tmp/kafka
docker stop kafka0 kafka1 kafka2 zookeeper0 zookeeper1 zookeeper2
docker rm kafka0 kafka1 kafka2 zookeeper0 zookeeper1 zookeeper2
docker-compose up -d