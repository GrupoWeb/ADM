package mx.gob.se.rug.boleta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.boleta.to.CampoTO;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.util.MyLogger;

public class CamposDAO {
	
	public List<CampoTO> getCamposbyIdBoletaIdSeccion(Integer IdBoleta, Integer idSeccion,
			Connection connection) throws InfrastructureException, NoDataFoundException {

		String sql = "SELECT  ID_CAMPO_BOLETA, DESC_CAMPO_BOLETA, CVE_CAMPO_BOLETA, ORDEN_CAMPO, ORIENTACION FROM V_BOLETA_ESTRUCTURA WHERE ID_BOLETA= ? and  ID_SECCION_BOLETA=?";
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<CampoTO> campoTOs =null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, IdBoleta);
			ps.setInt(2, idSeccion);
			rs = ps.executeQuery();

			campoTOs = new ArrayList<CampoTO>();
			CampoTO campoTO= null;
			while(rs.next()){
				campoTO=new  CampoTO();
				campoTO.setIdCampo(new Integer(rs.getInt("ID_CAMPO_BOLETA")));
				campoTO.setLabel(rs.getString("DESC_CAMPO_BOLETA"));
				campoTO.setOrden(new Integer(rs.getInt("ORDEN_CAMPO")));
				campoTO.setOrientacion(rs.getString("ORIENTACION"));
				campoTO.setCveCampo(rs.getString("CVE_CAMPO_BOLETA"));
				campoTOs.add(campoTO);
			}
			if (campoTOs.size()<0) {
				throw new NoDataFoundException("No hay secciones para la Boleta:::"+ IdBoleta+" idSeccion::"+idSeccion);
			}
			

		} catch (SQLException e) {
			throw new InfrastructureException("Message::" + e.getMessage()+ "Cause::" + e.getCause());
		} finally {
			try {
			
				rs.close();
			} catch (Exception e) {
				MyLogger.Logger.log(Level.WARNING, "no cerro RS");
			}finally{
				try {
					ps.close();
				} catch  (Exception e) {
					MyLogger.Logger.log(Level.WARNING, "no cerro ps");
				}
			}
		}
		return campoTOs;
	}


}
