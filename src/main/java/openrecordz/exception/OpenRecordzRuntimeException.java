package openrecordz.exception;

public class OpenRecordzRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7269472685821384142L;

	public OpenRecordzRuntimeException(){
		super();
	}
	
	public OpenRecordzRuntimeException(String message ){
		super(message);
	}
	
	public OpenRecordzRuntimeException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public OpenRecordzRuntimeException(Throwable cause ){
		super(cause);
	}
	
}
