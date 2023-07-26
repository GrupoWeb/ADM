package mx.gob.se.rug.seguridad.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.org.apache.xpath.internal.operations.Bool;
import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.inscripcion.to.VigenciaTO;
import mx.gob.se.rug.masiva.to.string.Vigencia;
import mx.gob.se.rug.seguridad.to.MenuTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class MenuDAO {

	public UsuarioTO getPerfil(UsuarioTO usuarioTO) {
		System.out.println("Get Perfil");
		if(usuarioTO.getPersona()!=null){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "select CVE_PERFIL from V_USUARIO_SESION_RUG where ID_PERSONA=?";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
                       
			MyLogger.Logger.log(Level.INFO, "idpersona 3 "		+ usuarioTO.getPersona().getIdPersona());
			ps.setInt(1, usuarioTO.getPersona().getIdPersona());
			rs = ps.executeQuery();
			if (rs.next()) {
				usuarioTO.getPersona().setPerfil(rs.getString("CVE_PERFIL"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		}else{
			MyLogger.Logger.log(Level.WARNING, "MENU DAO ID PERSONA NULL ------ -- ----- -- -----");
		}
		return usuarioTO;
	}

	public MenuTO getMenuPrincipal(MenuTO menuTO, UsuarioTO usuarioTO) {
		System.out.println("Get Menu Principal");
		usuarioTO=	 getPerfil( usuarioTO);
		if (!usuarioTO.getPersona().getPerfil().equalsIgnoreCase("AUTORIDAD")
				&& !usuarioTO.getPersona().getPerfil().equalsIgnoreCase("ROOT")) {
			if (getAcreedoresByIdUser(usuarioTO.getPersona().getIdPersona())
					.booleanValue()) {

				ConexionBD bd = new ConexionBD();
				Connection connection = null;
				List<String> html = new ArrayList<String>();
				String sql = "Select DISTINCT HTML ,ORDEN from V_USUARIO_ACREEDOR_GRUPOS Where ID_SUB_USUARIO = ? AND ID_RECURSO = 1  and ORDEN not in(6) ORDER BY ORDEN";
				ResultSet rs = null;
				PreparedStatement ps = null;
				try {
					connection = bd.getConnection();
					ps = connection.prepareStatement(sql);
					MyLogger.Logger.log(Level.INFO, "idpersona 1 "
							+ usuarioTO.getPersona().getIdPersona());

					ps.setInt(1, usuarioTO.getPersona().getIdPersona());

					rs = ps.executeQuery();
					while (rs.next()) {
						html.add(rs.getString("HTML").replace("@contextPath",
								menuTO.getContextPath()));
					}
					menuTO.setHtml(html);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					bd.close(connection, rs, ps);
				}
			} else {
				MyLogger.Logger.log(Level.INFO, "getMenuPrincipal --INICIA NORMAL___");
				menuTO = getMenu(menuTO, usuarioTO);
			}
		} else {
			MyLogger.Logger.log(Level.INFO, "getMenuPrincipal --INICIA NORMAL___");
			menuTO = getMenu(menuTO, usuarioTO);
		}
		return menuTO;
	}

	public Boolean verificarGarantiaPermiso(Integer idPersona,Integer idTramite, Integer idGarantia){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		List<String> registro = new ArrayList<String>();
		PreparedStatement ps = null;
		String query = "SELECT ID FROM  RUG_GARANTIA_PERMISO WHERE ID_GARANTIA = ? AND ID_TRAMITE = ? AND ID_USUARIO = ? AND SIT = 'AC'";
		try{
			connection = bd.getConnection();
			ps = connection.prepareStatement(query);
			ps.setInt(1, idGarantia);
			ps.setInt(2, idTramite);
			ps.setInt(3, idPersona);
			rs = ps.executeQuery();
			while (rs.next()) {
				registro.add(rs.getString("ID"));
			}
			if(registro.size() > 0){
				return true;
			}else{
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}finally {
			bd.close(connection, rs, ps);
		}
	}

	public Boolean verificarEsAdmin(Integer idPersona)
	{
		Boolean response = false;
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = "SELECT NVL(IS_ADMIN, 0) AS cuenta FROM RUG_SECU_USUARIOS WHERE ID_PERSONA= ?";
		try{
			connection = bd.getConnection();
			ps = connection.prepareStatement(query);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()) {
				String cuenta = rs.getString("cuenta");
				if(cuenta.equals("1"))
				{
					response = true;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new JdbcDaoException(e);
		}finally {
			bd.close(connection, rs, ps);
		}
		return response;
	}
	public Boolean verificarMaestra(Integer idPersona)
	{
		Boolean response = false;
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = "SELECT NVL(cve_usuario_padre, 'MAESTRA') AS cuenta FROM V_USUARIO_SESION_RUG WHERE ID_PERSONA= ?";
		try{
			connection = bd.getConnection();
			ps = connection.prepareStatement(query);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()) {
				String cuenta = rs.getString("cuenta");
				if(cuenta.equals("MAESTRA"))
				{
					response = true;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new JdbcDaoException(e);
		}finally {
			bd.close(connection, rs, ps);
		}
		return response;
	}

	public MenuTO getMenuSub(Integer idAcreedor, UsuarioTO usuarioTO, MenuTO menuTO, Boolean vigencia) {
		usuarioTO=	 getPerfil(usuarioTO);
		if (!usuarioTO.getPersona().getPerfil().equalsIgnoreCase("AUTORIDAD") && !usuarioTO.getPersona().getPerfil().equalsIgnoreCase("ROOT")) {
				ConexionBD bd = new ConexionBD();
				Connection connection = null;
				ResultSet rs = null;
				PreparedStatement ps = null;
				GarantiasDAO gdao = new GarantiasDAO();
				idAcreedor = gdao.getCuentaMaestra(idAcreedor.longValue()).intValue();
				List<String> html = new ArrayList<String>();
				String sql = "Select DISTINCT HTML ,ORDEN  from V_USUARIO_ACREEDOR_GRUPOS Where ID_ACREEDOR = ? AND ID_SUB_USUARIO = ? AND ID_RECURSO = 2 ";
				if(vigencia!=null && !vigencia) {
					sql	+= " AND ID_PRIVILEGIO = 9";
				}
				sql	+= " ORDER BY ORDEN";

				try {
					connection = bd.getConnection();
					ps = connection.prepareStatement(sql);
					ps.setInt(1, idAcreedor);
					ps.setInt(2, usuarioTO.getPersona().getIdPersona());

					rs = ps.executeQuery();
					while (rs.next()) {
						html.add(rs.getString("HTML").replace("@contextPath",menuTO.getContextPath()));
					}
					menuTO.setHtml(html);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					bd.close(connection, rs, ps);
				}

		} else {
			menuTO = getMenu(menuTO, usuarioTO);
		}
		return menuTO;
	}
	

	public MenuTO getMenuSub(Integer idAcreedor, UsuarioTO usuarioTO, MenuTO menuTO, Boolean vigencia, Integer idTramite, Integer idGarantia) {
		usuarioTO=	 getPerfil(usuarioTO);
		if (!usuarioTO.getPersona().getPerfil().equalsIgnoreCase("AUTORIDAD") && !usuarioTO.getPersona().getPerfil().equalsIgnoreCase("ROOT")) {
			if(verificarGarantiaPermiso(usuarioTO.getPersona().getIdPersona(), idTramite, idGarantia) || verificarEsAdmin(usuarioTO.getPersona().getIdPersona()) || verificarMaestra(usuarioTO.getPersona().getIdPersona())){
				ConexionBD bd = new ConexionBD();
				Connection connection = null;
				ResultSet rs = null;
				PreparedStatement ps = null;
				List<String> html = new ArrayList<String>();
				String sql = "SELECT HTML, ORDEN FROM RUG_PRIVILEGIOS WHERE ID_PRIVILEGIO in (74,75,76,77,78)";
				sql	+= " ORDER BY ORDEN";

				try {
					connection = bd.getConnection();
					ps = connection.prepareStatement(sql);
					rs = ps.executeQuery();
					while (rs.next()) {
						html.add(rs.getString("HTML").replace("@contextPath",menuTO.getContextPath()));
					}
					menuTO.setHtml(html);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					bd.close(connection, rs, ps);
				}
			}else{
				ConexionBD bd = new ConexionBD();
				Connection connection = null;
				ResultSet rs = null;
				PreparedStatement ps = null;
				GarantiasDAO gdao = new GarantiasDAO();
				idAcreedor = gdao.getCuentaMaestra(idAcreedor.longValue()).intValue();
				List<String> html = new ArrayList<String>();
				String sql = "Select DISTINCT HTML ,ORDEN  from V_USUARIO_ACREEDOR_GRUPOS Where ID_ACREEDOR = ? AND ID_SUB_USUARIO = ? AND ID_RECURSO = 2 ";
				if(vigencia!=null && !vigencia) {
					sql	+= " AND ID_PRIVILEGIO = 9";
				}
				sql	+= " ORDER BY ORDEN";

				System.out.println("URL menu " + sql + " parametros " + idAcreedor + " " + usuarioTO.getPersona().getIdPersona());
				try {
					connection = bd.getConnection();
					ps = connection.prepareStatement(sql);
					ps.setInt(1, idAcreedor);
					ps.setInt(2, usuarioTO.getPersona().getIdPersona());

					rs = ps.executeQuery();
					while (rs.next()) {
						html.add(rs.getString("HTML").replace("@contextPath",menuTO.getContextPath()));
					}
					menuTO.setHtml(html);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					bd.close(connection, rs, ps);
				}
			}
		} else {
			menuTO = getMenu(menuTO, usuarioTO);
		}
		return menuTO;
	}


	public MenuTO getMenuAnotacionSinGarantia(UsuarioTO usuarioTO,
			MenuTO menuTO) {
		System.out.println("Get Menu Anotacion Sin Garantia");
		usuarioTO=	 getPerfil( usuarioTO);
			if (!usuarioTO.getPersona().getPerfil().equalsIgnoreCase("AUTORIDAD")
				&& !usuarioTO.getPersona().getPerfil().equalsIgnoreCase("ROOT")) {
			MyLogger.Logger.log(Level.INFO, "submenuAnotacionSinGarantia-----Inicia subAcreedor");
			// if(getAcreedoresByIdUser(usuarioTO.getPersona().getIdPersona()).booleanValue()){
			MyLogger.Logger.log(Level.INFO, "submenu de  acreedor test 2"+ usuarioTO.getPersona().getIdPersona());
			ConexionBD bd = new ConexionBD();
			Connection connection = null;
			List<String> html = new ArrayList<String>();
			String sql = "Select DISTINCT HTML ,ORDEN  from V_USUARIO_ACREEDOR_GRUPOS Where ID_SUB_USUARIO = ? AND ID_RECURSO = 11 ORDER BY ORDEN";
			ResultSet rs = null;
			PreparedStatement ps = null;
			try {
				connection = bd.getConnection();
				ps = connection.prepareStatement(sql);
				MyLogger.Logger.log(Level.INFO, "idpersona 2"
						+ usuarioTO.getPersona().getIdPersona());

				ps.setInt(1, usuarioTO.getPersona().getIdPersona());

				rs = ps.executeQuery();
				while (rs.next()) {
					html.add(rs.getString("HTML").replace("@contextPath",
							menuTO.getContextPath()));
				}
				menuTO.setHtml(html);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				bd.close(connection, rs, ps);
			}
		} else {
			MyLogger.Logger.log(Level.INFO, "submenuAnotacionSinGarantia de  grupo definido------------------------------------");
			menuTO = getMenu(menuTO, usuarioTO);
		}
		return menuTO;
	}

	public Boolean getJudicialMenu(Integer idUser) {
		System.out.println("Get Menu Judicial");
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "select is_judicial from rug_secu_usuarios where id_persona = ? and is_judicial is not null";
		ResultSet rs = null;
		PreparedStatement ps = null;
		Boolean flag = new Boolean(false);
		try {
			connection = bd.getConnection();
			ps=connection.prepareStatement(sql);
			MyLogger.Logger.log(Level.INFO, "idpersona 3" + idUser);

			ps.setInt(1, idUser);

			rs = ps.executeQuery();
			if (rs.next()) {
				MyLogger.Logger.log(Level.INFO, "is_judicial:::::"
						+ rs.getString("is_judicial"));
				flag = new Boolean(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return flag;
	}

	public Boolean getAcreedoresByIdUser(Integer idUser) {
		System.out.println("Get Acreedores Por Usuario");
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "SELECT DISTINCT ID_ACREEDOR FROM V_USUARIO_ACREEDOR  WHERE ID_USUARIO =?  AND B_FIRMADO = 'Y'";
		ResultSet rs = null;
		PreparedStatement ps = null;
		Boolean flag = new Boolean(false);
		try {
			connection = bd.getConnection();
			ps=connection.prepareStatement(sql);
			MyLogger.Logger.log(Level.INFO, "idpersona 4 " + idUser);

			ps.setInt(1, idUser);

			rs = ps.executeQuery();
			if (rs.next()) {
				MyLogger.Logger.log(Level.INFO, "id_acreedor::::: Certificaciones "
						+ rs.getString("id_acreedor"));
				flag = new Boolean(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return flag;

	}

	public MenuTO getMenu(MenuTO menuTO, UsuarioTO usuarioTO) {
		System.out.println("Get Menu " + menuTO.getIdMenu() + " usuario "+ usuarioTO.getPersona().getIdPersona());
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		List<String> html = new ArrayList<String>();
		String sql = "SELECT  RP.HTML" + " FROM RUG_SECU_USUARIOS RSU,"
				+ " RUG_GRUPOS RG," + " RUG_REL_GRUPO_PRIVILEGIO RRGP,"
				+ " RUG_PRIVILEGIOS RP," + " RUG_SECU_PERFILES_USUARIO RSPU,"
				+ " RUG_RECURSOS RR" + " WHERE RSU.ID_GRUPO = RG.ID_GRUPO"
				+ " AND RR.ID_RECURSO = RP.ID_RECURSO"
				+ " AND RSU.ID_GRUPO = RRGP.ID_GRUPO"
				+ " AND RRGP.ID_PRIVILEGIO = RP.ID_PRIVILEGIO"
				+ " AND RSU.CVE_USUARIO = RSPU.CVE_USUARIO"
				+ " AND RSU.ID_PERSONA=?" + " AND RP.ID_RECURSO=?"
				+ " ORDER BY RP.ORDEN";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			MyLogger.Logger.log(Level.INFO, "idRecurso" + menuTO.getIdMenu());
			MyLogger.Logger.log(Level.INFO, "idpersona 5"
					+ usuarioTO.getPersona().getIdPersona());

			ps.setInt(1, usuarioTO.getPersona().getIdPersona());
			ps.setInt(2, menuTO.getIdMenu());

			rs = ps.executeQuery();
			while (rs.next()) {
				html.add(rs.getString("HTML").replace("@contextPath",
						menuTO.getContextPath()));
			}
			menuTO.setHtml(html);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return menuTO;
	}

}
