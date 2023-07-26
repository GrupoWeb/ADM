package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.RugGarantiasBienesHRepository;
import gt.gob.rgm.adm.model.RugGarantiasBienesH;

@Stateless
public class RugGarantiasBienesHServiceImp implements RugGarantiasBienesHService {
	
	@Inject
	private RugGarantiasBienesHRepository garantiasBienesDao;

	@Override
	public List<RugGarantiasBienesH> findByTramite(Long idTramite) {
		return garantiasBienesDao.findByTramite(idTramite);
	}
}
