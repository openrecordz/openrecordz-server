package openrecordz.domain;

import java.io.IOException;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import openrecordz.domain.customdata.CustomDatable;
import openrecordz.exception.ShoppinoRuntimeException;

@JsonIgnoreProperties({ "_type", "_status", "id" })
public class MessageSource implements CustomDatable {

	public static String MESSAGE_SOURCE_CLASS_NAME = "_message_source";

	private String code;
	private String message;
	private String basename;
	private String language;
	private String country;
	private String variant;

	public MessageSource() {
	}

	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBasename() {
		return basename;
	}

	public void setBasename(String basename) {
		this.basename = basename;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	@Override
	public String toString() {
		return "code:" + code + " message: " + message;
	}

	public String toJSON() {
		// http://stackoverflow.com/questions/15786129/converting-java-objects-to-json-with-jackson
		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		String json;
		try {
			json = ow.writeValueAsString(this);
		} catch (IOException e) {
			throw new ShoppinoRuntimeException(
					"Error generating JSON for object " + this.toString(), e);
		}
		return json;
	}

	@Override
	public String getType() {
		return MESSAGE_SOURCE_CLASS_NAME;
	}
}
