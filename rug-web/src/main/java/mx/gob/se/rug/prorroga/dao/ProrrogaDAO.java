package mx.gob.se.rug.prorroga.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.util.MyLogger;

public class ProrrogaDAO {

	public GarantiaTO getVigencia(Integer idGarantia) throws NoDataFoundException{
		MyLogger.Logger.log(Level.INFO, "entra al DAO prorroga con la garantia");
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		GarantiaTO garantiaTO = new GarantiaTO();
	
		String sql = "SELECT VIGENCIA, FECHA_FIN FROM (SELECT VIGENCIA, FECHA_FIN, FECHA_TRAMITE FROM V_PRORROGA WHERE ID_GARANTIA = ? ORDER BY 3 DESC ) WHERE ROWNUM = 1";
		
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			rs = ps.executeQuery();
			MyLogger.Logger.log(Level.INFO, "la consulta : "+ sql);
			if(rs.next()){
				garantiaTO.setVigencia(rs.getInt("VIGENCIA"));
				MyLogger.Logger.log(Level.INFO, "la vigencia de la consulta: " +rs.getInt("VIGENCIA"));
				garantiaTO.setFechaFin(rs.getDate("FECHA_FIN"));
				MyLogger.Logger.log(Level.INFO, "la FECHA FIN de la consulta: " +rs.getString("FECHA_FIN"));
			}else{
				throw new NoDataFoundException("No se encontro vigencia con el id garantia: "+idGarantia);
			}
			MyLogger.Logger.log(Level.INFO, "en el DAO la vigencia es: " + garantiaTO.getVigencia());
			MyLogger.Logger.log(Level.INFO, "en el DAO la fecha fin es: " + garantiaTO.getFechaFin());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return garantiaTO;
	}
	
	public boolean altaProrroga(Integer idGarantia, Integer vigencia, Integer idTramiteTemporal){
		MyLogger.Logger.log(Level.INFO, "entra al DAO prorroga con el alta de prorroga " + vigencia);
		boolean regresa = false;
		String sql = "{ call RUG.SP_ALTA_PRORROGA(?, ?, ?, ?, ? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramiteTemporal);
			cs.setInt(2, idGarantia);
			cs.setInt(3, vigencia);
			
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.execute();
			regresa = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
	}
}
