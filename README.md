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
* On-premise version. Read the installation documentation.




# Prerequisites
* Java 1.7
* Maven
* MongoDB 

# Configuration


# Run 
Checkout the project and 

## Run in development mode

run `mvn clean install tomcat7:run -Dmy.env=development`

## Run in production mode

run `mvn clean install tomcat7:run -Dmy.env=production&`

# Usage

Open the browser to : `http://localhost:9090/service/v1/datasets`

Read REST API documentation here: http://www.openrecordz.com/documentazione/api/




# MongoDB Indexes
Create OpenRecordz mongodb indexes with:

```
db.customData.createIndex({ _location: "2dsphere" })
db.customData.createIndex({"_tenants":1});
db.customData.createIndex({"_dataset_ref_id":1});
db.customData.createIndex({"_status":1});
db.customData.createIndex({"_createdby":1});
db.customData.createIndex({"_modifiedby":1});
```
	
Create OpenRecordz mongodb fulltext index for all text fields with:

```
db.customData.createIndex( { "$**": "text" }, { name: "ORTextIndex" },  { default_language: "italian" })
db.person.createIndex( { "$**": "text" }, { name: "ORPeopleTextIndex" },  { default_language: "italian" })
```
     
   










