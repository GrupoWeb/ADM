/*
 * ExceptionDwrInterceptor.java        27/11/2009
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
package mx.gob.se.rug.fwk.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mx.gob.economia.dgi.framework.exception.ExceptionLogger;
import mx.gob.economia.dgi.framework.exception.ExceptionMessage;
import mx.gob.economia.dgi.framework.exception.ExceptionMessageHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

/**
 * @author Topiltzin Dominguez
 * 
 */
public class ExceptionDwrInterceptor implements AjaxFilter {

	private Log logger = LogFactory.getLog(getClass());

	@Override
	public Object doFilter(Object obj, Method method, Object[] params,
			AjaxFilterChain chain) throws Exception {

		try {
			return chain.doFilter(obj, method, params);
		} catch (Exception e) {
			// TODO Checar como quitar esto.
			if(e instanceof InvocationTargetException){
				logger.error("Es instancia de InvocationTargetException.");
				InvocationTargetException invocationTargetException = (InvocationTargetException) e;
				e = (Exception) invocationTargetException.getCause();
				logger.error("Se convirtio la excepcion.");
			}
			ExceptionMessage exceptionMessage = ExceptionMessageHandler
					.getExceptionMessage(e);
			ExceptionLogger exceptionLogger = new ExceptionLogger(
					exceptionMessage, e);
			exceptionLogger.log();
			throw new Exception(exceptionMessage.getCause());
		}
	}

}
