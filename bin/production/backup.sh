#!/bin/bash

cd /mnt/backup/backup

time_stamp=`date +%Y%m%d%H%M%S`
mkdir ${time_stamp}
cd ${time_stamp}

echo 'created backup directory :'
pwd

echo 'backing up mysql db..'
mysqldump shoppino > shoppino_dump_db.sql -u root -pshoppino
echo 'backup mysql db complete.'

echo 'backing up mongo db..'
mongodump --db shoppino
echo 'backup mongo db complete.'

echo 'backing up indexes..'
tar -zcvf indexes.tar.gz /home/ubuntu/apps/apache-solr-3.6.1/example/solr/conf/data
echo 'backup repo complete.'


echo 'backing up repos..'
tar -zcvf repo.tar.gz /mnt/ebsvolume/repos/
echo 'backup repo complete.'

echo 'backing up ROOT.war ..'
cp /home/ubuntu/apps/apache-tomcat-7.0.27/webapps/ROOT.war .
echo 'backup ROOT.war complete.'

echo 'backup complete...'

echo "Backup completato" | mail -s "Backup Smart21" andrea.leo@frontiere21.it