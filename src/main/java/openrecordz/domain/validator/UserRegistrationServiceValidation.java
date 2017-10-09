package openrecordz.domain.validator;

import org.apache.commons.validator.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import shoppino.exception.ShoppinoException;
import shoppino.util.UsernameValidator;
import shoppino.web.form.UserRegistrationForm;

public class UserRegistrationServiceValidation implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return UserRegistrationForm.class.equals(arg0);
	}

	@Override
	public void validate(Object obj, Errors e) {
		UserRegistrationForm userRegistration = (UserRegistrationForm) obj;
//		ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "fullName",
				"required.fullName", "Field Name is required.");
		
		
		
		
		
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "username",
				"required.username", "Field username is required.");
		
		UsernameValidator usernameValidator = new UsernameValidator();
		boolean isUsernameValid = usernameValidator.validate(userRegistration.getUsername());
		if (!userRegistration.getUsername().equals("") && !isUsernameValid)
			e.rejectValue("username", "username.notvalid");
		
		
		
		
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "email",
				"required.email", "Field email is required.");
		
		boolean isEmail = EmailValidator.getInstance().isValid(userRegistration.getEmail());
		
		if (!userRegistration.getEmail().equals("") && !isEmail)
		 e.rejectValue("email", "notemail");
		 
//		if (e.hasFieldErrors("email")) {
//			userRegistration.setFromSocial(false);
//		}
	

		if (userRegistration.isFromSocial()==false) {
			ValidationUtils.rejectIfEmptyOrWhitespace(e, "password",
					"required.password", "Field password is required.");
		}
			
	}
	
	
	
	
	public void validate(String username, String fullName, String email) throws ShoppinoException { 
		
		if (fullName==null || fullName.equals(""))
			throw new ShoppinoException("Field fullName is required");
		
		
		
		if (username==null || username.equals(""))
			throw new ShoppinoException("Field username is required");
		
		
		
		UsernameValidator usernameValidator = new UsernameValidator();
		boolean isUsernameValid = usernameValidator.validate(username);
		if (!username.equals("") && !isUsernameValid)
			throw new ShoppinoException("Username is not valid");
		
		
		
		
		if (email==null || email.equals(""))
			throw new ShoppinoException("Field email is required");
		
		boolean isEmail = EmailValidator.getInstance().isValid(email);
		
		if (!email.equals("") && !isEmail)
			throw new ShoppinoException("Not an email");
		 
	
			
	}
	
}
