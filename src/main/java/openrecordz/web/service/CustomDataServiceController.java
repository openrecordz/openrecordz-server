package openrecordz.web.service;

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

import openrecordz.domain.customdata.CustomData;
import openrecordz.exception.ShoppinoException;
import openrecordz.service.CustomDataService;


@Controller
//@RequestMapping(value = "/cdata/")
public class CustomDataServiceController implements BaseServiceController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	CustomDataService customDataService;
   
//	@Autowired
//	CustomDataSearchService customDataSearchService;
	
	@Value("$shoppino{default.page}")
	private int defaultPage = 0;
	
	@Value("$shoppino{default.pagesize}")
	private int defaultPageSize = 20;
    
	//@Autowired
	//MessageSource cdmessageSource;
//    https://www.parse.com/docs/rest#objects-types
    
    @RequestMapping(value = "/cdata/{className}", method = RequestMethod.POST)  
	 public @ResponseBody CustomData create(Model model, @PathVariable String className, 
//			 @RequestParam("json") String json
			 @RequestBody String jsonStr
			 ) throws ShoppinoException {
   	
    	String cdataId = customDataService.add(className, jsonStr);
    	CustomData cdata = customDataService.getById(cdataId);
            
       
       return cdata;
   }
   
    @RequestMapping(value = "/cdata/", method = RequestMethod.GET)  
	 public @ResponseBody CustomData get(Model model,
			 @RequestParam(value = "_id", required=true) String id, 
			 HttpServletRequest request) throws ShoppinoException {
 	
    	CustomData cdata = customDataService.getByIdInternal(id);
          
     return cdata;
    }
    
    @RequestMapping(value = "/cdata/{className}/{id}", method = RequestMethod.GET)  
	 public @ResponseBody CustomData get(Model model, @PathVariable String className, 
			 @PathVariable("id") String id, HttpServletRequest request) throws ShoppinoException {
  	
    	CustomData cdata = customDataService.getByIdInternal(id);
           
      return cdata;
  }
    
    
    @RequestMapping(value = "/cdata/{className}/{id}", method = RequestMethod.DELETE)  
	 public @ResponseBody String delete(Model model, @PathVariable String className, 
			 @PathVariable("id") String id, HttpServletRequest request) throws ShoppinoException {
 	
    	customDataService.remove(id);
          
     
     return "{success:true}";
 }
    
    
    @RequestMapping(value = "/cdata/{className}", method = RequestMethod.DELETE)  
	 public @ResponseBody String deleteAllByClassName(Model model, @PathVariable String className, 
			 HttpServletRequest request) throws ShoppinoException {
	
   	customDataService.removeAll(className);
         
    
    return "{success:true}";
}
    
    @RequestMapping(value = "/cdata/{className}/{id}", method = RequestMethod.PUT)  
  	 public @ResponseBody CustomData put(Model model, @PathVariable String className, 
  			@PathVariable("id") String id,  
//  			@RequestParam("json") String json, 
  			@RequestBody String jsonStr,
  			HttpServletRequest request) throws ShoppinoException {
    	
    	String cdataId = customDataService.update(id, className, jsonStr);
    	CustomData cdata = customDataService.getById(cdataId);    
        
        return cdata;
    }
    
    @RequestMapping(value = "/cdata/{className}/{id}", method = RequestMethod.PATCH)  
 	 public @ResponseBody CustomData patch(Model model, @PathVariable String className, 
 			@PathVariable("id") String id,  
// 			@RequestParam("json") String json, 
 			@RequestBody String jsonStr,
 			HttpServletRequest request) throws ShoppinoException {
   	
   	String cdataId = customDataService.patch(id, className, jsonStr);
   	CustomData cdata = customDataService.getById(cdataId);    
       
       return cdata;
   }
//    geo: { "_location" : { "$nearSphere" : { "$geometry" : { "type" : "Point" , "coordinates" : [ -73.93414657 , 40.82302903]} , "$maxDistance" : 50000000000}}}
//    full text : { $text: { $search: "leo" } } 
    @RequestMapping(value = "/cdata/{className}.map", method = RequestMethod.GET)  
	 public @ResponseBody Map queryStructured(Model model, @PathVariable String className, 
     
			 @RequestParam(value = "q", required=false, defaultValue="{}") String query,

			 @RequestParam(value = "text", required=false) String text,
			 @RequestParam(value = "near", required=false) String near,

			 //			 deprecated use solr			 
//			 @RequestParam(value = "fq", required=false) String fullQuery,
			 
			 @RequestParam(value = "sort", required=false) String sortFields, 
			 @RequestParam(value = "direction", required=false) String direction, 
			 @RequestParam(value = "status", required=false) Integer status,
			 
			 HttpServletRequest request) throws ShoppinoException {
 	
    	int page = defaultPage;
    	int pageSize = defaultPageSize;
//    	String test = cdmessageSource.getMessage("test", null, Locale.getDefault());
    	if (request.getParameter("page")!=null)
    		page=Integer.parseInt(request.getParameter("page"));
    	
    	if (request.getParameter("pagesize")!=null)
    		pageSize = Integer.parseInt(request.getParameter("pagesize"));
    	
    	
    	logger.debug("page : " + page);
    	logger.debug("pageSize : " + pageSize);
    	logger.debug("className : " + className);    	
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
    	
    	List<CustomData> ret=customDataService.findByQueryInternal(query, className, page, pageSize, direction, sortFields, status);
    	
    	Map valuesWithCount = new HashMap();
		valuesWithCount.put("items", ret);
		
		long count= customDataService.countByQueryInternal(query, className, status);
		valuesWithCount.put("count", count);
		
		return valuesWithCount;
		    
    	
    	
 }
    
    
    @RequestMapping(value = "/cdata/{className}", method = RequestMethod.GET)  
	 public @ResponseBody List<CustomData> query(Model model, @PathVariable String className,
			 @RequestParam(value = "q", required=false, defaultValue="{}") String query,

			 @RequestParam(value = "text", required=false) String text,
			 @RequestParam(value = "near", required=false) String near,

			 //			 deprecated use solr			 
			// @RequestParam(value = "fq", required=false) String fullQuery,
			 
			 @RequestParam(value = "sort", required=false) String sortFields, 
			 @RequestParam(value = "direction", required=false) String direction, 
			 @RequestParam(value = "status", required=false) Integer status,
			 
			 HttpServletRequest request) throws ShoppinoException {
    	
    	
//    	List<CustomData> ret=this.query(model, className, query, text, near, null, sortFields, direction, status, request);
    	return (List<CustomData>)queryStructured(model, className, query, text, near, sortFields, direction, status, request).get("items");
    	
    	
    	
    }
			 
    @RequestMapping(value = "/cdata/{className}.csv", method = RequestMethod.GET)  
	 public void queryCSV(Model model, @PathVariable String className, 
			 @RequestParam(value = "q", required=false, defaultValue="{}") String query,
			 
			 @RequestParam(value = "text", required=false) String text,
			 @RequestParam(value = "near", required=false) String near,

			 //			 deprecated use solr			 
//			 @RequestParam(value = "fq", required=false) String fullQuery,
			 
			 @RequestParam(value = "sort", required=false) String sort, 
			 @RequestParam(value = "direction", required=false) String direction, 
			 @RequestParam(value = "status", required=false) Integer status,
			 HttpServletRequest request, HttpServletResponse response) throws ShoppinoException, IOException {
 
        String csvFileName = className+".csv";
 
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
        List<CustomData> cdatas = this.query(model, className, query, text, near, sort, direction, status, request);
        
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