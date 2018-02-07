#!/usr/bin/env bash
nohup  /usr/local/sbin/mesos-slave \
       --master=127.0.0.1:5050   \
        --work_dir=/tmp/mesos/agent > /dev/null 2>&1 &  

