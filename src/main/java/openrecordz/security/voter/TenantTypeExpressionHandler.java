package openrecordz.security.voter;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

import openrecordz.service.TenantService;
import openrecordz.service.impl.TenantSettingsSetter;

//dovrebbe chiamarsi TenantTypeMethodSecurityExpressionHandler
public class TenantTypeExpressionHandler extends DefaultMethodSecurityExpressionHandler implements MethodSecurityExpressionHandler {

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

//    @Autowired
    TenantSettingsSetter tenantSettingsSetter;
    
//    @Value("$shoppino{security.mt.enabled}")
	private boolean enableMTSecurity;
    
//    @Autowired	
	
//    @Autowired
    private TenantService tenantService;

    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
    	TenantTypeSecurityExpressionRoot root = new TenantTypeSecurityExpressionRoot(authentication);
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        
        root.setTenantService(tenantService);
        root.setTenantSettingsSetter(tenantSettingsSetter);
        root.setEnableMTSecurity(enableMTSecurity);
        return root;
    }


	 
	 




	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}








	public TenantSettingsSetter getTenantSettingSetter() {
		return tenantSettingsSetter;
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
	 
	 
}
