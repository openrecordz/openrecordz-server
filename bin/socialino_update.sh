#!/bin/bash


cd /home/ubuntu/pacchetti/shoppino
svn update

cd lib
#mvn install:install-file -DgroupId=com.google.javapns  -DartifactId=javapns  -Dversion=2.2  -Dfile=JavaPNS_2.2.jar  -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=com.google.javapns  -DartifactId=javapns  -Dversion=2.3  -Dfile=JavaPNS_2.3_Beta_3.jar  -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=org.bouncycastle  -DartifactId=bouncycastle  -Dversion=1.0  -Dfile=bcprov-jdk15-146.jar  -Dpackaging=jar -DgeneratePom=true
cd ..

cp -R override/socialino/* .

cp /home/ubuntu/pacchetti/shoppino/src/main/webapp/resources/robots-preprod.txt /home/ubuntu/pacchetti/shoppino/src/main/webapp/resources/robots.txt  
echo 'replaced robots.txt'
cp /home/ubuntu/pacchetti/shoppino/configuration/apache-tomcat-7.0.27/conf/Catalina/localhost/ROOT.xml.preprod /home/ubuntu/pacchetti/shoppino/src/main/webapp/META-INF/context.xml


mvn clean tomcat7:deploy -Dmaven.test.skip=true -Dmaven.tomcat.update=true 

#see on pom.xml
#-Dmaven.tomcat.tag=${time_stamp}

