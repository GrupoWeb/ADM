package mx.gob.se.rug.encuesta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.util.MyLogger;

public class EncuestaDao extends BaseRugDao{

	public boolean insertarRespuesta(Long idPregunta, String respuesta) {
	
		boolean regresa = false;
		ConexionBD bd = new ConexionBD();
		String sql = "INSERT INTO RUG.RUG_RESPUESTA(ID_RESPUESTA, ID_PREGUNTA, RESPUESTA, FECHA) VALUES (RUG.SEQ_RUG_RESPUESTA.NEXTVAL,?,?,SYSDATE)";
		
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setLong(1, idPregunta);
			ps.setString(2, respuesta);
			ps.executeUpdate();
			regresa = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		
		return regresa;
	}
	
	public Boolean validarToken(String token) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		Boolean regresa= false;
		
		String sql = "SELECT ESTADO   FROM RUG_ENCUESTA_TOKEN   WHERE TOKEN = ?";
		ResultSet rs= null;
		PreparedStatement ps =null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, token);

			rs = ps.executeQuery();

			if (rs.next()) {
				Integer estado = new Integer(rs.getInt("ESTADO"));
				if(estado==0) regresa = true;
			}else{
				MyLogger.Logger.log(Level.INFO, "No se encontro token ::::"+token);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		return regresa;
	}
	
	public Boolean actualizarToken(String token) {
		Boolean flagUpdateOk=false;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();		
		
		String sql = "UPDATE RUG_ENCUESTA_TOKEN SET ESTADO = 1 WHERE TOKEN LIKE ?" ;
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {			
			ps = connection.prepareStatement(sql);			
			ps.setString(1, token);			
			int nRows = ps.executeUpdate();
			
			if(nRows>0){
				flagUpdateOk=true;
			}else{
				MyLogger.Logger.log(Level.INFO, "No se encontro token ::::"+token);
			}
		} catch (SQLException e) {
			e.printStackTrace();			
		}finally{
			bd.close(connection,rs,ps);
		}
		return flagUpdateOk;
	}
}
