/*
 * PerfilServicePojoImpl.java        09/11/2009
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
package mx.gob.se.rug.administracion.service.impl;

import mx.gob.economia.dgi.framework.base.service.AbstractBaseService;
import mx.gob.economia.dgi.framework.dao.exception.DaoException;
import mx.gob.economia.dgi.framework.exception.ExceptionMessage;
import mx.gob.se.rug.administracion.dao.PerfilDao;
import mx.gob.se.rug.administracion.dto.Perfil;
import mx.gob.se.rug.administracion.exception.PerfilNoActualizadoException;
import mx.gob.se.rug.administracion.exception.UsuarioException;
import mx.gob.se.rug.administracion.service.PerfilService;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.dto.PersonaFisica;

/**
 * @author Alfonso Esquivel
 * 
 */
public class PerfilServicePojoImpl extends AbstractBaseService implements
		PerfilService {

	private PerfilDao perfilDao;

	public void setPerfilDao(PerfilDao perfilDao) {
		this.perfilDao = perfilDao;
	}

	@Override
	public Mensaje update(Perfil perfil, String claveUsuario, PersonaFisica personaFisica) throws PerfilNoActualizadoException {
		try {
			return perfilDao.update(perfil, claveUsuario, personaFisica);
		} catch (DaoException daoe) {
			daoe.printStackTrace();
			throw new PerfilNoActualizadoException(new ExceptionMessage(
					"Ocurrio un error al intentar actualizar el perfil."), daoe);
		}

	}

	@Override
	public PersonaFisica getConsultaRfcUsuario(PersonaFisica personaFisica)
			throws PerfilNoActualizadoException {
		try {
			return perfilDao.getConsultaRfcUsuario(personaFisica);
		} catch (DaoException daoe) {
			daoe.printStackTrace();
			throw new PerfilNoActualizadoException(new ExceptionMessage("Ocurrio un error al intentar consultar el rfc y clave de usuario."), daoe);
		}
	}

}
