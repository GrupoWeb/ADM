package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;


import gt.gob.rgm.adm.dao.RolRepository;
import gt.gob.rgm.adm.domain.Rol;
import gt.gob.rgm.adm.model.Roles;


@Stateless
public class RolServiceImp implements RolService{
	
	@Inject
	private RolRepository rolDao;

	@Override	
	public List<Roles> listRoles() {
		return rolDao.findWithFilter();
	}

        @Override
        public List<Roles> crearRoles( Integer page, Integer size, Rol filtro) {
		return rolDao.crearWithFilter( page, size,filtro);
	}
        
         @Override
        public List<Roles> delRol( Rol filtro) {
		return rolDao.delWithFilter( filtro);
	}
        @Override	
	public Long countRoles() {
		return rolDao.countWithFilter();
	}
        
        public void setRolDao(RolRepository rolDao) {
		this.rolDao = rolDao;
	}

	public RolRepository getRolDao() {
		return rolDao;
	}

   
	
}
