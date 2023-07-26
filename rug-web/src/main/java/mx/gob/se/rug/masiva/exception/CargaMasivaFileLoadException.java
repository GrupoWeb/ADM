package mx.gob.se.rug.masiva.exception;

import mx.gob.se.rug.constants.ErrorRug;

public class CargaMasivaFileLoadException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codeError;
	

	public CargaMasivaFileLoadException(Integer codeError){
		super(ErrorRug.getErrorDesc(codeError));
		setCodeError(codeError);
	}


	public Integer getCodeError() {
		return codeError;
	}


	public void setCodeError(Integer codeError) {
		this.codeError = codeError;
	}

	
}
