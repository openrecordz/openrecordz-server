/*
 * 
 * == SAMPLES REST WEB SERVICES ==
 * 
 */

function hello(req, res, params) { 
   logger.debug('Hello, ' + req.getParameter('name')); 
	model.put('name',req.getParameter('name'));
	return model;
}

function helloCurrentUser(req, res, params) { 
	   logger.debug('Hello, ' + authenticationService.getCurrentLoggedUsername()); 
		model.put('name', authenticationService.getCurrentLoggedUsername());
		return model;
}

function hellohtml(req, res, params) { 
	model.put('body',"andrea");
	return model;

}


function hellohtmlvm(req, res, params) { 
	model.put('nome',"andrea");
	model.put('cognome',"leo");
	return model;

}

function allproducts(req, res, params) {
	logger.debug('Hello, ' + authenticationService.getCurrentLoggedUsername()); 
	model.put('user', authenticationService.getCurrentLoggedUsername());
	model.put('products', productService.findAllPaginated(0, 10));
	return model;
}

function testsendmail(req,res, params) {
	mailService.sendMail("example@example.com","Test subject","/email_templates/test_email_template",params,false);
}

/*
 * 
 * == SAMPLES EVENTS HANDLERS ==
 * 
 */

/*
 * === PRODUCT EVENTS HANDLERS ===
 */

function onBeforeProductAdd(params) { 
	logger.debug('onBeforeProductAdd ' + params); 
	logger.debug('title ' + params.get('title'));
	logger.debug('description ' + params.get('description'));
	//etc..
	
	//validation example
	//TODO refactoring propagation of validation exception 
	
	/*
	if (params.get('description') == 'ciccio') {
		throw "Description not valid";
	}
	*/
		
}

function onAfterProductAdd(params) { 
	logger.debug('onAfterProductAdd ' + params.get('product'));  
		
}


/*
 * === USER REGISTRATION EVENTS HANDLERS ===
 */

function onAfterUserRegister(params) {
	logger.debug('onAfterUserRegister ' + params);  
}



/*
* === CUSTOM DATA HANDLERS
*/


//for all custom data types
function onBeforeCustomDataAdd(params) { 
	logger.debug('params ' + params); 
		
}

function onAfterCustomDataAdd(params) { 
	logger.debug('params ' + params);  
		
}


//for messages custom data type
function onBeforeMessagesCustomDataAdd(params) { 
	logger.debug('params ' + params); 
		
}

function onAfterMessagesCustomDataAdd(params) { 
	logger.debug('params ' + params);  
		
}

function onAfterAbuseReport(params) {
	logger.debug('onAfterAbuseReport ' + params.get('abuse'));  
}

