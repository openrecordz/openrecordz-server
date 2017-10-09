package shoppino.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import shoppino.exception.EmailAlreadyInUseException;
import shoppino.exception.ShoppinoException;
import shoppino.security.exception.UserNotExistsException;
import shoppino.security.exception.UsernameAlreadyInUseException;
import shoppino.security.service.UserService;
import shoppino.service.PersonService;
import shoppino.service.RegistrationService;


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
