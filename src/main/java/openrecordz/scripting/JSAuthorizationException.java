package openrecordz.scripting;

import openrecordz.security.exception.AuthorizationException;


public final class JSAuthorizationException extends AuthorizationException {
  
	/**
	 * 
	 */
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -666530283722736363L;

	/**
	 * 
	 */

	public JSAuthorizationException(String message ){
		super(message);
	}
	
	public JSAuthorizationException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public JSAuthorizationException(Throwable cause ){
		super(cause);
	}
}
