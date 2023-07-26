package mx.gob.se.rug.cancelacionAnotacionSNGarantia.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.MyLogger;

public class CancelacionAnotacionSNGarantiaDAO extends BaseRugDao {

	/**
	 * 
	 * @param IdUsuario
	 * @param idTramiteTemporal
	 * @param IdAnotacion
	 * @param Anotacion
	 * @param  VigenciaAnotacion
	 * @return
	 */
	public PlSql altaCancelacion(Integer IdUsuario, Integer idTipoTram, Integer IdAnotacion, String autoInst,String Anotacion, Integer VigenMes) {
		
		MyLogger.Logger.log(Level.INFO, IdUsuario+" "+idTipoTram+" "+IdAnotacion+" "+autoInst +" "+ Anotacion +" "+ VigenMes);
		
		MyLogger.Logger.log(Level.INFO, "CancelacionAnotacionSNGarantiaDAO.altaCancelacion");
		MyLogger.Logger
				.log(Level.INFO,
				"CancelacionAnotacionSNGarantiaDAO.altaCancelacion.RUG.SP_ALTA_ANOTACION_SN_GARANTIA1 valores de entrada (): idGarantia="
								+ IdUsuario
								+ ",idTramiteTemporal="
								+ idTipoTram);

		System.out.println("#################");
		System.out.println("idUsuario:" +IdUsuario + "\n"+
				"idTipoTram:" +idTipoTram+ "\n"+
				"idAnotacion:" + IdAnotacion+"\n"+
				"autoInst:"+autoInst+ "\n"+
				"anotacion:"+Anotacion+ "\n"+
				"VigenMes:" +VigenMes+ "\n"
				
				);
		
		PlSql regresa = new PlSql();
		String sql = "{ call RUG.SP_ALTA_ANOTACION_SN_GARANTIA1(?, ?, ?, ?, ?, ?, ?, ?) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
		
			cs.setInt(1, IdUsuario);
			cs.setInt(2, idTipoTram);
			cs.setInt(3, IdAnotacion);
			cs.setString(4, autoInst);
			cs.setString(5, Anotacion);
			cs.setInt(6, VigenMes);		
			cs.registerOutParameter(7, Types.INTEGER); 
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			regresa.setIntPl(cs.getInt(7));
			regresa.setStrPl(cs.getString(8));
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage());
			MyLogger.Logger.log(Level.SEVERE,
					"Sucedio un error al ejecutar el PL SP_ALTA_ANOTACION_SN_GARANTIA1",
					e.getStackTrace());
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "CancelacionAnotacionSNGarantiaDAO.altaCancelacion");
		MyLogger.Logger
				.log(Level.INFO,
						"CancelacionAnotacionSNGarantiaDAO.altaCancelacion.SP_ALTA_ANOTACION_SN_GARANTIA1 valores de salida: plSql.intPl="
								+ regresa.getIntPl()
								+ ",plSql.intPl="
								+ regresa.getStrPl());
		
		return regresa;
	}
}
