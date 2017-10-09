package openrecordz.exception;

public class TenantAlreadyInUseException extends Exception {

	private static final long serialVersionUID = -5040222297292699287L;

	/**
	 * non funziona con restexception ritorna sempre errore 500
	 */

	public TenantAlreadyInUseException(){
		super();
	}
	
	public TenantAlreadyInUseException(String message ){
		super(message);
	}
	
	public TenantAlreadyInUseException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public TenantAlreadyInUseException(Throwable cause ){
		super(cause);
	}
	
}
