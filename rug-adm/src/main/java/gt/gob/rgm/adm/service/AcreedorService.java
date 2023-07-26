package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.domain.Acreedor;
import gt.gob.rgm.adm.model.VRepListadoAcreedores;
import gt.gob.rgm.adm.model.VRepListadoAcreedoresUsuario;

public interface AcreedorService {
    /*Devuelve todos los acreedores paginado*/
    List<VRepListadoAcreedores> listAcreedores(Acreedor filter, Integer page, Integer size);
    Long countAcreedores(Acreedor filter);
    /*Devuelve un acreedor*/
    List<VRepListadoAcreedoresUsuario> listAcreedores_u(Acreedor filter, Integer page, Integer size);
    Long countAcreedores_u(Acreedor filter);
    /*Devuelve todos los acreedores sin paginado*/
  
    VRepListadoAcreedores getAcreedor(Long idPersona);
    
    
    
}
