package openrecordz.exception;

public class TooManyNotificationException extends ShoppinoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5040111297292699287L;

	/**
	 * 
	 */
	

	public TooManyNotificationException(){
		super();
	}
	
	public TooManyNotificationException(String message ){
		super(message);
	}
	
	public TooManyNotificationException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public TooManyNotificationException(Throwable cause ){
		super(cause);
	}
	
}
