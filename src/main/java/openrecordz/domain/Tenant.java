package openrecordz.domain;

public class Tenant {

	public static String PUBLIC_TENANT_TYPE = "public";
	public static String MODERATE_TENANT_TYPE = "moderate";
	public static String PRIVATE_TENANT_TYPE = "private"; //TODO
			
	private String name;
	
	public Tenant(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}
