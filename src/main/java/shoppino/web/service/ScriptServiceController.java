package shoppino.web.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import openrecordz.exception.ShoppinoRuntimeException;
import shoppino.service.ScriptService;
import shoppino.service.TemplateService;


@Controller
//@RequestMapping(value = "/scripts")
public class ScriptServiceController implements BaseServiceController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	ScriptService scriptService;
    
	@Autowired
	TemplateService templateService;
	
	@Autowired
	MultipartResolver multipartResolver;
//	@Autowired
//	private VelocityEngine velocityEngine;
//	
//	@Autowired
//	TenantService tenantService;
//	
//	@Value("$shoppino{scripting.filesystem.path}")
//	public String fileSystemScriptsPath;
	
	 @RequestMapping(value = {"/wscripts/{scriptName}", "/wscripts/{scriptName}.json"})
	    public @ResponseBody Object eval(@PathVariable String scriptName, HttpServletRequest request, HttpServletResponse response) throws ScriptException, IOException  {     	
	    logger.debug("scriptName : " + scriptName);	
		 
		 //TODO inserire nello script il controllo sui permessi
//		 	fare un metodo in authenticationservice oppure vedi tu che vede se l'utente correntemente loggato puo eseguire lo script altrimenti solleva errore 403
		 
		 Map params = new HashMap<>();
		 params.put("req", request);
		 params.put("res", response);
		 
//		 params.put("", request.getParameterMap());
		 
		 	return scriptService.eval(scriptName, params);

	 }
	 
	 @RequestMapping(value = {"/functions/{functionName}", "/functions/{functionName}.json"})
	    public @ResponseBody Object call(@PathVariable String functionName, HttpServletRequest request, HttpServletResponse response) throws ScriptException, IOException, NoSuchMethodException  {     	
		 logger.debug("functionName : " + functionName);	
		 
		 if (functionName.startsWith("g_" )) {
			 throw new ShoppinoRuntimeException("You cant't invoke a guest function from here");
		 }
		 
		 //TODO inserire nello script il controllo sui permessi
//		 	fare un metodo in authenticationservice oppure vedi tu che vede se l'utente correntemente loggato puo eseguire lo script altrimenti solleva errore 403
		 
		 Map params = new HashMap<>();
		 params.put("req", request);
		 params.put("res", response);
		 
//		 params.put("", request.getParameterMap());
		 
		 	return scriptService.call(functionName, params);

	 }
	 
	 
	 @RequestMapping(value = {"/functions/{functionName}.html"}
//	 http://stackoverflow.com/questions/3616359/who-sets-response-content-type-in-spring-mvc-responsebody
	 
			 ,produces = "text/html; charset=utf-8"
			 )
	    public @ResponseBody String callAsHtml(@PathVariable String functionName, HttpServletRequest request, HttpServletResponse response) throws ScriptException, IOException, NoSuchMethodException {
//	    , FileUploadException       	
		 logger.debug("functionName : " + functionName);	
		 
		 if (functionName.startsWith("g_" )) {
			 throw new ShoppinoRuntimeException("You cant't invoke a guest function from here");
		 }
		 
		 //TODO inserire nello script il controllo sui permessi
//		 	fare un metodo in authenticationservice oppure vedi tu che vede se l'utente correntemente loggato puo eseguire lo script altrimenti solleva errore 403
		 
		 Map params = new HashMap<>();
		 params.put("req", request);
		 params.put("res", response);
		 
		 boolean isMultipart = multipartResolver.isMultipart(request);
		 if (isMultipart) {
			 logger.debug("It's a multipart request");
//			 do nothing...request with multipart is already into params :O :)
			 
//			MultipartHttpServletRequest mhsr= multipartResolver.resolveMultipart(request);
//			params.put("req", request);
		 }
//		 boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//		 
//		 if (isMultipart) {
//			// Create a factory for disk-based file items
//			 DiskFileItemFactory factory = new DiskFileItemFactory();
//	
//			 factory.setSizeThreshold(100000);
//			 factory.setRepository(new File("/tmp/"));
//	
//			 // Create a new file upload handler
//			 ServletFileUpload upload = new ServletFileUpload(factory);
//	
//			 // Parse the request
//			 List<FileItem> items = upload.parseRequest(request);
//			 
//			 params.put("items", items);
//			 
//		 }
//		 params.put("", request.getParameterMap());
		 
		 	Map<String,Object> map = scriptService.call(functionName, params);
		 	
		 	if (map==null){
		 		throw new ShoppinoRuntimeException("Your script controller return a null Map<String,Object>. ");
		 	}
		 	
		 	if (map.containsKey("body"))
		 		return map.get("body").toString();
		 	else {
		 		
		 		return templateService.process(functionName+TemplateService.DEFAULT_TEMPLATE_VELOCITY_EXTENSION, map);
//		 	//velocity move to service
//		 	 Map model = new HashMap();
//		 	 String velocityTemplete = null;
//		 	 
//		 	 
//		 	String pathFileSystemVelocityTemplate = fileSystemScriptsPath + tenantService.getCurrentTenantName() + "velocity_templates" + functionName + ".vm";
//		 	logger.debug("pathFileSystemVelocityTemplate : "  + pathFileSystemVelocityTemplate);
//    		
//    		Resource fileSystemVelocityTemplates = new FileSystemResource(pathFileSystemVelocityTemplate);
//    		if (fileSystemVelocityTemplates.exists()) {
//    			logger.debug("fileSystemVelocityTemplates found here: "  + fileSystemVelocityTemplates);
//    			velocityTemplete = fileSystemVelocityTemplates.getFile().getAbsolutePath();
//    		}
//    	
//    		if (velocityTemplete==null)
//    			throw new FileNotFoundException("Not found templat with name : " + functionName +" not found");
//    		
//    		String text = VelocityEngineUtils.mergeTemplateIntoString(
//	                 velocityEngine, velocityTemplete, "utf-8", model);
//    		
//    		 return text;
		 	}
    		
    		
		 	

	 }
	 
	 
	 
	 
    //for admin
//	 @RequestMapping(value = {"/afunctions/{functionName}", "/afunctions/{functionName}.json"})
//	    public @ResponseBody Object callAdmin(@PathVariable String functionName, HttpServletRequest request, HttpServletResponse response) throws ScriptException, IOException, NoSuchMethodException  {     	
//		 logger.debug("functionName : " + functionName);	
//		 
//		 
//		 //TODO inserire nello script il controllo sui permessi
////		 	fare un metodo in authenticationservice oppure vedi tu che vede se l'utente correntemente loggato puo eseguire lo script altrimenti solleva errore 403
//		 
//		 Map params = new HashMap<>();
//		 params.put("req", request);
//		 params.put("res", response);
//		 
////		 params.put("", request.getParameterMap());
//		 
//		 	return scriptService.call(functionName, params);
//
//	 }
	 
	 
	
	 
	 //not auth
	 @RequestMapping(value = {"/gfunctions/{functionName}", "/gfunctions/{functionName}.json"})
	    public @ResponseBody Object guestCall(@PathVariable String functionName, HttpServletRequest request, HttpServletResponse response) throws ScriptException, IOException, NoSuchMethodException  {     	
		 logger.debug("functionName : " + functionName);	
		 
		 if (!functionName.startsWith("g_" )) {
			 throw new ShoppinoRuntimeException("You cant't invoke an authenticated function from here");
		 }
		 
		 //TODO inserire nello script il controllo sui permessi
//		 	fare un metodo in authenticationservice oppure vedi tu che vede se l'utente correntemente loggato puo eseguire lo script altrimenti solleva errore 403
		 
		 Map params = new HashMap<>();
		 params.put("req", request);
		 params.put("res", response);
		 
//		 params.put("", request.getParameterMap());
		 
		 	return scriptService.call(functionName, params);

	 }
	 
	 
	 //not auth
	 @RequestMapping(value = {"/gfunctions/{functionName}.html"},produces = "text/html; charset=utf-8")
	    public @ResponseBody String guestCallAsHtml(@PathVariable String functionName, HttpServletRequest request, HttpServletResponse response) throws ScriptException, IOException, NoSuchMethodException  {     	
		 logger.debug("functionName : " + functionName);	
		 
		 if (!functionName.startsWith("g_" )) {
			 throw new ShoppinoRuntimeException("You cant't invoke an authenticated function from here");
		 }
		 
		 //TODO inserire nello script il controllo sui permessi
//		 	fare un metodo in authenticationservice oppure vedi tu che vede se l'utente correntemente loggato puo eseguire lo script altrimenti solleva errore 403
		 
		 Map params = new HashMap<>();
		 params.put("req", request);
		 params.put("res", response);
		 
//		 params.put("", request.getParameterMap());
		 
		 Map<String,Object> map = scriptService.call(functionName, params);
		 	
			if (map==null){
		 		throw new ShoppinoRuntimeException("Your script controller return a null Map<String,Object>. ");
		 	}
		 	
		 	if (map.containsKey("body"))
		 		return map.get("body").toString();
		 	else {
		 		
		 		return templateService.process(functionName+TemplateService.DEFAULT_TEMPLATE_VELOCITY_EXTENSION, map);
		 	}
	 }
	 
//	 /**
//	     * Create a map of arguments from Web Script Request (for scripting)
//	     * 
//	     * @param req  Web Script Request
//	     * @return  argument map
//	     */
//	    final protected Map<String, String> createArgs(HttpServletRequest req)
//	    {
////	        String[] names = req.getParameterNames();
////	        Map<String, String> args = new HashMap<String, String>(names.length, 1.0f);
////	        for (String name : names)
////	        {
////	            args.put(name, req.getParameter(name));
////	        }
////	        return args;
//	    	return req.getParameterMap();
//	    }
//
//	    /**
//	     * Create a map of (array) arguments from Web Script Request (for scripting)
//	     * 
//	     * @param req  Web Script Request
//	     * @return  argument map
//	     */
//	    final protected Map<String, String[]> createArgsM(HttpServletRequest req)
//	    {
//	        String[] names = req.getParameterNames();
//	        Map<String, String[]> args = new HashMap<String, String[]>(names.length, 1.0f);
//	        for (String name : names)
//	        {
//	            args.put(name, req.getParameterValues(name));
//	        }
//	        return args;
//	    }


    
}