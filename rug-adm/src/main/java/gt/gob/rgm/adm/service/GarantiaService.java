package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.domain.Garantia;
import gt.gob.rgm.adm.model.VRepListadoGarantias;

public interface GarantiaService {
    List<VRepListadoGarantias> listGarantias(Garantia filter, Integer page, Integer size);    
    Long countGarantias(Garantia filter);
    
    List<VRepListadoGarantias> listGarantiasAcr(Garantia filter, Integer page, Integer size);
    Long countGarantiasAcr(Garantia filter);
}
