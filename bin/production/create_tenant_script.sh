#!/bin/bash
 

sudo cp -R /mnt/ebsvolume/repos/scripts/cloud_data_tenant_template/ /mnt/ebsvolume/repos/scripts/$1/ 
sudo chown -R vsftpd:ubuntu /mnt/ebsvolume/repos/scripts/$1/
echo 'script folder with name ' $1 + ' created'

sudo htpasswd -bd /etc/vsftpd/ftpd.passwd $1 $1
echo 'ftp user with name ' $1 + ' added'





