package shoppino.web.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.connect.UserProfile;

import openrecordz.domain.Person;

public class UserRegistrationForm extends Person {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
//	@NotEmpty
//    @Size(min = 1, max = 10, message = "Password must between 1 to 10 Characters.")
	private String password;
	private boolean fromSocial;
	private boolean acceptTermsOfService;
	
	public UserRegistrationForm() {
		super();
		fromSocial=false;
		acceptTermsOfService=false;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public boolean isFromSocial() {
		return fromSocial;
	}

	public void setFromSocial(boolean fromSocial) {
		this.fromSocial = fromSocial;
	}

	
	public boolean isAcceptTermsOfService() {
		return acceptTermsOfService;
	}

	public void setAcceptTermsOfService(boolean acceptTermsOfService) {
		this.acceptTermsOfService = acceptTermsOfService;
	}

	public static UserRegistrationForm fromProviderUser(UserProfile providerUser) {
		UserRegistrationForm form = new UserRegistrationForm();
		form.setFullName(providerUser.getFirstName() + providerUser.getLastName());
		form.setUsername(providerUser.getUsername());
		form.setEmail(providerUser.getEmail());	
		form.setFromSocial(true);
//		form.setPhoto(providerUser.get)
		return form;
	}
	
}
