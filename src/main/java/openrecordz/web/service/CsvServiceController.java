package openrecordz.web.service;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.util.JSON;

import openrecordz.domain.customdata.CustomData;
import openrecordz.exception.OpenRecordzException;
import openrecordz.scripting.CSVUtil;
import openrecordz.scripting.JSONUtil;
import openrecordz.security.service.AuthenticationService;
import openrecordz.service.CustomDataService;
import openrecordz.service.RecordDataService;
import openrecordz.service.TenantService;
import openrecordz.service.impl.ConfigThreadLocal;


@Controller
public class CsvServiceController implements BaseServiceController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	 public static Connection connect = null;

    
    @Autowired
    AuthenticationService authenticationService;
    
	@Autowired
	CustomDataService customDataService;
  
    @Autowired
    @Value("$platform{file.filesystem.path}")
	public String fileSystemTemplatesPath;
    
    @Autowired
    public TenantService tenantService;

    @Autowired
	private RecordDataService recordDataService;
    
    @RequestMapping(value = "/csv/previewcsv", method = RequestMethod.GET)  
	 public @ResponseBody List<Map<String, String>> previewcsv(Model model, HttpServletRequest req) throws OpenRecordzException, IOException {
   	
	    	String charSeparator = ",";
	    	if (req.getParameter("charseparator")!=null)
	    			charSeparator = req.getParameter("charseparator");

	    	logger.info("charSeparator: "+ charSeparator); 

	    	String file =req.getParameter("file");
	    	 logger.info("file : " + file);
	
	    	 Boolean withHeader=true;
	    	if (req.getParameter("withheader")!=null)
	    			withHeader = Boolean.parseBoolean(req.getParameter("withheader"));
	
	    //withHeader=false;
	    	logger.info("withheader: "+ withHeader); 
	    	CSVUtil csvUtil = new CSVUtil();
	    	
	     	//var firstLines = _utils.get("csv").parse(environmentService.getFileFilesystemPath()+info.get("tenantName")+"/"+file, withHeader ,null,java.lang.Character(charSeparator.charAt(0)), 20);
	    	List<Map<String, String>> firstLines = csvUtil.parse(fileSystemTemplatesPath+tenantService.getCurrentTenantName()+"/"+file, withHeader ,null, charSeparator.charAt(0), 20);
	    	 logger.info("firstLines : " + firstLines);
	  
	    	 return firstLines;
	  
    	
    	
   }
    
    
    
    @RequestMapping(value = "/csv/parsecsvheader", method = RequestMethod.GET)  
	 public @ResponseBody  Map<String, Integer> parsecsvheader(Model model, HttpServletRequest req) throws OpenRecordzException, IOException {
   	    	
 	   ConfigThreadLocal.get().put("mailEnabled", false);

 	
 	 String file =req.getParameter("file");
 	 logger.info("file : " + file);


 	String charSeparator = ",";
 	if (req.getParameter("charseparator")!=null)
 			charSeparator = req.getParameter("charseparator");

 	logger.info("charSeparator: "+ charSeparator); 
 	 

 	String dsSlug=req.getParameter("ds");
 	logger.info("dsSlug: "+ dsSlug); 
 	CustomData dataset = null;
 	if (dsSlug!=null ) {
 		dataset = customDataService.findByQueryInternal("{\"_slug\":\"" +dsSlug+"\"}", "dataset").get(0);
 	}

 	Boolean withHeader=true;
 	if (req.getParameter("withheader")!=null)
 			withHeader = Boolean.parseBoolean(req.getParameter("withheader"));

 	logger.info("withheader: "+ withHeader); 

 	CSVUtil csvUtil = new CSVUtil();
 	 Map<String, Integer>  headers = csvUtil.getHeader(fileSystemTemplatesPath+tenantService.getCurrentTenantName()+"/"+file,withHeader,charSeparator.charAt(0));
 	 //var headers = _utils.get("csv").getHeader(environmentService.getFileFilesystemPath()+info.get("tenantName")+"/"+file,withHeader, java.lang.Character(charSeparator.charAt(0)));
 	 
 	 
 	 logger.info("headers : " + headers);

 
 	 return headers;
 
    	
   }

    
    
    
    @RequestMapping(value = "/csv/parsecsv", method = RequestMethod.GET)  
  	 public @ResponseBody String parsecsv(Model model, HttpServletRequest req) throws OpenRecordzException, IOException {
     	    	
    	
    	   ConfigThreadLocal.get().put("mailEnabled", false);
    	   ConfigThreadLocal.get().put("scriptingEnabled", false);

    	   String dsName=req.getParameter("ds");
    	
    	   String fileds =req.getParameter("file");
    	   logger.info("fileds : " + fileds);


    	   String charSeparator = ",";
    	   if (req.getParameter("charseparator")!=null)
    			charSeparator = req.getParameter("charseparator");

    	   logger.info("charSeparator: "+ charSeparator); 
    	 

	    	Boolean withHeader=true;
	    	if (req.getParameter("withheader")!=null)
	    			withHeader = Boolean.parseBoolean(req.getParameter("withheader"));

	    	logger.info("withheader: "+ withHeader); 


 


    	String reqBody=IOUtils.toString(req.getReader());
    	logger.info(reqBody);
    	Map reqBodyAsJson = (Map)JSON.parse(reqBody);
    	logger.info("reqBodyAsJson : " + reqBodyAsJson);
    	logger.info("reqBodyAsJson.columnname : " + reqBodyAsJson.get("columnname"));
    	logger.info("reqBodyAsJson.mapping : " + reqBodyAsJson.get("mapping"));
    	
    	String columnNameAsString =reqBodyAsJson.get("columnname").toString();
    	String[] columnNameArray =columnNameAsString.split(",");
    	
    	logger.info("columnNameArray: "+ columnNameArray);

    	for(int i=0;i<columnNameArray.length;i++){
    	 	logger.info("columnNameArray : " + columnNameArray[i]);
    	}



    	CSVUtil csvUtil= new CSVUtil();

//    	 var results = _utils.get("csv").parse("/mnt/ebsvolume/repos/scripts/"+info.get("tenantName")+"/"+fileds,true,columnNameArray);
    	  List<Map<String,String>> results = csvUtil.parse(fileSystemTemplatesPath+tenantService.getCurrentTenantName()+"/"+fileds,true,columnNameArray,charSeparator.charAt(0));
    	 //var results = _utils.get("csv").parse(environmentService.getFileFilesystemPath()+info.get("tenantName")+"/"+fileds,true,columnNameArray,java.lang.Character(charSeparator.charAt(0)));
    	 

    	 logger.info("results : " + results);



    	CustomData dataset = customDataService.findByQueryInternal("{\"_slug\":\"" +dsName+"\"}", "dataset").get(0);
    	logger.info("dataset.id : " + dataset.getId() );

    	JSONUtil jsonUtil = new JSONUtil();
    	for (int i = 0;i<results.size();i++) {
    		Map<String,String> result = results.get(i);


    		logger.info("result : " + result);

    		String resAsJson = jsonUtil.toJSON(result);
    		logger.info("resAsJson : " + resAsJson);

    		//if csv contains _idext it's possible a patch over a record
    		if (result.containsKey("_idext") && result.get("_idext")!=null) {
    			String _idext=result.get("_idext").toString();
    			logger.info("Mapping contains _idext key con valore: "+_idext);
    			
    			List<CustomData> searchExistingRecordByIdExt=recordDataService.findByQueryInternal("{\"_idext\":\"" +_idext+"\"}", dataset.getId(), "record");
    			//not found..could be first import....
    			if (searchExistingRecordByIdExt.size()==0) {
    				logger.info("not found records with _idext: "+_idext +". Creating new record");
    				String recordId=recordDataService.add(dataset.getId(),"record", resAsJson);	
    			}else {
    				String existingRecordId=searchExistingRecordByIdExt.get(0).getId();
    				logger.info("found records with _idext: "+_idext +". Patching the record with id : "+existingRecordId );
    				String recordId=recordDataService.patch(existingRecordId,"record", resAsJson);			
    			}			
    		} else {

    		//	toreturn[i]=contentService.getById(cid);
    		//	var cdId=customDataService.add(dsName, resAsJson);
    			String recordId=recordDataService.add(dataset.getId(),"record", resAsJson);

    		//toreturn[i]=customDataService.getById(cdId);
    		}
    	}

    	Map datasetMapping = new HashMap();
    	datasetMapping.put("._mapping", reqBodyAsJson.get("mappingArray"));
    	logger.info("jsonUtil.toJSON(datasetMapping) : " +jsonUtil.toJSON(datasetMapping));

    	customDataService.patch(dataset.getId(),"dataset", jsonUtil.toJSON(datasetMapping));
    	 
    	  return "success";
    }

    
}