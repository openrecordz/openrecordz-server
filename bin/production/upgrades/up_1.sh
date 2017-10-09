#!/bin/bash

#move solr from tomcat to jetty

cd /home/ubuntu/apps/apache-tomcat-7.0.27/conf/Catalina/localhost
mv solr.xml solr.xml_OLD

cd /home/ubuntu/apps/apache-tomcat-7.0.27/webapps
mv solr ../other_webapp/
mv solr ../other_webapp/

/etc/init.d/smart21d restart
echo "Aggiornamento avvenuto.controlla" | mail -s "Aggiornamento Smart21" andrea.leo@frontiere21.it