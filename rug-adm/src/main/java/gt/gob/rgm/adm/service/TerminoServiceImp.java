package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.ClasificacionTerminoRepository;
import gt.gob.rgm.adm.model.ClasificacionTermino;

@Stateless
public class TerminoServiceImp {
	@Inject
	ClasificacionTerminoRepository terminoDao;
	
	public List<ClasificacionTermino> getTerminos(Boolean sinClasificar, Integer page, Integer size) {
		if(sinClasificar != null && sinClasificar) {
			return terminoDao.findNotClassified(page, size);
		}
		return terminoDao.findAll(page, size);
	}
	
	public Long countTerminos(Boolean sinClasificar) {
		if(sinClasificar != null && sinClasificar) {
			return terminoDao.countNotClassified();
		}
		return terminoDao.countAll();
	}
	
	public ClasificacionTermino create(ClasificacionTermino termino) {
		return terminoDao.save(termino);
	}
	
	public ClasificacionTermino getById(Long clasificacionTerminoId) {
		return terminoDao.findById(clasificacionTerminoId);
	}
	
	public ClasificacionTermino update(Long clasificacionTerminoId, ClasificacionTermino termino) {
		ClasificacionTermino terminoAnterior = terminoDao.findById(clasificacionTerminoId);
		if(terminoAnterior != null) {
			terminoAnterior.setCategoriaId(termino.getCategoriaId());
			terminoAnterior.setAplicado(0);
		}
		return terminoAnterior;

	}
}
