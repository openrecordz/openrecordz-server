package shoppino.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;



public class TenantableEntity extends BaseEntity implements Tenantable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2119832492507774513L;
	
	
	@Indexed 
//	@Field(Searchable.TENANTS_FIELD)
	private List<String> tenants;
	
	
	public TenantableEntity() {
		this.tenants = new ArrayList<String>();
	}
	
	@Override
	public List<String> getTenants() {
		return tenants;
	}
	@Override
	public void setTenants(List<String> tenants) {
		this.tenants = tenants;
	}
	@Override
	public void addAllTenants(List<String> names) {
		for (String name : names) {
			addTenant(name);
		}
	}
	@Override
	public void addTenant(String name) {
		if (name!=null && !tenants.contains(name)) {			
			this.tenants.add(name);
		}
	}

	

}
