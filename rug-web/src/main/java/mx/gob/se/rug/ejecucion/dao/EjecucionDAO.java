package mx.gob.se.rug.ejecucion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.util.MyLogger;

public class EjecucionDAO {

	public boolean altaEjecucion(Integer idGarantia, String observaciones, Integer idTramiteTemporal){
		MyLogger.Logger.log(Level.INFO, "entra al DAO ejecucion con el alta de ejecucion " + idGarantia);
		boolean regresa = false;
		String sql = "{ call RUG.SP_ALTA_EJECUCION(?, ?, ?, ?, ? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramiteTemporal);
			cs.setInt(2, idGarantia);
			cs.setString(3, observaciones);
			
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
