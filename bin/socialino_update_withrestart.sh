#!/bin/bash


cd /home/ubuntu/pacchetti/shoppino
svn update

cd lib
mvn install:install-file -DgroupId=com.google.javapns  -DartifactId=javapns  -Dversion=2.2  -Dfile=JavaPNS_2.2.jar  -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=org.bouncycastle  -DartifactId=bouncycastle  -Dversion=1.0  -Dfile=bcprov-jdk15-146.jar  -Dpackaging=jar -DgeneratePom=true
cd ..

cp -R override/socialino/* .
echo 'updated socialino'

cp /home/ubuntu/pacchetti/shoppino/src/main/webapp/resources/robots-preprod.txt /home/ubuntu/pacchetti/shoppino/src/main/webapp/resources/robots.txt  
echo 'replaced robots.txt'
cp /home/ubuntu/pacchetti/shoppino/configuration/apache-tomcat-7.0.27/conf/Catalina/localhost/ROOT.xml.preprod /home/ubuntu/pacchetti/shoppino/src/main/webapp/META-INF/context.xml


mvn clean install -Dmaven.test.skip=true
cd target
echo 'coping ROOT.war'
cp ROOT.war /home/ubuntu/apps/apache-tomcat-7.0.27/webapps/
cd /home/ubuntu/apps/apache-tomcat-7.0.27/webapps/
echo 'removing ROOT'
rm -rf ROOT
echo 'starting tomcat'
#killall -9 java

T_PID=$(ps aux | grep tomcat | awk 'NR==1{print $2}')
echo 'tomcat pid : ' $T_PID
kill -9 $T_PID

/home/ubuntu/apps/apache-tomcat-7.0.27/bin/startup.sh

