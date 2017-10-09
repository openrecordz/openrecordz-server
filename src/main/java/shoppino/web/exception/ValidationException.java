package shoppino.web.exception;


//@ResponseStatus( value = HttpStatus.NOT_ACCEPTABLE )
public final class ValidationException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -431442761198706801L;

	public ValidationException(){
		super();
	}
	
	public ValidationException(String message ){
		super(message);
	}
	
	public ValidationException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public ValidationException(Throwable cause ){
		super(cause);
	} 
}
