/*
 * TuEmpresaBaseJdbcDao.java        11/11/2009
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

package mx.gob.se.rug.fwk.dao.spring;

import java.util.Locale;

import mx.gob.economia.dgi.framework.security.user.dto.User;
import mx.gob.economia.dgi.framework.spring.jdbc.AbstractBaseJdbcDao;

/**
 * Clase base para las implementaciones de los DAO's de tipo Jdbc del proyecto
 * tuempresa.
 * 
 * @author Alfonso Esquivel
 * @version 0.1
 * 
 */
public abstract class RugBaseJdbcDao extends AbstractBaseJdbcDao {
	private User user;

	/**
	 * @return the user
	 */
	protected User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	protected Locale getUserLocale() {
		return new Locale("es");
	}

}
