package mx.gob.se.rug.certificacion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.certificacion.to.CertificacionTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.util.MyLogger;

public class CertificacionDAO {
	
	public CertificacionTO getCertificacionByTramite(Integer idTramite){
		CertificacionTO certificacionTO = null;
		
		return certificacionTO;
	}

	
	public Integer setCertificacion(Integer idTramiteCert,Integer idTramite, Integer idTipoTramite, Integer idGarantia, Integer idPersona) {
		Integer regresa = new Integer(0);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_CERTIFICACION(?,?,?,?,?,?,? ) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramiteCert);
			cs.setInt(2, idTramite);
			cs.setInt(3, idGarantia);
			cs.setInt(4, idTipoTramite);
			cs.setInt(5, idPersona);
			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getInt(6);
			// MyLogger.Logger.log(Level.INFO, "setCertificacion: Integer Result  = "	+ cs.getInt(6));
//			MyLogger.Logger.log(Level.INFO, "setCertificacion: Varchar Result  = "	+ cs.getString(7));
			cs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
		return regresa;
	}
}
