package openrecordz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import openrecordz.service.EnvironmentService;
import openrecordz.service.TenantService;



public class EnvironmentServiceImpl  implements EnvironmentService {

	public static String URL_HTTP_PROTOCOL_PREFIX = "http://";
	public static String URL_HTTPS_PROTOCOL_PREFIX = "https://";
	
	@Autowired
	TenantService tenantService;
	
	@Value("$shoppino{openrecordz.hostname}")
	String platformHostname;
	
	@Value("$shoppino{smart21.web.port}")
	String port;
	
	@Value("$shoppino{smart21.web.context}")
	String webContext;
	
	@Autowired 
	Environment env;
	
	public String getTenantUrl() {
		String url = URL_HTTP_PROTOCOL_PREFIX + tenantService.getCurrentTenantName() + platformHostname;
		
		if (port!=null && !port.equals("80"))
			url =url+":"+port;
		
		if (webContext!=null)
			url = url+webContext;
		
		return url;
	}
	
	public String getEnvironmentName() {
//		logger.debug("Environment var : " + env.getProperty("my.env"));
		return env.getProperty("my.env");
	}
	
	public String getShortEnvironmentName() {
		return this.getEnvironmentName().substring(0, 3);
	}
	
	public boolean isProduction() {
		if (this.getShortEnvironmentName().equalsIgnoreCase("pro")) {
			return true;
		}
		else {
			return false;
		}
	}
//	doesn't work
	public String getSystemProperty(String propertyName){
		return env.getProperty(propertyName);
	}
}
