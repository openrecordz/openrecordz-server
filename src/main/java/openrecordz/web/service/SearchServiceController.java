package openrecordz.web.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import openrecordz.domain.customdata.CustomData;
import openrecordz.exception.OpenRecordzException;
import openrecordz.service.CustomDataService;

@Controller
public class SearchServiceController implements BaseServiceController {

protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	CustomDataService customDataService;
	
	@Value("$platform{default.page}")
	private int defaultPage = 0;
	
	@Value("$platform{default.pagesize}")
	private int defaultPageSize = 20;
	
	 @RequestMapping(value = "/search", method = RequestMethod.GET)  
	 public @ResponseBody List<CustomData> query(Model model, 
//			 @PathVariable String className, 
			 @RequestParam(value = "q", required=false, defaultValue="{}") String query,
			 
			 @RequestParam(value = "text", required=false) String text,
			 
			 @RequestParam(value = "fq", required=false) String fullQuery,
			 @RequestParam(value = "sort", required=false) String sortFields, 
			 @RequestParam(value = "direction", required=false) String direction, 
			 @RequestParam(value = "status", required=false) Integer status,
			 HttpServletRequest request) throws OpenRecordzException {
	
    	String className=null; //with null search datasets and records
    	
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
   	logger.debug("fullQuery : " + fullQuery);
   	logger.debug("text : " + text);
   	
   	if (text!=null && !text.equals("")) {    		
		query="{ $text: { $search: \""+text+"\" } } ";	
	}
   	
//   	if (fullQuery!=null)
//   		return customDataSearchService.findByQueryLocationPaginated(fullQuery, page, pageSize, status);
//   	else
   		return customDataService.findByQueryInternal(query, className, page, pageSize, direction, sortFields, status);
    
   	
}
    
}
