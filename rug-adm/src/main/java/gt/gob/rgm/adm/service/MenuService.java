package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.domain.Menu;
import gt.gob.rgm.adm.model.Menus;

public interface MenuService {
    List<Menus> listMenus( Integer page, Integer size);
    List<Menus> updateMenus( Integer page, Integer size, Menu filtro);
    List<Menus> crearMenus( Integer page, Integer size, Menu filtro);
    List<Menus> delCMenus( Menu filtro);
    List<Menus> delMenus( Menu filtro);
    
    //Menus getMenu(Integer idMenu);
    Long countMenus();     
}
