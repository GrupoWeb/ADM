package mx.gob.se.rug.acreedores.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.acreedores.dao.GrupoDao;
import mx.gob.se.rug.acreedores.to.GrupoPerfilTO;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.fwk.dao.spring.RugBaseJdbcDao;
import mx.gob.se.rug.util.MyLogger;

public class GrupoDaoJdbcImpl extends RugBaseJdbcDao implements GrupoDao {
	
	
	public int getNumbersOfUsersByGroup(Integer idUsuario, Integer idAcreedor,Integer idGrupo) throws InfrastructureException {
		MyLogger.Logger.log(Level.INFO, "entro en: getUsuariosFirmados");
		
		int numeroUsuarios=0;
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = " SELECT count(*) NUMERO FROM RUG.V_USUARIOS_ACREEDORES WHERE" +
				" ID_ACREEDOR = ? AND B_FIRMADO = 'Y' AND STATUS_CUENTA = 'AC' AND STATUS_REL = 'AC' AND ID_SUBUSUARIO != ? AND ID_GRUPO=? ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		MyLogger.Logger.log(Level.INFO, "entro a getNumbersOfUsersByGroup" + idUsuario + "-"+ idAcreedor+"-"+idGrupo);
		try {
			connection= bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			ps.setInt(2, idUsuario);
			ps.setInt(3, idGrupo);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				numeroUsuarios= rs.getInt("NUMERO");
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "ocurrio error al intentar obtener numero de usuarios en el grupo", e);
			throw new InfrastructureException("ocurrio error al intentar obtener numero de usuarios en el grupo");
		} finally {
				bd.close(connection, rs, ps);
		}
		return numeroUsuarios;
	}
	public Boolean isNotExistGroup(String nombreGrupo, Integer idAcreedor, Integer idGrupo) throws InfrastructureException {
		MyLogger.Logger.log(Level.INFO, "entro en: isExistGroup");
		Boolean noExiste=true;
		int nExiste= 0;
		
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		
		String sql = "SELECT COUNT(*) existe FROM RUG_GRUPOS WHERE ID_ACREEDOR = ? AND UPPER(DESC_GRUPO) = UPPER(?) AND SIT_GRUPO = 'AC' AND ID_GRUPO <> ?";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			connection= bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			ps.setString(2, nombreGrupo);
			ps.setInt(3, idGrupo);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				nExiste= rs.getInt("existe");
			}
			
			if(nExiste==0){
				noExiste=true;
			}
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "ocurrio error al intentar obtener numero de usuarios en el grupo", e);
			throw new InfrastructureException("ocurrio error al intentar obtener numero de usuarios en el grupo");
		} finally {
			bd.close(connection, rs, ps);
		}
		return noExiste;
	}
	
	public boolean updateUsuarioGrupo(Integer idUsuario, String idSubUsuario, Integer idAcreedor, Integer idGrupoNuevo){
		boolean valor = false;
		String sql = "{call SP_REASIGNAR_GRUPO (?,?,?,?,?,?)}";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, idAcreedor);
			cs.setString(2, idSubUsuario);
			cs.setInt(3, idGrupoNuevo);
			cs.setInt(4, idUsuario);
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.execute();
			if (cs.getInt(5) == 0) {
				valor = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			bd.close(connection, null, cs);
		}
		return valor;
	}
	
	public Integer saveGrupo(GrupoPerfilTO grupoPerfilTO, String perfiles, Integer idUsuairo) {
		
		MyLogger.Logger.log(Level.INFO, "saveGrupo()");
		MyLogger.Logger.log(Level.INFO, "grupoPerfilTO=" + grupoPerfilTO);
		Integer result = 0;
		if (grupoPerfilTO == null) {
			return 0;
		}
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		String sql = "{call RUG.SP_ALTA_GRUPO (?,?,?,?,?,?)}";
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, Integer
					.valueOf(grupoPerfilTO.getIdPersonaCrea() == null ? "0"
							: grupoPerfilTO.getIdPersonaCrea()));
			cs.setInt(2, idUsuairo == null ? 0 : idUsuairo);
			cs.setString(3, grupoPerfilTO.getDescripcion());
			cs.setString(4, perfiles);
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			MyLogger.Logger.log(Level.INFO, "GrupoDaoJdbcImpl: PL = " + sql);
			MyLogger.Logger.log(Level.INFO, "GrupoDaoJdbcImpl: grupoPerfilTO = "
					+ grupoPerfilTO);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "GrupoDaoJdbcImpl: Integer Result = "
					+ cs.getInt(5));
			MyLogger.Logger.log(Level.INFO, "GrupoDaoJdbcImpl: String Result = "
					+ cs.getString(6));
			result = cs.getInt(5);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		return result;
	}

	public boolean deleteGrupo(GrupoPerfilTO grupoPerfilTO, Integer idUsuairo) {
		MyLogger.Logger.log(Level.INFO, "deleteGrupo()");
		MyLogger.Logger.log(Level.INFO, "grupoPerfilTO=" + grupoPerfilTO);
		boolean result = false;
		if (grupoPerfilTO == null) {
			return false;
		}
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		String sql = "{call RUG.SP_BAJA_GRUPO (?,?,?,?,?)}";
		try {
			cs = connection.prepareCall(sql);

			cs.setInt(1, Integer
					.valueOf(grupoPerfilTO.getIdPersonaCrea() == null ? "0"
							: grupoPerfilTO.getIdPersonaCrea()));
			cs.setInt(2, idUsuairo == null ? 0 : idUsuairo);
			cs.setInt(3, Integer.valueOf(grupoPerfilTO.getId()));
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			MyLogger.Logger.log(Level.INFO, "GrupoDaoJdbcImpl: PL = " + sql);
			MyLogger.Logger.log(Level.INFO, "GrupoDaoJdbcImpl: grupoPerfilTO = "
					+ grupoPerfilTO);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "GrupoDaoJdbcImpl: Integer Result = "
					+ cs.getInt(4));
			MyLogger.Logger.log(Level.INFO, "GrupoDaoJdbcImpl: String Result = "
					+ cs.getString(5));
			result = true;
		} catch (Exception e) {
				e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		return result;
	}
	public boolean updatePrivilegiosGrupo(Integer idUsuario, Integer idAcreedor, Integer idGrupo, String privilegios){
		boolean valor = false;
		String sql = "{call SP_MODIFICA_GRUPO (?,?,?,?,?,?)}";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;		
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, idAcreedor);
			cs.setInt(2, idGrupo);
			cs.setString(3, privilegios);
			cs.setInt(4, idUsuario);
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.execute();
			if (cs.getInt(5) == 0) {
				valor = true;
			}else{
				MyLogger.Logger.log(Level.WARNING, "ERROR SQL :: al actualizar los privilegios de un grupo" + cs.getString(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			bd.close(connection, null, cs);
		}
		return valor;
	}

	
	public GrupoPerfilTO getGrupo(int idAcreedor, int idUsuario) throws JdbcDaoException {
		MyLogger.Logger.log(Level.INFO, "--getGrupos DAO--");
		MyLogger.Logger.log(Level.INFO, "IMPRIME ID PERSONA EN DAO: "+idAcreedor);
		ConexionBD bd = new ConexionBD();		
		
		String sql =" SELECT RRG.ID_GRUPO, RGG.DESC_GRUPO FROM RUG.RUG_REL_GRUPO_ACREEDOR RRG," +
				"    RUG.RUG_GRUPOS RGG WHERE RRG.ID_GRUPO = RGG.ID_GRUPO AND RRG.ID_ACREEDOR = ?" +
				" AND RRG.ID_SUB_USUARIO = ? AND RRG.STATUS_REG = 'AC'";
		GrupoPerfilTO grupoPerfilTO = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection= null;
		try {
			connection= bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			ps.setInt(2, idUsuario);			
			rs = ps.executeQuery();
			while (rs.next()){
				grupoPerfilTO = new GrupoPerfilTO();
				grupoPerfilTO.setId(rs.getString("ID_GRUPO"));
				grupoPerfilTO.setDescripcion(rs.getString("DESC_GRUPO"));				
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally{
		bd.close(connection, rs, ps);
		}
		return grupoPerfilTO;
	}
	public Boolean setNombreGrupo(Integer idGrupo, String nombreGrupo) throws JdbcDaoException {
		MyLogger.Logger.log(Level.INFO, "--getGrupos DAO--");
		MyLogger.Logger.log(Level.INFO, "IMPRIME  idGrupo EN DAO: "+idGrupo);
		ConexionBD bd = new ConexionBD();		
		
		String sql ="UPDATE RUG_GRUPOS SET DESC_GRUPO = ? WHERE ID_GRUPO = ?" ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection= null;
		
		Boolean correcto= false;
		try {
			connection= bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, nombreGrupo);			
			ps.setInt(2, idGrupo);
			int registrosModificados=ps.executeUpdate();
			
			if(registrosModificados>0){
				correcto= true;
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			correcto= false;
		} finally{
			bd.close(connection, rs, ps);
		}
		return correcto;
	}

}
