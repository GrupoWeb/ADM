/*
 * PortalException.java        01/05/2009
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140, 
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y de uso exclusivo de la
 * Secretaría de Economía.
 *
 */

package mx.gob.se.rug.common.exception;

import mx.gob.economia.dgi.framework.base.exception.BaseBusinessException;
import mx.gob.economia.dgi.framework.exception.ExceptionMessage;

/**
 * 
 * 
 * 
 * @version 0.1
 * @author Alfonso Esquivel
 * 
 */
@SuppressWarnings("serial")
public class PortalException extends BaseBusinessException {

	/**
	 * @param exceptionMessage
	 * @param cause
	 */
	public PortalException(ExceptionMessage exceptionMessage, Throwable cause) {
		super(exceptionMessage, cause);
	}

	/**
	 * @param exceptionMessage
	 */
	public PortalException(ExceptionMessage exceptionMessage) {
		super(exceptionMessage);
	}

}
