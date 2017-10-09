package openrecordz.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import shoppino.security.service.AuthenticationService;

@Component("authenticationInterceptor")
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private AuthenticationService authenticationService;
	
	private static String AUTHENTICATION_SERVICE_REQUEST_ATTRIBUTE = "authenticationService";
	
	static{
		BasicConfigurator.configure();
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {		
		
		//put authenticationService into request		
		request.setAttribute(AUTHENTICATION_SERVICE_REQUEST_ATTRIBUTE, authenticationService);
		
		
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
