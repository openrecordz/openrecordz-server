# Prerequisites
* Java
* Maven
* MongoDB

# Run 
Checkout the project and 

## Run in development mode

run 'mvn clean install tomcat7:run -Dmy.env=development'

## Run in production mode

run 'mvn clean install tomcat7:run -Dmy.env=production&'.





# MongoDB Indexes
Create OpenRecordz mongodb indexes with:

db.customData.createIndex({ _location: "2dsphere" })
db.customData.createIndex({"_tenants":1});
db.customData.createIndex({"_dataset_ref_id":1});
db.customData.createIndex({"_status":1});
db.customData.createIndex({"_createdby":1});
db.customData.createIndex({"_modifiedby":1});

	
Create OpenRecordz mongodb fulltext index for all text fields with:
db.customData.createIndex( { "$**": "text" }, { name: "ORTextIndex" },  { default_language: "italian" })
db.person.createIndex( { "$**": "text" }, { name: "ORPeopleTextIndex" },  { default_language: "italian" })

     
   










