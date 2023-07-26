package mx.gob.se.rug.usuario.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.to.UsuarioTO;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.usuario.dao.ControlUsuarioDao;
import mx.gob.se.rug.util.MyLogger;

/**
 * @author Getsemani Correa
 * 
 */
public class ControlUsuarioDaoJdbcImpl implements ControlUsuarioDao {

	@Override
	public boolean relationUsuario(UsuarioTO usuarioTO, Integer idUsuario, Integer idGrupo) {
		boolean regresa = false;
		if (usuarioTO == null) {
			return false;
		}
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		String sql = "{call RUG.SP_REL_GRUPO_ACREEDOR (" + "?," + // peIdUsuarioHijo
				"?," + // peIdAcreedor
				"?," + // peIdUsuarioPadre
				"?," + // peIdGrupo
				"?," + // psResult
				"?) }";// psTxResult
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, usuarioTO.getIdPersona() == null ? 0 : usuarioTO
					.getIdPersona());
			cs.setInt(2, usuarioTO.getIdAcreedor() == null ? 0 : usuarioTO
					.getIdAcreedor());
			cs.setInt(3, idUsuario == null ? 0 : idUsuario);
			cs.setInt(4, idGrupo == null ? 0 :idGrupo);
			cs.registerOutParameter(5, Types.INTEGER); // pepsResult OUT NUMBER,
			cs.registerOutParameter(6, Types.VARCHAR); // pepsTxResult OUT
														// VARCHAR2

			MyLogger.Logger.log(Level.INFO, "UsusarioDaoJdbcImpl: PL = " + sql);
			MyLogger.Logger.log(Level.INFO, "UsusarioDaoJdbcImpl: UsuarioTO = " + usuarioTO);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "UsusarioDaoJdbcImpl: Integer Result = "
					+ cs.getInt(5));
			MyLogger.Logger.log(Level.INFO, "UsusarioDaoJdbcImpl: String Result = "
					+ cs.getString(6));

			if (cs.getInt(5) == 0){
				regresa = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}

		return regresa;
	}

	@Override
	public boolean saveUsuario(UsuarioTO usuarioTO, Integer idAcreedor,
			String operacion) {
		boolean regresa = false;
		if (usuarioTO == null) {
			return false;
		}
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		String sql = "{call RUG.SP_REGISTRO_USUARIO_ACREEDOR (?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) }";
		try {
			cs = connection.prepareCall(sql);

			cs.setInt(1, idAcreedor == null ? 0 : idAcreedor);// peIdAcreedor IN
																// NUMBER,
			cs.setString(2, operacion);// peOperacion IN VARCHAR2,
			cs.setInt(3, usuarioTO.getIdPersona() == null ? 0 : usuarioTO
					.getIdPersona());// peIdPersonaModif IN NUMBER,
										// --IdPersonaModifcar solo Modif y
										// Eliminacion
			cs.setString(4, usuarioTO.getNombre());// peNombre IN VARCHAR2,
			cs.setString(5, usuarioTO.getApaterno());// peApellidoP IN VARCHAR2,
			cs.setString(6, usuarioTO.getAmaterno());// peApellidoM IN VARCHAR2,
			cs.setString(7, usuarioTO.getRfc());// peRFC IN VARCHAR,
			cs.setString(8, usuarioTO.getCveUsuario());// peCuentaCorreo IN
														// VARCHAR2,
														// --peCveUsuario
			cs.setString(9, usuarioTO.getPassword());// pePassword IN
														// RUG_SECU_USUARIOS.PASSWORD%TYPE,
			cs.setString(10, usuarioTO.getIdGrupo() == null ? "0" : usuarioTO
					.getIdGrupo().toString());// peIdGrupo IN VARCHAR2,
			cs.setInt(11, usuarioTO.getIdRepresentanteAcreedor() == null ? 0
					: usuarioTO.getIdRepresentanteAcreedor());// peRepresentanteAcreedor
																// IN
																// NUMBER,--peIdUsuario
			cs.registerOutParameter(12, Types.INTEGER); // psIdTramiteIncomp OUT
														// NUMBER,
			cs.registerOutParameter(13, Types.INTEGER); // psIdPersonaAlta OUT
														// NUMBER,
			cs.registerOutParameter(14, Types.INTEGER);// psResult OUT NUMBER,
			cs.registerOutParameter(15, Types.VARCHAR);// psTxResult OUT
														// VARCHAR2

			MyLogger.Logger.log(Level.INFO, "UsusarioDaoJdbcImpl: PL = " + sql);
			MyLogger.Logger.log(Level.INFO, "UsusarioDaoJdbcImpl: UsuarioTO = " + usuarioTO
					+ ", idAcredor=" + idAcreedor);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "UsusarioDaoJdbcImpl: Integer Result = "
					+ cs.getInt(14));
			MyLogger.Logger.log(Level.INFO, "UsusarioDaoJdbcImpl: String Result = "
					+ cs.getString(15));

			if (cs.getInt(14)==0){
				regresa = true;				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}

		return regresa;
	}

	/*
	 * RUG.V_USUARIOS_ACREEDORES (
	 * 
	 * ID_TRAMITE_TEMP, --tramiteTemporal ID_ACREEDOR, --IdAcreedorRepresentado
	 * ID_USUARIO_LOGIN, -- IdUsuario que da de alta ID_SUBUSUARIO, --Id usuario
	 * dado de alta CVE_USUARIO, --Mail del usuario dado de alta
	 * NOMBRE_COMPLETO, --Nombre completo del usuario dado de alta PERFIL,
	 * --Perfil del usuario dado de alta ID_GRUPO, --Id de Grupo del usuario
	 * dado de alta DESC_GRUPO, --Descripcion del grupo al que pertenece el
	 * usuario dado de alta B_FIRMADO --bandera que indica si el usuario esta
	 * firmado o no, valores posibles: 'Y', 'N'
	 */
	@Override
	public List<UsuarioTO> getUsuariosFirmados(Connection connection,
			Integer idUsuario, Integer idAcreedor) {
		MyLogger.Logger.log(Level.INFO, "entro en: getUsuariosFirmados");
		// TODO : Agregar columna status_rel
		List<UsuarioTO> lista = new ArrayList<UsuarioTO>();
		String sql = "SELECT ID_TRAMITE_TEMP, ID_ACREEDOR, ID_USUARIO_LOGIN, ID_SUBUSUARIO, CVE_USUARIO, NOMBRE_COMPLETO, "
				+ " PERFIL, ID_GRUPO, DESC_GRUPO, B_FIRMADO FROM RUG.V_USUARIOS_ACREEDORES WHERE "
				+ " ID_ACREEDOR = ? AND B_FIRMADO = 'Y' AND STATUS_CUENTA = 'AC' AND STATUS_REL = 'AC' AND ID_SUBUSUARIO != ? ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		MyLogger.Logger.log(Level.INFO, "entro a getUsuariosFirmados" + idUsuario + "-"
				+ idAcreedor);
		try {
			ps = connection.prepareStatement(sql);
			// ps.setInt(1, idUsuario);
			ps.setInt(1, idAcreedor);
			ps.setInt(2, idUsuario);
			rs = ps.executeQuery();
			UsuarioTO usuarioTO;
			while (rs.next()) {
				usuarioTO = new UsuarioTO();
				usuarioTO.setIdTramiteTemporal(rs.getInt("ID_TRAMITE_TEMP"));
				usuarioTO.setIdAcreedor(rs.getInt("ID_ACREEDOR"));
				usuarioTO.setIdPersona(rs.getInt("ID_USUARIO_LOGIN"));
				usuarioTO.setIdSubusuario(rs.getInt("ID_SUBUSUARIO"));
				usuarioTO.setCveUsuario(rs.getString("CVE_USUARIO"));
				usuarioTO.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
				usuarioTO.setPerfil(rs.getString("PERFIL"));
				usuarioTO.setIdGrupo(rs.getInt("ID_GRUPO"));
				usuarioTO
						.setbFirmado(getBooleanByTxt(rs.getString("B_FIRMADO")));
				usuarioTO.setDesGrupo(rs.getString("DESC_GRUPO"));
				lista.add(usuarioTO);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	private boolean getBooleanByTxt(String bandera) {
		boolean regresa = false;
		if (bandera.equals("Y")) {
			regresa = true;
		}
		return regresa;
	}

	@Override
	public List<UsuarioTO> getUsuariosNoFirmados(Connection connection,
			Integer idUsuario, Integer idAcreedor) {
		MyLogger.Logger.log(Level.INFO, "entro a getUsuariosNoFirmados del DAO" + idUsuario
				+ "-" + idAcreedor);
		// TODO : Agregar columna status_rel
		List<UsuarioTO> lista = new ArrayList<UsuarioTO>();
		String sql = "SELECT ID_TRAMITE_TEMP, ID_ACREEDOR, ID_USUARIO_LOGIN, ID_SUBUSUARIO, CVE_USUARIO, NOMBRE_COMPLETO, "
				+ " PERFIL, ID_GRUPO, DESC_GRUPO, B_FIRMADO FROM RUG.V_USUARIOS_ACREEDORES WHERE "
				+ " ID_ACREEDOR = ? AND B_FIRMADO = 'N'  AND STATUS_REL = 'AC' AND ID_SUBUSUARIO != ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// MyLogger.Logger.log(Level.INFO, "idUsuario" + idUsuario);
			MyLogger.Logger.log(Level.INFO, "idAcreedor" + idAcreedor);
			ps = connection.prepareStatement(sql);
			// ps.setInt(1, idUsuario);
			ps.setInt(1, idAcreedor);
			ps.setInt(2, idUsuario);
			rs = ps.executeQuery();
			UsuarioTO usuarioTO;
			while (rs.next()) {
				usuarioTO = new UsuarioTO();
				usuarioTO.setIdTramiteTemporal(rs.getInt("ID_TRAMITE_TEMP"));
				usuarioTO.setIdAcreedor(rs.getInt("ID_ACREEDOR"));
				usuarioTO.setIdPersona(rs.getInt("ID_USUARIO_LOGIN"));
				usuarioTO.setIdSubusuario(rs.getInt("ID_SUBUSUARIO"));
				usuarioTO.setCveUsuario(rs.getString("CVE_USUARIO"));
				usuarioTO.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
				usuarioTO.setPerfil(rs.getString("PERFIL"));
				usuarioTO.setIdGrupo(rs.getInt("ID_GRUPO"));
				usuarioTO.setDesGrupo(rs.getString("DESC_GRUPO"));
				usuarioTO
						.setbFirmado(getBooleanByTxt(rs.getString("B_FIRMADO")));
				lista.add(usuarioTO);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	public UsuarioTO getBySubUsuario(Connection connection, String idSubUsuario) {
		MyLogger.Logger.log(Level.INFO, "entro en : getBySubUsuario");

		UsuarioTO usuarioTO = null;
		String sql = "SELECT ID_PERSONA, E_MAIL, NOMBRE_PERSONA, AP_PATERNO, AP_MATERNO, RFC, CVE_PERFIL, ID_GRUPO FROM V_USUARIOS_ALL WHERE ID_PERSONA = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(idSubUsuario));
			rs = ps.executeQuery();
			if (rs.next()) {
				usuarioTO = new UsuarioTO();
				usuarioTO.setIdPersona(rs.getInt("ID_PERSONA"));
				usuarioTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				usuarioTO.setApaterno(rs.getString("AP_PATERNO"));
				usuarioTO.setAmaterno(rs.getString("AP_MATERNO"));
				usuarioTO.setRfc(rs.getString("RFC"));
				usuarioTO.setPerfil(rs.getString("CVE_PERFIL"));
				usuarioTO.setIdGrupo(rs.getInt("ID_GRUPO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		MyLogger.Logger.log(Level.INFO, "salio de: getBySubUsuario");
		return usuarioTO;
	}

	@Override
	public UsuarioTO getByCorreoElectronico(Connection connection,
			String correoElectronico) {
		MyLogger.Logger.log(Level.INFO, "entro en : getByCorreoElectronico");
		UsuarioTO usuarioTO = null;
		String sql = "SELECT ID_PERSONA, E_MAIL, NOMBRE_PERSONA, AP_PATERNO, AP_MATERNO, NOMBRE_COMPLETO, RFC, CVE_PERFIL, ID_GRUPO FROM V_USUARIOS_ALL WHERE E_MAIL = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, correoElectronico);
			rs = ps.executeQuery();
			if (rs.next()) {
				usuarioTO = new UsuarioTO();
				usuarioTO.setIdPersona(rs.getInt("ID_PERSONA"));
				usuarioTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				usuarioTO.setApaterno(rs.getString("AP_PATERNO"));
				usuarioTO.setAmaterno(rs.getString("AP_MATERNO"));
				usuarioTO.setRfc(rs.getString("RFC"));
				usuarioTO.setPerfil(rs.getString("CVE_PERFIL"));
				usuarioTO.setIdGrupo(rs.getInt("ID_GRUPO"));
				usuarioTO.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		MyLogger.Logger.log(Level.INFO, "salio de: getByCorreoElectronico");
		return usuarioTO;
	}

	@Override
	public UsuarioTO getByCorreoElectronicoAcredor(Connection connection,
			String correoElectronico, String idAcreedor) {
		// TODO : Agregar columna status_rel
		MyLogger.Logger.log(Level.INFO, "entro en : getByCorreoElectronicoAcredor");
		UsuarioTO usuarioTO = null;
		String sql = "SELECT " +
				" ID_SUBUSUARIO,  NOMBRE_PERSONA, AP_PATERNO, AP_MATERNO, RFC, PERFIL, ID_GRUPO FROM V_USUARIOS_ACREEDORES WHERE CVE_USUARIO = ? AND ID_ACREEDOR = ? AND STATUS_REL = 'AC'";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, correoElectronico);
			ps.setInt(2, Integer.valueOf(idAcreedor));
			rs = ps.executeQuery();
			if (rs.next()) {
				usuarioTO = new UsuarioTO();
				usuarioTO.setIdPersona(rs.getInt("ID_SUBUSUARIO"));
				usuarioTO.setNombre(rs.getString("NOMBRE_PERSONA"));
				usuarioTO.setApaterno(rs.getString("AP_PATERNO"));
				usuarioTO.setAmaterno(rs.getString("AP_MATERNO"));
				usuarioTO.setRfc(rs.getString("RFC"));
				usuarioTO.setPerfil(rs.getString("PERFIL"));
				usuarioTO.setIdGrupo(rs.getInt("ID_GRUPO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		MyLogger.Logger.log(Level.INFO, "salio de: getByCorreoElectronicoAcredor");
		return usuarioTO;
	}

}
