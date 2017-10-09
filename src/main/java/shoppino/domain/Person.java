package shoppino.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import org.apache.solr.client.solrj.beans.Field;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import shoppino.domain.properties.Propertyable;
import shoppino.domain.properties.Property;

@JsonIgnoreProperties( { "type","tenant", "tenants" })
public class Person extends TypeableEntity implements Typeable, SearchablePerson,
		SingleTenantable, Tenantable, Propertyable, Auditable, Taggable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7377583736545796553L;

	public static String TYPE_PERSON = "Person";
	
//  @Max(64)
//	@NotNull
	
	@Id	
//	@Field(Searchable.ID_FIELD)
	private String username; //is an email???
	
//	@Field(FULLNAME_FIELD)
	private String fullName;
	
	private String email;
	
//	@Field(PHOTO_FIELD)
	private String photo;
	
	private Boolean defaultPhoto;

//	@Field(CREATEDBY_FIELD)
	private String createdBy;
	
//	@Field(CREATEDON_FIELD)
	private Date createdOn;
	
//	@Field(MODIFIEDBY_FIELD)
	private String modifiedBy;
	
//	@Field(MODIFIEDON_FIELD)
	private Date modifiedOn;
	
//	@Field(TENANT_FIELD)
	private String tenant; //registration tenant
	
	@Indexed
//	@Field(Searchable.TENANTS_FIELD)	
	private List<String> tenants; //tenants where person is logged in
	
	private boolean publishOnFb; 
	

	private Map<String,Property> properties;
	
//	@Field(Searchable.TAGS_FIELD)	
	private List<String> tags;
	
	public Person() {
		this(null);
	}

	public Person(String username) {
		this.username = username;
		this.setType(this.getClass().getSimpleName());
		this.createdOn = new Date();
		this.publishOnFb=false;
		this.properties = new HashMap<String, Property>();
		this.tenants = new ArrayList<String>();
		this.tags =  new ArrayList<String>();
		this.defaultPhoto=true;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public boolean isPublishOnFb() {
		return publishOnFb;
	}

	public void setPublishOnFb(boolean publishOnFb) {
		this.publishOnFb = publishOnFb;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	@Override
	public String getCreatedOnRFC822() {
		if (this.createdOn!=null) {
			  SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
			  return sdf.format(this.createdOn);
			}else return "";
	}

	@Override
	public String getModifiedOnRFC822() {
		if (this.modifiedOn!=null) {
			  SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
			  return sdf.format(this.modifiedOn);
			}else return "";
	}

	public Map<String, Property> getProperties() {
		return properties;
	}
	
	public Property getProperty(String name) {
		return properties.get(name);
	}
	
	
	public void removeProperty(String name) {
		properties.remove(name);
	}
	
	public void setProperties(Map<String, Property> properties) {
		this.properties = properties;
	}
	
	public void addProperty(String name, Property property) {
		this.properties.put(name, property);
	}
	

	@Override
	public List<String> getTenants() {
		return this.tenants;
	}

	@Override
	public void setTenants(List<String> tenants) {
		this.tenants = tenants;
	}

	@Override
	public void addTenant(String name) {
		if (name!=null && !tenants.contains(name)) {			
			this.tenants.add(name);
		}
	}

	@Override
	public void addAllTenants(List<String> names) {
		for (String name : names) {
			addTenant(name);
		}
	}

	public Boolean isDefaultPhoto() {
		return defaultPhoto;
	}

	public void setDefaultPhoto(Boolean defaultPhoto) {
		this.defaultPhoto = defaultPhoto;
	}

	public List<String> getTags() {
		return this.tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	
	
}
