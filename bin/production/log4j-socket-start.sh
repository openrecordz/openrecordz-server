#/bin/bash

#java -jar /home/andrea/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar org.apache.log4j.net.SimpleSocketServer 4445 log4j.properties
java -classpath /home/andrea/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar org.apache.log4j.net.SimpleSocketServer 4445 "/home/andrea/work/android dev/android-projects/shoppino-ultima/src/main/resources/log4j.properties"
