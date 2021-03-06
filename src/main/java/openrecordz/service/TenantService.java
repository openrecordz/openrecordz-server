package openrecordz.service;

import java.util.List;

import openrecordz.domain.Tenant;
import openrecordz.web.exception.ValidationException;

public interface TenantService {

	public static String DEFAULT_TENANT = "default";
	
	public String getCurrentTenantName();
	
	@Deprecated
	public boolean isDefaultTenant();
	
	public Tenant getCurrentTenant();
	
	public List<String> getTenantsName();

	public String getCurrentTenantType();
	
	public void setCurrentTenant(Tenant tenant);
	
	public Integer getCurrentTenantDefaultStatus();

	public String getCurrentTenantDisplayName();
	
//	not tested..hight instable
	public void createTenant(String tenantName)throws ValidationException;
}
