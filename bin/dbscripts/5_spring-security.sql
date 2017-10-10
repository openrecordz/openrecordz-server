create table users(username VARCHAR(50) NOT NULL PRIMARY KEY,password VARCHAR(50) NOT NULL,enabled BOOLEAN NOT NULL);

create table authorities(username VARCHAR(50) NOT NULL,authority VARCHAR(50) NOT NULL,CONSTRAINT FK_AUTHORITIES_USERS FOREIGN KEY(username) REFERENCES users(username));

create unique index ix_auth_username ON authorities(username,authority);

alter table authorities DROP FOREIGN KEY FK_AUTHORITIES_USERS;

ALTER TABLE authorities 
CHANGE COLUMN `authority` `authority` VARCHAR(250) NOT NULL ;
