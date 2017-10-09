package openrecordz.domain.customdata;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import microdev.util.StringUtils;
import openrecordz.domain.Auditable;
import openrecordz.domain.Patchable;
import openrecordz.domain.Statusable;
import openrecordz.domain.Tenantable;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.data.annotation.Id;

import shoppino.exception.ShoppinoRuntimeException;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;


@JsonIgnoreProperties( { "_id","_class", "_tenants" })
//@JsonIgnoreProperties( { "_class", "_tenants" })

//@JsonSerialize(using = CustomDataJsonSerializer.class)
//http://stackoverflow.com/questions/14363555/spring-3-2-and-jackson-2-add-custom-object-mapper
public class CustomDataImpl extends BasicDBObject implements CustomData, Patchable, Statusable, Auditable, Tenantable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1931928662404292552L;
	
	public static final String TENANTS_PROP_KEY = "_tenants";
	public static final String TYPE_PROP_KEY = "_type";
	public static final String STATUS_PROP_KEY = "_status";
	
	public static final String CREATEDBY_PROP_KEY = "_createdby";
	public static final String MODIFIEDBY_PROP_KEY = "_modifiedby";
	public static final String CREATEDON_PROP_KEY = "_createdon";
	public static final String MODIFIEDON_PROP_KEY = "_modifiedon";

	
	@Id 
//	@JsonSerialize(using=ObjectIdSerializer.class)
	private ObjectId id;
	
//	@Indexed 
//	private List<String> tenants;
//
//	@Indexed 
//	private String type;
	
	
	public CustomDataImpl() {
		this(null,null);
	}
	
	public CustomDataImpl(BasicDBObject basicDBObject) {
		super(basicDBObject);	
//		this.id = super.getObjectId("_id").toString();
//		this.removeField("_id");
		this.put("id", super.getObjectId("_id").toString());
		this.id = super.getObjectId("_id");
		
		if (this.getString(TENANTS_PROP_KEY) == null || this.getString(TYPE_PROP_KEY) == null || this.getString(STATUS_PROP_KEY) == null)
			throw new RuntimeException("Error instanziating CustomData ... ");
		
//		this.removeField("_id");
		if (this.getCreatedOn() == null)
			this.setCreatedOn(new Date());
	}
	
	public CustomDataImpl(String type, String json) {
//		super();
		super((BasicDBObject)JSON.parse(json));
//		this.id = this.getObjectId("_id").toString();
//		this.removeField("_id");
//		this.put("id", this.id);
//		this.put("id", null);
//		this.put("id", UUID.randomUUID().toString());
		this.put(TENANTS_PROP_KEY, new ArrayList<String>());
		
		this.put(TYPE_PROP_KEY, type);
		
		this.put(STATUS_PROP_KEY, Statusable.STATUS_VISIBLE);
		
		this.setCreatedOn(new Date());
//		this.type = type;
		
	}

//	public CustomDataImpl(JSONObject propertyAsJson) {
//	super();
//	this.id = propertyAsJson.get("_id").toString();
//	this.displayName = propertyAsJson.get("displayName").toString();		
//	JSONArray jsonArrayValues = (JSONArray)propertyAsJson.get("values");
//	this.values = (List)jsonArrayValues;
////	this.values = null;
//}
	
	

	@Override	
//	@JsonSerialize(using=ObjectIdSerializer.class)
	public String getId() {
		if (this.get("_id")!=null)
			return this.get("_id").toString();
		else
			return null;
//		return this.getString("id");
	}
	
	@Override
//	@JsonSerialize(using=ObjectIdSerializer.class)
	public void setId(String id) {
		this.put("id", id );
		this.id = new ObjectId(id);
		this.put("_id", new ObjectId(id));
	}

//	@Override
//	public String getId() {
//		return this.getObjectId("_id").toString();
//	}
//	@Override
//	public void setId(String id) {
////		this.id = id;
//	}


	@Override
	public List<String> getTenants() {
		return (List<String>) this.get(TENANTS_PROP_KEY);
	}
	@Override
	public void setTenants(List<String> tenants) {
		this.put(TENANTS_PROP_KEY, tenants);
	}
	@Override
	public void addAllTenants(List<String> names) {
		for (String name : names) {
			addTenant(name);
		}
	}
	@Override
	public void addTenant(String name) {
		if (name!=null && !getTenants().contains(name)) {			
			getTenants().add(name);
		}
	}
	
	@Override
	public String getType() {
		return this.getString(TYPE_PROP_KEY);
	}
	
	@Override
	public void setType(String type) {
		this.put(TYPE_PROP_KEY, type);
	}
	
	@Override
	public Integer getStatus() {
		return this.getInt(STATUS_PROP_KEY);
	}
	
	
	
	public String getCreatedBy() {
		return this.getString(CREATEDBY_PROP_KEY);
	}

	public void setCreatedBy(String createdBy) {
		this.put(CREATEDBY_PROP_KEY, createdBy);
	}
	
	

	public Date getCreatedOn() {
//		return new Date(this.get(CREATEDON_PROP_KEY));
		return (Date)this.get(CREATEDON_PROP_KEY);
	}

	public void setCreatedOn(Date createdOn) {
		this.put(CREATEDON_PROP_KEY, createdOn);
	}

	public String getCreatedOnRFC822() {
		if (this.getCreatedOn()!=null) {
		  SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
		  return sdf.format(this.getCreatedOn());
		}else return "";
	}
	
	
	
	public String getModifiedBy() {
		return this.getString(MODIFIEDBY_PROP_KEY);
	}

	public void setModifiedBy(String modifiedBy) {
		this.put(MODIFIEDBY_PROP_KEY, modifiedBy);
	}

	public Date getModifiedOn() {
//		return new Date(this.getLong(MODIFIEDON_PROP_KEY));
		return (Date)this.get(MODIFIEDON_PROP_KEY);
	}

	public String getModifiedOnRFC822() {
		if (this.getModifiedOn()!=null) {
		  SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
		  return sdf.format(this.getModifiedOn());
		}else return "";
	}
	
	public void setModifiedOn(Date modifiedOn) {
		this.put(MODIFIEDON_PROP_KEY, modifiedOn);
	}

	
	@Override
	public String toString() {
		return StringUtils.to_s(this);
	}
	
//	@Override
//	public String toString() {
//		try {
//			return this.toJSON();
//		} catch (JsonGenerationException e) {
//			// TODO Auto-generated catch block			
//			e.printStackTrace();
//			return null;
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
	
//	 public String toString()
//	    {
//	        if (this == null)
//	            return null;
//
//	        StringBuffer buf = new StringBuffer().append('{');
//	        Set keys = this.keySet();
//	        Iterator iterator = keys.iterator();
//	        Object key;
//	        Object value;
//
//	        boolean first = true;
//	        while (iterator.hasNext())
//	        {
//	            if (first)
//	                first = false;
//	            else
//	                buf.append(',');
//	            key = iterator.next();
//	            value = this.get(key);
//	            buf.append( key + "->" + value );
//	        }
//	        buf.append('}');
//	        return buf.toString();
//	    }
	
	public String inspect() {
		return StringUtils.inspect(this);
	}

	public LinkedHashMap<String,Object> toMap() {
		LinkedHashMap<String,Object> clone= new LinkedHashMap<String, Object>();
		clone.put("id", this.getId());
		clone.putAll(this);
		clone.remove("_id");
		clone.remove("_tenants");
		clone.remove("_class");

		return clone;
	}
	
	public String toJSON() throws ShoppinoRuntimeException {
//		http://stackoverflow.com/questions/15786129/converting-java-objects-to-json-with-jackson
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json=null;
		try {
			json = ow.writeValueAsString(this);
		
		} catch (IOException e) {
			throw new ShoppinoRuntimeException("Error creating json :" ,e);
		}
		return json;
	}
	

	public void patch(Map map) {
//		this.putAll(basicDBObject);
		this.putAll(map);		
		
	}
	
	
}
