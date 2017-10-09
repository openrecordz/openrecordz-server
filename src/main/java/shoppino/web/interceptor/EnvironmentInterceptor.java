package shoppino.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component("envInterceptor")
public class EnvironmentInterceptor extends HandlerInterceptorAdapter {

	@Autowired 
	Environment env;
	private static String ENVIRONMENT_VAR_REQUEST_ATTRIBUTE = "env";

	private Log logger = LogFactory.getLog(this.getClass());	
	
	static{
		BasicConfigurator.configure();
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.debug("Environment var : " + env.getProperty("my.env"));
		request.setAttribute(ENVIRONMENT_VAR_REQUEST_ATTRIBUTE, env.getProperty("my.env"));
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
