package gt.gob.rgm.adm.service;

import gt.gob.rgm.adm.model.RugCertificaciones;

public interface RugCertificacionesService {
    RugCertificaciones findByTramite(Long idTramite);
}
