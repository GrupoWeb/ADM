package mx.gob.se.rug.usuario.dao;

import java.sql.Connection;
import java.util.List;

import mx.gob.se.rug.acreedores.to.UsuarioTO;

/**
 * @author Getsemani Correa
 * 
 */
public interface ControlUsuarioDao {

	public boolean saveUsuario(UsuarioTO usuarioTO, Integer idAcreedor,
			String operacion);

	public List<UsuarioTO> getUsuariosFirmados(Connection connection,
			Integer idUsuario, Integer idAcreedor);

	public List<UsuarioTO> getUsuariosNoFirmados(Connection connection,
			Integer idUsuario, Integer idAcreedor);

	public UsuarioTO getByCorreoElectronico(Connection connection,
			String correoElectronico);


	UsuarioTO getByCorreoElectronicoAcredor(Connection connection,
			String correoElectronico, String idAcreedor);

	boolean relationUsuario(UsuarioTO usuarioTO, Integer idUsuario,
			Integer idGrupo);
}
