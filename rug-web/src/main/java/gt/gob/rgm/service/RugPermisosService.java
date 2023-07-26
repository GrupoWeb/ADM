package gt.gob.rgm.service;

import gt.gob.rgm.model.RugGarantiasPermiso;
import gt.gob.rgm.model.RugPrivilegio;
import gt.gob.rgm.model.RugRelGrupoAcreedor;
import gt.gob.rgm.model.RugRelGrupoPrivilegio;
import gt.gob.rgm.util.ReporteTramites;

import java.util.List;

public interface RugPermisosService {

    List<RugRelGrupoPrivilegio> findByUser(Long idUsuario);

    Integer deletePermisoById(Long idRelacion, String sitRelacion);

    Long getIdRolByUser(Long idUsuario);

    List<RugPrivilegio> getPermisosMenu();

    String setOperacionPermiso(Long idAcreedor,Long idOperacion);

    Iterable<RugGarantiasPermiso> getGarantiaPermisos(Integer idPersona);

    Boolean setGarantiaPermisoByUser(Integer idGarantia, Integer idUsuario);

    Boolean desabilitarGarantia(Integer idGarantia, Integer idUsuario);

    boolean validarGarantiaPertenencia(Integer idGarantia, Integer idUsuario);

    String usuariosGarantia(Integer idUsuario);

    Integer usuarioPadre(Integer idUsuario);


}
