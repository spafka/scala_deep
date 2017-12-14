#!/usr/bin/env bash

# Scriptname: forloop.sh
for name in Tom Dick Harry Joe
do
   echo "Hi $name"
done
echo "out of loop"


for name in `cat cluster.txt`
do
   echo $name
done
