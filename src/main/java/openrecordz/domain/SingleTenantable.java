package openrecordz.domain;

public interface SingleTenantable {

	public String getTenant();

	public void setTenant(String tenant);
}
