package mx.gob.se.rug.seguridad.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.seguridad.to.PrivilegioTO;
import mx.gob.se.rug.seguridad.to.PrivilegiosTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class PrivilegiosDAO {
	public boolean esAdministrador(Integer idAcreedor, Integer idSubUsuario){
		String sql = "SELECT ID_ACREEDOR, ID_SUB_USUARIO, ID_PRIVILEGIO, SIT_USUARIO  FROM V_USUARIO_ACREEDOR_GRUPOS WHERE SIT_USUARIO = 'AC' " +
				"  AND ID_ACREEDOR = ?   AND ID_SUB_USUARIO = ?   AND (ID_GRUPO IN (1,3) OR CVE_PERFIL = 'AUTORIDAD') ";
		boolean regresa = false;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		MyLogger.Logger.log(Level.INFO, "idSubUsuario"+idSubUsuario);
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			ps.setInt(2, idSubUsuario);
	
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			bd.close(connection,rs, ps);
		}
		return regresa;
	}
	public boolean getTienePermiso(Integer idAcreedor, Integer idSubUsuario, Integer idPrivilegio){
		String sql = "select ID_ACREEDOR, ID_SUB_USUARIO, ID_PRIVILEGIO, SIT_USUARIO from " +
				" V_USUARIO_ACREEDOR_GRUPOS where SIT_USUARIO = 'AC' AND ID_ACREEDOR = ? AND ID_SUB_USUARIO = ?" +
				" AND ID_PRIVILEGIO = ? ";
		boolean regresa = false;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			ps.setInt(2, idSubUsuario);
			ps.setInt(3, idPrivilegio);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			bd.close(connection,rs, ps);
		}
		return regresa;
	}

	
	public PrivilegiosTO getPrivilegios(PrivilegiosTO privilegiosTO,UsuarioTO usuarioTO){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		List<PrivilegioTO>  privilegioTOs= new ArrayList<PrivilegioTO>();
		Map<Integer, PrivilegioTO> mapPrivilegio= new HashMap<Integer, PrivilegioTO>();
		String sql = "SELECT RP.ID_PRIVILEGIO, RP.HTML, RP.DESC_PRIVILEGIO" 
				+ " FROM RUG_SECU_USUARIOS RSU,"
				+ " RUG_GRUPOS RG," 
				+ " RUG_REL_GRUPO_PRIVILEGIO RRGP,"
				+ " RUG_PRIVILEGIOS RP," 
				+ " RUG_SECU_PERFILES_USUARIO RSPU"
				+ " WHERE RSU.ID_GRUPO = RG.ID_GRUPO"
				+ " AND RSU.ID_GRUPO = RRGP.ID_GRUPO"
				+ " AND RRGP.ID_PRIVILEGIO = RP.ID_PRIVILEGIO"
				+ " AND RSU.CVE_USUARIO = RSPU.CVE_USUARIO" 
				+ " AND RRGP.SIT_RELACION = 'AC'"
				+ " AND RSU.ID_PERSONA=?" 
				+ " AND RP.ID_RECURSO=?" 
				+ " ORDER BY RP.ORDEN";
		ResultSet rs= null;
		PreparedStatement ps = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			MyLogger.Logger.log(Level.INFO, "idRecurso"+privilegiosTO.getIdRecurso());
			MyLogger.Logger.log(Level.INFO, "idpersona 7"+usuarioTO.getPersona().getIdPersona());
			
			ps.setInt(1, usuarioTO.getPersona().getIdPersona());
			ps.setInt(2, privilegiosTO.getIdRecurso());
			
			rs = ps.executeQuery();
			PrivilegioTO privilegioTO=null;
			while(rs.next()){
				 privilegioTO = new PrivilegioTO();
				 privilegioTO.setDescripcion(rs.getString("DESC_PRIVILEGIO"));
				 privilegioTO.setHtml(rs.getString("HTML"));
				 privilegioTO.setIdPrivilegio(rs.getInt("ID_PRIVILEGIO"));
				 mapPrivilegio.put(new Integer(rs.getInt("ID_PRIVILEGIO")),privilegioTO);
				privilegioTOs.add(privilegioTO);
			}
			privilegiosTO.setPrivilegioTOs(privilegioTOs);
			privilegiosTO.setMapPrivilegio(mapPrivilegio);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
				bd.close(connection,rs,ps);
		}
		return privilegiosTO;
	}
	public PrivilegiosTO getPrivilegios(PrivilegiosTO privilegiosTO,Integer idPersona){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		List<PrivilegioTO>  privilegioTOs= new ArrayList<PrivilegioTO>();
		Map<Integer, PrivilegioTO> mapPrivilegio= new HashMap<Integer, PrivilegioTO>();
		String sql = "SELECT RP.ID_PRIVILEGIO, RP.HTML, RP.DESC_PRIVILEGIO" 
				+ " FROM RUG_SECU_USUARIOS RSU,"
				+ " RUG_GRUPOS RG," 
				+ " RUG_REL_GRUPO_PRIVILEGIO RRGP,"
				+ " RUG_PRIVILEGIOS RP," 
				+ " RUG_SECU_PERFILES_USUARIO RSPU"
				+ " WHERE RSU.ID_GRUPO = RG.ID_GRUPO"
				+ " AND RSU.ID_GRUPO = RRGP.ID_GRUPO"
				+ " AND RRGP.ID_PRIVILEGIO = RP.ID_PRIVILEGIO"
				+ " AND RSU.CVE_USUARIO = RSPU.CVE_USUARIO"
				+ " AND RSU.ID_PERSONA=?" 
				+ " AND RP.ID_RECURSO=?" 
				+ " ORDER BY RP.ORDEN";
		ResultSet rs= null;
		PreparedStatement ps = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			MyLogger.Logger.log(Level.INFO, "idRecurso"+privilegiosTO.getIdRecurso());
			MyLogger.Logger.log(Level.INFO, "idpersona 2"+idPersona);
			
			ps.setInt(1, idPersona);
			ps.setInt(2, privilegiosTO.getIdRecurso());
			
			rs = ps.executeQuery();
			PrivilegioTO privilegioTO=null;
			while(rs.next()){
				privilegioTO = new PrivilegioTO();
				privilegioTO.setDescripcion(rs.getString("DESC_PRIVILEGIO"));
				privilegioTO.setHtml(rs.getString("HTML"));
				privilegioTO.setIdPrivilegio(rs.getInt("ID_PRIVILEGIO"));
				mapPrivilegio.put(new Integer(rs.getInt("ID_PRIVILEGIO")),privilegioTO);
				privilegioTOs.add(privilegioTO);
			}
			privilegiosTO.setPrivilegioTOs(privilegioTOs);
			privilegiosTO.setMapPrivilegio(mapPrivilegio);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return privilegiosTO;
	}
//SP_MODIFICA_PERFIL
	
	public void modificaPerfil(UsuarioTO usuarioTO){
		MyLogger.Logger.log(Level.INFO, "cambio perfil");
		MyLogger.Logger.log(Level.INFO, "idpersona::"+usuarioTO.getPersona().getIdPersona());
		MyLogger.Logger.log(Level.INFO, "idGrupo::"+usuarioTO.getPersona().getIdGrupo());
			ConexionBD bd = new ConexionBD();
			Connection connection = bd.getConnection();
			
			CallableStatement cs = null;		
//			 peIdPersona     IN  NUMBER,
//			    peIdGrupo       IN  NUMBER,
//			    psResult       OUT  INTEGER,   
//			    psTxResult     OUT  VARCHAR2
			String sql ="{call RUG.SP_MODIFICA_PERFIL (?,?,?,? ) }";		
			try{
				connection.setAutoCommit(false);
				cs = connection.prepareCall(sql);
				cs.setInt(1,usuarioTO.getPersona().getIdPersona());
				cs.setInt(2,usuarioTO.getPersona().getIdGrupo());
				cs.registerOutParameter(3, Types.INTEGER);
				cs.registerOutParameter(4, Types.VARCHAR);
				cs.execute();
				
				MyLogger.Logger.log(Level.INFO, "salida");
				MyLogger.Logger.log(Level.INFO, "" + cs.getInt(3)); 
				MyLogger.Logger.log(Level.INFO, cs.getString(4)); 
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				bd.close(connection,null,cs);
			}
			}
	
	
	public List<String> getPrivilegios(Integer idPersona) {
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps= null;
		List<String>  privilegio = new ArrayList<String>();

		String sqlQuery = "SELECT DISTINCT C.DESC_PRIVILEGIO FROM RUG_SECU_USUARIOS A,RUG_REL_GRUPO_PRIVILEGIO B,RUG_PRIVILEGIOS C WHERE A.ID_GRUPO = B.ID_GRUPO "+
						"AND B.ID_PRIVILEGIO = C.ID_PRIVILEGIO AND A.ID_PERSONA = ? AND C.ID_RECURSO = 10 AND A.SIT_USUARIO='AC'"; 
				
		try {
			
			ps = connection.prepareStatement(sqlQuery);
			
			ps.setInt(1, idPersona);
			
			rs = ps.executeQuery();
			while (rs.next()) {
				privilegio.add(rs.getString("DESC_PRIVILEGIO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return privilegio;
	}
	
}
