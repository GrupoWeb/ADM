package gt.gob.rgm.adm.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;


import gt.gob.rgm.adm.dao.MenuRepository;
import gt.gob.rgm.adm.domain.Menu;
import gt.gob.rgm.adm.model.Menus;


@Stateless
public class MenuServiceImp implements MenuService{
	
	@Inject
	private MenuRepository menuDao;

	@Override	
	public List<Menus> listMenus( Integer page, Integer size) {
		return menuDao.findWithFilter( page, size);
	}
        @Override
        public List<Menus> updateMenus( Integer page, Integer size, Menu filtro) {
		return menuDao.updateWithFilter( page, size,filtro);
	}
        @Override
        public List<Menus> crearMenus( Integer page, Integer size, Menu filtro) {
		return menuDao.crearWithFilter( page, size,filtro);
	}
         @Override
        public List<Menus> delCMenus( Menu filtro) {
		return menuDao.delCWithFilter(filtro);
	}
         @Override
        public List<Menus> delMenus( Menu filtro) {
		return menuDao.delWithFilter( filtro);
	}
        @Override	
	public Long countMenus() {
		return menuDao.countWithFilter();
	}
        
        public void setMenuDao(MenuRepository menuDao) {
		this.menuDao = menuDao;
	}

	public MenuRepository getMenuDao() {
		return menuDao;
	}
	
}
