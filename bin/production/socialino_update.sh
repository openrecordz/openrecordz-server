#!/bin/bash

cd /mnt/backup/backup

time_stamp=`date +%Y%m%d%H%M%S`
mkdir ${time_stamp}
cd ${time_stamp}

echo 'backing up ROOT.war ..'
cp /home/ubuntu/apps/apache-tomcat-7.0.27/webapps/ROOT.war .
echo 'backup ROOT.war complete on ' ${time_stamp}

cd /home/ubuntu/pacchetti/shoppino
svn update

cd lib
#mvn install:install-file -DgroupId=com.google.javapns  -DartifactId=javapns  -Dversion=2.2  -Dfile=JavaPNS_2.2.jar  -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=com.google.javapns  -DartifactId=javapns  -Dversion=2.3  -Dfile=JavaPNS_2.3_Beta_3.jar  -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=org.bouncycastle  -DartifactId=bouncycastle  -Dversion=1.0  -Dfile=bcprov-jdk15-146.jar  -Dpackaging=jar -DgeneratePom=true
cd ..

cp -R override/socialino/* .

mvn clean tomcat7:deploy -Dmaven.test.skip=true -Dmaven.tomcat.update=true 

#see on pom.xml
#-Dmaven.tomcat.tag=${time_stamp}

echo "Aggiornamento avvenuto.controlla" | mail -s "Aggiornamento Smart21" andrea.leo@frontiere21.it