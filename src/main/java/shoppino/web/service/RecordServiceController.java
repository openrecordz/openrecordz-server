package shoppino.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import shoppino.domain.customdata.CustomData;
import shoppino.exception.ResourceNotFoundException;
import shoppino.exception.ShoppinoException;
import shoppino.service.CustomDataService;
import shoppino.service.RecordDataService;



//http://matera.api.openrecordz.com/service/v1/datasets POST (creo ds)
//http://matera.api.openrecordz.com/service/v1/datasets/dsId PUT (aggiorno ds)

//http://matera.api.openrecordz.com/service/v1/datasets/dsId POST (aggiungo dati nel ds)
//http://matera.api.openrecordz.com/service/v1/datasets/dsId/recordId Put (aggiungo dati nel ds)


	
@Controller
//@RequestMapping(value = "/cdata/")
public class RecordServiceController implements BaseServiceController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	CustomDataService customDataService;
	
	@Autowired
	RecordDataService recordDataService;
   
//	@Autowired
//	CustomDataSearchService customDataSearchService;
	
	@Value("$shoppino{default.page}")
	private int defaultPage = 0;
	
	@Value("$shoppino{default.pagesize}")
	private int defaultPageSize = 20;
    
	//@Autowired
	//MessageSource cdmessageSource;
//    https://www.parse.com/docs/rest#objects-types
    
    @RequestMapping(value = "/datasets/{dsId}", method = RequestMethod.POST)  
	 public @ResponseBody CustomData create(Model model, @PathVariable String dsId, 
			 @RequestParam(value = "type", required=false, defaultValue="record") String type,
			 @RequestBody String jsonStr
			 ) throws ShoppinoException {
   	
    	
//    	Map json=(Map) JSON.parse(jsonStr);
//    	if (json!=null && json.containsKey("_slug")) {
//    		String dsSlug = (String) json.get("_slug");
//    		List<CustomData> resuls=customDataService.findByQuery("{\"_slug\":\"" +dsSlug+"\"}", "dataset");
//    		if (resuls!=null && resuls.size()>=1)
//    			throw new ShoppinoException("Dataset slug already exists");    		    			
//    	}else {
//    		throw new ShoppinoException("Dataset doesn't contains slug field");
//    	}
    	
    	
//    	String cdataId = customDataService.add("record", jsonStr);
    	String cdataId = recordDataService.add(dsId,type, jsonStr);
    	CustomData cdata = recordDataService.getById(cdataId);
            
       
       return cdata;
   }
   
    @RequestMapping(value = "/datasets/{dsId}/{id}.map", method = RequestMethod.GET)  
	 public @ResponseBody CustomData getAsMap(
			 Model model, 
			 @PathVariable String dsId, 
			 @PathVariable("id") String id,
			 @RequestParam(value = "byslug", required=false) Boolean searchbySlug,
			 HttpServletRequest request) throws ShoppinoException {
  	
    	logger.debug("searchbySlug : " + searchbySlug);

    
    	
    	CustomData cdata = recordDataService.getByIdInternal(id);
           
    	
    	CustomData ds =null;
    	if (searchbySlug!=null) {
    		List<CustomData> dsArrayAsSlug = customDataService.findByQueryInternal("{\"_slug\":\"" +dsId+"\"}","dataset");
    		if (dsArrayAsSlug!=null && dsArrayAsSlug.size()>0){
    			ds=dsArrayAsSlug.get(0);
    			dsId=ds.getId();
    		} else
    			throw new ResourceNotFoundException("Dataset not found with slug : " + dsId);
    	}else {
    		ds = customDataService.getByIdInternal(dsId);
    	}
    	cdata.put("dataset", ds);
    	
      return cdata;
  }
    
    
    @RequestMapping(value = "/datasets/{dsId}/{id}", method = RequestMethod.GET)  
	 public @ResponseBody CustomData get(
			 Model model, 
			 @PathVariable String dsId, 
			 @PathVariable("id") String id,
			 HttpServletRequest request) throws ShoppinoException {
  	
    	
    	CustomData cdata = recordDataService.getByIdInternal(id);
           
      return cdata;
  }
    
    
    @RequestMapping(value = "/datasets/{dsId}/{id}", method = RequestMethod.DELETE)  
	 public @ResponseBody String delete(Model model, @PathVariable String dsId, 
			 @PathVariable("id") String id, HttpServletRequest request) throws ShoppinoException {
 	
    	recordDataService.remove(id);
          
     
     return "{success:true}";
 }
    
    
    @RequestMapping(value = "/datasets/{dsId}/onlyrecord", method = RequestMethod.DELETE)  
	 public @ResponseBody String deleteAllByClassName(Model model, @PathVariable String dsId, 
			 HttpServletRequest request) throws ShoppinoException {
	
//   	customDataService.removeAll("record");
    	recordDataService.removeAll(dsId);
         
    
    return "{success:true}";
}
    
    @RequestMapping(value = "/datasets/{dsId}/{id}", method = RequestMethod.PUT)  
  	 public @ResponseBody CustomData put(Model model, @PathVariable String dsId, 
  			@PathVariable("id") String id,  
//  			@RequestParam("json") String json, 
  			@RequestBody String jsonStr,
  			@RequestParam(value = "versioning", required=false, defaultValue="false") Boolean versioningEnabled,
  			
  			HttpServletRequest request) throws ShoppinoException {
    	
    	String cdataId = recordDataService.update(id, dsId, jsonStr, versioningEnabled);
//    	String cdataId = customDataService.update(id, "record", jsonStr);
    	CustomData cdata = recordDataService.getById(cdataId);    
        
        return cdata;
    }
    
    @RequestMapping(value = "/datasets/{dsId}/{id}", method = RequestMethod.PATCH)  
 	 public @ResponseBody CustomData patch(Model model, @PathVariable String dsId, 
 			@PathVariable("id") String id,  
// 			@RequestParam("json") String json, 
 			@RequestBody String jsonStr,
 			HttpServletRequest request) throws ShoppinoException {
   	
   	String cdataId = recordDataService.patch(id, dsId, jsonStr);
//   	String cdataId = customDataService.patch(id, "record", jsonStr);
   	CustomData cdata = recordDataService.getById(cdataId);    
       
       return cdata;
   }
//    geo: { "_location" : { "$nearSphere" : { "$geometry" : { "type" : "Point" , "coordinates" : [ -73.93414657 , 40.82302903]} , "$maxDistance" : 50000000000}}}
//    full text : { $text: { $search: "leo" } } 
    @RequestMapping(value = "/datasets/{dsId}.map", method = RequestMethod.GET)  
	 public @ResponseBody Map queryStructured(Model model, @PathVariable String dsId, 
     
			 @RequestParam(value = "q", required=false, defaultValue="{}") String query,

			 @RequestParam(value = "text", required=false) String text,
			 @RequestParam(value = "near", required=false) String near,

			 //			 deprecated use solr			 
//			 @RequestParam(value = "fq", required=false) String fullQuery,
			 
			 @RequestParam(value = "type", required=false) String type,
			 
			 @RequestParam(value = "sort", required=false, defaultValue="_modifiedOn") String sortFields, 
			 @RequestParam(value = "direction", required=false, defaultValue="asc") String direction, 
			 @RequestParam(value = "status", required=false) Integer status,
			 
			 @RequestParam(value = "byslug", required=false) Boolean searchbySlug,
			 
			 HttpServletRequest request) throws ShoppinoException {
 	
    	int page = defaultPage;
    	int pageSize = defaultPageSize;
//    	String test = cdmessageSource.getMessage("test", null, Locale.getDefault());
    	if (request.getParameter("page")!=null)
    		page=Integer.parseInt(request.getParameter("page"));
    	
    	if (request.getParameter("pagesize")!=null)
    		pageSize = Integer.parseInt(request.getParameter("pagesize"));
    	
    	
    	logger.debug("type: " + type);
    	logger.debug("page : " + page);
    	logger.debug("pageSize : " + pageSize);
    	logger.debug("dsId : " + dsId);    	
    	logger.debug("query : " + query);
//    	logger.debug("fullQuery : " + fullQuery);
    	logger.debug("text : " + text);
    	logger.debug("near : " + near);
    	logger.debug("searchbySlug : " + searchbySlug);
    	
    	
    	
    	if (near!=null && !near.equals("")) {
    		String latitude=near.split(",")[0];
    		String longitude=near.split(",")[1];
    		query="{ \"_location\" : { \"$nearSphere\" : { \"$geometry\" : { \"type\" : \"Point\" , \"coordinates\" : ["+latitude+" , "+longitude+"]} }}} ";
    	}
    	
    	if (text!=null && !text.equals("")) {    		
    		query="{ $text: { $search: \""+text+"\" } } ";	
    	}
    	
    	
//    	if (fullQuery!=null)
//    		return customDataSearchService.findByQueryLocationPaginated(fullQuery, page, pageSize, status);
//    	else

    	Map valuesWithCount = new HashMap();
    	
    	CustomData ds =null;
    	if (searchbySlug!=null) {
    		List<CustomData> dsArrayAsSlug = customDataService.findByQueryInternal("{\"_slug\":\"" +dsId+"\"}","dataset");
    		if (dsArrayAsSlug!=null && dsArrayAsSlug.size()>0){
    			ds=dsArrayAsSlug.get(0);
    			dsId=ds.getId();
    		} else
    			throw new ResourceNotFoundException("Dataset not found with slug : " + dsId);
    	}else {
    		ds = customDataService.getByIdInternal(dsId);
    	}
    	valuesWithCount.put("dataset", ds);
    	
    	
    	
    	List<CustomData> ret=recordDataService.findByQueryInternal(query, dsId, type,page, pageSize, direction, sortFields, status);
    	
    	
		valuesWithCount.put("records", ret);
		
		long count= recordDataService.countByQueryInternal(query, dsId, type, status);
		valuesWithCount.put("count", count);
		
//		try {
		
//		}catch (Exception e) {
//			logger.warn("Dataset not found with id: "+ dsId, e);
//		}
		return valuesWithCount;
		    
    	
    	
 }
    
    
    @RequestMapping(value = "/datasets/{dsId}", method = RequestMethod.GET)  
	 public @ResponseBody List<CustomData> query(Model model, @PathVariable String dsId,
			 @RequestParam(value = "q", required=false, defaultValue="{}") String query,

			 @RequestParam(value = "text", required=false) String text,
			 @RequestParam(value = "near", required=false) String near,

			 //			 deprecated use solr			 
			// @RequestParam(value = "fq", required=false) String fullQuery,
			 
			 @RequestParam(value = "type", required=false) String type,
			 
			 @RequestParam(value = "sort", required=false) String sortFields, 
			 @RequestParam(value = "direction", required=false) String direction, 
			 @RequestParam(value = "status", required=false) Integer status,
			 
			 HttpServletRequest request) throws ShoppinoException {
    	
    	
//    	List<CustomData> ret=this.query(model, className, query, text, near, null, sortFields, direction, status, request);
//    	return (List<CustomData>)queryStructured(model, dsId, query, text, near, sortFields, direction, status, request).get("items");
    	
    	
    	int page = defaultPage;
    	int pageSize = defaultPageSize;
//    	String test = cdmessageSource.getMessage("test", null, Locale.getDefault());
    	if (request.getParameter("page")!=null)
    		page=Integer.parseInt(request.getParameter("page"));
    	
    	if (request.getParameter("pagesize")!=null)
    		pageSize = Integer.parseInt(request.getParameter("pagesize"));
    	
    	
    	logger.debug("page : " + page);
    	logger.debug("pageSize : " + pageSize);
    	logger.debug("className : " + dsId);    	
    	logger.debug("query : " + query);
//    	logger.debug("fullQuery : " + fullQuery);
    	logger.debug("text : " + text);
    	logger.debug("near : " + near);
    	
    	
    	if (near!=null && !near.equals("")) {
    		String latitude=near.split(",")[0];
    		String longitude=near.split(",")[1];
    		query="{ \"_location\" : { \"$nearSphere\" : { \"$geometry\" : { \"type\" : \"Point\" , \"coordinates\" : ["+latitude+" , "+longitude+"]} }}} ";
    	}
    	
    	if (text!=null && !text.equals("")) {    		
    		query="{ $text: { $search: \""+text+"\" } } ";	
    	}
    	
    	
//    	if (fullQuery!=null)
//    		return customDataSearchService.findByQueryLocationPaginated(fullQuery, page, pageSize, status);
//    	else
    	
    	List<CustomData> ret=recordDataService.findByQueryInternal(query, dsId,type, page, pageSize, direction, sortFields, status);
    	
    	
		return ret;
		    
    	
    	
    	
    }
			 
    @RequestMapping(value = "/datasets/{dsId}.csv", method = RequestMethod.GET)  
	 public void queryCSV(Model model, @PathVariable String dsId, 
			 @RequestParam(value = "q", required=false, defaultValue="{}") String query,
			 
			 @RequestParam(value = "text", required=false) String text,
			 @RequestParam(value = "near", required=false) String near,

			 //			 deprecated use solr			 
//			 @RequestParam(value = "fq", required=false) String fullQuery,
			 
			 @RequestParam(value = "type", required=false) String type,
			 
			 @RequestParam(value = "sort", required=false) String sort, 
			 @RequestParam(value = "direction", required=false) String direction, 
			 @RequestParam(value = "status", required=false) Integer status,
			 HttpServletRequest request, HttpServletResponse response) throws ShoppinoException, IOException {
 
        String csvFileName = dsId+".csv";
 
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
 
//        Book book1 = new Book("Effective Java", "Java Best Practices",
//                "Joshua Bloch", "Addision-Wesley", "0321356683", "05/08/2008",
//                38);
// 
//        Book book2 = new Book("Head First Java", "Java for Beginners",
//                "Kathy Sierra & Bert Bates", "O'Reilly Media", "0321356683",
//                "02/09/2005", 30);
// 
//        Book book3 = new Book("Thinking in Java", "Java Core In-depth",
//                "Bruce Eckel", "Prentice Hall", "0131872486", "02/26/2006", 45);
// 
//        Book book4 = new Book("Java Generics and Collections",
//                "Comprehensive guide to generics and collections",
//                "Naftalin & Philip Wadler", "O'Reilly Media", "0596527756",
//                "10/24/2006", 27);
// 
//        List<Book> listBooks = Arrays.asList(book1, book2, book3, book4);
 
//        List<CustomData> cdatas = this.query(model, className, query,null, sort, direction, status, request);
//        List<CustomData> cdatas = this.query(model, className, query,text,near,fullQuery, sort, direction, status, request);
        List<CustomData> cdatas = this.query(model, dsId, query, text, near, type, sort, direction, status, request);
        
//        CsvDozerBeanWriter csvWriter = new  CsvDozerBeanWriter(response.getWriter(),
//              CsvPreference.STANDARD_PREFERENCE);
//        		
//        for (CustomData cdata: cdatas) {
//        	csvWriter.write(cdata);
//      }
//        csvWriter.close();
        
//        // uses the Super CSV API to generate CSV data from the model data
        CsvMapWriter csvWriter = new  	CsvMapWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
//        
//        ICsvListWriter listWriter = new CsvListWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
 
        if (cdatas!=null && cdatas.size()>0) {
        	int i=0;
	        String[] header = new String[cdatas.get(0).keySet().size()];
	        Iterator iterator =cdatas.get(0).keySet().iterator();
	        while (iterator.hasNext())
		       {
	        	header[i]=iterator.next().toString();
	       		i++;
		       }
	        
	        csvWriter.writeHeader(header);
	        
	        for (CustomData cdata: cdatas) {
	        	 csvWriter.write(cdata.toMap(),header);
	//        	Iterator iteratorCData =cdata.keySet().iterator();
	//        	 while (iteratorCData.hasNext())
	//  	       {
	//        		 String key = iteratorCData.next().toString();
	// 	            Object value = cdata.get(key);
	// 	            
	// 	            if (value instanceof ObjectId)
	// 	            	value = ((ObjectId)value).toString();
	// 	            
	//        		 csvWriter.write(value,header);
	//  	       }
	        }
        }
//        String[] header = { "Title", "Description", "Author", "Publisher",
//                "isbn", "PublishedDate", "Price" };
// 
//        csvWriter.writeHeader(header);
 
//        for (CustomData cdata: cdatas) {
//            csvWriter.write(cdata);
////            csvWriter.write(cdata, header);
//        }
//        
//        listWriter.write(cdatas,header);
//// 
//        listWriter.close();
        csvWriter.close();
    }
    
}