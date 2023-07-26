package gt.gob.rgm.service;

import gt.gob.rgm.dao.*;
import gt.gob.rgm.model.*;
import gt.gob.rgm.util.ReporteTramites;
import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.dao.ConexionBD;
import org.springframework.context.annotation.Scope;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Scope("prototype")
public class RugPermisosServiceImp implements RugPermisosService{

    private RugRelGrupoPrivilegioRepository privilegioDao;

    private RugRelGrupoAcreedorRepository acreedorDao;

    private RugPrivilegioRepository rugprivilegioDao;

    private RugGrupoRepository grupoDao;

    private RugGarantiasPermisosRepository garantiaPermisoDao;

    private RugGarantiasRespository garantiasDao;

    public void setGarantiasDao(RugGarantiasRespository garantiasDao) {
        this.garantiasDao = garantiasDao;
    }

    public void setPrivilegioDao(RugRelGrupoPrivilegioRepository privilegioDao) {
        this.privilegioDao = privilegioDao;
    }

    public void setAcreedorDao(RugRelGrupoAcreedorRepository acreedorDao) {
        this.acreedorDao = acreedorDao;
    }


    public void setRugprivilegioDao(RugPrivilegioRepository rugprivilegioDao) {
        this.rugprivilegioDao = rugprivilegioDao;
    }

    public void setGrupoDao(RugGrupoRepository grupoDao) {
        this.grupoDao = grupoDao;
    }

    public void setGarantiaPermisoDao(RugGarantiasPermisosRepository garantiaPermisoDao) {
        this.garantiaPermisoDao = garantiaPermisoDao;
    }

    @Override
    public List<RugRelGrupoPrivilegio> findByUser(Long idUsuario) {
        RugRelGrupoAcreedor acreedor = new RugRelGrupoAcreedor();
        acreedor.setIdGrupo(acreedorDao.findByAcreedor(idUsuario));
        List<RugRelGrupoPrivilegio> permisos = privilegioDao.findByGrupo(acreedor.getIdGrupo());
        return permisos;
    }

    @Override
    public Integer deletePermisoById(Long idRelacion, String sitRelacion) {
        return privilegioDao.inactivarRelacion(idRelacion, sitRelacion);
    }

    @Override
    public Long getIdRolByUser(Long idUsuario) {
        return acreedorDao.findByAcreedor(idUsuario);
    }

    @Override
    public List<RugPrivilegio> getPermisosMenu() {
        return rugprivilegioDao.getPermisosMenu();
    }

    @Override
    public String setOperacionPermiso(Long idAcreedor, Long idOperacion) {
        try{
            String sitNuevo;
            String grupo = grupoDao.findByUsuario(new BigDecimal(idAcreedor));

            RugRelGrupoPrivilegio grupoPermiso = privilegioDao.buscarPrivilegio(new Long(grupo), idOperacion);

            if(grupoPermiso !=  null){
                sitNuevo = grupoPermiso.getSitRelacion().equals("AC") ? "IN" : "AC";
                privilegioDao.inactivarRelacion(grupoPermiso.getIdRelacion(), sitNuevo);
            }else{
                RugRelGrupoPrivilegio privilegio = new RugRelGrupoPrivilegio();
                privilegio.setIdGrupo(new Long(grupo));
                privilegio.setIdPrivilegio(idOperacion);
                privilegio.setSitRelacion("AC");
                privilegioDao.save(privilegio);
                sitNuevo = "AC";
            }

            return sitNuevo;
        }catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public Iterable<RugGarantiasPermiso> getGarantiaPermisos(Integer idPersona) {
        return garantiaPermisoDao.getGarantias(idPersona);
    }

    /**
     * @param idGarantia
     * @return
     */
    @Override
    public Boolean setGarantiaPermisoByUser(Integer idGarantia, Integer idUsuario) {
        try{

            if(validarGarantiaPertenencia(idGarantia, idUsuario)){
                RugGarantiasPermiso existente = garantiaPermisoDao.verificarExistente(idGarantia);

                if(existente == null){
                    LocalDate now = LocalDate.now();
                    RugGarantias garantia = garantiasDao.getGarantiaById(idGarantia);
                    RugGarantiasPermiso permiso = new RugGarantiasPermiso();
                    permiso.setIdGarantia(idGarantia);
                    permiso.setIdTramite(garantia.getIdUltimoTramite());
                    permiso.setIdUsuario(idUsuario);
                    permiso.setSit("AC");
                    permiso.setCreadoEn(Date.valueOf(now));
                    garantiaPermisoDao.save(permiso);
                    return true;
                }else{
                    return false;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return null;
    }

    /**
     * @param idGarantia
     * @param idUsuario
     * @return
     */
    @Override
    public Boolean desabilitarGarantia(Integer idGarantia, Integer idUsuario) {
        LocalDate now = LocalDate.now();
        Integer permiso = garantiaPermisoDao.findByidGarantiaAndSit(idGarantia, idUsuario, Date.valueOf(now), Date.valueOf(now));
        if(permiso > 0){
            return true;
        }
        return false;
    }

    /**
     * @param idGarantia
     * @param idUsuario
     * @return
     */
    @Override
    public boolean validarGarantiaPertenencia(Integer idGarantia, Integer idUsuario) {
        ConexionBD bd = new ConexionBD();
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String usuarios = usuariosGarantia(idUsuario);
        String query = "SELECT * FROM (\n" +
                "    SELECT * FROM ( \n" +
                "        SELECT ROWNUM RN, TT.* FROM (  \n" +
                "            SELECT ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ID_GARANTIA, DESCRIP_STATUS, ID_TIPO_TRAMITE, ID_PERSONA_LOGIN, USUARIO AS NOMBRE_PERSONA \n" +
                "                FROM RUG.V_TRAMITES_TERMINADOS_REG \n" +
                "                WHERE ID_PERSONA_LOGIN IN ("+ usuarios +") AND TRAMITE_REASIGNADO = 'F' ORDER BY FECHA_STATUS DESC ) TT ) \n" +
                "        ) T\n" +
                "WHERE T.ID_GARANTIA = ?";
        try{
            connection = bd.getConnection();
            ps = connection.prepareStatement(query);
            ps.setInt(1, idGarantia);
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer cuenta = rs.getInt("ID_TRAMITE");
                return true;

            }
        }catch (Exception e){
            e.printStackTrace();
            throw new JdbcDaoException(e);
        }finally {
            bd.close(connection, rs, ps);
        }
        return false;
    }

    /**
     * @param idUsuario
     * @return
     */
    @Override
    public String usuariosGarantia(Integer idUsuario) {
        ConexionBD bd = new ConexionBD();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlTramites = "SELECT id_persona \n" +
                "    FROM rug_secu_usuarios \n" +
                "    WHERE id_persona = ?\n" +
                "UNION \n" +
                "    SELECT id_persona \n" +
                "        FROM rug_secu_usuarios \n" +
                "        WHERE cve_usuario_padre = ( \t\n" +
                "            SELECT cve_usuario_padre \t\n" +
                "                FROM rug_secu_usuarios \t\n" +
                "                    WHERE id_persona = ?) \n" +
                "UNION \n" +
                "    SELECT id_persona \n" +
                "        FROM rug_secu_usuarios \n" +
                "            WHERE cve_usuario = ( \t\n" +
                "                SELECT cve_usuario_padre \t\n" +
                "                    FROM rug_secu_usuarios \t\n" +
                "                        WHERE id_persona = ?) \n" +
                "UNION \n" +
                "    SELECT id_persona \n" +
                "        FROM rug_secu_usuarios \n" +
                "            WHERE cve_usuario_padre = ( \t\n" +
                "                SELECT cve_usuario \t\n" +
                "                    FROM rug_secu_usuarios \t\n" +
                "                    WHERE id_persona = ?\n" +
                "                    )";
        try{
            List<String> lista = new ArrayList<>();
            connection = bd.getConnection();
            ps = connection.prepareStatement(sqlTramites);
            Integer idPersona = usuarioPadre(idUsuario);
            ps.setInt(1, idPersona);
            ps.setInt(2, idPersona);
            ps.setInt(3, idPersona);
            ps.setInt(4, idPersona);
            rs = ps.executeQuery();
            StringBuffer sbUsuarios = new StringBuffer();
            while(rs.next()){
                sbUsuarios.append(rs.getInt("id_persona") + ",");
            }
            return sbUsuarios.toString().substring(0, sbUsuarios.toString().length() - 1);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bd.close(connection, rs, ps);
        }
        return String.valueOf(idUsuario);
    }

    /**
     * @param idUsuario
     * @return
     */
    @Override
    public Integer usuarioPadre(Integer idUsuario) {
        ConexionBD bd = new ConexionBD();
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String query = "SELECT (CASE WHEN S.CVE_USUARIO_PADRE IS NULL THEN ID_PERSONA ELSE (SELECT ID_PERSONA FROM RUG_SECU_USUARIOS WHERE CVE_USUARIO = S.CVE_USUARIO_PADRE)  END) AS CODIGO\n" +
                "                    FROM RUG_SECU_USUARIOS S WHERE S.ID_PERSONA = ?";
        try{
            connection = bd.getConnection();
            ps = connection.prepareStatement(query);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                Integer cuenta = rs.getInt("CODIGO");
                return cuenta;

            }
        }catch (Exception e){
            e.printStackTrace();
            throw new JdbcDaoException(e);
        }finally {
            bd.close(connection, rs, ps);
        }
        return null;
    }
}
