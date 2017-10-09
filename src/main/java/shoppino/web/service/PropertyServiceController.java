//package shoppino.web.service;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import shoppino.exception.ShoppinoException;
//import shoppino.service.PropertyService;
//
//
//@Controller
////@RequestMapping(value = "/cdata/")
//public class PropertyServiceController implements BaseServiceController {
//	
//	protected final Log logger = LogFactory.getLog(getClass());
//	
//	@Autowired
//	PropertyService propertyService;
//   
//    
//
//    
//    @RequestMapping(value = "/properties/add", method = RequestMethod.POST)  
//	 public @ResponseBody String create(Model model,
//			 @RequestParam("key") String key,@RequestParam("value") String value) throws ShoppinoException {
//   	
//    	propertyService.add(key, value);
//       
//    	 return "{success:true}";
//   }
//    
//    @RequestMapping(value = "/properties/{key}", method = RequestMethod.GET)  
//	 public @ResponseBody String get(Model model,  
//			 @PathVariable("key") String key, HttpServletRequest request) throws ShoppinoException {
//  	
//    	return propertyService.get(key);
//  }
//    
//    
//    @RequestMapping(value = "/properties/{key}", method = RequestMethod.DELETE)  
//	 public @ResponseBody String delete(Model model,  
//			 @PathVariable("key") String key, HttpServletRequest request) throws ShoppinoException {
// 	
//    	propertyService.remove(key);
//          
//     
//     return "{success:true}";
// }
//    
//    @RequestMapping(value = "/properties/{key}", method = RequestMethod.PUT)  
//  	 public @ResponseBody String put(Model model,  
//  			@PathVariable("key") String key,@RequestParam("value") String value, HttpServletRequest request) throws ShoppinoException {
//    	
//    	propertyService.update(key, value);
//        
//    	return "{success:true}";
//    }
//    
//
//       
//   
//}