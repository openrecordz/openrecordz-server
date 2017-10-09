package shoppino.security.service;

import org.springframework.security.core.Authentication;

import shoppino.security.exception.AuthenticationException;

public interface AuthenticationService {
	
	public static String ADMIN_USERNAME = "admin";
	
	public void authenticate(String username) throws AuthenticationException;
	
	public void authenticate(String username, String password) throws AuthenticationException;
	
	public String getCurrentLoggedUsername();
	
	public Authentication getAuthentication();

	public void clearCurrentSecurityContext();
	
	//use authorizationservice.isAdministrator
	@Deprecated
	public boolean isAdministrator();

	void logout();
	
	boolean isAuthenticated();
	
	
	public interface RunAsWork<Result>
	{
	        /**
	         * Method containing the work to be done in the user transaction.
	         * 
	         * @return Return the result of the operation
	         */
	  Result doWork() throws Exception;
	}
	
	public <R> R runAs(RunAsWork<R> runAsWork, String orginalUsername,String username) throws AuthenticationException;
//	public <R> R runAs(RunAsWork<R> runAsWork, String username) throws AuthenticationException;
}
