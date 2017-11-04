package openrecordz.web.service;

import java.io.IOException;
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

import com.mongodb.util.JSON;

import openrecordz.domain.customdata.CustomData;
import openrecordz.exception.ResourceNotFoundException;
import openrecordz.exception.OpenRecordzException;
import openrecordz.service.CustomDataService;
import openrecordz.service.RecordDataService;



//http://matera.api.openrecordz.com/service/v1/datasets POST (creo ds)
//http://matera.api.openrecordz.com/service/v1/datasets/dsId PUT (aggiorno ds)
//http://matera.api.openrecordz.com/service/v1/datasets/dsId POST (aggiungo dati nel ds)
//http://matera.api.openrecordz.com/service/v1/datasets/dsId/recordId Put (aggiungo dati nel ds)


@Controller
//@RequestMapping(value = "/cdata/")
public class DatasetServiceController implements BaseServiceController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	CustomDataService customDataService;
   
//	@Autowired
//	CustomDataSearchService customDataSearchService;
	
	@Value("$platform{default.page}")
	private int defaultPage = 0;
	
	@Value("$platform{default.pagesize}")
	private int defaultPageSize = 20;
    
	@Autowired
	RecordDataService recordDataService;
	
	//@Autowired
	//MessageSource cdmessageSource;
//    https://www.parse.com/docs/rest#objects-types
    
	public void validate (String datasetJson) throws OpenRecordzException {
		Map json=(Map) JSON.parse(datasetJson);
    	if (json!=null && json.containsKey("_slug")) {
    		String dsSlug = (String) json.get("_slug");
    		List<CustomData> resuls=customDataService.findByQuery("{\"_slug\":\"" +dsSlug+"\"}", "dataset");
    		if (resuls!=null && resuls.size()>=1)
    			throw new OpenRecordzException("Dataset slug already exists");    		    			
    	}else {
    		throw new OpenRecordzException("Dataset doesn't contains slug field");
    	}
	}
	
//	@PreAuthorize("hasMTRole('ROLE_RESTUSER')")ATTENZIONE ANCHE SE MESSA IN POST BLOCCA LA CHIAMATA ANCHE PER LA GET ....STRANO!!!
    @RequestMapping(value = "/datasets", method = RequestMethod.POST)  
	 public @ResponseBody CustomData create(Model model, 
//			 @PathVariable String datasetname, 
//			 @RequestParam("json") String json
			 @RequestBody String jsonStr
			 ) throws OpenRecordzException {
   	
    	validate(jsonStr);
    	
    	String cdataId = customDataService.add("dataset", jsonStr);
    	CustomData cdata = customDataService.getById(cdataId);
            
       
       return cdata;
   }
    
    @RequestMapping(value = "/datasets/{id}/meta", method = RequestMethod.GET)  
	 public @ResponseBody CustomData get(Model model, 
//			 @PathVariable String className, 
			 @PathVariable("id") String id,
			 @RequestParam(value = "byslug", required=false, defaultValue="false") Boolean searchbySlug,
			 @RequestParam(value = "countr", required=false, defaultValue="false") Boolean countRecords,
			 @RequestParam(value = "countb", required=false, defaultValue="false") Boolean countBinary,

			 HttpServletRequest request) throws OpenRecordzException {
 	
    	CustomData ds =null;
     	if (searchbySlug!=null) {
     		List<CustomData> dsArrayAsSlug = customDataService.findByQueryInternal("{\"_slug\":\"" +id+"\"}","dataset");
    		if (dsArrayAsSlug!=null && dsArrayAsSlug.size()>0){
    			ds=dsArrayAsSlug.get(0);
    			id=ds.getId();
    		} else
    			throw new ResourceNotFoundException("Dataset not found with slug : " + id);
	    	}else {
	    		ds = customDataService.getByIdInternal(id);
	    	}
     	
     	if (countRecords==true) 
     		ds.put("_countRecords", recordDataService.countByQueryInternal("{}", id, "record", null));
     	
     	if (countBinary==true) 
     		ds.put("_countBinaries", recordDataService.countByQueryInternal("{}", id, "binary", null));
     	
   return ds;
 }
    
   
    
    
    @RequestMapping(value = "/datasets/{id}", method = RequestMethod.PUT)  
 	 public @ResponseBody CustomData put(Model model, 
// 			@PathVariable String className, 
 			@PathVariable("id") String id,  
// 			@RequestParam("json") String json, 
 			@RequestBody String jsonStr,
 			HttpServletRequest request) throws OpenRecordzException {
   	
    	
    	CustomData cdataOld = customDataService.getById(id); 
    	Map json=(Map) JSON.parse(jsonStr);
    	
    	if (cdataOld.get("_slug")!=null && json!=null && json.containsKey("_slug") && json.get("_slug")!=null && cdataOld.get("_slug").equals(json.get("_slug"))){
    		
    	} else     	
    		validate(jsonStr);

	   	String cdataId = customDataService.update(id, "dataset", jsonStr);
	   	CustomData cdata = customDataService.getById(cdataId);    
       
       return cdata;
   }
    
    @RequestMapping(value = "/datasets/{id}", method = RequestMethod.PATCH)  
	 public @ResponseBody CustomData patch(Model model, 
//			 @PathVariable String className, 
			@PathVariable("id") String id,  
//			@RequestParam("json") String json, 
			@RequestBody String jsonStr,
			HttpServletRequest request) throws OpenRecordzException {
  	
    	
    	CustomData cdataOld = customDataService.getById(id); 
    	Map json=(Map) JSON.parse(jsonStr);
    	
    	if (cdataOld.get("_slug")!=null && json!=null && json.containsKey("_slug") && json.get("_slug")!=null && cdataOld.get("_slug").equals(json.get("_slug"))){
    		
    	} else     	
    		validate(jsonStr);
    	
  	String cdataId = customDataService.patch(id, "dataset", jsonStr);
  	CustomData cdata = customDataService.getById(cdataId);    
      
      return cdata;
  }
    
    
    @RequestMapping(value = "/datasets/{id}", method = RequestMethod.DELETE)  
	 public @ResponseBody String delete(Model model,
//			 @PathVariable String className, 
			 @PathVariable("id") String id, HttpServletRequest request) throws OpenRecordzException {
	
   	customDataService.remove(id);
         
    
    return "{success:true}";
}
   
    
    @RequestMapping(value = "/datasets", method = RequestMethod.GET)  
	 public @ResponseBody List<CustomData> query(Model model, 
//			 @PathVariable String className, 
			 @RequestParam(value = "q", required=false, defaultValue="{}") String query,
			 
			 @RequestParam(value = "text", required=false) String text,
			 
			 @RequestParam(value = "sort", required=false) String sortFields, 
			 @RequestParam(value = "direction", required=false) String direction, 
			 @RequestParam(value = "status", required=false) Integer status,
			 HttpServletRequest request) throws OpenRecordzException {
	
    	String className="dataset";
    	
   	int page = defaultPage;
   	int pageSize = defaultPageSize;
//   	String test = cdmessageSource.getMessage("test", null, Locale.getDefault());
   	if (request.getParameter("page")!=null)
   		page=Integer.parseInt(request.getParameter("page"));
   	
   	if (request.getParameter("pagesize")!=null)
   		pageSize = Integer.parseInt(request.getParameter("pagesize"));
   	
   	
   	logger.debug("page : " + page);
   	logger.debug("pageSize : " + pageSize);
   	logger.debug("className : " + className);    	
   	logger.debug("query : " + query);
   	logger.debug("text : " + text);
   	
   	if (text!=null && !text.equals("")) {    		
		query="{ $text: { $search: \""+text+"\" } } ";	
	}
   	

   		return customDataService.findByQueryInternal(query, className, page, pageSize, direction, sortFields, status);
    
   	
}
    
    
    
    @RequestMapping(value = "/datasets.csv", method = RequestMethod.GET)  
	 public void queryCSV(Model model,
//			 @PathVariable String className, 
			 @RequestParam(value = "q", required=false, defaultValue="{}") String query,
			 
			 @RequestParam(value = "text", required=false) String text,
			 
			 @RequestParam(value = "sort", required=false) String sort, 
			 @RequestParam(value = "direction", required=false) String direction, 
			 @RequestParam(value = "status", required=false) Integer status,
			 HttpServletRequest request, HttpServletResponse response) throws OpenRecordzException, IOException {

       String csvFileName = "datasets"+".csv";

       response.setContentType("text/csv");

       // creates mock data
       String headerKey = "Content-Disposition";
       String headerValue = String.format("attachment; filename=\"%s\"",
               csvFileName);
       response.setHeader(headerKey, headerValue);

//       Book book1 = new Book("Effective Java", "Java Best Practices",
//               "Joshua Bloch", "Addision-Wesley", "0321356683", "05/08/2008",
//               38);
//
//       Book book2 = new Book("Head First Java", "Java for Beginners",
//               "Kathy Sierra & Bert Bates", "O'Reilly Media", "0321356683",
//               "02/09/2005", 30);
//
//       Book book3 = new Book("Thinking in Java", "Java Core In-depth",
//               "Bruce Eckel", "Prentice Hall", "0131872486", "02/26/2006", 45);
//
//       Book book4 = new Book("Java Generics and Collections",
//               "Comprehensive guide to generics and collections",
//               "Naftalin & Philip Wadler", "O'Reilly Media", "0596527756",
//               "10/24/2006", 27);
//
//       List<Book> listBooks = Arrays.asList(book1, book2, book3, book4);

       List<CustomData> cdatas = this.query(model, query,text, sort,direction, status, request);
       
//       CsvDozerBeanWriter csvWriter = new  CsvDozerBeanWriter(response.getWriter(),
//             CsvPreference.STANDARD_PREFERENCE);
//       		
//       for (CustomData cdata: cdatas) {
//       	csvWriter.write(cdata);
//     }
//       csvWriter.close();
       
//       // uses the Super CSV API to generate CSV data from the model data
       CsvMapWriter csvWriter = new  	CsvMapWriter(response.getWriter(),
               CsvPreference.STANDARD_PREFERENCE);
//       
//       ICsvListWriter listWriter = new CsvListWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);

       int i=0;
       String[] header = new String[cdatas.get(0).keySet().size()];
       Iterator iterator =cdatas.get(0).toMap().keySet().iterator();
       while (iterator.hasNext())
	       {
       	header[i]=iterator.next().toString();
      		i++;
	       }
       
       csvWriter.writeHeader(header);
       
       for (CustomData cdata: cdatas) {
       	 csvWriter.write(cdata.toMap(),header);
//       	Iterator iteratorCData =cdata.keySet().iterator();
//       	 while (iteratorCData.hasNext())
// 	       {
//       		 String key = iteratorCData.next().toString();
//	            Object value = cdata.get(key);
//	            
//	            if (value instanceof ObjectId)
//	            	value = ((ObjectId)value).toString();
//	            
//       		 csvWriter.write(value,header);
// 	       }
       }
       		
//       String[] header = { "Title", "Description", "Author", "Publisher",
//               "isbn", "PublishedDate", "Price" };
//
//       csvWriter.writeHeader(header);

//       for (CustomData cdata: cdatas) {
//           csvWriter.write(cdata);
////           csvWriter.write(cdata, header);
//       }
//       
//       listWriter.write(cdatas,header);
////
//       listWriter.close();
       csvWriter.close();
   }
   
    
}