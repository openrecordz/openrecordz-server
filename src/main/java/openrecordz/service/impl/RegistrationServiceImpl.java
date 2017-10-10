package openrecordz.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import openrecordz.exception.EmailAlreadyInUseException;
import openrecordz.exception.ShoppinoException;
import openrecordz.security.exception.UserNotExistsException;
import openrecordz.security.exception.UsernameAlreadyInUseException;
import openrecordz.security.service.UserService;
import openrecordz.service.PersonService;
import openrecordz.service.RegistrationService;


public class RegistrationServiceImpl implements RegistrationService{

	@Autowired
	UserService userService;
	
	@Autowired
	PersonService personService;
	
	
	protected Log logger = LogFactory.getLog(getClass());
	
	
	public String register(String username, String fullName, String email, String password) throws UsernameAlreadyInUseException,EmailAlreadyInUseException, ShoppinoException {
//	public void register(String username, String fullName, String email, String password) throws UsernameAlreadyInUseException,EmailAlreadyInUseException, ShoppinoException {
		if (userService.exists(username))
			throw new UsernameAlreadyInUseException(username);
		 
		//to lower case
		username = username.toLowerCase();
		
		//user creation			
		userService.add(username, password);
		logger.debug("User created");
		
		//person creation
		String id=null;
		try {
			id =personService.add(username, fullName, email);
			
		}catch (ShoppinoException e) {
			try {
				userService.delete(username);
			} catch (UserNotExistsException e1) {}
			logger.error("Error registering user : " + username, e);
			throw e;
		}
		
		logger.debug("Person created with id : " + id);	
		
		logger.info("Registration complete for user : " + username);
		
		return username;
	}

	
	
}
