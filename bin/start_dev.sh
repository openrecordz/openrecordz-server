#!/bin/bash

#cd /home/ubuntu/pacchetti/smart21-server-v3/
#svn update
##mvn tomcat7:run -Dmy.env=production&

mvn clean install tomcat7:run -Dmy.env=development
