package shoppino.scripting;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import openrecordz.domain.Tenant;
import openrecordz.exception.ShoppinoRuntimeException;
import shoppino.security.exception.UserNotExistsException;
import shoppino.security.service.AuthenticationService;
import shoppino.security.service.AuthorizationService;
import shoppino.service.TenantService;
import shoppino.service.impl.TenantSettingsSetter;

public class JSAuthorizationService {

	@Autowired
	AuthorizationService authorizationService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	TenantService tenantService;
	
	@Autowired
	TenantSettingsSetter tenantSettingsSetter;
	
	protected Log log = LogFactory.getLog(getClass());
	
	public boolean allowToRole(String roleName) throws JSAuthorizationException {
		try {
			Collection<? extends GrantedAuthority> authorities = authorizationService.getAuthorities(authenticationService.getCurrentLoggedUsername());
			for (GrantedAuthority grantedAuthority : authorities) {
				if (grantedAuthority.getAuthority().equals(roleName)){
					return true;
				}
			}
			
			throw new JSAuthorizationException("Current user can't call this function");
			
		} catch (UserNotExistsException e) {
			throw new ShoppinoRuntimeException(e);
		}
	}
	
	
	public boolean allowToMTRole(String roleName) throws JSAuthorizationException {
    	
    	String tenant = tenantService.getCurrentTenantName();			
    		
//    		String tenantType = tenantService.getCurrentTenantType();
		String tenantType = tenantSettingsSetter.getTenantType(tenant);
		log.debug("tenantType: " + tenantType);
		
		
			if (tenantType.equals(Tenant.PUBLIC_TENANT_TYPE)) {
				
				if (roleName.contains("ROLE_ADMIN")) {
					log.info("tenant : " + tenant + " public tenant, using hasRole(role@tenant): " + roleName+"@"+tenant);
					return allowToRole(roleName+"@"+tenant);
				}else {
					log.info("tenant : " + tenant +" public tenant, using hasRole(role): " + roleName);
					return allowToRole(roleName);
				}
				
			}else  if (tenantType.equals(Tenant.MODERATE_TENANT_TYPE)) {
				log.info("tenant : " + tenant + " moderate tenant, using hasRole(role@tenant): " + roleName+"@"+tenant);
				return allowToRole(roleName+"@"+tenant);
			}else {
				throw new RuntimeException("Other tenant type not allowed");
			}
			
	}
	
	public boolean allowToUsername(String username) throws JSAuthorizationException {			
			if (authenticationService.getCurrentLoggedUsername().equals(username)){
				return true;
			}else {
				throw new JSAuthorizationException("Current user can't call this function");
			}		
	}
}
