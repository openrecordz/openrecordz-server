package openrecordz.security.voter;
//package shoppino.security.voter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.expression.SecurityExpressionOperations;
//import org.springframework.security.authentication.AuthenticationTrustResolver;
//import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
//import org.springframework.security.web.access.expression.WebSecurityExpressionHandler;
//
//import shoppino.service.impl.TenantSetterRequest;
//
////non dovrebbe essere usata
//@Deprecated
//public class EEWebExpressionHandler extends DefaultWebSecurityExpressionHandler implements WebSecurityExpressionHandler {
//
//    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
//
////    @Autowired
////    TenantService tenantService;
//    
////    @Value("$shoppino{security.mt.enabled}")
//	private boolean enableMTSecurity;
//    
//    @Autowired
//	private TenantSetterRequest tenantSetterRequest;
//	
//
//	 @Override
//	    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
//		 EEWebSecurityExpressionRoot root = new EEWebSecurityExpressionRoot(authentication, fi);
//	        root.setPermissionEvaluator(getPermissionEvaluator());
//	        root.setTrustResolver(trustResolver);
//	        root.setRoleHierarchy(getRoleHierarchy());
////	        root.setTenantService(tenantService);
//	        root.setTenantSetterRequest(tenantSetterRequest);
//	        root.setEnableMTSecurity(enableMTSecurity);
//	        return root;
//	    }
//
////
////	public TenantService getTenantService() {
////		return tenantService;
////	}
////
////
////	public void setTenantService(TenantService tenantService) {
////		this.tenantService = tenantService;
////	}
//
//
//	public TenantSetterRequest getTenantSetterRequest() {
//		return tenantSetterRequest;
//	}
//
//
//	public void setTenantSetterRequest(TenantSetterRequest tenantSetterRequest) {
//		this.tenantSetterRequest = tenantSetterRequest;
//	}
//
//	public boolean isEnableMTSecurity() {
//		return enableMTSecurity;
//	}
//
//	public void setEnableMTSecurity(boolean enableMTSecurity) {
//		this.enableMTSecurity = enableMTSecurity;
//	}
//	 
//	 
//}
