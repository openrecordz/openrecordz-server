//package shoppino.security.voter;
//
//import org.springframework.security.access.expression.SecurityExpressionOperations;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;
//
//import shoppino.service.TenantService;
//import shoppino.service.impl.TenantSetterRequest;
//
////non dovrebbe essere usata
//@Deprecated
//public class EEWebSecurityExpressionRoot extends WebSecurityExpressionRoot implements SecurityExpressionOperations {
//	
//	private TenantSetterRequest tenantSetterRequest;
//	
//	private TenantService tenantService;
//	
//	private boolean enableMTSecurity;
//	
//	public EEWebSecurityExpressionRoot(Authentication a, FilterInvocation fi) {
//		super(a, fi);
////		AbstractAuthorizeTag ee = new AbstractAuthorizeTag();
////		JspAuthorizeTag 
//	}
//
//	/**
//     *
//     */
//    public boolean hasMTRole(String role) {
////	    	String tenant = TenantService.DEFAULT_TENANT;
////			try {
////				String thirdLevel = request.getServerName().substring(0, request.getServerName().indexOf("."));
////				tenant=thirdLevel;
////			} catch (Exception e) {
////				tenant = TenantService.DEFAULT_TENANT;
////			}
////			
////			if (tenant.equals("www"))
////				tenant = TenantService.DEFAULT_TENANT;
//    	
//    		String tenant = tenantSetterRequest.setTenant(request);			
//		
//////    		TenantService tenantService = (TenantService)request.getAttribute(TenantInterceptor.TENANT_SERVICE_REQUEST_ATTRIBUTE);
//////    		String tenantName = TenantThreadLocal.get().getName();
//////            return hasRole(role+"@"+tenantService.getCurrentTenantName());
//			if (tenant.equals(TenantService.DEFAULT_TENANT) || enableMTSecurity==false) {
//				return hasRole(role);
//			} else {
//				return hasRole(role+"@"+tenant);
//			}
//            
////        }
//    }
//
//	public TenantSetterRequest getTenantSetterRequest() {
//		return tenantSetterRequest;
//	}
//
//	public void setTenantSetterRequest(TenantSetterRequest tenantSetterRequest) {
//		this.tenantSetterRequest = tenantSetterRequest;
//	}
//
//	public TenantService getTenantService() {
//		return tenantService;
//	}
//
//	public void setTenantService(TenantService tenantService) {
//		this.tenantService = tenantService;
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
