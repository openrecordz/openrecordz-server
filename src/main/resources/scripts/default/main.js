function hello(req, res, params) { 
   logger.debug('Hello, ' + req.getParameter('testparam')); 
	model.put('name',"andrea");
	return model;
}


function allproducts(req, res, params) {
	logger.debug('Hello, ' + authenticationService.getCurrentLoggedUsername()); 
	model.put('user', authenticationService.getCurrentLoggedUsername());
	model.put('products', productService.findAllPaginated(0, 10));
	return model;
}


function onBeforeProductAdd(params) { 
	logger.debug('onBeforeProductAdd ' + params); 
	logger.debug('title ' + params.get('title'));
	logger.debug('description ' + params.get('description'));
	//etc..
	
	//validation example
	//TODO refactoring propagation of validation exception 
	if (params.get('description') == 'ciccio') {
		throw "Description not valid";
	}
		
}


function onAfterProductAdd(params) { 
	logger.debug('onAfterProductAdd ' + params.get('product'));  
		
}


//custom data events

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
