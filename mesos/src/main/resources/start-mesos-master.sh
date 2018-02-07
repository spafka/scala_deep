#!/usr/bin/env bash
nohup /usr/local/sbin/mesos-master --registry=in_memory --ip=127.0.0.1 > /dev/null 2>&1 &