package gt.gob.rgm.service;

import java.util.List;

import gt.gob.rgm.domain.Usuario;
import gt.gob.rgm.model.BitacoraOperaciones;
import gt.gob.rgm.model.RugGrupoSubUsuarios;
import gt.gob.rgm.util.FormUsuarios;

public interface UsuariosService {
	Boolean permisosDinamicos(Long idPersona, String Nombre, String isAdmin);

	Usuario add(Usuario usuario);
	
	int activar(Long id, Long idAcreedor, Long idGrupo);
	
	int update(Long id, Usuario usuario);
	
	int updateState(Long id, String state);
	
	List<Usuario> getSubusuarios(Long id);
	
	Usuario getUsuario(Long id);

	int verificarIsAdmin(FormUsuarios usuarios);
	
	Usuario getUsuarioExterno(String cveUsuario);
	
	void addBitacora(BitacoraOperaciones bitacora);

	Usuario GenerateGrupo(Long IdPersona, String name);

	RugGrupoSubUsuarios SetGrupoSubUsuario(Long idPersona, Integer idGrupo);
	RugGrupoSubUsuarios GetGrupoSubUsuario(Long idPersona);

}
