package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;


import gt.gob.rgm.adm.dao.DeudorRepository;
import gt.gob.rgm.adm.domain.Deudo;
import gt.gob.rgm.adm.model.VRepListadoDeudorUsuario;
import gt.gob.rgm.adm.model.VRepListadoDeudores;


@Stateless
public class DeudorServiceImp implements DeudorService {
	
	@Inject
	private DeudorRepository boletaDao;

	@Override	
	public List<VRepListadoDeudores> listDeudores(Deudo filter, Integer page, Integer size) {
		return boletaDao.findWithFilter(filter, page, size);
	}

	
	@Override
	public VRepListadoDeudores getDeudor(Long idPersona) {
		return boletaDao.findByidPersona(idPersona);
	}

	public void setBoletaDao(DeudorRepository boletaDao) {
		this.boletaDao = boletaDao;
	}

	public DeudorRepository getBoletaDao() {
		return boletaDao;
	}

	@Override
	
	public Long countDeudores(Deudo filter) {
		return boletaDao.countWithFilter(filter);
	}
	
        @Override	
	public List<VRepListadoDeudorUsuario> listDeudores_u(Deudo filter, Integer page, Integer size) {
		return boletaDao.findWithFilter_u(filter, page, size);
	}
	
        @Override
	
	public Long countDeudores_u(Deudo filter) {
		return boletaDao.countWithFilter_u(filter);
	}
	
}
