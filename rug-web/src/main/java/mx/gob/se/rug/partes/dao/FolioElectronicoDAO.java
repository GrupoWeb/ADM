package mx.gob.se.rug.partes.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;

public class FolioElectronicoDAO {
	
	public String creaFolioElectronico(Integer idPersona){
		String regresa = null;
		ConexionBD bd = new ConexionBD();
		String sql = "{ call RUG.SP_FOLIO_ELECTRONICO ( ?,?,?,? ) }";
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		
		try {			
			cs = connection.prepareCall(sql);
			cs.setInt(1, idPersona);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();			
			if (cs.getInt(3) == 0){
				regresa = cs.getString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			bd.close(connection,null,cs);
		}
		
		return regresa;
	}
	public String generaFolioElectronico(Integer idPersona) throws CargaMasivaException, InfrastructureException{
		String regresa = null;
		ConexionBD bd = new ConexionBD();
		String sql = "{ call RUG.SP_FOLIO_ELECTRONICO ( ?,?,?,? ) }";
		Connection connection = bd.getConnection();
		CallableStatement cs =null;
		
		try {			
			cs = connection.prepareCall(sql);
			cs.setInt(1, idPersona);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();			
			if (cs.getInt(3) == 0){
				regresa = cs.getString(2);
			}else{
				throw new CargaMasivaException(cs.getInt(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InfrastructureException("SP_FOLIO_ELECTRONICO ",e);
		}finally{
			
			bd.close(connection,null,cs);
		}
		
		return regresa;
	}
	
	
}
