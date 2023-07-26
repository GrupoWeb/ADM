package gt.gob.rgm.adm.service;

import java.util.List;


import gt.gob.rgm.adm.model.RugRelTramGaran;

public interface RugRelTramGaranService {
    RugRelTramGaran findByTramite(Long idTramite);
    
    List<RugRelTramGaran> findByGarantia(Long idGarantia);
}
