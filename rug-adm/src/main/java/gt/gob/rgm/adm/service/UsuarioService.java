package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;
import gt.gob.rgm.adm.model.Usuario;

public interface UsuarioService {
    List<Usuario> listUsuarios(String estado);
    
    Usuario getUsuario(Long id);
    
    Usuario save(Usuario usuario) throws EntityAlreadyExistsException;
    
    int update(Long id, Usuario usuario);
}
