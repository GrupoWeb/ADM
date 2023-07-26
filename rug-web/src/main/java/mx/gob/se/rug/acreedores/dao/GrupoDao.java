package mx.gob.se.rug.acreedores.dao;

import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;

public interface GrupoDao {

	public Integer saveGrupo(GrupoPerfilTO grupoPerfilTO, String perfiles,
			Integer idUsuario);

	public boolean deleteGrupo(GrupoPerfilTO grupoPerfilTO, Integer idUsuario);

}
