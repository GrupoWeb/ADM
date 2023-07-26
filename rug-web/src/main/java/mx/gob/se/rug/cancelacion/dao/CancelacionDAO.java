package mx.gob.se.rug.cancelacion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.MyLogger;

/**
 * 
 * @author Abraham Stalin
 * 
 */
public class CancelacionDAO extends BaseRugDao {

	/**
	 * 
	 * @param idGarantia
	 * @param idTramiteTemporal
	 * @return
	 */
	public PlSql altaCancelacion(Integer idGarantia, Integer idTramiteTemporal, String observaciones) {
		MyLogger.Logger.log(Level.INFO, "CancelacionDAO.altaCancelacion");
		MyLogger.Logger
				.log(Level.INFO,
						"CancelacionDAO.altaCancelacion.SP_ALTA_CANCELACION valores de entrada..: idGarantia="
								+ idGarantia
								+ ",idTramiteTemporal="
								+ idTramiteTemporal);
		PlSql regresa = new PlSql();
		String sql = "{ call RUG.SP_ALTA_CANCELACION(?, ?, ?, ?, ?) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramiteTemporal);
			cs.setInt(2, idGarantia);
			cs.setString(3, observaciones);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.execute();
			regresa.setIntPl(cs.getInt(4));
			regresa.setStrPl(cs.getString(5));
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage());
			MyLogger.Logger.log(Level.SEVERE,
					"Sucedio un error al ejecutar el PL altaCancelacion",
					e.getStackTrace());
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "CancelacionDAO.altaCancelacion");
		MyLogger.Logger
				.log(Level.INFO,
						"CancelacionDAO.altaCancelacion.SP_ALTA_CANCELACION valores de salida: plSql.intPl="
								+ regresa.getIntPl()
								+ ",plSql.intPl="
								+ regresa.getStrPl());
		return regresa;
	}
}
