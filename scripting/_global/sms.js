
function sendsms(req,res, params) {

	security.allowToRole("ROLE_ADMIN");


	var smsText = req.getParameter("text");
	logger.info("smsText : "+smsText);

	var objectId = null;
	if (req.getParameter("objectid")!=null) {
		objectId = req.getParameter("objectid");
		logger.info("objectId : "+ objectId);
	}

	var recipientsAddress = customDataService.findByQuery("{}","_phones");
	logger.info(recipientsAddress.size());
	
	 var parameters = {
	    method:"send_sms_classic",
//	    username:"frontiere21", 
//	    password:"Frontiere21",
//    	    "recipients[0]": "393495862771",
//          "recipients[1]": "393289353211",
//	    text: "ciaociao",
	    charset: "UTF-8",
	  };

	var headers = { 
	    "Content-Type" : "application/x-www-form-urlencoded",
	    };



	//set parameters
	parameters.username = msg.getMessage("tenants.settings."+info.get("tenantName")+".sms.ext.auth.username",null,"frontiere21",null)+"";
	logger.info("username: "+ parameters.username);
	parameters.password = msg.getMessage("tenants.settings."+info.get("tenantName")+".sms.ext.auth.password",null,"Frontiere21",null)+"";
	logger.info("password: "+ parameters.password);
	parameters.text = smsText+"";
//	parameters.sender_number = msg.getMessage("tenants.settings."+info.get("tenantName")+".sms.ext.sender.number",null,"390836569329",null)+"";
	logger.info("sendNumber: "+ parameters.sender_number);
	parameters.sender_string = msg.getMessage("tenants.settings."+info.get("tenantName")+".sms.ext.sender.string",null,"Smart21",null)+"";
	logger.info("sendString: "+ parameters.sender_string);

	var i = 0;
	for each (var destA in recipientsAddress.toArray() ) {
		logger.info("destA :" + destA.toString());
		var jsDest = JSON.parse(destA.toJSON());
//okfunziona		 
		logger.info("jsDest : " + jsDest);
//okfunziona		
		logger.info(destA.getString("firstname"));
		
		
		logger.info(jsDest.firstname);
		logger.info(jsDest.telephone);	
		
		parameters["recipients["+i+"][recipient]"]=jsDest.telephone;
		parameters["recipients["+i+"][nome]"]=jsDest.firstname;
		parameters["recipients["+i+"][cognome]"]=jsDest.lastname;
		
		logger.info("info: " + info.get("tenantUrl"));
		var linkUrl = info.get("tenantUrl")+"/products/"+objectId+"?pusername=admin";
		//var linkUrl = "http://default.frontiere21.it/products/"+objectId+"?pusername=admin";
		logger.info("linkUrl : " + linkUrl);
		
		var s21Url = info.get("tenantUrl")+"/urls/" + urlService.save(linkUrl) + "/r";
		logger.info("s21Url : "+ s21Url);
		
		var shortUrl = "http://tinyurl.com/api-create.php?url="+s21Url;
		logger.info("shortUrl : "+ shortUrl);
		
		var linkResponse = rest.httpRequest(shortUrl, "GET", null, null);
		logger.info("linkResponse : "+ linkResponse);
		
		parameters["recipients["+i+"][link]"]=linkResponse+"";		

		i++;
	}
//	logger.info("parameters: " + parameters);
	logger.info("parameters: "+ JSON.stringify(parameters));


//	var responseSend = rest.httpRequest("http://gateway.skebby.it/api/send/smseasy/advanced/http.php", "POST", parameters, headers);
	logger.info("responseSend: " + responseSend);

}