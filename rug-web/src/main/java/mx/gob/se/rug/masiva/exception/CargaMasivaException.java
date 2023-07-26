package mx.gob.se.rug.masiva.exception;

import java.util.Map;

import mx.gob.se.rug.constants.ErrorRug;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;

public class CargaMasivaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codeError;
	
	
	public Integer getCodeError() {
		return codeError;
	}

	public void setCodeError(Integer codeError) {
		this.codeError = codeError;
	}

	public CargaMasivaException() {
		// TODO Auto-generated constructor stub
	}

	public CargaMasivaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CargaMasivaException(Integer codeError){
		super(ErrorRug.getErrorDesc(codeError));
		setCodeError(codeError);
	}
	public CargaMasivaException(Integer codeError,String msgConcat){
		super(ErrorRug.getErrorDesc(codeError)+msgConcat);
		setCodeError(codeError);
	}
	public CargaMasivaException(Integer codeError,Map<String, String> params){
		super(getMsgParam(codeError,params));
		
		setCodeError(codeError);
	}
	
	private static String getMsgParam(Integer codeError,Map<String, String> params){
		String msg=ErrorRug.getErrorDesc(codeError);
		
		for(Map.Entry<String, String> entry:params.entrySet()){
			msg=msg.replace("@"+entry.getKey(), entry.getValue());
		}
		
		return msg;
	}
	
	public CargaMasivaException(String msg,Integer codeError){
		super(msg);
		setCodeError(codeError);
	}
	public CargaMasivaException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public CargaMasivaException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	public  TramiteRes getTramiteIncorrecto(String cveRastreo){
		TramiteRes tramite = new TramiteRes();
		tramite.setClaveRastreo(cveRastreo);
		tramite.setCodigoError(codeError.toString());
		tramite.setMensajeError(super.getMessage());
		return tramite;
	}

}
