package mx.gob.se.rug.anotacion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.util.MyLogger;

public class AnotacionDAO {

	public int insert(int idPersona,int idTramite,String autoridad,String anotacion, Integer meses){
		MyLogger.Logger.log(Level.INFO, "entra al DAO de anotacion");
		MyLogger.Logger.log(Level.INFO, "Meses: "+meses);
		int regresa=0;
		String sql ="{call RUG.SP_ALTA_ANOTACION_SN_GARANTIA ( ?,?,?,?,?,?,? )} ";
		ConexionBD bd=new ConexionBD();
		Connection connection= bd.getConnection();
		CallableStatement cs = null;
		try {
			 cs = connection.prepareCall(sql);
			cs.setInt(1, idPersona);
			cs.setInt(2, idTramite);
			cs.setString(3, autoridad);
			cs.setString(4, anotacion);
			cs.setInt(5, Integer.valueOf(meses));
			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getInt(6);
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Integer Result  = " + cs.getInt(6));
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Varchar Result  = " + cs.getString(7));
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
			
			
		}
		
		return regresa;
	}
	 

}
