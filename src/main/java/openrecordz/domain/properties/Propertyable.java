package openrecordz.domain.properties;


import java.util.Map;

public interface Propertyable {

	public Map<String,Property> getProperties();
	
	public void setProperties(Map<String,Property> properties);

}
