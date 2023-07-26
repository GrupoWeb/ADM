package gt.gob.rgm.adm.service;

import java.util.List;

import gt.gob.rgm.adm.domain.ExternalUser;
import gt.gob.rgm.adm.domain.SecuUsuarioStats;
import gt.gob.rgm.adm.model.GenericCount;
import gt.gob.rgm.adm.model.RugPersonasFisicas;
import gt.gob.rgm.adm.model.RugSecuUsuario;

public interface RugSecuUsuarioService {


	List<RugSecuUsuario> listUsuarios(ExternalUser filter, Integer page, Integer size);
	
    List<RugSecuUsuario> listUsuarios(String estado, Integer page, Integer size);
    
    List<RugSecuUsuario> listAllUsuarios(Integer page, Integer size);
    
    List<RugSecuUsuario> listUsuariosNoMigracion(Integer page, Integer size);
    
    List<GenericCount> summaryUsuarios(String fechaInicio, String fechaFin, Boolean migracion);
    
    RugSecuUsuario getUsuario(Long id);
    
    int update(Long id, String estado);
    
    Long countUsuarios(ExternalUser filter);
    
    Long countUsuarios(String estado);
    
    Long countAllUsuarios();
    
    Long countUsuariosNoMigracion();
    
    List<SecuUsuarioStats> statsSecuUsuarios(String fechaInicio, String fechaFin, String cfecha, String fields, Integer diaInicioSemana, Boolean migracion);
    
    Double getSaldo(Long idPersona);
    
    Boolean modificarMigrado(Long idPersona, String causa, Long usuario);
    
    Boolean modificarCorreo(Long idPersona, String nuevoCorreo, Long usuario);
}
