package gt.gob.rgm.adm.service;

import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.domain.Tramites;
import gt.gob.rgm.adm.model.RugPersonasFisicas;
import gt.gob.rgm.adm.model.VTramitesPagados;

import java.util.List;

public interface TramitesService {

    List<VTramitesPagados> reporteTramite(Tramites tramite);

    List<RugPersonasFisicas> ListUserAll();

}
