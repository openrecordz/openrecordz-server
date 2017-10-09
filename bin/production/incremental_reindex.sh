#!/bin/bash
cd /home/ubuntu/pacchetti/shoppino/
 mvn exec:java -Dexec.mainClass="shoppino.indexer.IncrementalReindexer" -Dmy.env=production -Dlog4j.configuration=log4j-simple.properties -Dexec.args="$1"
 
 #-D-Xmx512m -D-XX:MaxPermSize=128m
