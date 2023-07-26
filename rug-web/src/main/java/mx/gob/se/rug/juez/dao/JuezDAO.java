package mx.gob.se.rug.juez.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.to.PersonaSolicitaAutoridadInstruyeAsiento;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.MyLogger;

public class JuezDAO {
	
	public Boolean insertJuez(Integer idTramiteTemp, String autoridad){
		MyLogger.Logger.log(Level.INFO, "entra al dao del juez");
		Boolean regresa= false;
		String sql ="{call RUG.SP_ORDEN_AUTORIDAD( ?,?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramiteTemp);
			cs.setString(2, autoridad);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.executeQuery();	
			if(cs.getInt(3)==0){
				regresa = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		MyLogger.Logger.log(Level.INFO, "sale bien del dao del juez");
		return regresa;
	}
	public PlSql insertAutoridadInstruye(Integer idTramiteTemp, String autoridad){
		PlSql plSql= new PlSql();
		MyLogger.Logger.log(Level.INFO, "entra al dao del juez");
		String sql ="{call RUG.SP_ORDEN_AUTORIDAD( ?,?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramiteTemp);
			cs.setString(2, autoridad);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.executeQuery();	
			
			plSql.setIntPl(cs.getInt(3));
			plSql.setStrPl(cs.getString(4));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		MyLogger.Logger.log(Level.INFO, "sale bien del dao del juez");
		return plSql;
	}
	public void insertAutoridadInstruyeAsiento(Integer idTramiteTemp, String autoridad) throws InfrastructureException, CargaMasivaException{
		String sql ="{call RUG.SP_ORDEN_AUTORIDAD( ?,?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramiteTemp);
			cs.setString(2, autoridad);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.executeQuery();	
			if(cs.getInt(3)!=0){
				throw new CargaMasivaException(cs.getInt(3));
			}
		} catch (SQLException e) {
			throw new InfrastructureException("SP_ORDEN_AUTORIDAD",e);
		}finally{
			bd.close(connection,null,cs);
		}
	}
	public void insertAutoridadInstruyeAsiento(Integer idTramiteTemp, PersonaSolicitaAutoridadInstruyeAsiento autoridad) throws InfrastructureException, CargaMasivaException{
		String sql ="{call RUG.SP_ORDEN_AUTORIDAD( ?,?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramiteTemp);
			if(autoridad !=null){
			cs.setString(2, autoridad.getContenidoResolucion());
			}else{
				cs.setNull(2, Types.NULL );
			}
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.executeQuery();	
			if(cs.getInt(3)!=0){
				throw new CargaMasivaException(cs.getInt(3));
			}
		} catch (SQLException e) {
			throw new InfrastructureException("SP_ORDEN_AUTORIDAD",e);
		}finally{
			bd.close(connection,null,cs);
		}
	}

	public String showJuez (Integer idTramiteTemp){
		String regresa= "";
		String sql = "SELECT ANOTACION_JUEZ FROM V_AUTORIDAD WHERE ID_TRAMITE_TEMP = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;
		ResultSet rs =null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramiteTemp);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getString("ANOTACION_JUEZ");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	

	public String showJuezTram (Integer idTramiteTerminado){
		String regresa= "";
		String sql = "SELECT ANOTACION_JUEZ FROM V_AUTORIDAD_TRAMITE WHERE ID_TRAMITE = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps= connection.prepareStatement(sql);
			ps.setInt(1, idTramiteTerminado);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getString("ANOTACION_JUEZ");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
}
