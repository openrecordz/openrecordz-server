package shoppino.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import openrecordz.domain.Tenant;
import openrecordz.exception.ShoppinoRuntimeException;
import shoppino.security.service.AuthenticationService;
import shoppino.service.RDBService;
import shoppino.service.TenantService;
import shoppino.web.exception.ValidationException;

public class TenantServiceImpl implements TenantService {

	@Autowired
	private TenantSettingsSetter tenantSettingsSetter;
	

	@Autowired
	AuthenticationService authenticationService;
	
	
	@Autowired
	RDBService rdbService;
	
	public String getCurrentTenantName() {
		Tenant t= getCurrentTenant();
		if (t!=null)
			return TenantThreadLocal.get().getName();
		else 
			return TenantService.DEFAULT_TENANT;
	}
	
	public Tenant getCurrentTenant() {
		return TenantThreadLocal.get();
	}
	
	public void setCurrentTenant(Tenant tenant) {
		 TenantThreadLocal.set(tenant);
	}

	@Override
	public boolean isDefaultTenant() {
		if (getCurrentTenantName().equals(DEFAULT_TENANT))
			return true;
		else 
			return false;
	}

	@Override
	public List<String> getTenantsName() {
		List<String> tenants= new ArrayList<String>();
		
		tenants.add(DEFAULT_TENANT);
		
		if (!isDefaultTenant())
			tenants.add(getCurrentTenantName());
		
		return tenants;
	}

	@Override
	public String getCurrentTenantDisplayName() {
		String dn=tenantSettingsSetter.getTenantDisplayName(this.getCurrentTenantName());
		if (dn!=null)
			return dn;
		else
			return this.getCurrentTenantName();
			
	}
	
	@Override
	public String getCurrentTenantType() {
		return tenantSettingsSetter.getTenantType(this.getCurrentTenantName());
	}

	@Override
	public Integer getCurrentTenantDefaultStatus() {
		return tenantSettingsSetter.getTenantDefaultStatus(this.getCurrentTenantName());
	}

	@Override
	//not tested
	public void createTenant(String tenantName) throws ValidationException {
		if (tenantName.equals("") || tenantName.startsWith("_"))
			throw new ValidationException("App name not specified");
		
//		if (Integer.parseInt(rdbService.select("jdbc:mysql://localhost/shoppino?user=root&password=root", "select count(*) as mycount from tenant where name='"+tenantadd+"'").get(0).get("mycount")) ==0){
//			rdbService.update("jdbc:mysql://localhost/shoppino?user=root&password=root", "insert into tenant values('"+tenantadd+"')");
		try {
			if (Integer.parseInt(rdbService.select("jdbc:mysql://localhost/shoppino?user=root&password=shoppino", "select count(*) as mycount from tenant where name='"+tenantName+"'").get(0).get("mycount")) ==0){
				rdbService.update("jdbc:mysql://localhost/shoppino?user=root&password=shoppino", "insert into tenant values('"+tenantName+"')");
				
				final String curTenantName = this.getCurrentTenantName();
				
				this.setCurrentTenant(new Tenant(tenantName));
//			personService.joinCurrentTenant(authenticationService.getCurrentLoggedUsername());
				
				
				 
//			 if (!tenantService.getCurrentTenantType().equals(Tenant.PUBLIC_TENANT_TYPE))
//				 finalRole=role+"@"+tenantService.getCurrentTenantName();
//			 
//			Boolean myresult = authenticationService.runAs(new AuthenticationService.RunAsWork<Boolean>() {
//				@Override
//		        public Boolean doWork() throws Exception {
//					String finalRole = "ROLE_ADMIN@" + tenantService.getCurrentTenantName();
//					authorizationService.addRole(authenticationService.getCurrentLoggedUsername(), finalRole);
//					
////					finalRole = "ROLE_ADMIN@" + curTenantName;
////					authorizationService.addRole(authenticationService.getCurrentLoggedUsername(), finalRole);
//					
//		            return true;
//		        }
//			}, authenticationService.getCurrentLoggedUsername(),"admin");
//			
//			return "{\"status\":\"success\"}";
			}else {
				throw new ValidationException("App name : " + tenantName +" already in use");
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new ShoppinoRuntimeException(e);
		}
		
	}
	
//	public String getCurrentTenantType() {
//		String currentTenantName = this.getCurrentTenantName();
//		
//		//default
//		String currentTenantType = Tenant.PUBLIC_TENANT_TYPE;
//		
//		return currentTenantType;
//	}
}
