#!/bin/bash
 
 

if line=$(grep "Permgen" /home/ubuntu/apps/apache-tomcat-7.0.27/logs/shoppino.log)
then
    #echo "Found line $line"
else
    mail -s "smart21 Permgen" andrea.leo@frontiere21.it
fi

 
