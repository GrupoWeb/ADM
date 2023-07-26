package gt.gob.rgm.adm.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import gt.gob.rgm.adm.dao.RugCertificacionesRepository;
import gt.gob.rgm.adm.model.RugCertificaciones;

@Stateless
public class RugCertificacionesServiceImp implements RugCertificacionesService {
	
	@Inject
	private RugCertificacionesRepository certificacionesDao;

	@Override
	public RugCertificaciones findByTramite(Long idTramite) {
		try {
			return certificacionesDao.findByTramite(idTramite);
		} catch(NoResultException nre) {
			return null;
		}
	}
}
