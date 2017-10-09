#!/bin/bash


cd /home/ubuntu/pacchetti/image-repo
svn update
cp /home/ubuntu/pacchetti/shoppino/configuration/imagerepo-configuration.properties /home/ubuntu/pacchetti/image-repo/src/main/resources/META-INF/imagerepo/imagerepo-configuration.properties
mvn clean
#mvn test
#mvn install
#mvn tomcat:run
mvn tomcat7:deploy -Dmaven.test.skip=true -Dmaven.tomcat.update=true

#cd /home/ubuntu/pacchetti/shoppino
#svn update
#mvn clean
##mvn install
#mvn tomcat:run
