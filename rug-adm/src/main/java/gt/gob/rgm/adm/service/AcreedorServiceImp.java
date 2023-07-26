package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;


import gt.gob.rgm.adm.dao.AcreedorRepository;
import gt.gob.rgm.adm.domain.Acreedor;
import gt.gob.rgm.adm.model.VRepListadoAcreedores;
import gt.gob.rgm.adm.model.VRepListadoAcreedoresUsuario;


@Stateless
public class AcreedorServiceImp implements AcreedorService {
	
	@Inject
	private AcreedorRepository acreedorDao;

	@Override	
	public List<VRepListadoAcreedores> listAcreedores(Acreedor filter, Integer page, Integer size) {
		return acreedorDao.findWithFilter(filter, page, size);
	}
	@Override
	public VRepListadoAcreedores getAcreedor(Long idPersona) {
		return acreedorDao.findByidPersona(idPersona);
	}

	public void setBoletaDao(AcreedorRepository acreedorDao) {
		this.acreedorDao = acreedorDao;
	}

	public AcreedorRepository getBoletaDao() {
		return acreedorDao;
	}

	@Override
	
	public Long countAcreedores(Acreedor filter) {
		return acreedorDao.countWithFilter(filter);
	}
	
        @Override
	
	public Long countAcreedores_u(Acreedor filter) {
		return acreedorDao.countWithFilter_u(filter);
	}
	@Override	
	public List<VRepListadoAcreedoresUsuario> listAcreedores_u(Acreedor filter, Integer page, Integer size) {
		return acreedorDao.findWithFilter_u(filter, page, size);
	}
}
