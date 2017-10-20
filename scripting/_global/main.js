function g_getalltenants(req,res,params) {
  var tenants=rdbService.select("jdbc:mysql://localhost/smart21?user=root&password=shoppino","select * from tenant;");
   model.put("tenants",tenants);
   return model;
}


function previewcsv(req,res,params) {
	importClass(Packages.java.lang.Character);
//importClass(Packages.java.lang.Boolean)
	
	var charSeparator = ",";
	if (req.getParameter("charseparator")!=null)
			charSeparator = req.getParameter("charseparator");

	logger.info("charSeparator: "+ charSeparator); 

	 var file =req.getParameter("file");
	 logger.info("file : " + file);

	var withHeader=true;
	if (req.getParameter("withheader")!=null)
			withHeader = Boolean(req.getParameter("withheader"));

//withHeader=false;
	logger.info("withheader: "+ withHeader); 


 	//var firstLines = _utils.get("csv").parse(environmentService.getFileFilesystemPath()+info.get("tenantName")+"/"+file, withHeader ,null,java.lang.Character(charSeparator.charAt(0)), 20);
	var firstLines = _utils.get("csv").parse("/var/lib/openrecordz/files/"+info.get("tenantName")+"/"+file, withHeader ,null,java.lang.Character(charSeparator.charAt(0)), 20);
	 logger.info("firstLines : " + firstLines);
	model.put("firstLines",firstLines);
   return model;
}


function parsecsvheader(req,res,params) {
    importClass(Packages.shoppino.service.impl.ConfigThreadLocal);
	
	   ConfigThreadLocal.get().put("mailEnabled", false);

	
	 var file =req.getParameter("file");
	 logger.info("file : " + file);


	var charSeparator = ",";
	if (req.getParameter("charseparator")!=null)
			charSeparator = req.getParameter("charseparator");

	logger.info("charSeparator: "+ charSeparator); 
	 

	var dsSlug=req.getParameter("ds");
	logger.info("dsSlug: "+ dsSlug); 
	var dataset = null;
	if (dsSlug!=null ) {
		dataset = customDataService.findByQueryInternal("{\"_slug\":\"" +dsSlug+"\"}", "dataset").get(0);
	}

	var withHeader=true;
	if (req.getParameter("withheader")!=null)
			withHeader = Boolean(req.getParameter("withheader"));

	logger.info("withheader: "+ withHeader); 


	 var headers = _utils.get("csv").getHeader("/var/lib/openrecordz/files/"+info.get("tenantName")+"/"+file,withHeader, java.lang.Character(charSeparator.charAt(0)));
	 //var headers = _utils.get("csv").getHeader(environmentService.getFileFilesystemPath()+info.get("tenantName")+"/"+file,withHeader, java.lang.Character(charSeparator.charAt(0)));
	 
	 
	 logger.info("headers : " + headers);

	model.put("headers",headers);
	model.put("dataset",dataset);

	return model;
}


function parsecsv (req,res,params) {
	importClass(Packages.shoppino.service.impl.ConfigThreadLocal);
	importClass(Packages.org.apache.commons.io.IOUtils);
	
	   ConfigThreadLocal.get().put("mailEnabled", false);
	   ConfigThreadLocal.get().put("scriptingEnabled", false);

	var dsName=req.getParameter("ds");
	
	  var fileds =req.getParameter("file");
	 logger.info("fileds : " + fileds);


	var charSeparator = ",";
	if (req.getParameter("charseparator")!=null)
			charSeparator = req.getParameter("charseparator");

	logger.info("charSeparator: "+ charSeparator); 
	 

	var withHeader=true;
	if (req.getParameter("withheader")!=null)
			withHeader = Boolean(req.getParameter("withheader"));

	logger.info("withheader: "+ withHeader); 


/*
	var mappingArray =req.getParameterValues("mapping");
	if (mappingArray!=null) {
		for(var i=0;i<mappingArray.length;i++){
		 	logger.info("mappingfields : " + mappingArray[i]);
		}
	}
*/


	var reqBody=IOUtils.toString(req.getReader());
	logger.info(reqBody);
	var reqBodyAsJson = JSON.parse(reqBody);
	logger.info("reqBodyAsJson : " + reqBodyAsJson);
	logger.info("reqBodyAsJson.columnname : " + reqBodyAsJson.columnname);
	logger.info("reqBodyAsJson.mapping : " + reqBodyAsJson.mapping);
	
	var columnNameAsString =reqBodyAsJson.columnname;
	var columnNameArray =columnNameAsString.split(",");
	
	logger.info("columnNameArray: "+ columnNameArray);

	for(var i=0;i<columnNameArray.length;i++){
	 	logger.info("columnNameArray : " + columnNameArray[i]);
	}



	//var mappingAsString =reqBodyAsJson.mappingArray;
	//var mappingArray =mappingAsString.split(",");
	
	//logger.info("mappingArray: "+ mappingArray);

	//for(var i=0;i<mappingArray.length;i++){
	 //	logger.info("mappingArray : " + mappingArray[i]);
	//}
	//logger.info("reqBodyAsJson.mappingArray: "+ reqBodyAsJson.mappingArray);


//	 var results = _utils.get("csv").parse("/mnt/ebsvolume/repos/scripts/"+info.get("tenantName")+"/"+fileds,true,columnNameArray);
	 var results = _utils.get("csv").parse("/var/lib/openrecordz/files/"+info.get("tenantName")+"/"+fileds,true,columnNameArray,java.lang.Character(charSeparator.charAt(0)));
	 //var results = _utils.get("csv").parse(environmentService.getFileFilesystemPath()+info.get("tenantName")+"/"+fileds,true,columnNameArray,java.lang.Character(charSeparator.charAt(0)));
	 

	 logger.info("results : " + results);



	var dataset = customDataService.findByQueryInternal("{\"_slug\":\"" +dsName+"\"}", "dataset").get(0);
	logger.info("dataset.id : " + dataset.getId() );

	var toreturn=[];

	for (var i = 0;i<results.size();i++) {
//	for (var i = 0;i<10;i++) {
		var result = results.get(i);


		logger.info("result : " + result);

		var resAsJson = _utils.get("json").toJSON(result);
		logger.info("resAsJson : " + resAsJson);

		//if csv contains _idext it's possible a patch over a record
		if (result.containsKey("_idext") && result.get("_idext")!=null) {
			var _idext=result.get("_idext").toString();
			logger.info("Mapping contains _idext key con valore: "+_idext);
			
			var searchExistingRecordByIdExt=recordDataService.findByQueryInternal("{\"_idext\":\"" +_idext+"\"}", dataset.getId(), "record");
			//not found..could be first import....
			if (searchExistingRecordByIdExt.size()==0) {
				logger.info("not found records with _idext: "+_idext +". Creating new record");
				var recordId=recordDataService.add(dataset.getId(),'record', resAsJson);	
			}else {
				var existingRecordId=searchExistingRecordByIdExt.get(0).getId();
				logger.info("found records with _idext: "+_idext +". Patching the record with id : "+existingRecordId );
				var recordId=recordDataService.patch(existingRecordId,'record', resAsJson);			
			}			
		} else {

		//	toreturn[i]=contentService.getById(cid);
		//	var cdId=customDataService.add(dsName, resAsJson);
			var recordId=recordDataService.add(dataset.getId(),'record', resAsJson);

		//toreturn[i]=customDataService.getById(cdId);
		}
	}

	var datasetMapping = {};
	datasetMapping._mapping = reqBodyAsJson.mappingArray;
	logger.info("JSON.stringify(datasetMapping) : " +JSON.stringify(datasetMapping));

	customDataService.patch(dataset.getId(),'dataset', JSON.stringify(datasetMapping));

//	   model.put("resultset", toreturn);
	   model.put("status", "success");
	  return model;
}
































