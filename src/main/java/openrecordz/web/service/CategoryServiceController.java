package openrecordz.web.service;
//package shoppino.web.service;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import shoppino.Util;
//import shoppino.comparator.CategoryLabelComparator;
//import shoppino.domain.Category;
//import shoppino.exception.ResourceNotFoundException;
//import shoppino.exception.ShoppinoException;
//import shoppino.service.CategoryService;
//import shoppino.service.TenantService;
//import shoppino.web.interceptor.CategoryInterceptor;
//
//
//@Controller
////@RequestMapping("/{tenant}/service/v1")
//public class CategoryServiceController implements BaseServiceController {
//	
//	protected final Log logger = LogFactory.getLog(getClass());
//	
//	@Autowired
//    private CategoryService categoryService;
//    @Autowired
//	private TenantService tenantService;
//    
//	
//	@RequestMapping(value = "/categories")
//    public ModelAndView list(Model model, HttpServletRequest request) throws ResourceNotFoundException {
//    	
//    	logger.debug("Searching categories...");
//    	 
//    	Comparator<Category> comparator = new CategoryLabelComparator();
//    	String locale = Locale.getDefault().toString();
//    	String path = null; 
//    	String parentId = null;
//    			
//    	if (request.getParameter("locale")!=null)
//    		locale=request.getParameter("locale");
//    	
//        logger.debug("locale : " + locale);
//        
//        
//        if (request.getParameter("path")!=null)
//        	path=request.getParameter("path");
//        
//        logger.debug("path : " + path);
//        
//        
//        if (request.getParameter("parentid")!=null)
//        	parentId=request.getParameter("parentid");
//        
//        logger.debug("parentid : " + parentId);
//       
//        logger.info("Searching categories for path: " + path + " and parentid: " + parentId + " and locale: " + locale);
//        
//        List<Category> categories = null; 
//        
//        if (path!=null) {
//        	Category category = categoryService.getById(path, locale);
//        	categories = new ArrayList<Category>();
//        	categories.add(category);
//        
//        } else if (parentId!=null) {
//        	categories = categoryService.getChildren(parentId, locale);
//        } else {
////        	categories = categoryService.findAll(locale);
//        	//TODO use ehcache indeed memory hashmap
//        	categories = CategoryInterceptor.allCategories.get(tenantService.getCurrentTenantName());
//        	
//        }
//        
//        
//          
//        if (request.getParameter("sortby")!=null && request.getParameter("sortby").equalsIgnoreCase("label")) {
//        	logger.debug("sortby : label");
//        	Collections.sort(categories, comparator);
//        }
//        	
//
//        
//        logger.debug("Categories size: " + categories.size());
//        model.addAttribute("categories", categories);
//        
//        String now = Util.dateRFC822(new Date());
//        model.addAttribute("now", now);
//        
//        return new ModelAndView("categories-service", "model", model);
//    }
//
//    
//    
//   
//
//
//    
//    @RequestMapping(value = "/categories/add", method = RequestMethod.POST)
//	 public @ResponseBody String add(
//			 	@RequestParam("path") String path,
//			 	@RequestParam(value="otype", required=false, defaultValue="photo") String otype,
//			 	@RequestParam(value="order", required=false, defaultValue="0") Integer order,
//			 	@RequestParam(value="label", required=false) String label,
//			 	@RequestParam(value="labels", required=false)  Map<String, String> labels,
//			 	@RequestParam(value="allowUserContentCreation", required=false, defaultValue="false") Boolean allowUserContentCreation,
//			 	@RequestParam(value="visibility", required=false, defaultValue="30") Integer visibility,
//			 	
//			 	HttpServletRequest request
//			 	 
//	       ) throws ShoppinoException {
//
//		 logger.debug("path : " + path);
//		 
////		 Map<String, String> labels = null;
//		 
//		 if (label!=null)  {
//			 labels = new HashMap<String, String>();
//			 labels.put("en_US", label);
//		 }
//		 
//		 String catId = categoryService.add(path,otype, order, labels,visibility);
////		 String catId = categoryService.add(path,otype, order, labels,allowUserContentCreation);
//		 
//		 
//		 if (request.getParameter("refresh")!=null)
//	    		categoryService.refreshCache();
//		 
////		 logger.info("Category : " + path + " is created");
//		 
//		 return "{\"success\":true, \"id\": \""+catId +"\"}";
//    }
//    
//    
//    @RequestMapping(value = "/categories/delete", method = RequestMethod.DELETE)
//	 public @ResponseBody String delete(
//			 	@RequestParam("path") String path,
//			 	HttpServletRequest request
//	       ) throws ShoppinoException {
//
//		 logger.debug("path : " + path);
//		 
//		 categoryService.delete(path);
//		 
////		 logger.info("category : " + path + " is deleted");
//		 
//		 if (request.getParameter("refresh")!=null)
//	    		categoryService.refreshCache();
//		 
//		 return "{success:true}";
//   }
//    
//    @RequestMapping(value = "/categories/deleteall", method = RequestMethod.DELETE)
//	 public @ResponseBody String deleteAll(HttpServletRequest request			 	
//	       ) throws ShoppinoException {
//
//		 logger.debug("calling delete all");
//		 
//		 categoryService.deleteAll();
//		 
//		 if (request.getParameter("refresh")!=null)
//	    		categoryService.refreshCache();
//		 
////		 logger.info("all categories by tenant " + tenantService.getCurrentTenantName() + " are deleted");
//		 
//		 return "{success:true}";
//  }
//		 
//	public void setCategoryService(CategoryService categoryService) {
//		this.categoryService = categoryService;
//	}
//    
//    
//	 
//    
//	
//}