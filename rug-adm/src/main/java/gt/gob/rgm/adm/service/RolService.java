package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.domain.Rol;
import gt.gob.rgm.adm.model.Roles;

public interface RolService {
    List<Roles> listRoles();
    //Menus getMenu(Integer idMenu);
    Long countRoles();     
    List<Roles> crearRoles( Integer page, Integer size, Rol filtro);
    List<Roles> delRol( Rol filtro);
    
}
