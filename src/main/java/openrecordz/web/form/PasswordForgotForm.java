package openrecordz.web.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PasswordForgotForm {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String email;
	
	public PasswordForgotForm() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
