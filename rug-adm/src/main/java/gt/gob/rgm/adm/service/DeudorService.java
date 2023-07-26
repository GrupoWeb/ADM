package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.domain.Deudo;
import gt.gob.rgm.adm.model.VRepListadoDeudorUsuario;
import gt.gob.rgm.adm.model.VRepListadoDeudores;

public interface DeudorService {
    List<VRepListadoDeudores> listDeudores(Deudo filter, Integer page, Integer size);
    VRepListadoDeudores getDeudor(Long idPersona);
    Long countDeudores(Deudo filter);
    
    List<VRepListadoDeudorUsuario> listDeudores_u(Deudo filter, Integer page, Integer size);    
    Long countDeudores_u(Deudo filter);
}
