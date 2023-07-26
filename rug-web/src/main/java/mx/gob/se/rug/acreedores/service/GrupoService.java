package mx.gob.se.rug.acreedores.service;

import mx.gob.se.rug.acreedores.exception.AcreedoresException;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;

public interface GrupoService {

	public Integer saveGrupo(GrupoPerfilTO grupoPerfilTO, String perfiles,
			Integer idUsuario) throws AcreedoresException;

	public boolean deleteGrupo(GrupoPerfilTO grupoPerfilTO, Integer idUsuario)
			throws AcreedoresException;

}
