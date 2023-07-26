package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import gt.gob.rgm.adm.dao.RugRelTramGaranRepository;
import gt.gob.rgm.adm.model.RugRelTramGaran;


@Stateless
public class RugRelTramGaranServiceImp implements RugRelTramGaranService {
	
	@Inject
	private RugRelTramGaranRepository tramGaranDao;

	@Override
	public RugRelTramGaran findByTramite(Long idTramite) {
		try {
                        
			return tramGaranDao.findByTramite(idTramite);
                        
		} catch(NoResultException nre) {
			return null;
		}
	}
	
	@Override
	public List<RugRelTramGaran> findByGarantia(Long idGarantia) {
		return tramGaranDao.findByGarantia(idGarantia);
	}
}
