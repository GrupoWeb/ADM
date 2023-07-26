package gt.gob.rgm.adm.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.VDetalleGarantiaRepository;
import gt.gob.rgm.adm.model.VDetalleGarantia;

@Stateless
public class VDetalleGarantiaServiceImp implements VDetalleGarantiaService {
	
	@Inject
	private VDetalleGarantiaRepository detalleGarantiaDao;

	@Override
	public VDetalleGarantia findByTramite(Long idTramite, Long idGarantia) {
		return detalleGarantiaDao.findByTramite(idTramite, idGarantia);
	}
}
