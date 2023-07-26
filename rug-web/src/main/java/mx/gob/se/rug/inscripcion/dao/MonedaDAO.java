package mx.gob.se.rug.inscripcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.inscripcion.to.MonedaTO;

public class MonedaDAO {
	
	public List<MonedaTO> getMonedas(){
		List <MonedaTO> lista = new ArrayList<MonedaTO>();
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT ID_MONEDA, DESC_MONEDA, SIMBOLO FROM RUG.RUG_CAT_MONEDAS WHERE STATUS_REG = 'AC'";		
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps =null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			MonedaTO monedaTO;
			while (rs.next()){
				monedaTO = new MonedaTO();
				monedaTO.setIdMoneda(rs.getInt("ID_MONEDA"));
				monedaTO.setDescMoneda(rs.getString("DESC_MONEDA"));
				monedaTO.setMoneda(rs.getString("SIMBOLO"));
				lista.add(monedaTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		
		
		return lista;
	}
}
