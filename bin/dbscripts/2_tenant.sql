CREATE TABLE `tenant` (
    `name` VARCHAR( 150 ) NOT NULL
);



ALTER TABLE `tenant` 
ADD COLUMN `description` varchar(350) DEFAULT NULL;

ALTER TABLE `tenant` 
ADD COLUMN `created_on`  timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP;