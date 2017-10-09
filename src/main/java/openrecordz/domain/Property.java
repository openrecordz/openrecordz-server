package openrecordz.domain;

import java.io.IOException;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import openrecordz.domain.customdata.CustomDatable;
import shoppino.exception.ShoppinoRuntimeException;

@JsonIgnoreProperties( { "_type","_status", "id" })
public class Property implements CustomDatable 
//Auditable, 
{

	public static String PROPERTY_CLASS_NAME = "_property";
	
	private String key;
	private String value;
	
//	private String _createdBy;
//	private String _modifiedBy;
//	private Date _createdOn;
//	private Date _modifiedOn;
	
	public Property() {
//		if (this.getCreatedOn() == null)
//			this.setCreatedOn(new Date());
	}
	
	
	
	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



//	public String getCreatedBy() {
//		return _createdBy;
//	}
//	
//	public void setCreatedBy(String createdBy) {
//		this._createdBy = createdBy;
//	}
//	
//	public Date getCreatedOn() {
//		return _createdOn;
//	}
//	
//	public void setCreatedOn(Date createdOn) {
//		this._createdOn = createdOn;
//	}
//
//	public String getCreatedOnRFC822() {
//		return null;
//	}
//	
//	
//	
//	public String getModifiedBy() {
//		return this._modifiedBy;
//	}
//
//	public void setModifiedBy(String modifiedBy) {
//		this._modifiedBy = modifiedBy;
//	}
//
//	public Date getModifiedOn() {
//		return this._modifiedOn;
//	}
//
//	public String getModifiedOnRFC822() {
//		return null;
//	}
//	
//	public void setModifiedOn(Date modifiedOn) {
//		this._modifiedOn = modifiedOn;
//	}
//	
	@Override
	public String toString() {
		return "key:"+key +" value: " + value;
	}
	
	public String toJSON() {
//		http://stackoverflow.com/questions/15786129/converting-java-objects-to-json-with-jackson
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json;
		try {
			json = ow.writeValueAsString(this);
		} catch (IOException e) {
			throw new ShoppinoRuntimeException("Error generating JSON for object " + this.toString(), e);
		}
		return json;
	}



	@Override
	public String getType() {
		return PROPERTY_CLASS_NAME;
	}
}
