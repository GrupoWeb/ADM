package mx.gob.se.rug.exception;

public class AltaParteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer codeError;
	
	public Integer getCodeError() {
		return codeError;
	}

	public AltaParteException() {
		// TODO Auto-generated constructor stub
	}

	public AltaParteException(Integer codeError,String message) {
		super(message);
		this.codeError=codeError;
		// TODO Auto-generated constructor stub
	}

	public AltaParteException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AltaParteException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
