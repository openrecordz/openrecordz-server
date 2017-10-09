#!/bin/bash

cd /home/ubuntu/pacchetti/smart21-server-v3/
svn update
mvn clean install -Dmaven.test.skip=true tomcat7:run -Dmy.env=production&

##mvn tomcat7:run -Dmy.env=development
