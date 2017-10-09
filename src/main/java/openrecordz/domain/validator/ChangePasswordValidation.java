//package openrecordz.domain.validator;
//
//import org.springframework.validation.Errors;
//import org.springframework.validation.ValidationUtils;
//import org.springframework.validation.Validator;
//
//import shoppino.web.form.SettingsPasswordForm;
//
//public class ChangePasswordValidation implements Validator {
//
//	@Override
//	public boolean supports(Class<?> arg0) {
//		// TODO Auto-generated method stub
//		return ChangePasswordValidation.class.equals(arg0);
//	}
//
//	@Override
//	public void validate(Object obj, Errors e) {
//		SettingsPasswordForm settingsPasswordForm = (SettingsPasswordForm) obj;
////		ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
//		
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "newPassword",
//				"required.newPassword", "Field New password is required.");
//		
//		if (settingsPasswordForm.getNewPassword()!=null && settingsPasswordForm.getNewPassword().length()<6) 
//			e.rejectValue("newPassword",
//				"settings.password.length.error");
//		
//		ValidationUtils.rejectIfEmptyOrWhitespace(e, "confirmPassword",
//				"required.confirmPassword", "Field Confirm password is required.");
//		
//		if (!settingsPasswordForm.getNewPassword().equals(settingsPasswordForm.getConfirmPassword()))
//			e.reject("settings.different.passord", "The passwords you entered must match.");
//		
////		if (!isEmail)
////		 e.rejectValue("email", "notemail");
//		 
//			
//	}
//
//}
