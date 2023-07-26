package mx.gob.se.rug.inscripcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.gob.se.rug.dao.ConexionBD;

public class AcreedoresAdicionalesDAO {
	
	public boolean esAcreedorBueno(Integer idUsuario, Integer idAcreedor){
		boolean regresa = false;
		String sql = "SELECT NOMBRE_ACREEDOR FROM RUG.V_USUARIO_ACREEDOR_REP WHERE USUARIO_LOGIN = ? AND ID_PERSONA = ? ";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idUsuario);
			ps.setInt(2, idAcreedor);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			bd.close(connection,rs,ps);
		}
		
		return regresa;
	}

}
