#!/bin/bash


#cd /home/ubuntu/pacchetti/image-repo
#svn update
#mvn clean
##mvn install
#mvn tomcat:run

cd /home/ubuntu/pacchetti/shoppino
svn update
#mvn clean
#mvn test
#mvn install
mvn clean tomcat7:deploy -Dmaven.test.skip=true -Dmaven.tomcat.update=true

