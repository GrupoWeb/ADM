/*
 * HttpErrorHandler.java        19/01/2009
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
package mx.gob.se.rug.common.action;

import mx.gob.economia.dgi.framework.struts2.action.AbstractBaseAction;

/**
 * Clase que maneja los errores m&aacute;s com&uacute;nes de HTTP.<br>
 * Se agrupan en:<br>
 * <ul>
 * <li>Errores de petici&oacute;n
 * <ul>
 * <li>400</li>
 * <li>404</li>
 * <li>406</li>
 * <li>408</li>
 * <li>414</li>
 * </ul>
 * </li>
 * <li>Errores de autorizaci&oacute;n
 * <ul>
 * <li>403</li>
 * <li>407</li>
 * </ul>
 * </li>
 * <li>Errores del servidor
 * <ul>
 * <li>500</li>
 * <li>501</li>
 * <li>503</li>
 * </ul>
 * </li>
 * <li>Errores del gateway
 * <ul>
 * <li>502</li>
 * <li>504</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @version 0.1
 * @author Erika Astorga
 * 
 */
@SuppressWarnings("serial")
public class HttpErrorHandlerAction extends AbstractBaseAction {

	private String messageKey;

	/**
	 * @return the messageKey
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * @param messageKey
	 *            the messageKey to set
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String badRequestHandler() {
		logger.debug("-- badRequestHandler --");
		logger.debug("messageKey: " + messageKey);
		setMessageKey(getText("bad.request"));
		return SUCCESS;
	}

	public String authorizationHandler() {
		logger.debug("-- authorizationHandler --");
		logger.debug("messageKey: " + messageKey);
		setMessageKey(getText("not.authorized"));
		return SUCCESS;
	}

	public String serverErrorHandler() {
		logger.debug("-- serverErrorHandler --");
		logger.debug("messageKey: " + messageKey);
		setMessageKey(getText("server.error"));
		return SUCCESS;
	}

	public String gatewayServerHandler() {
		logger.debug("-- gatewayServerHandler --");
		logger.debug("messageKey: " + messageKey);
		setMessageKey(getText("gateway.server"));
		return SUCCESS;
	}

}
