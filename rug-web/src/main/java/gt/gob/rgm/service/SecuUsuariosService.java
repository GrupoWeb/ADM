package gt.gob.rgm.service;

import gt.gob.rgm.model.RugSecuUsuarios;
import gt.gob.rgm.util.FormUsuarios;

import java.util.List;

public interface SecuUsuariosService {

    List<RugSecuUsuarios> findCveUser(FormUsuarios usuarios);
}
