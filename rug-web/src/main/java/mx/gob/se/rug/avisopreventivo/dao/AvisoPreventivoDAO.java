package mx.gob.se.rug.avisopreventivo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.util.MyLogger;

public class AvisoPreventivoDAO {

	public int insertAviso (Integer idTramite, String descripcion, Integer idPersona){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_ALTA_AVISO_PREVENTIVO ( ?,?,?,?,? ) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramite);
			cs.setString (2,descripcion );
			cs.setInt(3, idPersona );
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getInt(4);
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Integer Result  = " + cs.getInt(4));
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Varchar Result  = " + cs.getString(5));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}
}
