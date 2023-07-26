/**
 * 
 */
package mx.gob.se.rug.administracion.exception;

import mx.gob.economia.dgi.framework.exception.ExceptionMessage;
import mx.gob.se.rug.common.exception.PortalException;

/**
 * @author Alfonso Esquivel
 *
 */
@SuppressWarnings("serial")
public class UsuarioNoGuardadoException extends PortalException {

	public UsuarioNoGuardadoException(ExceptionMessage exceptionMessage) {
		super(exceptionMessage);
	}
	
	public UsuarioNoGuardadoException(ExceptionMessage exceptionMessage,
			Throwable cause) {
		super(exceptionMessage, cause);
	}
	
}
