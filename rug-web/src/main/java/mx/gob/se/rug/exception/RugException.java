package mx.gob.se.rug.exception;

public class RugException extends Exception {

	private static final long serialVersionUID = 1L;
	private Integer codeError;
	
	public Integer getCodeError() {
		return codeError;
	}

	public RugException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RugException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RugException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	public RugException(Integer codeError, String message) {
		super(message);
		this.codeError=codeError;
		// TODO Auto-generated constructor stub
	}

	public RugException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
