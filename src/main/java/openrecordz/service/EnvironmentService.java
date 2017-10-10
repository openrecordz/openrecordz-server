package openrecordz.service;

public interface EnvironmentService {

	public String getTenantUrl();
	
	public String getEnvironmentName();
	
	public String getShortEnvironmentName();
	
	public boolean isProduction();
	
//	doesn't work
	public String getSystemProperty(String propertyName);
}
