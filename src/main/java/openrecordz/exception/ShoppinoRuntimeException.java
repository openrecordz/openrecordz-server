package openrecordz.exception;

public class ShoppinoRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7269472685821384142L;

	public ShoppinoRuntimeException(){
		super();
	}
	
	public ShoppinoRuntimeException(String message ){
		super(message);
	}
	
	public ShoppinoRuntimeException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public ShoppinoRuntimeException(Throwable cause ){
		super(cause);
	}
	
}
