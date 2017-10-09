#!/bin/bash
# Script Name : backup-cleaner.sh
# Version     : 1.0
# Last Update : 2011.dec.19
# Author      : Andrea Leo
# Note        : A backup script to clean a backup directory.
#
#				Requirements:
#				- root operative system credentials
#				- a directory to clean
# ------------------------------------------------------------------------------

# SET HERE CLEANER PARAMETERS
#------------------------------
BACKUP_DIR=/mnt/backup/backup
BACKUP_TO_KEEP=1


# Make sure only root can run script
if [[ $EUID -ne 0 ]]; then
	echo " "
   echo -e  "\nThis script must be run as root" 1>&2
   exit 0
fi

# Check parameter settings
if [ ! -d $BACKUP_DIR ]; then
	echo " "
	echo "ERROR: Cleaning aborted. Source directory not found. Make sure that $BACKUP_DIR exist!"
	exit 0
fi

NOW=$(date +%Y.%M.%d-%H:%m)

cd $BACKUP_DIR
echo ""
echo "Start cleaning of $BACKUP_DIR at: $NOW ..."
rm -rfv `ls -r1 | tail -n +$(($BACKUP_TO_KEEP+1))`
echo "Finished cleaning!"

echo "Backup cleaning avvenuto.controlla" | mail -s "Bkp clean Smart21" andrea.leo@frontiere21.it