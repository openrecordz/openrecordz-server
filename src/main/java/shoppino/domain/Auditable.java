package shoppino.domain;

import java.util.Date;



public interface Auditable {

	
	

	
	public String getCreatedBy();
	
	public void setCreatedBy(String createdBy);
	
	

	public Date getCreatedOn();
	
	public void setCreatedOn(Date createdOn);

	public String getCreatedOnRFC822();
	
	
	
	public String getModifiedBy();

	public void setModifiedBy(String modifiedBy);

	public Date getModifiedOn();

	public String getModifiedOnRFC822();
	
	public void setModifiedOn(Date modifiedOn);

}
