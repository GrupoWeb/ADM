package mx.gob.se.rug.firma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.util.MyLogger;

public class FirmaDAO {
	public String getRFCbyIdTramite (Integer idTramiteNuevo) throws NoDataFoundException{
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "select RFC from V_ACREEDOR_REP_FIRMA where ID_TRAMITE_TEMP=?";
		ResultSet rs =null;
		PreparedStatement ps= null;
		String rfc= null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramiteNuevo);
			rs = ps.executeQuery();
			if(rs.next()){
				rfc= new String(rs.getString("RFC"));
			}else{
				throw new NoDataFoundException("No se encontro el rfc del idTramiteNuevo" +idTramiteNuevo);
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bd.close(connection,rs,null);
		}
		return rfc;
	}
	public String getRFCbyIdPersona(Integer idPersona) throws NoDataFoundException{
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "select RFC from RUG_PERSONAS where ID_PERSONA=?";
		ResultSet rs =null;
		PreparedStatement ps= null;
		String rfc= null;
		MyLogger.Logger.log(Level.INFO, "::FirmaDAO.getRFCbyIdPersona()::query:"+sql+"parametro"+idPersona);
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if(rs.next()){
				rfc= rs.getString("RFC");
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		MyLogger.Logger.log(Level.INFO, "::FirmaDAO.getRFCbyIdPersona()::resultado:"+rfc);
		return rfc;
	}
	public String getNumSeriebyIdPersona(Integer idPersona) throws NoDataFoundException{
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "select NUM_SERIE from V_REP_PERSONAS where ID_PERSONA=?";
		ResultSet rs =null;
		PreparedStatement ps= null;
		String numSerie= "99999";
		MyLogger.Logger.log(Level.INFO, "::FirmaDAO.getNumSeriebyIdPersona()::query:"+sql+"parametro"+idPersona);
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getString("NUM_SERIE")!=null && !rs.getString("NUM_SERIE").equalsIgnoreCase(""))
					numSerie= rs.getString("NUM_SERIE");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		MyLogger.Logger.log(Level.INFO, "::FirmaDAO.getNumSeriebyIdPersona()::resultado:"+numSerie);
		return numSerie;
	}
	
	
	
	public String setRFCbyIdPersona(Integer idPersona,String rfc) throws NoDataFoundException{
		MyLogger.Logger.log(Level.INFO, "GUARDA RFC ");
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "update RUG_PERSONAS set rfc=?   WHERE ID_PERSONA=?";
		ResultSet rs =null;
		PreparedStatement ps= null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, rfc);
			ps.setInt(2, idPersona);
			 ps.executeUpdate();

			ps.close();
		} catch (SQLException e) {
			throw new NoDataFoundException("No se pudo establecer el rfc del idPersona" +idPersona);
		}
		finally{
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bd.close(connection,rs,null);
		}
		return rfc;
	}
	
	public  Boolean validateTramiteFirma(Integer idTramiteNuevo) {
		MyLogger.Logger.log(Level.INFO, "::FirmaElectronicaAction.FirmaDAO.validateTramiteFirma()::IdTramite que se va a firmar -"+idTramiteNuevo);
		Boolean flag= new Boolean(false);
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "select ID_TRAMITE_TEMP from TRAMITES_RUG_INCOMP where ID_TRAMITE_TEMP = ? and id_status_tram=5";
		ResultSet rs =null;
		PreparedStatement ps= null;
		MyLogger.Logger.log(Level.INFO, "::FirmaElectronicaAction.FirmaDAO.validateTramiteFirma()::consulta si existe true sino false -query-"+sql+"-parametro-"+idTramiteNuevo);
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramiteNuevo);
			rs = ps.executeQuery();
			if(rs.next()){
				flag= true; 
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			
			MyLogger.ErrorFirma.log(Level.INFO, "ERROR::: idTramite="+idTramiteNuevo);
			MyLogger.ErrorFirma.log(Level.INFO, "ERROR::: ");
		}
		finally{
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bd.close(connection,rs,null);
		}
		MyLogger.Logger.log(Level.INFO, "::FirmaElectronicaAction.FirmaDAO.validateTramiteFirma()::Resultado que regresa -"+flag);
		return flag;
	}
	
}
