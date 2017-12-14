#!/usr/bin/env bash

# This script is to test the usage of read
# Scriptname: ex4read.sh
echo "=== examples for testing read ==="
echo -e "What is your name? \c"
read name
echo "Hello $name"
echo
echo -n "Where do you work? "
read
echo "I guess $REPLY keeps you busy!"
echo
read -p "Enter your job title: "#自动读给REPLY
echo "I thought you might be an $REPLY."
echo
echo "=== End of the script ==="
