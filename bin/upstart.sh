#!/bin/bash


git pull

pkill -9 java

mvn clean install -Dmaven.test.skip=true tomcat7:run -Dmy.env=production&
