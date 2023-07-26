package mx.gob.se.rug.acreedores.service.impl;

import java.util.List;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.base.service.AbstractBaseService;
import mx.gob.economia.dgi.framework.dao.exception.DaoException;
import mx.gob.economia.dgi.framework.exception.ExceptionMessage;
import mx.gob.se.rug.acreedores.dao.AcreedoresCatalogosDao;
import mx.gob.se.rug.acreedores.exception.AcreedoresException;
import mx.gob.se.rug.acreedores.service.AcreedoresCatalogosService;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;
import mx.gob.se.rug.acreedores.to.PerfilTO;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.util.MyLogger;

public class AcreedoresCatalogosServiceImpl extends AbstractBaseService implements AcreedoresCatalogosService{
	
	private AcreedoresCatalogosDao acreedoresCatalogosDao;
	
	public void setAcreedoresCatalogosDao(AcreedoresCatalogosDao acreedoresCatalogosDao) {
		this.acreedoresCatalogosDao = acreedoresCatalogosDao;
	}
	
	@Override
	public List<PerfilTO> getPerfiles() throws AcreedoresException {
		try{
			MyLogger.Logger.log(Level.INFO, "Metodo: getProcesos");
			return acreedoresCatalogosDao.getPerfiles();
			
		} catch (DaoException daoe) {
			throw new AcreedoresException(new ExceptionMessage("Error al obtener la lista Perfiles."), daoe);
		}
	}

	@Override
	public List<GrupoPerfilTO> getGrupos(int idPersona) throws AcreedoresException {
		try{
			return acreedoresCatalogosDao.getGrupos(idPersona);
	
		}catch (DaoException geo) {
			throw new AcreedoresException(new ExceptionMessage("Error al obtener la lista de grupos."), geo);
		}
	}

	@Override
	public List<PersonaFisica> getUsuariosAfiliados(Integer idUsuarioPadre)
			throws AcreedoresException {
		try{
			return acreedoresCatalogosDao.getUsuariosAfiliados(idUsuarioPadre);
	
		}catch (DaoException uaeo) {
			throw new AcreedoresException(new ExceptionMessage("Error al obtener la lista usuarios afiliados."), uaeo);
		}
		
	}

	@Override
	public List<PerfilTO> getUsuariosPerfiles(String perfil) throws AcreedoresException {
		try{
			return acreedoresCatalogosDao.getUsuariosPerfiles(perfil);
	
		}catch (DaoException upeo) {
			throw new AcreedoresException(new ExceptionMessage("Error al obtener la lista perfiles de usuarios."), upeo);
		}
	}

	@Override
	public PersonaFisica getRegistro(String claveUsuario)
			throws AcreedoresException {
		try{
			return acreedoresCatalogosDao.getRegistro(claveUsuario);
	
		}catch (DaoException uaeo) {
			throw new AcreedoresException(new ExceptionMessage("Error al obtener el registro del usuario."), uaeo);
		}
	}

	@Override
	public List<PerfilTO> busquedaGrupo(PersonaFisica personaFisica)
			throws AcreedoresException {
		try {
			MyLogger.Logger.log(Level.INFO, "--Metodo busqueda de grupos--");
			return acreedoresCatalogosDao.busquedaGrupo(personaFisica);
		} catch (Exception e) {
			throw new AcreedoresException(new ExceptionMessage(
					"Error al obtener la lista de perfiles"), e);
		}
	}

	@Override
	public PersonaFisica getRegistroByToken(String token) throws AcreedoresException {
		try{
			return acreedoresCatalogosDao.getRegistroByToken(token);
	
		}catch (DaoException uaeo) {
			throw new AcreedoresException(new ExceptionMessage("Error al obtener el registro del usuario."), uaeo);
		}
	}

	

	

}
