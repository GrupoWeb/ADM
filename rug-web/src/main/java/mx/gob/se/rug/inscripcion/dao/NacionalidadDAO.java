package mx.gob.se.rug.inscripcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;

public class NacionalidadDAO {
	
	public List<NacionalidadTO> getNacionalidades(){
		List <NacionalidadTO> lista = new ArrayList<NacionalidadTO>();
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT ID_NACIONALIDAD, DESC_NACIONALIDAD, CVE_PAIS FROM RUG.RUG_CAT_NACIONALIDADES" +
				" ORDER BY DESC_NACIONALIDAD";
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			NacionalidadTO nacionalidadTO;
			rs = ps.executeQuery();
			while(rs.next()){
				nacionalidadTO = new NacionalidadTO();
				nacionalidadTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				nacionalidadTO.setCvePais(rs.getString("CVE_PAIS"));
				nacionalidadTO.setDescNacionalidad(rs.getString("DESC_NACIONALIDAD"));
				lista.add(nacionalidadTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		
		
		return lista;
	}

}
