package openrecordz.web.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import openrecordz.domain.Category;
import shoppino.service.CategoryService;
import shoppino.service.TenantService;



@Component("categoryInterceptor")
public class CategoryInterceptor extends HandlerInterceptorAdapter {

	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	TenantService tenantService;
	
	public static Map<String, List<Category>> allCategories;
	
	private static String ALL_CATEGORIES_REQUEST_ATTRIBUTE = "allcategories";
	private static String REFRESH_ALL_CATEGORIES_REQUEST_ATTRIBUTE = "refreshallcategories";

	
	static{
		BasicConfigurator.configure();
	}

	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {		
		
//		Locale locale = RequestContextUtils.getLocale(request);
//        locale.toString();
        
		String locale = Locale.getDefault().toString();
		
		if (request.getParameter("locale")!=null)
    		locale=request.getParameter("locale");
    	
        logger.debug("locale : " + locale);
        
		if (allCategories==null || request.getParameter(REFRESH_ALL_CATEGORIES_REQUEST_ATTRIBUTE)!=null) {
//			allCategories = new HashMap<String, List<Category>>();
			categoryService.clearCache();
			logger.debug("allCategories hasmap created.");
		}
		
		if (!allCategories.containsKey(tenantService.getCurrentTenantName())) {
			logger.debug("Loading all the categories and putting them into memory hashmap for tenant " + tenantService.getCurrentTenantName());
//			allCategories.put(tenantService.getCurrentTenantName(), categoryService.findAll(0,200,locale));
			categoryService.refreshCache();
//			allCategories.put(tenantService.getCurrentTenantName(), categoryService.findAll(locale));
			logger.info("Loaded all the categories and putted them into memory hashmap for tenant : " + tenantService.getCurrentTenantName());
		} else {
			logger.debug("Categories already present into memory hashmap for tenant : "+ tenantService.getCurrentTenantName());
		}
		
		
		//put categories into request		

		request.setAttribute(ALL_CATEGORIES_REQUEST_ATTRIBUTE, allCategories.get(tenantService.getCurrentTenantName()));
		
		
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
//		logger.debug("After handling the request");
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
//		logger.debug("After rendering the view");
		super.afterCompletion(request, response, handler, ex);
	}
}
