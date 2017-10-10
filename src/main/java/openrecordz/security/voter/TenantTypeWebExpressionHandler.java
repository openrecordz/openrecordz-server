package openrecordz.security.voter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionHandler;

import openrecordz.service.impl.TenantSetterRequest;
import openrecordz.service.impl.TenantSettingsSetter;

public class TenantTypeWebExpressionHandler extends DefaultWebSecurityExpressionHandler implements WebSecurityExpressionHandler {

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Autowired
    TenantSettingsSetter tenantSettingsSetter;
    
//    @Value("$shoppino{security.mt.enabled}")
	private boolean enableMTSecurity;
    
    @Autowired
	private TenantSetterRequest tenantSetterRequest;
	

	 @Override
	    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
		 TenantTypeWebSecurityExpressionRoot root = new TenantTypeWebSecurityExpressionRoot(authentication, fi);
	        root.setPermissionEvaluator(getPermissionEvaluator());
	        root.setTrustResolver(trustResolver);
	        root.setRoleHierarchy(getRoleHierarchy());
//	        root.setTenantService(tenantService);
	        root.setTenantSettingsSetter(tenantSettingsSetter);
	        root.setTenantSetterRequest(tenantSetterRequest);
	        root.setEnableMTSecurity(enableMTSecurity);
	        return root;
	    }


//	public TenantService getTenantService() {
//		return tenantService;
//	}
//
//
//	public void setTenantService(TenantService tenantService) {
//		this.tenantService = tenantService;
//	}
	 
	 


	public TenantSetterRequest getTenantSetterRequest() {
		return tenantSetterRequest;
	}


	public TenantSettingsSetter getTenantSettingSetter() {
		return tenantSettingsSetter;
	}


	public void setTenantSettingsSetter(TenantSettingsSetter tenantSettingsSetter) {
		this.tenantSettingsSetter = tenantSettingsSetter;
	}


	public void setTenantSetterRequest(TenantSetterRequest tenantSetterRequest) {
		this.tenantSetterRequest = tenantSetterRequest;
	}

	public boolean isEnableMTSecurity() {
		return enableMTSecurity;
	}

	public void setEnableMTSecurity(boolean enableMTSecurity) {
		this.enableMTSecurity = enableMTSecurity;
	}
	 
	 
}
