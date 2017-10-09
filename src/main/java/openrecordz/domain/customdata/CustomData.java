package openrecordz.domain.customdata;

import com.mongodb.DBObject;

import openrecordz.domain.Auditable;
import openrecordz.domain.Baseable;
import openrecordz.domain.Patchable;
import openrecordz.domain.Statusable;
import openrecordz.domain.Tenantable;
import openrecordz.domain.Typeable;


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
