package openrecordz.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import openrecordz.domain.Tenant;
import shoppino.service.TenantService;
import shoppino.service.impl.TenantThreadLocal;

@Deprecated
@Component("tenantInterceptor")
public class TenantInterceptor extends HandlerInterceptorAdapter {

	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private TenantService tenantService;
	
	public static String TENANT_SERVICE_REQUEST_ATTRIBUTE="tenantService";
	private static String RESOURCES_PATH ="resources";
	
	static{
		BasicConfigurator.configure();
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.debug("Before handling the request");
		String uri=request.getRequestURI();
		logger.debug("Uri : " + uri);
		
		String contextPath=request.getContextPath();
		logger.debug("contextPath: " +contextPath);
		
		String[] uriSplit = uri.split("/");
		
		int occuranceSlash = StringUtils.countOccurrencesOf(uri, "/");
		logger.debug("slash occurrence : " + occuranceSlash);
		
		if (!uri.contains(RESOURCES_PATH) && occuranceSlash>=3) {
//		if (uriSplit.length>3) {
			String tenant = uriSplit[2];
			logger.info("tenant : " + tenant) ;
			TenantThreadLocal.set(new Tenant(tenant));
			
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
