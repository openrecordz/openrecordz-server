package openrecordz.domain.properties;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import openrecordz.exception.OpenRecordzRuntimeException;



/**
 * Property Implementation.
 */
public class PropertyImpl<T> implements Property<T> {
	
	/** The id. */
	private String id;
	
	/** The display name. */
	private String displayName;
	
//	/** The local name. */
//	private String localName;
	
	/** The values. */
	private List<T> values;

	
	public PropertyImpl() {
		
	}
	/**
	 * Instantiates a new PropertyImpl.
	 * 
	 * @param id the id
	 * @param displayName the display name
	 * @param localName the local name
	 * @param values the values
	 */
//	public PropertyImpl(String id, String displayName, String localName, List<T> values) {
	public PropertyImpl(String id, String displayName, List<T> values) {
		super();
		this.id = id;
		this.displayName = displayName;
//		this.localName = localName;
		this.values = values;
	}
	
	public PropertyImpl(JSONObject propertyAsJson) {
		super();
			if (propertyAsJson.get("_id") == null) {
				throw new OpenRecordzRuntimeException("The property must have a field _id");
			}
		this.id = propertyAsJson.get("_id").toString();
		this.displayName = propertyAsJson.get("displayName").toString();		
		JSONArray jsonArrayValues = (JSONArray)propertyAsJson.get("values");
		this.values = (List)jsonArrayValues;
//		this.values = null;
	}
		
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;		
	}

	
	public List<T> getValues() {
		return values;
	}
	
	public void setValues(List<T> values) {
		if (values == null) {
			this.values = Collections.emptyList();
		} else {
			this.values = values;
		}
	}
	
	// public void setValue(T value) {
	// if (value == null) {
	// values = Collections.emptyList();
	// } else {
	// values = new ArrayList<T>(1);
	// values.add(value);
	// }
	// }
	
	
	public T getFirstValue() {
		if ((values != null) && (!values.isEmpty())) {
			return values.get(0);
		}
		
		return null;
	}
	
	
	@Override
	public String toString() {
		return "Property [id=" + id + ", display Name=" + displayName + ", values=" + values +
				"]" + super.toString();
	}


	@Override
	public <U> U getValue() {
		List<T> values = getValues();
		// if (propertyDefinition.getCardinality() == Cardinality.SINGLE) {
		// return values.size() == 0 ? null : (U) values.get(0);
		// } else {
			return (U) values;
		// }
	}
	

	public String getValueAsString() {
		List<T> values = getValues();
		if (values.size() == 0) {
			return null;
		}
		
		return formatValue(values.get(0));
	}
	

	public String getValuesAsString() {
		List<T> values = getValues();
		
		StringBuilder result = new StringBuilder();
		for (T value : values) {
			if (result.length() > 0) {
				result.append(", ");
			}
			
			result.append(formatValue(value));
		}
		
		return "[" + result.toString() + "]";
	}
	
	
	private String formatValue(T value) {
		String result;
		
		if (value == null) {
			return null;
		}
		
		if (value instanceof GregorianCalendar) {
			result = ((GregorianCalendar) value).getTime().toString();
		} else {
			result = value.toString();
		}
		
		return result;
	}

	
	
}
