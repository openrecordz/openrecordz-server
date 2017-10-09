package shoppino.security.voter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import shoppino.domain.Tenant;
import shoppino.service.TenantService;
import shoppino.service.impl.TenantSettingsSetter;

public class TenantTypeSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
	
	protected Log log = LogFactory.getLog(getClass());
	
//	@Autowired
	private TenantService tenantService;
	
//	@Autowired
	private TenantSettingsSetter tenantSettingsSetter;
	
	private boolean enableMTSecurity;
	

	    private Object filterObject;
	    private Object returnObject;
	    private Object target;

	    TenantTypeSecurityExpressionRoot(Authentication a) {
	        super(a);
	    }

	    public void setFilterObject(Object filterObject) {
	        this.filterObject = filterObject;
	    }

	    public Object getFilterObject() {
	        return filterObject;
	    }

	    public void setReturnObject(Object returnObject) {
	        this.returnObject = returnObject;
	    }

	    public Object getReturnObject() {
	        return returnObject;
	    }

	    /**
	     * Sets the "this" property for use in expressions. Typically this will be the "this" property of
	     * the {@code JoinPoint} representing the method invocation which is being protected.
	     *
	     * @param target the target object on which the method in is being invoked.
	     */
	    void setThis(Object target) {
	        this.target = target;
	    }

	    public Object getThis() {
	        return target;
	    }
	    
	/**
     *
     */
    public boolean hasMTRole(String role) {
    	
    	log.debug("TenantTypeSecurityExpressionRoot - hasMTRole role: " + role);
    	
//    	String tenant = tenantSetterRequest.setTenant(request);			
    	String tenant = tenantService.getCurrentTenantName();
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
//				se pubblico e la risorsa richiede ruolo di amministratore allora forso controllo che deve essere amminsitratore dello specifico tenant
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

	
	public void setTenantSettingsSetter(TenantSettingsSetter tenantSettingsSetter) {
		this.tenantSettingsSetter = tenantSettingsSetter;
		
	}

	public boolean isEnableMTSecurity() {
		return enableMTSecurity;
	}

	public void setEnableMTSecurity(boolean enableMTSecurity) {
		this.enableMTSecurity = enableMTSecurity;
	}

	public TenantService getTenantService() {
		return tenantService;
	}

	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}

	public TenantSettingsSetter getTenantSettingsSetter() {
		return tenantSettingsSetter;
	}

	
    
    
}
