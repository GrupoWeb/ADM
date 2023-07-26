package mx.gob.se.renapo.curp.consulta.exception;

public class NoServiceRenapoAvailable extends Exception {
	
	
	

	private Integer codeError;
	
	public NoServiceRenapoAvailable() {
		// TODO Auto-generated constructor stub
	}

	public NoServiceRenapoAvailable(String message,Integer codeError) {
		super(message);
		this.codeError=codeError;
		// TODO Auto-generated constructor stub
		System.out.println("codigo de error:::"+ codeError);
	}

	public NoServiceRenapoAvailable(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NoServiceRenapoAvailable(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public Integer getCodeError() {
		return codeError;
	}

	public void setCodeError(Integer codeError) {
		this.codeError = codeError;
	}


	
}
