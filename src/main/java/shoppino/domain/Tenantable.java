package shoppino.domain;

import java.util.List;



public interface Tenantable {

	
	
	public List<String> getTenants();
	
	public void setTenants(List<String> tenants);
	
	public void addTenant(String name);
	
	public void addAllTenants(List<String> names);
	

}
