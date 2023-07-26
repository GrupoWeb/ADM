package mx.gob.se.constants;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import mx.gob.se.conexion.ConexionDB;

public class ConstantesDAO {
	public Map<String, String> getConstants() {
		ConexionDB bd = new ConexionDB();
		ResultSet rs =null;
		Map<String, String> constantes = new HashMap<String, String>();
		try {
			String sqlQuery = "SELECT CVE_PARAMETRO,VALOR_PARAMETRO FROM RUG_PARAMETRO_CONF";
			bd.prepareStatement(sqlQuery);
			rs=bd.executeQuery();
			
			while (rs.next()) {
				System.out.println("Param_Config: "+rs.getString("CVE_PARAMETRO")+" valor: "+ rs.getString("VALOR_PARAMETRO"));
				constantes.put(rs.getString("CVE_PARAMETRO"), rs.getString("VALOR_PARAMETRO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bd.destroy();
		}
		return constantes;
	}

}
