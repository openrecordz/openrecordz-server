# Introduction
OpenRecordz is an OpenData as a Service platform written in Java and Spring that use MongoDB (NOSQL) database.  
See more here http://www.openrecordz.com

OpenRecordz is an OpenData cloud software with : 
* Powerful API to read and create opendata (datasets and records). Documentation here http://www.openrecordz.com/documentazione/api/
* Support LinkedData and RDF SPARQL 
* Full-stack with Mobile app skeleton (iOS and Android) 
* OpenSource. Source available on GitHub https://github.com/openrecordz 
* Social: Everyone can contribute to the opendata catalog

You can use :
* The cloud version here http://www.openrecordz.com. Create a free account and use the platform.
* The on-premise version. Read the installation documentation.




# Prerequisites
* Java 1.7
* Maven
* MongoDB 
* MYSQL

# Configuration

## MongoDB
* Create an `openrecordz` schema.
* Create the mongodb indexes with:

```
db.customData.createIndex({ _location: "2dsphere" })
db.customData.createIndex({"_tenants":1});
db.customData.createIndex({"_dataset_ref_id":1});
db.customData.createIndex({"_status":1});
db.customData.createIndex({"_createdby":1});
db.customData.createIndex({"_modifiedby":1});
```
	
* Create the mongodb fulltext index with:

```
db.customData.createIndex( { "$**": "text" }, { name: "ORTextIndex" },  { default_language: "italian" })
db.person.createIndex( { "$**": "text" }, { name: "ORPeopleTextIndex" },  { default_language: "italian" })
```

## MYSQL
Go to `./bin/dbscripts` and run:

```
$ mysql -uroot -proot < 1_create-schema.sql
$ mysql -uroot -proot openrecordz < 2_tenant.sql 
$ mysql -uroot -proot openrecordz < 3_scriptDB.sql
$ mysql -uroot -proot openrecordz < 4_spring-security-persistent-login.sql 
$ mysql -uroot -proot openrecordz < 5_spring-security.sql 
$ mysql -uroot -proot openrecordz < 6_url.sql     
```

## HornetQ 
* HornetQ data is automatically saved to ../openrecordz-data folder 
* If you want you can change it on ./src/main/resources/hornetq-configuration.xml

# Run 

## Run in development mode

run $ `mvn clean install tomcat7:run -Dmy.env=development`

## Run in production mode

run $ `mvn clean install tomcat7:run -Dmy.env=production&`

# Usage

Open the browser to : `http://localhost:9090/service/v1/datasets`

Read REST API documentation here: http://www.openrecordz.com/documentazione/api/















