package openrecordz.domain.customdata.scripting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import shoppino.exception.ResourceNotFoundException;
import shoppino.service.CustomDataService;
import shoppino.service.ScriptService;

public class CustomDataScriptingHandler {
	
	private static String BEFORE_PREFIX_FUNCTION_NAME = "onBefore";
	private static String AFTER_PREFIX_FUNCTION_NAME = "onAfter";
	private static String ADD_POSTFIX_FUNCTION_NAME = "Add";
	private static String UPDATE_POSTFIX_FUNCTION_NAME = "Update";
	
	private static String CUSTOMDATA_FUNCTION_NAME = "CustomData";
	
	private static String BEFORE_CUSTOMDATA_ADD_FUNCTION_NAME = BEFORE_PREFIX_FUNCTION_NAME + CUSTOMDATA_FUNCTION_NAME + ADD_POSTFIX_FUNCTION_NAME;
	private static String AFTER_CUSTOMDATA_ADD_FUNCTION_NAME = AFTER_PREFIX_FUNCTION_NAME + CUSTOMDATA_FUNCTION_NAME + ADD_POSTFIX_FUNCTION_NAME;
	
	private static String BEFORE_CUSTOMDATA_UPDATE_FUNCTION_NAME = BEFORE_PREFIX_FUNCTION_NAME + CUSTOMDATA_FUNCTION_NAME + UPDATE_POSTFIX_FUNCTION_NAME;
	private static String AFTER_CUSTOMDATA_UPDATE_FUNCTION_NAME = AFTER_PREFIX_FUNCTION_NAME + CUSTOMDATA_FUNCTION_NAME + UPDATE_POSTFIX_FUNCTION_NAME;
	
	@Autowired
	ScriptService scriptService;
	
	@Autowired
	CustomDataService customDataService;
	
	private static Log log = LogFactory.getLog(CustomDataScriptingHandler.class);	
	
	
	public void beforeCustomDataAdd(String className, String json) throws ScriptException {
		if (scriptService.isEnabled()) {
			log.debug("before customdata add called");
			
			log.debug("className" + className);
			log.debug("json :" + json);		
			
			Map params = new HashMap<>();
			params.put("className", className);
			params.put("json", json);
		
			String methodName = "";
			try {
				methodName = BEFORE_CUSTOMDATA_ADD_FUNCTION_NAME;
				scriptService.call(methodName, params);
				log.info(methodName + " executed with success");
				
				methodName = BEFORE_PREFIX_FUNCTION_NAME + StringUtils.capitalize(className) + CUSTOMDATA_FUNCTION_NAME + ADD_POSTFIX_FUNCTION_NAME;
				scriptService.call(methodName , params);
				log.info(methodName + " executed with success");
				
			}catch (FileNotFoundException fnfe) {
				log.warn("Script file not found : " + fnfe.getMessage());
			}catch (NoSuchMethodException nsme) {
				log.warn("Method "+ methodName + " not found for script file: " + nsme.getMessage());		
			} catch (IOException ioe) {
				log.warn(ioe);		
			}
		}
	}
	
	
	public void afterCustomDataAdd(String id, String className, String json) throws ScriptException, ResourceNotFoundException {
		if (scriptService.isEnabled()) {
			log.debug("after CustomData add called");
			
			log.debug("id :" + id);
			
			 Map params = new HashMap<>();
			 params.put("customData", customDataService.getById(id));
			 
			 String methodName = "";
			 
			try {
				methodName = AFTER_CUSTOMDATA_ADD_FUNCTION_NAME;
			 	scriptService.call(methodName, params);
			 	log.info(methodName + " executed with success");
			 	
			 	methodName = AFTER_PREFIX_FUNCTION_NAME + StringUtils.capitalize(className) + CUSTOMDATA_FUNCTION_NAME + ADD_POSTFIX_FUNCTION_NAME;
			 	scriptService.call(methodName, params);
			 	log.info(methodName + " executed with success");
				
			}catch (FileNotFoundException fnfe) {
				log.warn("Script file not found : " + fnfe.getMessage());
			}catch (NoSuchMethodException nsme) {
				log.warn("Method "+ methodName + " not found for script file: " + nsme.getMessage());		
			} catch (IOException ioe) {
				log.warn(ioe);		
			}
		}
	}
	
	
	
	
	
	
	public void beforeCustomDataUpdate(String id, String className, String json) throws ScriptException {
		if (scriptService.isEnabled()) {
			log.debug("before CustomData update called");
			
			log.debug("id :" + id);
			log.debug("className :" + className);
			log.debug("json :" + json);
			
			
			Map params = new HashMap<>();
			params.put("id", id);
			params.put("className", className);
			params.put("json", json);		
			
			 String methodName = "";
			 
			try {
				methodName = BEFORE_CUSTOMDATA_UPDATE_FUNCTION_NAME;
				scriptService.call(methodName, params);
				log.info(methodName + " executed with success");
				
				methodName = BEFORE_PREFIX_FUNCTION_NAME + StringUtils.capitalize(className) + CUSTOMDATA_FUNCTION_NAME + UPDATE_POSTFIX_FUNCTION_NAME;
				scriptService.call(methodName , params);
				log.info(methodName + " executed with success");
			}catch (FileNotFoundException fnfe) {
				log.warn("Script file not found : " + fnfe.getMessage());
			}catch (NoSuchMethodException nsme) {
				log.warn("Method "+ methodName + " not found for script file: " + nsme.getMessage());		
			} catch (IOException ioe) {
				log.warn(ioe);		
			}
		}
	}
	
	
	public void afterCustomDataUpdate(String returnId, String id, String className, String json) throws ScriptException, ResourceNotFoundException {
		if (scriptService.isEnabled()) {
			log.debug("after CustomData update called");
			
			log.debug("id :" + returnId);
			
			 Map params = new HashMap<>();
			 params.put("customData", customDataService.getById(returnId));
			 
			 String methodName = "";
			 
			 try {
				 methodName = AFTER_CUSTOMDATA_UPDATE_FUNCTION_NAME;
				 scriptService.call(methodName, params);
				 log.info(methodName + " executed with success");
				 
				 methodName = AFTER_PREFIX_FUNCTION_NAME + StringUtils.capitalize(className) + CUSTOMDATA_FUNCTION_NAME + UPDATE_POSTFIX_FUNCTION_NAME;
				scriptService.call(methodName , params);
				log.info(methodName + " executed with success");
				 
			}catch (FileNotFoundException fnfe) {
					log.warn("Script file not found : " + fnfe.getMessage());
			}catch (NoSuchMethodException nsme) {
				log.warn("Method "+ methodName + " not found for script file: " + nsme.getMessage());		
			} catch (IOException ioe) {
				log.warn(ioe);		
			}
		}
		
	}
	
}
