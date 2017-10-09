package openrecordz.domain;

import microdev.util.StringUtils;


public class TypeableEntity {
	
//	public static String TYPE_VARIABLE_NAME = "type";
	
//	@Field(Searchable.TYPE_FIELD)
	private String type;
	
	
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return StringUtils.to_s(this);
	}
	
	public String inspect() {
		return StringUtils.inspect(this);
	}
}
