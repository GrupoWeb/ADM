package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;


import gt.gob.rgm.adm.dao.GarantiaRepository;
import gt.gob.rgm.adm.domain.Garantia;
import gt.gob.rgm.adm.model.VRepListadoGarantias;


@Stateless
public class GarantiaServiceImp implements GarantiaService {
	
	@Inject
	private GarantiaRepository garantiaDao;

	@Override	
	public List<VRepListadoGarantias> listGarantias(Garantia filter, Integer page, Integer size) {
		return garantiaDao.findWithFilter(filter, page, size);
	}

	@Override	
	public Long countGarantias(Garantia filter) {
		return garantiaDao.countWithFilter(filter);
	}
        
	
	public void setGarantiaDao(GarantiaRepository garantiaDao) {
		this.garantiaDao = garantiaDao;
	}

	public GarantiaRepository getGarantiaDao() {
		return garantiaDao;
	}

	
	
        @Override	
	public List<VRepListadoGarantias> listGarantiasAcr(Garantia filter, Integer page, Integer size) {
		return garantiaDao.findWithFilterAsc(filter, page, size);
	}
        @Override
	
	public Long countGarantiasAcr(Garantia filter) {
		return garantiaDao.countWithFilterAsc(filter);
	}
	
}
