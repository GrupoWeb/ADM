/*
 * HomeService.java        04/08/2010
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
package mx.gob.se.rug.common.service;

import mx.gob.economia.dgi.framework.security.user.dto.User;
import mx.gob.se.rug.common.exception.UserException;


/**
 * @author Alfonso Esquivel
 * 
 */
public interface HomeService {

	public User getUser(String principal) throws UserException;

}
