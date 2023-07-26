package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.domain.Usuario;
import gt.gob.rgm.adm.model.VRepListadoUsuarios;

public interface Usuario_Service {
    List<VRepListadoUsuarios> listUsuarios(Usuario filter, Integer page, Integer size);
    VRepListadoUsuarios getUsuario(Long idPersona);
    Long countUsuarios(Usuario filter);
    
    List<VRepListadoUsuarios> listUsuarios_u(Usuario filter, Integer page, Integer size);    
    Long countUsuarios_u(Usuario filter);
}
