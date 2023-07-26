package mx.gob.se.rug.cancelacion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;

public class CancelacionAnotaDAO extends BaseRugDao {
	
	public Integer cancelarAnota(){
		Integer regresa = new Integer(0);
		String sql = "{call RUG.SP_ALTA_ANOTACION_SN_GARANTIA1 ( ?,?,?,?,?,? ) } ";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		
											/*                                   
											 peIdUsuario          IN  NUMBER,
                                             peIdTipoTramite      IN  NUMBER,
                                             peIdAnotacion        IN  NUMBER,                                             
                                             peAutoridadInstruye  IN  VARCHAR2,
                                             peAnotacion          IN  CLOB,                                             
                                             peVigenciaAnotacion  IN  NUMBER,
                                             */
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, 1);
			cs.setInt(2, 1);
			cs.setInt(3, 1);
			cs.setInt(4, 1);
			cs.setInt(5, 1);
			cs.setInt(6, 1);
			
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getInt(3);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
		return regresa;
	}
	
}
