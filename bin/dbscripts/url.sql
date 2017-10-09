CREATE  TABLE url (
  uid VARCHAR(250) NOT NULL ,
  url VARCHAR(500) NOT NULL ,
  PRIMARY KEY (uid) );
  
  create index urlIndex on url(uid, url);