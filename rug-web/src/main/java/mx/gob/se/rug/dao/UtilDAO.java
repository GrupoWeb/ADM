package mx.gob.se.rug.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilDAO {
	
	public Integer tipoTramiteByIdTramite(Integer idTramite){
		Integer i = 0;
		String sql ="SELECT ID_TIPO_TRAMITE FROM tramites_rug_incomp WHERE ID_TRAMITE_TEMP = ?";
		ConexionBD conexionBD = new ConexionBD();
		Connection connection = conexionBD.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			if (rs.next()){
				i = new Integer(rs.getInt("ID_TIPO_TRAMITE"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			conexionBD.close(connection, rs, ps);
		}
		return i;
	}

}
