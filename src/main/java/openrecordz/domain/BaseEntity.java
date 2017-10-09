package openrecordz.domain;

import java.io.Serializable;

import microdev.util.StringUtils;

import org.springframework.data.annotation.Id;


public class BaseEntity 
	extends TypeableEntity implements Baseable, Serializable
		{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 981699857782375508L;
	
	@Id 
//	@Field(Searchable.ID_FIELD)
	private String id;
//	private String type;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
//	public String getType() {
//		return type;
//	}
//	
//	public void setType(String type) {
//		this.type = type;
//	}
	
	public String toString() {
		return StringUtils.to_s(this);
	}
	
	public String inspect() {
		return StringUtils.inspect(this);
	}
}
