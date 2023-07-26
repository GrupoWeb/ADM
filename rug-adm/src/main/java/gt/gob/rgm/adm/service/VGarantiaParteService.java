package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.model.VGarantiaParte;

public interface VGarantiaParteService {
    List<VGarantiaParte> findByTramite(Long idTramite, Long idGarantia);
}
