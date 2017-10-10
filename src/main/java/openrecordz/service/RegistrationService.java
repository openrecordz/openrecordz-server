package openrecordz.service;

import openrecordz.exception.EmailAlreadyInUseException;
import openrecordz.exception.ShoppinoException;
import openrecordz.security.exception.UsernameAlreadyInUseException;

public interface RegistrationService {

	//aop-script related
	public String register(String username, String fullName, String email, String password) throws UsernameAlreadyInUseException, EmailAlreadyInUseException, ShoppinoException;
	
	
}
