package shoppino.domain.customdata;

import shoppino.domain.Auditable;
import shoppino.domain.Baseable;
import shoppino.domain.Patchable;
import shoppino.domain.Statusable;
import shoppino.domain.Tenantable;
import shoppino.domain.Typeable;

import com.mongodb.DBObject;


public interface CustomData extends DBObject, Baseable, Typeable, Tenantable, Statusable, Auditable, CustomDatable, Patchable {
	
	
//	/**
//	 * Returns the property id.
//	 * 
//	 * @return the property id
//	 */
//	String getId();
//	
//	void setId(String id);

	public String toString();
	
	
}
