package shoppino.domain.validator;

import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import shoppino.exception.ResourceNotFoundException;
import shoppino.service.PersonService;
import shoppino.web.form.PasswordForgotForm;

public class PasswordForgotValidation implements Validator {

	@Autowired
	PersonService personService;
	
	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return PasswordForgotValidation.class.equals(arg0);
	}

	@Override
	public void validate(Object obj, Errors e) {
		PasswordForgotForm passwordForgotForm = (PasswordForgotForm) obj;
		

	
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "email",
				"required.email", "Field email is required.");
		
		boolean isEmail = EmailValidator.getInstance().isValid(passwordForgotForm.getEmail());
		
		if (!passwordForgotForm.getEmail().equals("") && !isEmail)
		 e.rejectValue("email", "notemail");
	
		try {
			personService.getByEmail(passwordForgotForm.getEmail());
		}catch (ResourceNotFoundException ex) {
			e.rejectValue("email", "forgot_password.emailnotfound");
		}
			
	}

}
