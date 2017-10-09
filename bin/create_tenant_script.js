#!/bin/bash


mkdir /mnt/ebsvolume/repos/scripts/$1/
echo 'script folder with name ' $1 + ' created'

touch /mnt/ebsvolume/repos/scripts/$1/main.js

chmod -R a+w /mnt/ebsvolume/repos/scripts/$1/main.js

echo 'permission setted for script folder with name ' $1

sudo htpasswd -bd /etc/vsftpd/ftpd.passwd $1 $1
echo 'ftp user with name ' $1 + ' added'





