#!/usr/bin/env bash

echo -n "Do you wish to proceed [y/n]: "
read ans
case $ans in
   y|Y|yes|Yes)
     echo "yes is selected"
     ;;
   n|N|no|No)
     echo "no is selected"
	 ;;
   *)
     echo "`basename $0`: Unknown response"
	 exit 1
     ;;
esac


