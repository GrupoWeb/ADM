/*
 * AcreedoresCatalogosService.java        06/08/2010
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
package mx.gob.se.rug.acreedores.service;

import java.util.List;

import mx.gob.se.rug.acreedores.exception.AcreedoresException;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;
import mx.gob.se.rug.acreedores.to.PerfilTO;
import mx.gob.se.rug.dto.PersonaFisica;

/**
 * @author Alfonso Esquivel
 * 
 */
public interface AcreedoresCatalogosService {
	
	public List<PerfilTO> getPerfiles() throws AcreedoresException;

	public List<GrupoPerfilTO> getGrupos(int idPersona)
			throws AcreedoresException;

	public List<PersonaFisica> getUsuariosAfiliados(Integer idUsuarioPadre)
			throws AcreedoresException;
	
	public List<PerfilTO> getUsuariosPerfiles(String perfil)throws AcreedoresException;  
	
	public PersonaFisica getRegistro(String claveUsuario)throws AcreedoresException;
	
	public List<PerfilTO> busquedaGrupo(PersonaFisica personaFisica) throws AcreedoresException;
	
	public PersonaFisica getRegistroByToken(String token) throws AcreedoresException;	

}
