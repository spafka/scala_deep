#!/usr/bin/env bash

x=5; y=10
[ $x -gt $y ]
echo $?

x=15; y=10
test $x -gt $y
echo $?

x=1
[ $x -lt 10 ]
echo $?


if [ $x -eq  1 ]; then
   echo '111'

fi

[ $x -eq 1 ]
echo $?


