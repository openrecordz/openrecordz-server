package shoppino.security.exception;


public class AuthorizationException extends Exception {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = -6411167709648511899L;

	/**
	 * 
	 */

	public AuthorizationException(String message ){
		super(message);
	}
	
	public AuthorizationException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public AuthorizationException(Throwable cause ){
		super(cause);
	}
}
