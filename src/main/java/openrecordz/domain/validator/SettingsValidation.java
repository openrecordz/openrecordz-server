//package openrecordz.domain.validator;
//
//import org.apache.commons.validator.EmailValidator;
//import org.springframework.validation.Errors;
//import org.springframework.validation.ValidationUtils;
//import org.springframework.validation.Validator;
//
//import shoppino.web.form.SettingsForm;
//
//public class SettingsValidation implements Validator {
//
//	@Override
//	public boolean supports(Class<?> arg0) {
//		// TODO Auto-generated method stub
//		return SettingsValidation.class.equals(arg0);
//	}
//
//	@Override
//	public void validate(Object obj, Errors e) {
//		SettingsForm settingsForm = (SettingsForm) obj;
//		
//
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "fullName",
//				"required.fullName", "Field Name is required.");
//		
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "email",
//				"required.email", "Field email is required.");
//		
//		boolean isEmail = EmailValidator.getInstance().isValid(settingsForm.getEmail());
//		
//		if (!settingsForm.getEmail().equals("") && !isEmail)
//		 e.rejectValue("email", "notemail");
//		 
//			
//	}
//
//}
