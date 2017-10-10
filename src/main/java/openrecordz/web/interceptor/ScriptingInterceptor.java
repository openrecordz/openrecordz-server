package openrecordz.web.interceptor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import openrecordz.service.ScriptService;
import openrecordz.service.TenantService;

@Component("scriptingInterceptor")
public class ScriptingInterceptor extends HandlerInterceptorAdapter {

	private Log logger = LogFactory.getLog(this.getClass());
	
	@Value(value="$platform{scripting.interceptor.request.uri.filter}")
	public String requestUriFilter;
	
	@Autowired
	ScriptService scriptService;
	
	@Autowired
	@Qualifier(value="tenantSettingMappingCustomDataMessageSource")
	MessageSource messageSource;
	public static String TENANT_SETTINGS_SCRIPTING_INTERCEPTOR_REGEX_KEY = "tenants.settings.%s.scripting.interceptor.regex";
	
	@Autowired
	TenantService tenantService;
	
	private static String BEFORE_SCRIPTING_INTERCEPTOR_FUNCTION_NAME_PREFIX = "preHandle";
	private static String AFTER_SCRIPTING_INTERCEPTOR_FUNCTION_NAME_PREFIX = "postHandle";
	private static String AFTER_COMPLETION_SCRIPTING_INTERCEPTOR_FUNCTION_NAME_PREFIX = "afterCompletion";
	
	private static String SCRIPTING_INTERCEPTOR_FUNCTION_NAME_POSTFIX = "Exchange";
	
	
	static{
		BasicConfigurator.configure();
	}
	
	
//
//	ATTENZIONE <mvc:mapping path="/service/**"/>     **************************************************
//	QUINDI VALE SOLO PER I SERVIZI **********************************************************
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.debug("ScriptingInterceptor preHandle");
		logger.debug("request.getRequestURI() : "+ request.getRequestURI());
		
		String scriptingInterceptorRegEx=messageSource.getMessage(String.format(TENANT_SETTINGS_SCRIPTING_INTERCEPTOR_REGEX_KEY,tenantService.getCurrentTenantName()), null, null,Locale.getDefault());
		logger.debug("scriptingInterceptorRegEx : "+ scriptingInterceptorRegEx);
		
		if (request.getRequestURL().toString().matches(requestUriFilter) || (scriptingInterceptorRegEx!=null && request.getRequestURL().toString().matches(scriptingInterceptorRegEx))){
			//String functionName =  BEFORE_SCRIPTING_INTERCEPTOR_FUNCTION_NAME_PREFIX+request.getRequestURI().replace("/", "$")+SCRIPTING_INTERCEPTOR_FUNCTION_NAME_POSTFIX;
			String functionName =  BEFORE_SCRIPTING_INTERCEPTOR_FUNCTION_NAME_PREFIX+SCRIPTING_INTERCEPTOR_FUNCTION_NAME_POSTFIX;
			logger.debug("functionName : "+ functionName);
			
			Map params = new HashMap<>();
			params.put("req", request);
			params.put("res", response);
			
			try {
				scriptService.call(functionName, params);
			}catch (FileNotFoundException fnfe) {
				logger.warn("Script file not found", fnfe);
			}catch (NoSuchMethodException nsme) {
				logger.warn("Method not found for script file", nsme);		
			} catch (IOException ioe) {
				logger.warn(ioe);		
			}
			
			
			
			logger.info(functionName + " executed");
		}
		
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	

		logger.debug("ScriptingInterceptor postHandle");
		logger.debug("request.getRequestURL() : "+ request.getRequestURL());
		
		
		String scriptingInterceptorRegEx=messageSource.getMessage(String.format(TENANT_SETTINGS_SCRIPTING_INTERCEPTOR_REGEX_KEY,tenantService.getCurrentTenantName()), null, null,Locale.getDefault());
		logger.debug("scriptingInterceptorRegEx : "+ scriptingInterceptorRegEx);
		
		if (request.getRequestURL().toString().matches(requestUriFilter) || (scriptingInterceptorRegEx!=null && request.getRequestURL().toString().matches(scriptingInterceptorRegEx))){
			String functionName =  AFTER_SCRIPTING_INTERCEPTOR_FUNCTION_NAME_PREFIX+SCRIPTING_INTERCEPTOR_FUNCTION_NAME_POSTFIX;
//			String functionName =  AFTER_SCRIPTING_INTERCEPTOR_FUNCTION_NAME_PREFIX+request.getRequestURI().replace("/", "$")+SCRIPTING_INTERCEPTOR_FUNCTION_NAME_POSTFIX;
			logger.debug("functionName : "+ functionName);
			
			Map params = new HashMap<>();
			params.put("req", request);
			params.put("res", response);
			
			try {
				scriptService.call(functionName, params);
			}catch (FileNotFoundException fnfe) {
				logger.warn("Script file not found", fnfe);
			}catch (NoSuchMethodException nsme) {
				logger.warn("Method not found for script file", nsme);		
			} catch (IOException ioe) {
				logger.warn(ioe);		
			}
			
			logger.info(functionName + " executed");
		}
		
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		
		logger.debug("ScriptingInterceptor afterCompletion");
		logger.debug("request.getRequestURL() : "+ request.getRequestURL());
		
		
		String scriptingInterceptorRegEx=messageSource.getMessage(String.format(TENANT_SETTINGS_SCRIPTING_INTERCEPTOR_REGEX_KEY,tenantService.getCurrentTenantName()), null, null,Locale.getDefault());
		logger.debug("scriptingInterceptorRegEx : "+ scriptingInterceptorRegEx);
		
		if (request.getRequestURL().toString().matches(requestUriFilter) || (scriptingInterceptorRegEx!=null && request.getRequestURL().toString().matches(scriptingInterceptorRegEx))){
			String functionName =  AFTER_COMPLETION_SCRIPTING_INTERCEPTOR_FUNCTION_NAME_PREFIX+SCRIPTING_INTERCEPTOR_FUNCTION_NAME_POSTFIX;
//			String functionName =  AFTER_SCRIPTING_INTERCEPTOR_FUNCTION_NAME_PREFIX+request.getRequestURI().replace("/", "$")+SCRIPTING_INTERCEPTOR_FUNCTION_NAME_POSTFIX;
			logger.debug("functionName : "+ functionName);
			
			Map params = new HashMap<>();
			params.put("req", request);
			params.put("res", response);
			
			try {
				scriptService.call(functionName, params);
			}catch (FileNotFoundException fnfe) {
				logger.warn("Script file not found", fnfe);
			}catch (NoSuchMethodException nsme) {
				logger.warn("Method not found for script file", nsme);		
			} catch (IOException ioe) {
				logger.warn(ioe);		
			}
			
			logger.info(functionName + " executed");
		}
		
		super.afterCompletion(request, response, handler, ex);
	}
}
