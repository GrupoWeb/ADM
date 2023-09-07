package mx.gob.se.rug.inscripcion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.inscripcion.to.WSExternoTO;

public class WSExternoDAO {
	
	public List<WSExternoTO> getWebService(){
		List <WSExternoTO> lista = new ArrayList<WSExternoTO>();
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT *\n" +
                "FROM rug_parametro_conf\n" +
                "WHERE CVE_PARAMETRO IN (\n" +
                "'CnxSAT_login',\n" +
                "'CnxSAT_consulta_nit',\n" +
                "'CnxSAT_usuario',\n" +
                "'CnxSAT_password',\n" +
                "'CnxSAT_BotonVisible'\n" +
                ")";
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			WSExternoTO wsexternoTO;
			rs = ps.executeQuery();
			while(rs.next()){
                            String cve = rs.getString("CVE_PARAMETRO");
                            String par = rs.getString("VALOR_PARAMETRO");                            
				wsexternoTO = new WSExternoTO();
				wsexternoTO.setCVE_PARAMETRO(cve);
				wsexternoTO.setVALOR_PARAMETRO(par);
				
				lista.add(wsexternoTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		
		
		return lista;
	}

}
