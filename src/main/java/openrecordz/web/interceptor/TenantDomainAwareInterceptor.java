package openrecordz.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import openrecordz.service.TenantService;
import openrecordz.service.impl.TenantSetterRequest;

@Component("tenantDomainAwareInterceptor")
public class TenantDomainAwareInterceptor extends HandlerInterceptorAdapter {

	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private TenantService tenantService;
	
	private static String TENANT_SERVICE_REQUEST_ATTRIBUTE="tenantService";
	
	@Autowired
	private TenantSetterRequest tenantSetterRequest;
	
	public static String BLOCKED_TENANT_VALUE = "_BLOCKED";
	
	static{
		BasicConfigurator.configure();
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.debug("Before handling the request");
		String uri=request.getRequestURI();
		logger.debug("Uri : " + uri);
//		http://stackoverflow.com/questions/1923815/get-the-second-level-domain-of-an-url-java
		
		String currentTenant = tenantSetterRequest.setTenant(request);			
		
		if (request.getRequestURL().toString().contains("/blocked") ||  request.getRequestURL().toString().contains("/resources/")) {
			
		} else{
			if (currentTenant!=null && currentTenant.equals(BLOCKED_TENANT_VALUE)){
				ModelAndView mv = new ModelAndView("redirect:/blocked");
				ModelAndViewDefiningException mvde = new ModelAndViewDefiningException(mv);
				throw mvde; 
			}
		}
		//put tenantService into request		
		request.setAttribute(TENANT_SERVICE_REQUEST_ATTRIBUTE, tenantService);
		
		
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
