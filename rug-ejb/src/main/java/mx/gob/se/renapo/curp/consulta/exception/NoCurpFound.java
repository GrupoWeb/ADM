package mx.gob.se.renapo.curp.consulta.exception;

public class NoCurpFound extends Exception {
	
	
	

	private Integer coceError;
	
	public NoCurpFound() {
		// TODO Auto-generated constructor stub
	}

	public NoCurpFound(String message,Integer codeError) {
		super(message);
		this.coceError=codeError;
		// TODO Auto-generated constructor stub
	}

	public NoCurpFound(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NoCurpFound(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public Integer getCoceError() {
		return coceError;
	}

	public void setCoceError(Integer coceError) {
		this.coceError = coceError;
	}

}
