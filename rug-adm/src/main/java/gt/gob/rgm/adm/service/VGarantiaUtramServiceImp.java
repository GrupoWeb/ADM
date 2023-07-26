package gt.gob.rgm.adm.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.VGarantiaUtramRepository;
import gt.gob.rgm.adm.model.VGarantiaUtram;

@Stateless
public class VGarantiaUtramServiceImp implements VGarantiaUtramService {
	
	@Inject
	private VGarantiaUtramRepository garantiaUtramDao;

	@Override
	public VGarantiaUtram findByTramite(Long idTramite, Long idGarantia) {
		return garantiaUtramDao.findByTramite(idTramite, idGarantia);
	}
}
