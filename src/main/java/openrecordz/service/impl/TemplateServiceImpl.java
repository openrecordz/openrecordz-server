package openrecordz.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.ui.velocity.VelocityEngineUtils;

import openrecordz.exception.ResourceNotFoundException;
import openrecordz.security.service.AuthenticationService;
import openrecordz.service.EnvironmentService;
import openrecordz.service.PersonService;
import openrecordz.service.TemplateService;
import openrecordz.service.TenantService;
import openrecordz.web.vmtools.WebTools;

public class TemplateServiceImpl implements TemplateService {

	public static String CLASSPATH_TEMPLATE_PATH = "/templates/";
	private static Log log = LogFactory.getLog(TemplateServiceImpl.class);	
	
	@Value("$shoppino{templating.filesystem.path}")
	public String fileSystemTemplatesPath;
	
	@Autowired
	TenantService tenantService;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	
	@Value("$shoppino{platform.url}")
	private String platformUrl;
	
	@Value("$shoppino{platform.hostname}")
	private String platformHostname;
	
	
	@Value("$shoppino{imagerepo.url}")
	private String imageRepoUrl;
	
	@Value("$shoppino{imagerepo.search.url}")
	private String imageRepoSearchUrl;
	
	public final String CLASSPATH_GLOBAL_TEMPLATE_PATH = "_global";
	
	@Autowired
	EnvironmentService environmentService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Override
	public String process(String templateName, Map model) throws FileNotFoundException, IOException {
		return this.process(templateName, model, null, null);
	}
	@Override
	public String process(String templateName, Map model,HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException {
		//velocity move to service
		
		log.debug("model : " + model);
		
		 Map modelVm = new HashMap();
		 modelVm.put("model", model);
//		 modelVm.put("esc", new EscapeTool());
		 modelVm.put("shp", new WebTools());
		 
         Map info = new HashMap();
         info.put("hostname", platformHostname);
         info.put("url", platformUrl);
         info.put("tenantName", tenantService.getCurrentTenantName());
         info.put("imageRepoUrl", imageRepoUrl);
         info.put("imageRepoSearchUrl", imageRepoSearchUrl);
         info.put("tenantDisplayName", tenantService.getCurrentTenantDisplayName());
         info.put("tenantUrl", environmentService.getTenantUrl());
         
         
         if (authenticationService.isAuthenticated()) {
			try {
				modelVm.put("user", personService.getByUsername(authenticationService.getCurrentLoggedUsername()));
			} catch (ResourceNotFoundException e) {
				log.error("User not found with username : " + authenticationService.getCurrentLoggedUsername());
			}
         }
         
         modelVm.put("info", info);
         
         log.debug("modelVM : "+ modelVm);
         
         
         if (request!=null)
        	 modelVm.put("req", request);
         

         if (response!=null)
        	 modelVm.put("res", response);
         
	 	 String velocityTemplete = null;
	 	 
	 	 
 	  String pathClassPathTemplate = CLASSPATH_TEMPLATE_PATH +  templateName;
      log.debug("pathClassPathTemplate : "  + pathClassPathTemplate);
      
      
      //TODO puo generare un conflitto di template tra quelli contenuti in class path e quelli in cloud code
      Resource classPathTemplate = new ClassPathResource(pathClassPathTemplate);
  		if (classPathTemplate.exists()) {
  			log.debug("pathClassPathTemplate found here: "  + pathClassPathTemplate);
  			velocityTemplete = classPathTemplate.getFile().getAbsolutePath();
  		}
	 	 
//  		String globalScriptPathFileSystem = fileSystemScriptsPath + CLASSPATH_GLOBAL_SCRIPTS_PATH + File.separator + DEFAULT_SCRIPT_NAME + DEFAULT_SCRIPT_EXTENSION;
  	 	String pathFileSystemGlobalVelocityTemplate = fileSystemTemplatesPath + CLASSPATH_GLOBAL_TEMPLATE_PATH + File.separator + "templates" + File.separator + templateName;
	 	log.debug("pathFileSystemGlobalVelocityTemplate : "  + pathFileSystemGlobalVelocityTemplate);
		
		Resource fileSystemGlobalVelocityTemplate = new FileSystemResource(pathFileSystemGlobalVelocityTemplate);
		if (fileSystemGlobalVelocityTemplate.exists()) {
			log.debug("fileSystemGlobalVelocityTemplates found here: "  + fileSystemGlobalVelocityTemplate);
			velocityTemplete = fileSystemGlobalVelocityTemplate.getFile().getAbsolutePath();
		}
  		
  		
  		
	 	String pathFileSystemVelocityTemplate = fileSystemTemplatesPath + tenantService.getCurrentTenantName() + File.separator + "templates" + File.separator + templateName;
	 	log.debug("pathFileSystemVelocityTemplate : "  + pathFileSystemVelocityTemplate);
		
		Resource fileSystemVelocityTemplates = new FileSystemResource(pathFileSystemVelocityTemplate);
		if (fileSystemVelocityTemplates.exists()) {
			log.debug("fileSystemVelocityTemplates found here: "  + fileSystemVelocityTemplates);
			velocityTemplete = fileSystemVelocityTemplates.getFile().getAbsolutePath();
		}
	
		
		
		
		if (velocityTemplete==null)
			throw new FileNotFoundException("Template not found with name : " + templateName );
		
		log.debug("velocityTemplete : "+ velocityTemplete);
		
		String text = VelocityEngineUtils.mergeTemplateIntoString(
                velocityEngine, velocityTemplete, "UTF-8", modelVm);
		
		log.debug("text : "+ text);
		
		
		 return text;
	
	}


	
	
	
	
}
