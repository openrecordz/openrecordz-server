
-- 
-- Struttura della tabella `request_logs`
-- 
CREATE TABLE `request_logs` (
  `id` int(11) NOT NULL auto_increment,
  `remote_host` varchar(255) default NULL COMMENT 'ex. druido.unile.it',
  `remote_addr` varchar(255) default NULL COMMENT 'ex. 87.7.33.250',
  `request_uri` text COMMENT 'ex. /erp-dev/fleets/track_veicoli/startTrack.do?a=12',
  `query_string` text COMMENT 'ex. id=37&start=false&...',
  `request_url` text COMMENT 'ex. http://www.real.it/erp-dev/start.do?id=37',
  `user_lang` varchar(10) default NULL,
  `user_agent` varchar(255) default NULL,
  `username` varchar(255) default NULL COMMENT 'ex. antonio@tundo',
--  `user_id` bigint(20) default NULL,
  `created_on` timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
--  `organization_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=latin1;


ALTER TABLE `request_logs` 
CHANGE COLUMN `user_agent` `user_agent` VARCHAR(1000) NULL DEFAULT NULL ;


ALTER TABLE `request_logs` 
ADD COLUMN `tenant` varchar(255) DEFAULT NULL;