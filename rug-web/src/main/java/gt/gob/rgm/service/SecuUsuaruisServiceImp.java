package gt.gob.rgm.service;

import gt.gob.rgm.dao.RugSecuUsuariosRepository;
import gt.gob.rgm.model.RugSecuUsuarios;
import gt.gob.rgm.util.FormUsuarios;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Scope("prototype")
public class SecuUsuaruisServiceImp implements SecuUsuariosService{

    private RugSecuUsuariosRepository usuariosRepository;

    public void setUsuariosRepository(RugSecuUsuariosRepository usuariosRepository){ this.usuariosRepository =  usuariosRepository;}

    public List<RugSecuUsuarios> findCveUser(FormUsuarios usuarios){
        String rusUser = usuariosRepository.cveUsuario(usuarios.getIdUsuario());
        return usuariosRepository.countIsAdmin(rusUser);
    }

}
