package openrecordz.domain.properties;

import java.util.List;

public interface Property<T> {
	
	/**
	 * Returns the property id.
	 * 
	 * @return the property id
	 */
	String getId();
	
	void setId(String id);
//	/**
//	 * Returns the local name.
//	 * 
//	 * @return the local name or <code>null</code>
//	 */
//	String getLocalName();
	
	/**
	 * Returns the display name.
	 * 
	 * @return the display name or <code>null</code>
	 */
	String getDisplayName();
	
	void setDisplayName(String displayName);
	
	/**
	 * Returns the list of values of this property. For a single value property
	 * this is a list with one entry.
	 * 
	 * @return the list of values or (in rare cases) <code>null</code>
	 */
	List<T> getValues();
	
	void setValues(List<T> values);
	
	/**
	 * Returns the first entry of the list of values.
	 * 
	 * @return first entry of the list of values or (in rare cases)
	 *         <code>null</code>
	 */
	T getFirstValue();
	
	/**
	 * Returns the property value (single or multiple).
	 */
	<U> U getValue();
	
	/**
	 * Returns a human readable representation of the property value. If the
	 * property is multi-value property, only the first value will be returned.
	 */
	String getValueAsString();

    /**
	 * Returns a human readable representation of the property values.
	 */
	String getValuesAsString();
}
