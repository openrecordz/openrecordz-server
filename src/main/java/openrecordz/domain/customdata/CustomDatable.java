package openrecordz.domain.customdata;

import org.codehaus.jackson.annotate.JsonIgnore;

import shoppino.exception.ShoppinoRuntimeException;



//@JsonIgnoreProperties( { "_type","_status", "id" })
public interface CustomDatable {

	
//	public String getId();
	
	@JsonIgnore
	public String getType(); //className
	
	public String toJSON() throws ShoppinoRuntimeException ;
	


}
