package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.VGarantiaParteRepository;
import gt.gob.rgm.adm.model.VGarantiaParte;

@Stateless
public class VGarantiaParteServiceImp implements VGarantiaParteService {
	
	@Inject
	private VGarantiaParteRepository garantiaParteDao;

	@Override
	public List<VGarantiaParte> findByTramite(Long idTramite, Long idGarantia) {
		return garantiaParteDao.findByTramite(idTramite, idGarantia);
	}
}
