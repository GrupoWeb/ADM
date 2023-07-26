package mx.gob.se.rug.acreedores.service.impl;

import java.util.logging.Level;

import mx.gob.economia.dgi.framework.base.service.AbstractBaseService;
import mx.gob.economia.dgi.framework.exception.ExceptionMessage;
import mx.gob.se.rug.acreedores.dao.GrupoDao;
import mx.gob.se.rug.acreedores.exception.AcreedoresException;
import mx.gob.se.rug.acreedores.service.GrupoService;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;
import mx.gob.se.rug.util.MyLogger;

public class GrupoServiceImpl extends AbstractBaseService implements GrupoService {

	
	private GrupoDao grupoDao;
	
	public void setGrupoDao(GrupoDao grupoDao) {
		this.grupoDao = grupoDao;
	}
	
	@Override
	public Integer saveGrupo(GrupoPerfilTO grupoPerfilTO, String perfiles, Integer idUsuario) throws AcreedoresException {
		try{
			return grupoDao.saveGrupo(grupoPerfilTO, perfiles, idUsuario);
		}catch(Exception e){
			throw new AcreedoresException(new ExceptionMessage("Error al guardar el Grupo. "),e);
		}
		
	}

	@Override
	public boolean deleteGrupo(GrupoPerfilTO grupoPerfilTO, Integer idUsuario) throws AcreedoresException {
		try{
			MyLogger.Logger.log(Level.INFO, "ZXZX=" + grupoPerfilTO);
			MyLogger.Logger.log(Level.INFO, "ZXZX=" + idUsuario);
			return grupoDao.deleteGrupo(grupoPerfilTO, idUsuario);
		}catch(Exception e){
			e.printStackTrace();
			throw new AcreedoresException(new ExceptionMessage("Error al eliminar el Grupo."),e);
		}
		
	}
	

}
