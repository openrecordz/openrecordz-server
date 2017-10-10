package openrecordz.security.exception;


public class AuthenticationException extends Exception {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = -221524159957349551L;

	public AuthenticationException(String message ){
		super(message);
	}
	
	public AuthenticationException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public AuthenticationException(Throwable cause ){
		super(cause);
	}
}
