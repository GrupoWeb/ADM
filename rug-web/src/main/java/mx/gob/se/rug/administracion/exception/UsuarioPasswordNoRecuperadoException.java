/**
 * 
 */
package mx.gob.se.rug.administracion.exception;

import mx.gob.economia.dgi.framework.exception.ExceptionMessage;
import mx.gob.se.rug.common.exception.PortalException;

/**
 * @author Erika Astorga
 *
 */
@SuppressWarnings("serial")
public class UsuarioPasswordNoRecuperadoException extends PortalException {

	public UsuarioPasswordNoRecuperadoException(ExceptionMessage exceptionMessage) {
		super(exceptionMessage);
	}
	
	public UsuarioPasswordNoRecuperadoException(ExceptionMessage exceptionMessage,
			Throwable cause) {
		super(exceptionMessage, cause);
	}

}
