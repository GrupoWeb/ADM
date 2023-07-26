package mx.gob.se.rug.boleta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.boleta.to.BoletaInfoTO;
import mx.gob.se.rug.boleta.to.SeccionTO;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.util.MyLogger;

public class SeccionDAO {

	public BoletaInfoTO getSeccionesbyIdBoleta(BoletaInfoTO boletaInfoTO,
			Connection connection) throws InfrastructureException, NoDataFoundException {

		String sql = "SELECT  distinct ID_SECCION_BOLETA , DESC_SECCION_BOLETA,ORDEN_SECCION FROM V_BOLETA_ESTRUCTURA WHERE ID_BOLETA=? order by ORDEN_SECCION ";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, boletaInfoTO.getIdBoleta());
			rs = ps.executeQuery();

			List<SeccionTO> seccionTOs = new ArrayList<SeccionTO>();
			SeccionTO seccionTO= null;
			while(rs.next()){
				seccionTO=new  SeccionTO();
				seccionTO.setIdSeccion(new Integer(rs.getInt("ID_SECCION_BOLETA")));
				seccionTO.setLabel(rs.getString("DESC_SECCION_BOLETA"));
				seccionTO.setOrdenSeccion(rs.getInt("ORDEN_SECCION"));
				seccionTOs.add(seccionTO);
			}
			if (seccionTOs.size()<0) {
				throw new NoDataFoundException("No hay secciones para la Boleta:::"+ boletaInfoTO.getIdBoleta());
			}
			
			boletaInfoTO.setSeccionTOs(seccionTOs);

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
		return boletaInfoTO;
	}

}
