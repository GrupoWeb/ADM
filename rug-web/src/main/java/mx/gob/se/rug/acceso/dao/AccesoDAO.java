package mx.gob.se.rug.acceso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.dto.PersonaFisica;

public class AccesoDAO {
	public PersonaFisica getIdPersona(String usuario) {
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		PersonaFisica personaFisica = null;
		try {
			String sqlQuery = "SELECT ID_PERSONA, DECODE(ID_GRUPO, 1, 'ACREEDOR', 2, 'CIUDADANO', 3,'ADMINISTRADOR', 4, 'AUTORIDAD') CVE_PERFIL  FROM V_USUARIO_SESION_RUG WHERE CVE_USUARIO = ?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setString(1, usuario);
			rs = ps.executeQuery();
			if (rs.next()) {
				personaFisica = new PersonaFisica();
				personaFisica.setIdPersona(rs.getInt("ID_PERSONA"));
				personaFisica.setPerfil(rs.getString("CVE_PERFIL"));
			}
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		} finally {
			bd.close(connection, rs, ps);
		}
		return personaFisica;
	}
	
	public PersonaFisica getUsuario(Integer idUsusario) {
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		PersonaFisica personaFisica = null;
		try {
			String sqlQuery = "select CVE_USUARIO,NOMBRE_PERSONA, AP_PATERNO, AP_MATERNO from V_USUARIO_SESION_RUG WHERE ID_PERSONA=?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idUsusario);
			rs = ps.executeQuery();
			if (rs.next()) {
				personaFisica = new PersonaFisica();
				personaFisica.setIdPersona(idUsusario);
				personaFisica.setNombre(rs.getString("NOMBRE_PERSONA"));
				personaFisica.setApellidoPaterno(rs.getString("AP_PATERNO"));
				personaFisica.setApellidoMaterno(rs.getString("AP_MATERNO"));
			}
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		} finally {
			bd.close(connection, rs, ps);
		}
		return personaFisica;
	}

	public Boolean checkUserAdmin(Integer idUsuario){
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String sqlQuery = "SELECT NVL(IS_ADMIN, 0) AS cuenta FROM RUG_SECU_USUARIOS WHERE ID_PERSONA=  ?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idUsuario);
			rs = ps.executeQuery();
			if (rs.next()) {
				String cuenta = rs.getString("cuenta");
				return cuenta.equals("1");
			}
		}catch (Exception e){
			throw new JdbcDaoException(e);
		} finally {
			bd.close(connection, rs, ps);
		}
		return false;
	}
	
	public Boolean getCuentaMaestra(Integer idUsusario) {
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		PersonaFisica personaFisica = null;
		try {
			String sqlQuery = "SELECT NVL(cve_usuario_padre, 'MAESTRA') AS cuenta FROM V_USUARIO_SESION_RUG WHERE ID_PERSONA=?";
			ps = connection.prepareStatement(sqlQuery);
			ps.setInt(1, idUsusario);
			rs = ps.executeQuery();
			if (rs.next()) {
				String cuenta = rs.getString("cuenta");
				return cuenta.equals("MAESTRA");
			}
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		} finally {
			bd.close(connection, rs, ps);
		}
		return false;
	}
}