package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.model.RugBoletaPdf;

public interface RugBoletaPdfService {
    RugBoletaPdf getBoleta(Long id);
    
    List<RugBoletaPdf> getBoletasByTramite(Long idTramite);
   
    public void saveBoleta(Long idGarantia, byte[] archivo);
}
