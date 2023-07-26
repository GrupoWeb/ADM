package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;


import gt.gob.rgm.adm.dao.Usuario_Repository;
import gt.gob.rgm.adm.domain.Usuario;
import gt.gob.rgm.adm.model.VRepListadoUsuarios;


@Stateless
public class Usuario_ServiceImp implements Usuario_Service {
	
	@Inject
	private Usuario_Repository boletaDao;

	@Override	
	public List<VRepListadoUsuarios> listUsuarios(Usuario filter, Integer page, Integer size) {
		return boletaDao.findWithFilter(filter, page, size);
	}

	
	@Override
	public VRepListadoUsuarios getUsuario(Long idPersona) {
		return boletaDao.findByidPersona(idPersona);
	}

	public void setBoletaDao(Usuario_Repository boletaDao) {
		this.boletaDao = boletaDao;
	}

	public Usuario_Repository getBoletaDao() {
		return boletaDao;
	}

	@Override
	
	public Long countUsuarios(Usuario filter) {
		return boletaDao.countWithFilter(filter);
	}
	
        @Override	
	public List<VRepListadoUsuarios> listUsuarios_u(Usuario filter, Integer page, Integer size) {
		return boletaDao.findWithFilter_u(filter, page, size);
	}
	
        @Override
	
	public Long countUsuarios_u(Usuario filter) {
		return boletaDao.countWithFilter_u(filter);
	}
	
}
