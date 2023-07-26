package gt.gob.rgm.adm.service;

import gt.gob.rgm.adm.model.RugPersonas;

public interface RugPersonasService {
    RugPersonas getPersona(Long id);
    
    long getNextRegistry(String perJuridica);
    
    int updateRegistro(Long id, long codigoRegistro);
}
