package shoppino.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import shoppino.service.impl.RequestThreadLocal;



@Component("requestThreadLocalInterceptor")
public class RequestThreadLocalInterceptor extends HandlerInterceptorAdapter {

	private Log logger = LogFactory.getLog(this.getClass());
	
	
	public static String CONFIG_THREADLOCAL_INDEXER_ENABLED_REQUEST_PARAM_NAME = "_indexer_enabled";
	public static String CONFIG_THREADLOCAL_SCRIPTING_ENABLED_REQUEST_PARAM_NAME = "_scripting_enabled";

	
	static{
		BasicConfigurator.configure();
	}

	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {		
        
          
        if (request!=null && request.getHeader("user-agent")!=null) {
//        	http://uadetector.sourceforge.net/usage.html#improve_performance
//        	https://github.com/haraldwalker/user-agent-utils
        	RequestThreadLocal.get().put(RequestThreadLocal.REQUEST_INFO_TREADEDLOCAL_USER_AGENT, request.getHeader("user-agent"));
        }
		
								
		
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
		RequestThreadLocal.init();
		super.afterCompletion(request, response, handler, ex);
	}
}
