package shoppino.web.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SettingsPasswordForm {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String newPassword;
	private String confirmPassword;
	
	public SettingsPasswordForm() {
		super();
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	
	
}
