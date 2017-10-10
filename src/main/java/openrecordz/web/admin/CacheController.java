package openrecordz.web.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import openrecordz.exception.OpenRecordzException;
import openrecordz.service.PersonService;

@Controller
public class CacheController implements BaseAdminController {

	protected final Log logger = LogFactory.getLog(getClass());
	

//	@Autowired
//	ProductService productService;
	
	@Autowired
	PersonService personService;
//    
//    @RequestMapping(value = {"/cache/products/evict"})
//    public @ResponseBody String evictProductCache(Model model, HttpServletRequest request) throws ShoppinoException {    	
//    	
//    	productService.clearCache();
//    	
//    	return "{success:true}";
//    }    
    
//	aggiungi cusotm data
	
    @RequestMapping(value = {"/cache/people/evict"})
    public @ResponseBody String evictPersonCache(Model model, HttpServletRequest request) throws OpenRecordzException {    	
    	
    	personService.clearCache();
    	
    	return "{success:true}";
    }    
    
    @RequestMapping(value = {"/cache/evict"})
    public @ResponseBody String evictCache(Model model, HttpServletRequest request) throws OpenRecordzException {    	
    	
//    	productService.clearCache();
    	personService.clearCache();
    	
    	return "{success:true}";
    }    
    
 
}