package openrecordz.service.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import openrecordz.domain.Tenant;
import openrecordz.service.TenantService;

public class TenantSetterRequest {

	private Log logger = LogFactory.getLog(this.getClass());

//	@Value("$platform{platform.url}")
//	private String platformUrl;
//	@Autowired
	MessageSource messageSource;
	
	
	//if return "_BLOCKED" webapp redirect all pages to error
	public String setTenant(HttpServletRequest request) {
		logger.debug("Server name : "+ request.getServerName());
		String tenant = TenantService.DEFAULT_TENANT;
		try {
			String reqServerName = request.getServerName();
			logger.error("reqServerName: "+ reqServerName);

		    final String origin = request.getHeader("origin");
			logger.error("origin: "+ origin);

			
			


			String thirdLevel = request.getServerName().substring(0, request.getServerName().indexOf("."));
			
			
			tenant = messageSource.getMessage("tenants.mapping."+origin, null, thirdLevel, Locale.getDefault());
//			tenant = messageSource.getMessage("tenants.mapping."+reqServerName, null, thirdLevel, Locale.getDefault());
			
//			String serNameSetting = messageSource.getMessage("tenants.mapping."+tenant, null, null, Locale.getDefault());
//			
//			if (serNameSetting!=null && serNameSetting.equals(reqServerName)) {
//				return tenant;
//			}else {
//				return thirdLevel;
//			}
			
//			if (serverName.matches(platformUrl)) {
				
//				tenant=thirdLevel;
//			}
		}catch (Exception e) {
			//logger.warn("Error setting tenant for server name : " + request.getServerName()+ ". Setted tenant to default value. Error : ",e);
			logger.warn("Error setting tenant for server name : " + request.getServerName()+ ". Setted tenant to default value. Error : ");
			tenant = TenantService.DEFAULT_TENANT;
		}
		
//		if (tenant.equals("www"))
//			tenant = TenantService.DEFAULT_TENANT;
			
		logger.debug("tenant : " + tenant) ;
		TenantThreadLocal.set(new Tenant(tenant));
		
		return tenant;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	
}
