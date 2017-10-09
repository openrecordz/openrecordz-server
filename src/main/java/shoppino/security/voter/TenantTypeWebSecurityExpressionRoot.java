package shoppino.security.voter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

import openrecordz.domain.Tenant;
import shoppino.service.impl.TenantSetterRequest;
import shoppino.service.impl.TenantSettingsSetter;

public class TenantTypeWebSecurityExpressionRoot extends WebSecurityExpressionRoot implements SecurityExpressionOperations {
	
	protected Log log = LogFactory.getLog(getClass());

	private TenantSetterRequest tenantSetterRequest;
	
//	private TenantService tenantService;
	
	private TenantSettingsSetter tenantSettingsSetter;
	
	private boolean enableMTSecurity;
	
	public TenantTypeWebSecurityExpressionRoot(Authentication a, FilterInvocation fi) {
		super(a, fi);
//		AbstractAuthorizeTag ee = new AbstractAuthorizeTag();
//		JspAuthorizeTag 
	}

	/**
     *
     */
    public boolean hasMTRole(String role) {
    	
    	log.debug("TenantTypeWebSecurityExpressionRoot - hasMTRole role: " + role);
    	
    	String tenant = tenantSetterRequest.setTenant(request);			
    	log.debug("tenant: " + tenant);
    		
//    		String tenantType = tenantService.getCurrentTenantType();
		String tenantType = tenantSettingsSetter.getTenantType(tenant);
		log.debug("tenantType: " + tenantType);
		log.debug("enableMTSecurity: " + enableMTSecurity);
		
		if (enableMTSecurity==false) {
			log.info("tenant : " + tenant +" enableMTSecurity=false, using hasRole(role): " + role);
			return hasRole(role);
		} else {
			if (tenantType.equals(Tenant.PUBLIC_TENANT_TYPE)) {
				
				if (role.contains("ROLE_ADMIN")) {
					log.info("tenant : " + tenant + " public tenant, using hasRole(role@tenant): " + role+"@"+tenant);
					return hasRole(role+"@"+tenant);
				}else {
					log.info("tenant : " + tenant +" public tenant, using hasRole(role): " + role);
					return hasRole(role);
				}
				
			}else  if (tenantType.equals(Tenant.MODERATE_TENANT_TYPE)) {
				log.info("tenant : " + tenant + " moderate tenant, using hasRole(role@tenant): " + role+"@"+tenant);
				return hasRole(role+"@"+tenant);
			}else {
				throw new RuntimeException("Other tenant type not allowed");
			}
			
		}
            
//        }
    }

	public TenantSetterRequest getTenantSetterRequest() {
		return tenantSetterRequest;
	}

	public void setTenantSetterRequest(TenantSetterRequest tenantSetterRequest) {
		this.tenantSetterRequest = tenantSetterRequest;
	}

//	public TenantService getTenantService() {
//		return tenantService;
//	}
//
//	public void setTenantService(TenantService tenantService) {
//		this.tenantService = tenantService;
//	}
	
	public void setTenantSettingsSetter(TenantSettingsSetter tenantSettingsSetter) {
		this.tenantSettingsSetter = tenantSettingsSetter;
		
	}

	public boolean isEnableMTSecurity() {
		return enableMTSecurity;
	}

	public void setEnableMTSecurity(boolean enableMTSecurity) {
		this.enableMTSecurity = enableMTSecurity;
	}
    
    
}
