package gt.gob.rgm.adm.service;

import gt.gob.rgm.adm.model.VGarantiaUtram;

public interface VGarantiaUtramService {
    VGarantiaUtram findByTramite(Long idTramite, Long idGarantia);
}
