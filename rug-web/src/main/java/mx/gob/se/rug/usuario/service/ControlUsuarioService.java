package mx.gob.se.rug.usuario.service;

import java.util.List;

import mx.gob.se.rug.acreedores.to.UsuarioTO;

public interface ControlUsuarioService {

	public boolean estanRelacionados(Integer idPersona, Integer idAcreedor);

	public boolean saveUsuario(UsuarioTO usuarioTO, Integer idPersona,
			String operacion);

	public List<UsuarioTO> getListaUsuariosFirmados(Integer idPersona,
			Integer idAcreedor);

	public List<UsuarioTO> getListaUsuariosNoFirmados(Integer idPersona,
			Integer idAcreedor);

	public UsuarioTO getByCorreoElectronico(String correoElectronico);

	boolean relationUsuario(UsuarioTO usuarioTO, Integer idPersonaHijo, Integer idGrupos);

	UsuarioTO getByCorreoElectronicoAcreedor(String correoElectronico,
			String idAcreedor);
}
