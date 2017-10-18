#!/bin/bash

mvn clean install -Dmaven.test.skip=true tomcat7:run -Dmy.env=production&
