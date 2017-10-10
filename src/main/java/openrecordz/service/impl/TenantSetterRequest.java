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

//	@Value("$shoppino{openrecordz.url}")
//	private String shoppinoUrl;
//	@Autowired
	MessageSource messageSource;
	
	
	//if return "_BLOCKED" webapp redirect all pages to error
	public String setTenant(HttpServletRequest request) {
		logger.debug("Server name : "+ request.getServerName());
		String tenant = TenantService.DEFAULT_TENANT;
		try {
			String serverName = request.getServerName();
			String thirdLevel = request.getServerName().substring(0, request.getServerName().indexOf("."));
			tenant = messageSource.getMessage("tenants.mapping."+serverName, null, thirdLevel, Locale.getDefault());
//			if (serverName.matches(shoppinoUrl)) {
				
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
