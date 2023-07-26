package mx.gob.se.rug.masiva.exception;

import mx.gob.se.rug.to.PlSql;

public class GarantiaRepetidaException extends Exception {

	private static final long serialVersionUID = 1L;

	
	
	public PlSql getPlSql() {
		PlSql plSql= new PlSql();
		plSql.setIntPl(getCodeError());
		plSql.setStrPl(getMessage());
		return plSql;
	}

	
	public Integer getCodeError() {
		return mx.gob.se.rug.constants.Constants.CODE_EXCEPTION_GARANTIA_REPETIDA;
	}

	public GarantiaRepetidaException() {
		// TODO Auto-generated constructor stub
	}

	public GarantiaRepetidaException(Integer codeError, String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	public GarantiaRepetidaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public GarantiaRepetidaException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public GarantiaRepetidaException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
