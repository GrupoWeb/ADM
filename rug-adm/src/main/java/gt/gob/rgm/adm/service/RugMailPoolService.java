package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.model.RugMailPool;

public interface RugMailPoolService {
    List<RugMailPool> listMailUsuario(String emailUsuario, Long estado);
    
    Long countMailUsuario(String emailUsuario, Long estado);

    RugMailPool updateEstado(Long id, Long estado);
    
    RugMailPool find(Long id);
}
