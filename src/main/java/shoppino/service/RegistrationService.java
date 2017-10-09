package shoppino.service;

import shoppino.exception.EmailAlreadyInUseException;
import shoppino.exception.ShoppinoException;
import shoppino.security.exception.UsernameAlreadyInUseException;

public interface RegistrationService {

	//aop-script related
	public String register(String username, String fullName, String email, String password) throws UsernameAlreadyInUseException, EmailAlreadyInUseException, ShoppinoException;
	
	
}
