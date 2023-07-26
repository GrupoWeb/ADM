package gt.gob.rgm.service;

import gt.gob.rgm.dao.RugGarantiasBienesPendRepository;
import gt.gob.rgm.dao.RugGarantiasRespository;
import gt.gob.rgm.model.RugGarantias;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Scope("prototype")
public class RugGarantiaServiceImp implements RugGarantiasService{
    private RugGarantiasRespository garantiaDao;

    public void setGarantiaDao(RugGarantiasRespository garantiaDao) {
        this.garantiaDao = garantiaDao;
    }


    public List<RugGarantias> findTypeGarantia(Integer idGarantia){
        return garantiaDao.findTypeGarantia(idGarantia);
    }


}
