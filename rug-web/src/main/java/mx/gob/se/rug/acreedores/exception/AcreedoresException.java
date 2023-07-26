package mx.gob.se.rug.acreedores.exception;

import mx.gob.economia.dgi.framework.exception.ExceptionMessage;
import mx.gob.se.rug.common.exception.PortalException;

@SuppressWarnings("serial")
public class AcreedoresException extends PortalException{

	public AcreedoresException(ExceptionMessage exceptionMessage) {
		super(exceptionMessage);
	}
	
	public AcreedoresException(ExceptionMessage exceptionMessage,
			Throwable cause) {
		super(exceptionMessage, cause);
	}

}
