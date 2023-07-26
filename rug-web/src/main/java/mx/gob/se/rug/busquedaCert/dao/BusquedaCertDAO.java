package mx.gob.se.rug.busquedaCert.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import mx.gob.se.rug.BusquedaCertificacion.to.BusquedaCertTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.operaciones.dao.OperacionesDAO;
import mx.gob.se.rug.util.MyLogger;

public class BusquedaCertDAO{
	
	public String getNomOtorById(Integer idTramite){
		String nombreOtorgante = "";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "SELECT NOMBRE_PARTE FROM V_DETALLE_BOLETA_PARTES WHERE ID_TRAMITE = "+idTramite+"" ;
		PreparedStatement ps = null;
		MyLogger.Logger.log(Level.INFO, "consulta "+ sql);
		ResultSet rs =null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				nombreOtorgante= rs.getString("NOMBRE_PARTE");
			}			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return nombreOtorgante;
	}
	
	public String getFolioById(Integer idTramite){
		String folio = "";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "SELECT FOLIO_ELECTRONICO FROM V_DETALLE_BOLETA_PARTES WHERE ID_TRAMITE = "+idTramite+"" ;
		MyLogger.Logger.log(Level.INFO,"consulta "+ sql);
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				folio= rs.getString("FOLIO_ELECTRONICO");
			}
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return folio;
	}
	
	
	public BusquedaCertTO busquedaNum(String numOperacion) throws InfrastructureException{
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		MyLogger.Logger.log(Level.INFO,"entra al dao: " + numOperacion);
		BusquedaCertTO busquedaCertTO = new BusquedaCertTO() ;
		MyLogger.Logger.log(Level.INFO, "entra al dao de busqueda de certificaciones");
		String sql = "SELECT DESCRIPCION, FECHA_CREACION, ID_GARANTIA, PRECIO, ID_TRAMITE, ID_TRAMITE_TEMP FROM V_TRAMITES_TERMINADOS WHERE ID_TRAMITE = "+numOperacion+"" ;
		MyLogger.Logger.log(Level.INFO, "consulta "+ sql);
		ResultSet rs =null;
		PreparedStatement ps = null;
		OperacionesDAO dao = new OperacionesDAO();
		try {
		
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			//ps.setStringt(1, numOperacion);
			rs = ps.executeQuery();
			while (rs.next()){
			busquedaCertTO.setIdTramTem(rs.getInt("ID_TRAMITE_TEMP"));
			busquedaCertTO.setTipoOp(rs.getString("DESCRIPCION"));
			busquedaCertTO.setFechaInsc(rs.getDate("FECHA_CREACION"));
			busquedaCertTO.setOtorgantes(dao.getOtorganteByTramite(rs.getInt("ID_TRAMITE")));
			busquedaCertTO.setPrecio(rs.getDouble("PRECIO"));
			busquedaCertTO.setNumGarantia(rs.getInt("ID_GARANTIA"));
			}
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return busquedaCertTO;
	}
	
	
}
