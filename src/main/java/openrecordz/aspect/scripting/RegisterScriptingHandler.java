package openrecordz.aspect.scripting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import openrecordz.exception.ResourceNotFoundException;
import openrecordz.security.exception.UserNotExistsException;
import openrecordz.security.service.UserService;
import openrecordz.service.PersonService;
import openrecordz.service.ScriptService;

public class RegisterScriptingHandler {
	
	private static String AFTER_REGISTER_FUNCTION_NAME = "onAfterUserRegister";
	
	
	@Autowired
	ScriptService scriptService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	UserService userService;
	
	private static Log log = LogFactory.getLog(RegisterScriptingHandler.class);	
	

	

	
	
	public void afterRegister(String returnUsername) throws ScriptException, ResourceNotFoundException, UserNotExistsException {
		log.debug("after user registerd called");
		
		log.debug("username :" + returnUsername);
		
		 Map params = new HashMap<>();
		 params.put("person", personService.getByUsername(returnUsername));
		 params.put("user", userService.getByUsername(returnUsername));
		
		 try {
			scriptService.call(AFTER_REGISTER_FUNCTION_NAME, params);
		}catch (FileNotFoundException fnfe) {
			log.warn("Script file not found", fnfe);
		}catch (NoSuchMethodException nsme) {
			log.warn("Method not found for script file", nsme);		
		} catch (IOException ioe) {
			log.warn(ioe);		
		}
		log.info("afteruserRegister executed with success");
	}
	
	
	
	
	
	
}
