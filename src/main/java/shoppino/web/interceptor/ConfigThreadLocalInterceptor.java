package shoppino.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import shoppino.service.impl.ConfigThreadLocal;
import shoppino.service.impl.ScriptServiceMultiEngineImpl;



@Component("configThreadLocalInterceptor")
public class ConfigThreadLocalInterceptor extends HandlerInterceptorAdapter {

	private Log logger = LogFactory.getLog(this.getClass());
	
	
	public static String CONFIG_THREADLOCAL_INDEXER_ENABLED_REQUEST_PARAM_NAME = "_indexer_enabled";
	public static String CONFIG_THREADLOCAL_SCRIPTING_ENABLED_REQUEST_PARAM_NAME = "_scripting_enabled";
//	public static String CONFIG_THREADLOCAL_EMAIL_ENABLED_REQUEST_PARAM_NAME = "_email_enabled";

	
	static{
		BasicConfigurator.configure();
	}

	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {		
        
		Boolean indexerEnabled = true;
		if (request.getParameter(CONFIG_THREADLOCAL_INDEXER_ENABLED_REQUEST_PARAM_NAME)!=null)
			indexerEnabled=Boolean.getBoolean(request.getParameter(CONFIG_THREADLOCAL_INDEXER_ENABLED_REQUEST_PARAM_NAME));
    	
        logger.debug("indexerEnabled : " + indexerEnabled);
        
//		ConfigThreadLocal.get().put(CustomDataSearchServiceImpl.CONFIG_THREADLOCAL_INDEXER_ENABLED_KEY, indexerEnabled);
		
		
		
		
		
		Boolean scriptingEnabled = true;
		if (request.getParameter(CONFIG_THREADLOCAL_SCRIPTING_ENABLED_REQUEST_PARAM_NAME)!=null)
			scriptingEnabled=Boolean.getBoolean(request.getParameter(CONFIG_THREADLOCAL_SCRIPTING_ENABLED_REQUEST_PARAM_NAME));
    	
        logger.debug("scriptingEnabled : " + scriptingEnabled);
        
		ConfigThreadLocal.get().put(ScriptServiceMultiEngineImpl.CONFIG_THREADLOCAL_SCRIPTING_ENABLED_KEY, scriptingEnabled);
		
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
		ConfigThreadLocal.init();
		super.afterCompletion(request, response, handler, ex);
	}
}
