package mx.gob.se.rug.anotacionGarantia.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import mx.gob.se.rug.dao.ConexionBD;

public class AnotacionGarantiaDAO {
	public Integer insertAnotacionGarantia(Integer idPersona,Integer idTramite,String autoridad,String anotacion, Integer idGarantia,String meses){
		
		int regresa=1;
		String sql ="{call RUG.SP_ALTA_ANOTACION ( ?,?,?,?,?,?,?,? )} ";
		ConexionBD bd=new ConexionBD();
		Connection connection= bd.getConnection();
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idPersona);
			cs.setInt(2, idTramite);
			cs.setInt(3,idGarantia );
			cs.setString(4, autoridad);
			cs.setString(5, anotacion);
			cs.setString(6,meses);
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.executeQuery();
			if(cs.getInt(7)==0){				
				regresa = cs.getInt(7);
			}else{
				throw new SQLException("error al crear la anotacion con garantia el pl es RUG.SP_ALTA_ANOTACION el error de oracle es :" +cs.getString(8));
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
			
			
		}
		
		return regresa;
	}
}
