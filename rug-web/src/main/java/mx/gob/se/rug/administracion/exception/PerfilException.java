/*
 * PerfilException.java        20/11/2009
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140, 
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y contiene información perteneciente
 * a la Secretaría de Economía.
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
public class PerfilException extends PortalException {

	public PerfilException(ExceptionMessage exceptionMessage) {
		super(exceptionMessage);
	}

	public PerfilException(ExceptionMessage exceptionMessage, Throwable cause) {
		super(exceptionMessage, cause);
	}

}
