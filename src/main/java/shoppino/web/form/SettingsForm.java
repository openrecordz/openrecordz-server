package shoppino.web.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import openrecordz.domain.Person;

public class SettingsForm extends Person {
	
	protected final Log logger = LogFactory.getLog(getClass());
	

	
	public SettingsForm() {
		super();
	}

	public SettingsForm(Person person) {
		super();
		this.setFullName(person.getFullName());
		this.setEmail(person.getEmail());
		this.setUsername(person.getUsername());
		this.setPhoto(person.getPhoto());
		this.setPublishOnFb(person.isPublishOnFb());
	}
	
}
