package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.model.RugGarantiasBienesH;

public interface RugGarantiasBienesHService {
    List<RugGarantiasBienesH> findByTramite(Long idTramite);
}
