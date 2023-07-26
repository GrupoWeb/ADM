/*
 * AcreedoresCatalogosDao.java        06/08/2010
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
package mx.gob.se.rug.acreedores.dao;

import java.util.List;

import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;
import mx.gob.se.rug.acreedores.to.PerfilTO;
import mx.gob.se.rug.dto.PersonaFisica;

/**
 * @author Alfonso Esquivel
 * 
 */

public interface AcreedoresCatalogosDao {
	public List<PerfilTO> getPerfiles() throws JdbcDaoException;

	public List<GrupoPerfilTO> getGrupos(int idPersona) throws JdbcDaoException;

	public List<PersonaFisica> getUsuariosAfiliados(Integer idUsuarioPadre)
			throws JdbcDaoException;

	public List<PerfilTO> getUsuariosPerfiles(String perfil) throws JdbcDaoException;
	
	public PersonaFisica getRegistro(String claveUsuario)throws JdbcDaoException;
	
	public List<PerfilTO> busquedaGrupo(PersonaFisica personaFisica) throws JdbcDaoException;

	public GrupoPerfilTO getGrupo(int idGrupo) throws JdbcDaoException;
	
	public PersonaFisica getRegistroByToken(String token) throws JdbcDaoException;	

}
