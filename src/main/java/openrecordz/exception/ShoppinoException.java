package openrecordz.exception;

public class ShoppinoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7269472685821384142L;

	public ShoppinoException(){
		super();
	}
	
	public ShoppinoException(String message ){
		super(message);
	}
	
	public ShoppinoException(String message, Throwable cause ){
		super(message, cause);
	}
	
	public ShoppinoException(Throwable cause ){
		super(cause);
	}
	
}
