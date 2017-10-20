package openrecordz.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import openrecordz.mail.service.MailService;
import openrecordz.scripting.CSVUtil;
import openrecordz.scripting.JSAuthorizationService;
import openrecordz.scripting.JSONUtil;
import openrecordz.scripting.JSRestOperation;
import openrecordz.scripting.ScriptUtil;
import openrecordz.security.service.AuthenticationService;
import openrecordz.service.CategoryService;
import openrecordz.service.CustomDataService;
import openrecordz.service.EnvironmentService;
import openrecordz.service.FileService;
import openrecordz.service.ImageService;
import openrecordz.service.PersonService;
import openrecordz.service.RDBService;
import openrecordz.service.RecordDataService;
import openrecordz.service.ScriptService;
import openrecordz.service.TenantService;
import openrecordz.service.UrlService;

public class ScriptServiceMultiEngineImpl implements ScriptService {

//	http://docs.oracle.com/javase/6/docs/technotes/guides/scripting/programmer_guide/
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	protected final Log scriptLogger = LogFactory.getLog(ScriptLogger.class);
	
	public final String CLASSPATH_GLOBAL_SCRIPTS_PATH = "_global";
	public static String CLASSPATH_SCRIPTS_PATH = "/scripts/";
	
	public static String CLASSPATH_SCRIPTS_LIB_PATH = "/scripts-lib/";

	public static String MODEL_KEY = "model";
	public static String PARAMS_KEY = "params";
	public static String REQUEST_KEY = "req";
	public static String RESPONSE_KEY = "res";
	
	public static String DEFAULT_SCRIPT_NAME = "main";
	public static String DEFAULT_SCRIPT_EXTENSION = ".js";
	
	@Value("$platform{scripting.filesystem.path}")
	public String fileSystemScriptsPath;

	
	@Value("$platform{platform.url}")
	private String platformUrl;
	
	@Value("$platform{platform.hostname}")
	private String platformHostname;
	
	
//	@Autowired
	private AuthenticationService authenticationService;
	
//	@Autowired
	private TenantService tenantService;
	
	private CustomDataService customDataService;
	private MailService mailService;
//	private NotificationService notificationService;
	private UrlService urlService;
	private PersonService personService;
	private EnvironmentService environmentService;
	private CategoryService categoryService;
//	private RestOperations restOperation;
	private JSRestOperation jsRestOperation;
	private JSAuthorizationService jsAuthorizationService;
	private MessageSource messageSource;
//	private Remainder21Service remainderService;
	private RDBService rdbService;
	private ImageService imageService;
	private FileService fileService;
	private RecordDataService recordDataService;
	
	
//	private  ScriptEngine engine;
	private ScriptEngineManager scriptEngineManager;
	
	private Map<String, ScriptEngine> engines = new HashMap<String, ScriptEngine>();
	
	public ScriptServiceMultiEngineImpl(
			
			AuthenticationService authenticationService, TenantService tenantService, 
			CustomDataService customDataService, MailService mailService, 
//			NotificationService notificationService, 
			UrlService urlService, PersonService personService,
			EnvironmentService environmentService,CategoryService categoryService
//			,RestOperations restOperation
			, JSRestOperation jsRestOperation
			, JSAuthorizationService jsAuthorizationService
			, MessageSource messageSource
//			,Remainder21Service remainderService
			,RDBService rdbService
			,ImageService imageService
			,FileService fileService
			, RecordDataService recordDataService
			) {
		
		
		this.authenticationService = authenticationService;
		this.tenantService = tenantService;
		
	    this.customDataService=customDataService;
	    this.mailService=mailService;  
//	    this.notificationService = notificationService;
	    this.urlService=urlService;
	    this.personService = personService;
		this.environmentService = environmentService;
		
		this.categoryService = categoryService;
		
//		this.restOperation = restOperation;
		this.jsRestOperation = jsRestOperation;
		this.jsAuthorizationService=jsAuthorizationService;
		this.messageSource = messageSource;
//		this.remainderService = remainderService;
		this.rdbService = rdbService;
		this.imageService=imageService;
		this.fileService=fileService;
		this.recordDataService=recordDataService;
		
		 // create a script engine manager
        scriptEngineManager = new ScriptEngineManager();
        
//        "MULTITHREADED" - The engine implementation is internally thread-safe and scripts may execute concurrently although 
//        effects of script execution on one thread may be visible to scripts on other threads."
        
        List<ScriptEngineFactory> factories = scriptEngineManager.getEngineFactories();
        for ( ScriptEngineFactory factory : factories ) {
            logger.info("Full name = " + factory.getEngineName ());
            logger.info("Version = " + factory.getEngineVersion ());
            String [] params =
            {
               ScriptEngine.ENGINE,
               ScriptEngine.ENGINE_VERSION,
               ScriptEngine.LANGUAGE,
               ScriptEngine.LANGUAGE_VERSION,
               ScriptEngine.NAME,
               "THREADING"
            };
        
            for (String param: params)
            {
            	logger.info("Parameter " + param + "" +
                                    factory.getParameter (param));
            }
                
            logger.info( String.format(
                    "engineName: %s, THREADING: %s",
                    factory.getEngineName(),
                    factory.getParameter( "THREADING" ) ) );
        }
        
        
	}
	
	
	public static String CONFIG_THREADLOCAL_SCRIPTING_ENABLED_KEY = "scriptingEnabled";
	
	public boolean isEnabled() {
		return Boolean.parseBoolean(ConfigThreadLocal.get().get(CONFIG_THREADLOCAL_SCRIPTING_ENABLED_KEY).toString());
	}
	
	private void createEngine() {
		
		if (engines.containsKey(tenantService.getCurrentTenantName())) {
			cleanEngineParams();
			logger.debug("ScriptEngine already created for tenant : " + tenantService.getCurrentTenantName());
			
		}else {
				  // create a JavaScript engine
		//      JSR223
	      ScriptEngine engine = scriptEngineManager.getEngineByName("JavaScript");
	      
	      engine.put("authenticationService", authenticationService);
	      engine.put("tenantService", tenantService);
	      engine.put("customDataService", customDataService);
	      engine.put("mailService", mailService);  
//	      engine.put("notificationService", notificationService);
	      engine.put("urlService", urlService);
	      engine.put("personService", personService);
	      engine.put("environmentService", environmentService);
	      engine.put("categoryService", categoryService);
//	      engine.put("restOperation", restOperation);
	      engine.put("rest", jsRestOperation);
	      engine.put("security", jsAuthorizationService);
	      engine.put("msg", messageSource);
//	      engine.put("remainderService", remainderService);
	      engine.put("rdbService", rdbService);
	      engine.put("imageService", imageService);
	      engine.put("fileService", fileService);
	      engine.put("recordDataService", recordDataService);
	      
	      
	      Map utils = new HashMap();
	      utils.put("date", new ScriptUtil());
	      utils.put("json", new JSONUtil());
	      utils.put("csv", new CSVUtil());
	      
	      engine.put("_utils", utils);
	      
	      engine.put("_scriptUtil", new ScriptUtil());
	      engine.put("logger", scriptLogger);
	      
	      Map info = new HashMap();
	      info.put("hostname", platformHostname);
	      info.put("url", platformUrl);
	      info.put("tenantName", tenantService.getCurrentTenantName());
	      info.put("tenantDisplayName", tenantService.getCurrentTenantDisplayName());
          info.put("tenantUrl", environmentService.getTenantUrl());
	      engine.put("info", info);
	      
	      
	      engines.put(tenantService.getCurrentTenantName(), engine);
	      
	      
	      importExtLibray();
	      importGlobalScript();
	      
	      logger.debug("ScriptEngine created for tenant : " + tenantService.getCurrentTenantName());
		      
		}
	}
	
	private void cleanEngineParams() {
		engines.get(tenantService.getCurrentTenantName()).put(MODEL_KEY,  new HashMap<String, Object>());
		engines.get(tenantService.getCurrentTenantName()).put(PARAMS_KEY, null);
		engines.get(tenantService.getCurrentTenantName()).put(REQUEST_KEY, null);
		engines.get(tenantService.getCurrentTenantName()).put(RESPONSE_KEY, null);
	}
	
//	private void loadJavaScriptUrls(final String... urls) throws Exception {
//		for (final String urlString : urls) {
//			URL url = new URL(urlString);
//			String javaScript = FileUtilities.getContents(url.openStream(), Integer.MAX_VALUE).toString();
//			engine.evaluateString(scriptableObject, javaScript, url.getFile(), 1, null);
//		}
//	}

	
	public Map<String, Object> eval(String scriptName, Map parameters) throws FileNotFoundException, ScriptException, IOException {
//	public synchronized Map<String, Object> eval(String scriptName, Map parameters) throws FileNotFoundException, ScriptException, IOException {
		createEngine();
		 cleanEngineParams();
		 
		 engines.get(tenantService.getCurrentTenantName()).put(PARAMS_KEY, parameters);
		 
		 if(parameters!=null && parameters.containsKey(REQUEST_KEY))
			 engines.get(tenantService.getCurrentTenantName()).put(REQUEST_KEY, parameters.get(REQUEST_KEY));
		 
		 
		 if(parameters!=null && parameters.containsKey(RESPONSE_KEY))
			 engines.get(tenantService.getCurrentTenantName()).put(RESPONSE_KEY, parameters.get(RESPONSE_KEY));

		 
	 long start = 0L;
     start = System.nanoTime();
	        
		FileReader script = null;
		
		//leggi prima da classpath lo script
		
//		TODO nn legge da meta-inf 
		String pathClassPathScript = CLASSPATH_SCRIPTS_PATH +  tenantService.getCurrentTenantName() + File.separator + scriptName + DEFAULT_SCRIPT_EXTENSION;
		logger.debug("pathClassPathScript : "  + pathClassPathScript);
		
		Resource classPathScript = new ClassPathResource(pathClassPathScript);
		if (classPathScript.exists()) {
			logger.debug("pathClassPathScript found here: "  + pathClassPathScript);
			script = new FileReader(classPathScript.getFile());
		}
		//se presente su filesystem fai override
		
		String pathFileSystemScript = fileSystemScriptsPath + tenantService.getCurrentTenantName() + File.separator + scriptName + DEFAULT_SCRIPT_EXTENSION;
		logger.debug("pathFileSystemScript : "  + pathFileSystemScript);
		
		Resource fileSystemScript = new FileSystemResource(pathFileSystemScript);
		if (fileSystemScript.exists()) {
			logger.debug("fileSystemScript found here: "  + pathFileSystemScript);
			script = new FileReader(fileSystemScript.getFile());
		}
	
		if (script==null)
			throw new FileNotFoundException("Script with name : " + scriptName +" not found");
		
		
		
		
		engines.get(tenantService.getCurrentTenantName()).eval(script);
		
		logger.info("Executed script " + scriptName + " in " + (System.nanoTime() - start)/1000000f + "ms");
		
		return (Map<String, Object>)engines.get(tenantService.getCurrentTenantName()).get(MODEL_KEY);
	}
	
	
	private void importExtLibray() {
		//import ext library
		try {
			File underscoreJs = new ClassPathResource(CLASSPATH_SCRIPTS_LIB_PATH + "underscore.js/underscore-min.js").getFile();
			logger.debug("underscoreJs exists : " + underscoreJs.exists());
			
			File momentJs = new ClassPathResource(CLASSPATH_SCRIPTS_LIB_PATH + "moment.js/moment.js").getFile();
			logger.debug("momentJs exists : " + momentJs.exists());
			
//			File envJs = new ClassPathResource(CLASSPATH_SCRIPTS_LIB_PATH + "envjs/env.rhino.js").getFile();
//			logger.debug("envJs exists : " + envJs.exists());
			
			engines.get(tenantService.getCurrentTenantName()).eval(new FileReader(underscoreJs));
			engines.get(tenantService.getCurrentTenantName()).eval(new FileReader(momentJs));
//			engines.get(tenantService.getCurrentTenantName()).eval(new FileReader(envJs));
			
//			https://gist.github.com/UnquietCode/5614860
//			Object moment = ((Invocable) engine).invokeFunction("moment");
			
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (ScriptException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} 
//		catch (NoSuchMethodException e) {
//			logger.error(e);
//		}
	}
	
	
	private void importGlobalScript() {
		try {
			FileReader globalScript = null;
			
			String globalScriptPathFileSystem = fileSystemScriptsPath + CLASSPATH_GLOBAL_SCRIPTS_PATH + File.separator + DEFAULT_SCRIPT_NAME + DEFAULT_SCRIPT_EXTENSION;
			
			logger.debug("globalScriptPathFileSystem : "  + globalScriptPathFileSystem);
			
			Resource globalScriptFileSystem = new FileSystemResource(globalScriptPathFileSystem);
			if (globalScriptFileSystem.exists()) {
				logger.debug("globalScriptFileSystem found here: "  + globalScriptFileSystem);
				globalScript = new FileReader(globalScriptFileSystem.getFile());
			}else {
				throw new FileNotFoundException("File "+globalScriptPathFileSystem + " not found");
			}
			
			engines.get(tenantService.getCurrentTenantName()).eval(globalScript);
			logger.info("globalScript evalued"  );
			
			
//		} catch (FileNotFoundException e) {
//			logger.error(e);
		} catch (ScriptException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} 
	}
	
	public Map<String, Object> call(String functionName, Map parameters) throws FileNotFoundException, ScriptException, IOException, NoSuchMethodException {
//	public synchronized Map<String, Object> call(String functionName, Map parameters) throws FileNotFoundException, ScriptException, IOException, NoSuchMethodException {
		createEngine();
		cleanEngineParams();
		 
		 long start = 0L;
	     start = System.nanoTime();
		        
			FileReader script = null;
			
			//leggi prima da classpath lo script
			
//			TODO nn legge da meta-inf 
			String pathClassPathScript = CLASSPATH_SCRIPTS_PATH +  tenantService.getCurrentTenantName() + File.separator + DEFAULT_SCRIPT_NAME + DEFAULT_SCRIPT_EXTENSION;
			logger.debug("pathClassPathScript : "  + pathClassPathScript);
			
			Resource classPathScript = new ClassPathResource(pathClassPathScript);
			if (classPathScript.exists()) {
				logger.debug("pathClassPathScript found here: "  + pathClassPathScript);
				script = new FileReader(classPathScript.getFile());
			}
			//se presente su filesystem fai override
			
			String pathFileSystemScript = fileSystemScriptsPath + tenantService.getCurrentTenantName() + File.separator + DEFAULT_SCRIPT_NAME + DEFAULT_SCRIPT_EXTENSION;
			logger.debug("pathFileSystemScript : "  + pathFileSystemScript);
			
			Resource fileSystemScript = new FileSystemResource(pathFileSystemScript);
			if (fileSystemScript.exists()) {
				logger.debug("fileSystemScript found here: "  + pathFileSystemScript);
				script = new FileReader(fileSystemScript.getFile());
			}
		
			if (script==null) {
				logger.warn("Script with name : " + DEFAULT_SCRIPT_NAME +" not found for tenant : " + tenantService.getCurrentTenantName());
//				throw new FileNotFoundException("Script with name : " + DEFAULT_SCRIPT_NAME +" not found for tenant : " + tenantService.getCurrentTenantName());
			}else {
			
				
				//with this if i can execute script contained only in _global folder without tenat specific script
				if (logger.isTraceEnabled()) {
					String scriptText = null;
					
					if (fileSystemScript.exists())
					 scriptText = FileUtils.readFileToString(fileSystemScript.getFile());
					else
					 scriptText = FileUtils.readFileToString(classPathScript.getFile());
					
					logger.trace("Executing script with text :" + scriptText);
				}
			
				logger.debug("Selected engine : "+ engines.get(tenantService.getCurrentTenantName()));
				engines.get(tenantService.getCurrentTenantName()).eval(script);
			}
			
			// javax.script.Invocable is an optional interface.
	        // Check whether your script engine implements or not!
	        // Note that the JavaScript engine implements Invocable interface.
	        Invocable inv = (Invocable) engines.get(tenantService.getCurrentTenantName());

	        Object returnValues = null;
	        
	        //add parameter to function
	        //if is a controller function 
			 if(parameters!=null && parameters.containsKey(REQUEST_KEY) && parameters.containsKey(RESPONSE_KEY)) {
				  // invoke the global function named "hello"
			        returnValues = inv.invokeFunction(functionName, parameters.get(REQUEST_KEY), parameters.get(RESPONSE_KEY), parameters );
			 } else {
				 returnValues = inv.invokeFunction(functionName, parameters );
			 }
				 
	       
			logger.info("Executed function "+ functionName +" by script " + DEFAULT_SCRIPT_NAME + " in " + (System.nanoTime() - start)/1000000f + "ms");
			
			return (Map<String, Object>)returnValues;
		}

//	@Override
	public void resetEngines() {
		engines = new HashMap<String, ScriptEngine>();
		logger.info("Script Engines resetted");
		
	}
	
	public void resetEngine(String engineName) {
		engines.remove(engineName);
		logger.info("Script Engine with name : " + engineName + " resetted");
	}
	
	public String getFileSystemScriptsPath() {
		return fileSystemScriptsPath;
	}

	public void setFileSystemScriptsPath(String fileSystemScriptsPath) {
		this.fileSystemScriptsPath = fileSystemScriptsPath;
	}
	
	
}
