package mx.gob.se.rug.embargo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.util.MyLogger;

public class EmbargoDAO extends BaseRugDao{

	public boolean altaEmbargo(Integer idGarantia, String observaciones, Integer idTramiteTemporal){
		MyLogger.Logger.log(Level.INFO, "entra al DAO ejecucion con el alta de embargo " + idGarantia);
		boolean regresa = false;
		String sql = "{ call RUG.SP_ALTA_EMBARGO(?, ?, ?, ?, ? ) }";
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
	
	public boolean isEmbargo(Integer idGarantia) {
		boolean regresa = false;
		String sql = "SELECT ID_TIPO_TRAMITE FROM V_BUSQUEDA_TRAMITE_BASE WHERE ID_GARANTIA = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			
			rs = ps.executeQuery();
			while (rs.next()){
				Integer tipoTramite = rs.getInt("ID_TIPO_TRAMITE");
				if(tipoTramite == 13) //embargo
					regresa = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return regresa;
	}
}
