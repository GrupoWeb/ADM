package gt.gob.rgm.adm.service;

import gt.gob.rgm.adm.model.VDetalleGarantia;

public interface VDetalleGarantiaService {
    VDetalleGarantia findByTramite(Long idTramite, Long idGarantia);
}
