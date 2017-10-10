package openrecordz.exception;

public class OpenRecordzException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7269472685821384142L;

	public OpenRecordzException(){
		super();
	}
	
	public OpenRecordzException(String message ){
		super(message);
	}
	
	public OpenRecordzException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public OpenRecordzException(Throwable cause ){
		super(cause);
	}
	
}
