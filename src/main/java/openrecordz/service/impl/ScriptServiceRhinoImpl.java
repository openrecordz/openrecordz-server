package openrecordz.service.impl;
//package shoppino.service.impl;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.script.Invocable;
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineFactory;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.mozilla.javascript.Context;
//import org.mozilla.javascript.Scriptable;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//
//import shoppino.mail.service.MailService;
//import shoppino.security.service.AuthenticationService;
//import shoppino.service.CustomDataService;
//import shoppino.service.NotificationService;
//import shoppino.service.ProductSearchService;
//import shoppino.service.ProductService;
//import shoppino.service.ScriptService;
//import shoppino.service.TenantService;
//import shoppino.service.UrlService;
//
//public class ScriptServiceRhinoImpl implements ScriptService {
//
//	
//	protected final Log logger = LogFactory.getLog(getClass());
//	
//	protected final Log scriptLogger = LogFactory.getLog(ScriptLogger.class);
//	
//	public static String CLASSPATH_SCRIPTS_PATH = "/scripts/";
//	
//	public static String CLASSPATH_SCRIPTS_LIB_PATH = "/scripts-lib/";
//
//	public static String MODEL_KEY = "model";
//	public static String PARAMS_KEY = "params";
//	public static String REQUEST_KEY = "req";
//	public static String RESPONSE_KEY = "res";
//	
//	public static String DEFAULT_SCRIPT_NAME = "main";
//	public static String DEFAULT_SCRIPT_EXTENSION = ".js";
//	
//	@Value("$shoppino{scripting.filesystem.path}")
//	public String fileSystemScriptsPath;
//
//	
//	@Value("$shoppino{openrecordz.url}")
//	private String platformUrl;
//	
//	@Value("$shoppino{openrecordz.hostname}")
//	private String platformHostname;
//	
//	
////	@Autowired
//	private AuthenticationService authenticationService;
//	
////	@Autowired
//	private TenantService tenantService;
//	
//	private  ScriptEngine engine;
//	
//	public ScriptServiceRhinoImpl(ProductService productService, ProductSearchService productSearchService, 
//			AuthenticationService authenticationService, TenantService tenantService, 
//			CustomDataService customDataService, MailService mailService, 
//			NotificationService notificationService, UrlService urlService) {
//		
//		
//		this.authenticationService = authenticationService;
//		this.tenantService = tenantService;
//		
//		 // create a script engine manager
//        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
//        
////        "MULTITHREADED" - The engine implementation is internally thread-safe and scripts may execute concurrently although 
////        effects of script execution on one thread may be visible to scripts on other threads."
//        
//        List<ScriptEngineFactory> factories = scriptEngineManager.getEngineFactories();
//        for ( ScriptEngineFactory factory : factories ) {
//            logger.debug("Full name = " + factory.getEngineName ());
//            logger.debug("Version = " + factory.getEngineVersion ());
//            String [] params =
//            {
//               ScriptEngine.ENGINE,
//               ScriptEngine.ENGINE_VERSION,
//               ScriptEngine.LANGUAGE,
//               ScriptEngine.LANGUAGE_VERSION,
//               ScriptEngine.NAME,
//               "THREADING"
//            };
//        
//            for (String param: params)
//            {
//            	logger.debug("Parameter " + param + "" +
//                                    factory.getParameter (param));
//            }
//                
//            logger.debug( String.format(
//                    "engineName: %s, THREADING: %s",
//                    factory.getEngineName(),
//                    factory.getParameter( "THREADING" ) ) );
//        }
//        
//        // create a JavaScript engine
////        JSR223
//        engine = scriptEngineManager.getEngineByName("JavaScript");
//        
//        engine.put("authenticationService", authenticationService);
//        engine.put("tenantService", tenantService);
//        engine.put("productService", productService);
//        engine.put("productSearchService", productSearchService);
//        engine.put("customDataService", customDataService);
//        engine.put("mailService", mailService);  
//        engine.put("notificationService", notificationService);
//        engine.put("urlService", urlService);
//        
//        engine.put("logger", scriptLogger);
//        
//        Map info = new HashMap();
//        info.put("hostname", platformHostname);
//        info.put("url", platformUrl);
//        engine.put("info", info);
//      
//        
//        importExtLibray();
//        cleanEngineParams();
////        engine.put("testpp", "testvalue");
//     
//
//	}
//	
//	private void cleanEngineParams() {
//		engine.put(MODEL_KEY,  new HashMap<String, Object>());
//        engine.put(PARAMS_KEY, null);
//        engine.put(REQUEST_KEY, null);
//        engine.put(RESPONSE_KEY, null);
//	}
//	
//	
//	public Map<String, Object> eval(String scriptName, Map parameters) throws FileNotFoundException, ScriptException, IOException {
//	
//		throw new UnsupportedOperationException("Method not implemented");
//	}
//	
//	
//	private void importExtLibray() {
//		//import ext library
//		try {
//			engine.eval(new FileReader(new ClassPathResource(CLASSPATH_SCRIPTS_LIB_PATH + "underscore.js/underscore-min.js").getFile()));
//			engine.eval(new FileReader(new ClassPathResource(CLASSPATH_SCRIPTS_LIB_PATH + "moment.js/moment.js").getFile()));
//		} catch (FileNotFoundException e) {
//			logger.error(e);
//		} catch (ScriptException e) {
//			logger.error(e);
//		} catch (IOException e) {
//			logger.error(e);
//		}
//	}
//	
//	public Map<String, Object> call(String functionName, Map parameters) throws FileNotFoundException, ScriptException, IOException, NoSuchMethodException {
//		 cleanEngineParams();
//		 
//		 long start = 0L;
//	     start = System.nanoTime();
//		        
//			FileReader script = null;
//			
//			//leggi prima da classpath lo script
//			
////			TODO nn legge da meta-inf 
//			String pathClassPathScript = CLASSPATH_SCRIPTS_PATH +  tenantService.getCurrentTenantName() + File.separator + DEFAULT_SCRIPT_NAME + DEFAULT_SCRIPT_EXTENSION;
//			logger.debug("pathClassPathScript : "  + pathClassPathScript);
//			
//			Resource classPathScript = new ClassPathResource(pathClassPathScript);
//			if (classPathScript.exists()) {
//				logger.debug("pathClassPathScript found here: "  + pathClassPathScript);
//				script = new FileReader(classPathScript.getFile());
//			}
//			//se presente su filesystem fai override
//			
//			String pathFileSystemScript = fileSystemScriptsPath + tenantService.getCurrentTenantName() + File.separator + DEFAULT_SCRIPT_NAME + DEFAULT_SCRIPT_EXTENSION;
//			logger.debug("pathFileSystemScript : "  + pathFileSystemScript);
//			
//			Resource fileSystemScript = new FileSystemResource(pathFileSystemScript);
//			if (fileSystemScript.exists()) {
//				logger.debug("fileSystemScript found here: "  + pathFileSystemScript);
//				script = new FileReader(fileSystemScript.getFile());
//			}
//		
//			if (script==null)
//				throw new FileNotFoundException("Script with name : " + DEFAULT_SCRIPT_NAME +" not found");
//			
//			
//			if (logger.isTraceEnabled()) {
//				String scriptText = null;
//				
//				if (fileSystemScript.exists())
//				 scriptText = FileUtils.readFileToString(fileSystemScript.getFile());
//				else
//				 scriptText = FileUtils.readFileToString(classPathScript.getFile());
//				
//				logger.trace("Executing script with text :" + scriptText);
//			}
//		
//			
//			Context cx = Context.enter();
//			 try {
//				// Initialize the standard objects (Object, Function, etc.)
//				// This must be done before scripts can be executed. Returns
//				// a scope object that we use in later calls.
//				Scriptable scope = cx.initStandardObjects();
//			 } finally {
//				// Exit from the context.
//				Context.exit();
//			 }
//			
//			
//			// javax.script.Invocable is an optional interface.
//	        // Check whether your script engine implements or not!
//	        // Note that the JavaScript engine implements Invocable interface.
//	        Invocable inv = (Invocable) engine;
//
//	        Object returnValues = null;
//	        
//	        //add parameter to function
//	        //if is a controller function 
//			 if(parameters!=null && parameters.containsKey(REQUEST_KEY) && parameters.containsKey(RESPONSE_KEY)) {
//				  // invoke the global function named "hello"
//			        returnValues = inv.invokeFunction(functionName, parameters.get(REQUEST_KEY), parameters.get(RESPONSE_KEY), parameters );
//			 } else {
//				 returnValues = inv.invokeFunction(functionName, parameters );
//			 }
//				 
//	       
//			logger.info("Executed function "+ functionName +" by script " + DEFAULT_SCRIPT_NAME + " in " + (System.nanoTime() - start)/1000000f + "ms");
//			
//			return (Map<String, Object>)returnValues;
//		}
//
//	public String getFileSystemScriptsPath() {
//		return fileSystemScriptsPath;
//	}
//
//	public void setFileSystemScriptsPath(String fileSystemScriptsPath) {
//		this.fileSystemScriptsPath = fileSystemScriptsPath;
//	}
//	
//	
//}
