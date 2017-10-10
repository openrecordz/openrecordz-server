package openrecordz.security.exception;


public class AuthorizationRuntimeException extends RuntimeException {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = -6411167709648511899L;

	/**
	 * 
	 */

	public AuthorizationRuntimeException(String message ){
		super(message);
	}
	
	public AuthorizationRuntimeException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public AuthorizationRuntimeException(Throwable cause ){
		super(cause);
	}
}
