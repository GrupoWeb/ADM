package gt.gob.rgm.adm.service;

import gt.gob.rgm.adm.dao.VTramitesPagadosRepository;
import gt.gob.rgm.adm.domain.ResponseRs;
import gt.gob.rgm.adm.domain.Tramites;
import gt.gob.rgm.adm.model.RugPersonasFisicas;
import gt.gob.rgm.adm.model.VTramitesPagados;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class TramitesServiceImp implements TramitesService{

    @Inject
    private VTramitesPagadosRepository tramitesRepository;

    public List<VTramitesPagados> reporteTramite(Tramites tramite){
        return tramitesRepository.findTramiteByUser(tramite);
    };

    public List<RugPersonasFisicas> ListUserAll(){
      return tramitesRepository.getUser();
    };


}
