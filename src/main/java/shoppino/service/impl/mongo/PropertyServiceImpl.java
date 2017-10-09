package shoppino.service.impl.mongo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import shoppino.domain.Property;
import shoppino.domain.customdata.CustomData;
import shoppino.exception.ResourceNotFoundException;
import shoppino.service.CustomDataService;
import shoppino.service.PropertyService;

public class PropertyServiceImpl  implements PropertyService{

	
	
	protected Log log = LogFactory.getLog(getClass());
	
	@Autowired
	@Qualifier(value="customDataServiceWithCheckDisabled")
	private CustomDataService customDataService;
	
	@Override
	public String add(String key, String value) {
		Property p = new Property();
		p.setKey(key);
		p.setValue(value);
		
		return customDataService.add(p);
	}

	@Override
	public String update(String key, String value)
			throws ResourceNotFoundException {
		
		
		Property p = new Property();
		p.setKey(key);
		p.setValue(value);
		return customDataService.update(this.getPropertyId(key), p);
	}
	
	private String getPropertyId(String key) throws ResourceNotFoundException {
		CustomData cdataProperty;
		
		try {
			cdataProperty = customDataService.findByQuery("{\"key:\":\""+key+"\"}", Property.PROPERTY_CLASS_NAME).get(0);
		}catch (Exception e) {
			log.error("Property with key : " + key + " not found", e);
			throw new ResourceNotFoundException("Property with key : " + key + " not found");
		}
		return cdataProperty.getId();
	}
	
	@Override
	public String get(String key) throws ResourceNotFoundException {
		
		return customDataService.getById(this.getPropertyId(key)).toString();
	}

	@Override
	public void remove(String key) throws ResourceNotFoundException {
		customDataService.remove(this.getPropertyId(key));
	}
	
	

}
