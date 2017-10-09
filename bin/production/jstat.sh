#!/bin/bash

#jstatd -J-Djava.security.policy=/home/ubuntu/apps/apache-tomcat-7.0.27/bin/jstatd.policy -J-Djava.rmi.server.hostname=ciaotrip.it
jstatd -J-Djava.security.policy=/home/ubuntu/pacchetti/shoppino/script/production/jstatd.policy -J-Djava.rmi.server.hostname=ciaotrip.it &
