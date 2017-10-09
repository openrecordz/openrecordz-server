//package shoppino.web.service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//import org.supercsv.cellprocessor.ift.CellProcessor;
//import org.supercsv.io.CsvBeanWriter;
//import org.supercsv.io.ICsvBeanWriter;
//import org.supercsv.prefs.CsvPreference;
//
//import shoppino.Util;
//import shoppino.csv.cellprocessor.PersonCellProcessor;
//import shoppino.domain.Person;
//import shoppino.exception.ShoppinoException;
//import shoppino.service.PersonSearchService;
//
//@Controller
//public class PersonSearchServiceController implements BaseServiceController {
//
//	protected final Log logger = LogFactory.getLog(getClass());
//	
//	
//	@Autowired
//	PersonSearchService personSearchService;
//	
//	@Value("$shoppino{default.search.service.page}")
//	private int defaultPage;
//
//	@Value("$shoppino{default.search.service.pagesize}")
//	private int defaultPageSize;
//		
//	@RequestMapping(value = "/search/people")
//    public ModelAndView search(Model model, HttpServletRequest request) throws ShoppinoException {        
//        
//        int page = defaultPage;
//    	int pageSize = defaultPageSize;
//    	String q = "";    	
//    
//    	String now = Util.dateRFC822(new Date());
//
//    	if (request.getParameter("page")!=null)
//    		page=Integer.parseInt(request.getParameter("page"));
//
//    	if (request.getParameter("pageSize")!=null)
//    		pageSize = Integer.parseInt(request.getParameter("pageSize"));
//    	
//    	if (request.getParameter("q")!=null)
//    		q = request.getParameter("q");
//    	
//    	logger.info("Search people with query : " + q);            	    	
//    	    	  
//    	
//    	
//    	List<Person> returnVal= new ArrayList<Person>();
//    	long count=0;
//    	
//    	if (q!=null && !q.equals("")) {    							  
//	    	
//    		returnVal = personSearchService.findByQueryPaginated(q, page, pageSize);
//	    	
//	    	//count people
//    		count = personSearchService.countByQuery(q);	        
//	    		    
//	        
//    	}
//
//    	model.addAttribute("people", returnVal);
//		model.addAttribute("peopleCount", count);		
//		model.addAttribute("query", q);
//	   	model.addAttribute("now", now);
//	   	
//        return new ModelAndView("people-search-service", "model", model);
//
//    }
//	
//	
//	
//	
//	@RequestMapping(value = "/search/people.csv")
//    public void searchAsCsv(Model model, HttpServletRequest request, HttpServletResponse response) throws ShoppinoException, IOException {        
//        
//        int page = defaultPage;
//    	int pageSize = defaultPageSize;
//    	String q = "";    	
//    
//
//    	if (request.getParameter("page")!=null)
//    		page=Integer.parseInt(request.getParameter("page"));
//
//    	if (request.getParameter("pageSize")!=null)
//    		pageSize = Integer.parseInt(request.getParameter("pageSize"));
//    	
//    	if (request.getParameter("q")!=null)
//    		q = request.getParameter("q");
//    	
//    	logger.info("Search people with query : " + q);            	    	
//    	    	  
//    	
//    	
//    	List<Person> returnVal= new ArrayList<Person>();
//    	
//    	if (q!=null && !q.equals("")) {    							  
//	    	
//    		returnVal = personSearchService.findByQueryPaginated(q, page, pageSize);
//	    	
//    	}
//
//    	 String csvFileName ="people.csv";
//    	 
//         response.setContentType("text/csv");
//  
//         // creates mock data
//         String headerKey = "Content-Disposition";
//         String headerValue = String.format("attachment; filename=\"%s\"",
//                 csvFileName);
//         response.setHeader(headerKey, headerValue);
//  
//         
//         
////         int i=0;
////         String[] header = new String[Person.class.getDeclaredFields().length];
////         for (Field field : Person.class.get.getDeclaredFields()) {
//// 	       {
////         	header[i]=field.getName();
////        		i++;
//// 	       }
//         
//         final String[] header = new String[] { "username", "fullName", "email", "photo",
//                 "createdBy", "createdOn"
////                 , "modifiedBy", "modifiedOn"
//                 };
//         
//         final CellProcessor[] processors = PersonCellProcessor.getProcessors();
//         
//         ICsvBeanWriter beanWriter = null;
//                 beanWriter = new CsvBeanWriter(response.getWriter(),
//                         CsvPreference.STANDARD_PREFERENCE);
//              
//         beanWriter.writeHeader(header);
//                 
//         for (Person person: returnVal) {
//        	 beanWriter.write(person,header,processors);
//         }
//         
//         
//         beanWriter.close();
//
//    }
//
//    
//    
//
//}