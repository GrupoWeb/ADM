package mx.gob.se.rug.operaciones.dao;

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
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.RugException;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.to.OtorganteTO;
import mx.gob.se.rug.operaciones.to.CargaMasivaResumenTO;
import mx.gob.se.rug.operaciones.to.OperacionesTO;
import mx.gob.se.rug.util.CharSetUtil;
import mx.gob.se.rug.util.MyLogger;



public class OperacionesDAO {
	public List<OperacionesTO> muestraOpPendientesFirmaSinDueno(Integer idAcreedor){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		List <OperacionesTO> listaPendientesFirma = new ArrayList<OperacionesTO>();
		String sql = "select ID_TRAMITE_TEMP, ID_TIPO_TRAMITE, DESC_TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, ID_GARANTIA_MODIFICAR, DESCRIP_STATUS, URL, DESC_GARANTIA from V_TRAMITES_REASIGNADOS where ID_ACREEDOR=? AND ID_STATUS_TRAM=5";
		try {
			connection = bd.getConnection();
		ps=	connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			rs = ps.executeQuery();
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));
				operacionesTO.setIdTramiteTemporal(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setTipoTransaccion(rs.getString("DESC_TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA_PEND")==0){				
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				if (rs.getString("ID_GARANTIA_MODIFICAR")!=null){
					if (rs.getInt("ID_GARANTIA_MODIFICAR")==0){						
						operacionesTO.setIdGarantiaModificar("N/A");
					}else{						
						operacionesTO.setIdGarantiaModificar(rs.getString("ID_GARANTIA_MODIFICAR"));						
					}
				}else{					
					operacionesTO.setIdGarantiaModificar("N/A");					
				}				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));			
				operacionesTO.setPaso(rs.getString("URL"));
				operacionesTO.setDescripcionGeneral(rs.getString("DESC_GARANTIA"));				
				listaPendientesFirma.add(operacionesTO);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesFirma;
	}
	public List<OperacionesTO> muestraOpPendientesSinDueno(Integer idAcreedor){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaPendientes = new ArrayList<OperacionesTO>();
		String sql = "select ID_TRAMITE_TEMP, ID_TIPO_TRAMITE, DESC_TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, DESCRIP_STATUS, URL from V_TRAMITES_REASIGNADOS where ID_STATUS_TRAM=1 AND ID_ACREEDOR = ?";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			rs = ps.executeQuery();
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP"));				
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));
				operacionesTO.setTipoTransaccion(rs.getString("DESC_TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));				
				if (rs.getInt("ID_GARANTIA_PEND")==0){
					if (rs.getInt("ID_TIPO_TRAMITE")==1){						
						operacionesTO.setNumGarantia("PENDIENTE");
					}else{			
						operacionesTO.setNumGarantia("N/A");						
					}
				}else{					
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));				
				operacionesTO.setPaso(rs.getString("URL"));				
				listaPendientes.add(operacionesTO);
			}
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,null);
		}
		return listaPendientes;
	}
	public Integer getIdTramitebyIdGarantia(Integer idGarantia){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "select ID_TRAMITE from V_DETALLE_GARANTIA where id_garantia = ? order by id_tramite desc";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("ID_TRAMITE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	
	
	public List<UsuarioTO> getUsuarios(Connection connection, Integer idAcreedor) {

		// TODO : Agregar columna status_rel
		List<UsuarioTO> lista = new ArrayList<UsuarioTO>();
		String sql = "SELECT ID_TRAMITE_TEMP, ID_ACREEDOR, ID_USUARIO_LOGIN, ID_SUBUSUARIO, CVE_USUARIO, NOMBRE_COMPLETO, "
				+ " PERFIL, ID_GRUPO, DESC_GRUPO, B_FIRMADO FROM RUG.V_USUARIOS_ACREEDORES WHERE "
				+ " ID_ACREEDOR = ? AND B_FIRMADO = 'Y' AND STATUS_CUENTA = 'AC' AND STATUS_REL = 'AC' ";
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		try {
			ps = connection.prepareStatement(sql);
			// ps.setInt(1, idUsuario);
			ps.setInt(1, idAcreedor);

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
				usuarioTO.setbFirmado(getBooleanByTxt(rs.getString("B_FIRMADO")));
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
	
	
	public UsuarioTO getUsuario(Connection connection, Integer idSubUsuario) {
		MyLogger.Logger.log(Level.WARNING, "entro en: getUsuariosFirmados");
		UsuarioTO usuarioTO = new UsuarioTO();
		String sql = "SELECT ID_TRAMITE_TEMP, ID_ACREEDOR, ID_USUARIO_LOGIN, ID_SUBUSUARIO, CVE_USUARIO, NOMBRE_COMPLETO, "
				+ " PERFIL, ID_GRUPO, DESC_GRUPO, B_FIRMADO FROM RUG.V_USUARIOS_ACREEDORES WHERE "
				+ " B_FIRMADO = 'Y' AND STATUS_CUENTA = 'AC' AND STATUS_REL = 'AC' AND ID_SUBUSUARIO = ? ";
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		try {
			ps = connection.prepareStatement(sql);

			ps.setInt(1, idSubUsuario);

			rs = ps.executeQuery();
			
			if (rs.next()) {
				usuarioTO = new UsuarioTO();
				usuarioTO.setIdTramiteTemporal(rs.getInt("ID_TRAMITE_TEMP"));
				usuarioTO.setIdAcreedor(rs.getInt("ID_ACREEDOR"));
				usuarioTO.setIdPersona(rs.getInt("ID_USUARIO_LOGIN"));
				usuarioTO.setIdSubusuario(rs.getInt("ID_SUBUSUARIO"));
				usuarioTO.setCveUsuario(rs.getString("CVE_USUARIO"));
				usuarioTO.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
				usuarioTO.setPerfil(rs.getString("PERFIL"));
				usuarioTO.setIdGrupo(rs.getInt("ID_GRUPO"));
				usuarioTO.setbFirmado(getBooleanByTxt(rs.getString("B_FIRMADO")));
				usuarioTO.setDesGrupo(rs.getString("DESC_GRUPO"));
			

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
		return usuarioTO;
	}
	
	public List<OperacionesTO> muestraOpPendientes(Integer idPersona){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaPendientes = new ArrayList<OperacionesTO>();
		
		String sql = "select ID_TRAMITE_TEMP, ID_TIPO_TRAMITE, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, DESCRIP_STATUS, URL from V_TRAMITES_PENDIENTES where ID_PERSONA_LOGIN=? AND ID_STATUS=1 AND ID_TIPO_TRAMITE = 1 AND TRAMITE_REASIGNADO = 'F'";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();			
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP")); 
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));				
				if (rs.getInt("ID_GARANTIA_PEND")==0){
					if (rs.getInt("ID_TIPO_TRAMITE")==1){				
						operacionesTO.setNumGarantia("PENDIENTE");
					}else{						
						operacionesTO.setNumGarantia("N/A");						
					}
				}else{					
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));			
				operacionesTO.setPaso(rs.getString("URL"));
				
				listaPendientes.add(operacionesTO);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientes;
	}
	
	//consulta para traer de inicio a fin de los tramites pendientes
	public List<OperacionesTO> muestraOpPendientes(Integer idPersona, Integer inicio, Integer fin){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaPendientes = new ArrayList<OperacionesTO>();
		
		String sql = "select ID_TRAMITE_TEMP, ID_TIPO_TRAMITE, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, DESCRIP_STATUS, URL,ID_TIPO_GARANTIA from (select rownum rn, ID_TRAMITE_TEMP, ID_TIPO_TRAMITE, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, DESCRIP_STATUS, URL,ID_TIPO_GARANTIA from RUG.V_TRAMITES_PENDIENTES where ID_PERSONA_LOGIN=? AND ID_STATUS=1 AND ID_TIPO_TRAMITE = 1 AND TRAMITE_REASIGNADO = 'F') where rn BETWEEN ? AND ?";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setInt(2, inicio);
			ps.setInt(3, fin);
			rs = ps.executeQuery();			
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP")); 
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				operacionesTO.setOtorgantes(altaParteDAO.getDeudorByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));				
				if (rs.getInt("ID_GARANTIA_PEND")==0){
					if (rs.getInt("ID_TIPO_TRAMITE")==1){				
						operacionesTO.setNumGarantia("PENDIENTE");
					}else{						
						operacionesTO.setNumGarantia("N/A");						
					}
				}else{					
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));			
				operacionesTO.setPaso(rs.getString("URL"));
				operacionesTO.setIdTipoGarantia(rs.getInt("ID_TIPO_GARANTIA"));
				listaPendientes.add(operacionesTO);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientes;
	}
	
	public List<OperacionesTO> muestraBusOpPendientes(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaPendientes = new ArrayList<OperacionesTO>();
		
		String sql = "select ID_TRAMITE_TEMP, ID_TIPO_TRAMITE, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, DESCRIP_STATUS, URL from (select rownum rn, ID_TRAMITE_TEMP, ID_TIPO_TRAMITE, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, DESCRIP_STATUS, URL from RUG.V_TRAMITES_PENDIENTES where ID_PERSONA_LOGIN=? AND ID_STATUS=1 AND ID_TIPO_TRAMITE = 1 AND TRAMITE_REASIGNADO = 'F' AND TO_DATE(FECHA_STATUS, 'DD/MM/RRRR')  BETWEEN TO_DATE(?, 'DD/MM/RRRR') AND TO_DATE(?, 'DD/MM/RRRR') ORDER BY ID_TRAMITE_TEMP DESC) where rn BETWEEN ? AND ?";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setString(2, dateStart);
			ps.setString(3, dateEnd);
			ps.setInt(4, inicio);
			ps.setInt(5, fin);
			rs = ps.executeQuery();			
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP")); 
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));				
				if (rs.getInt("ID_GARANTIA_PEND")==0){
					if (rs.getInt("ID_TIPO_TRAMITE")==1){				
						operacionesTO.setNumGarantia("PENDIENTE");
					}else{						
						operacionesTO.setNumGarantia("N/A");						
					}
				}else{					
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));			
				operacionesTO.setPaso(rs.getString("URL"));
				
				listaPendientes.add(operacionesTO);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientes;
	}
	
	public List<OperacionesTO> muestraBusOpPenByOtorgante(Integer idPersona, Integer inicio, Integer fin, String nomOtorgante){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaPendientes = new ArrayList<OperacionesTO>();
		
		String sql = "select ID_TRAMITE_TEMP, ID_TIPO_TRAMITE, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, DESCRIP_STATUS, URL from (select rownum rn, ID_TRAMITE_TEMP, ID_TIPO_TRAMITE, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, DESCRIP_STATUS, URL from RUG.V_TRAMITES_PENDIENTES where ID_PERSONA_LOGIN=? AND ID_STATUS=1 AND ID_TIPO_TRAMITE = 1 AND TRAMITE_REASIGNADO = 'F' AND NOMBRE LIKE trim(upper('%"+nomOtorgante+"%')) ORDER BY ID_TRAMITE_TEMP DESC) where rn BETWEEN ? AND ?";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
//			ps.setString(2, nomOtorgante);
			ps.setInt(2, inicio);
			ps.setInt(3, fin);
			rs = ps.executeQuery();			
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP")); 
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));				
				if (rs.getInt("ID_GARANTIA_PEND")==0){
					if (rs.getInt("ID_TIPO_TRAMITE")==1){				
						operacionesTO.setNumGarantia("PENDIENTE");
					}else{						
						operacionesTO.setNumGarantia("N/A");						
					}
				}else{					
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));			
				operacionesTO.setPaso(rs.getString("URL"));
				
				listaPendientes.add(operacionesTO);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientes;
	}
	
	public List<OperacionesTO> muestraBusOpPendFirma(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaPendientesFirma = new ArrayList<OperacionesTO>();
		
		String sql = 
		
		" SELECT ID_TRAMITE_TEMP, "+
	    "   TIPO_TRAMITE, "+
	     "  FECHA_STATUS, "+
	      " ID_GARANTIA_PEND, "+
	       "ID_GARANTIA_MODIFICAR, "+
	       "DESCRIP_STATUS, "+
	       "URL, "+
	       "DESC_GARANTIA "+
	  "FROM (SELECT ROWNUM RN, "+
	   "            ID_TRAMITE_TEMP, "+
	    "           TIPO_TRAMITE, "+
	     "          FECHA_STATUS, "+
	      "         ID_GARANTIA_PEND, "+
	       "        ID_GARANTIA_MODIFICAR, "+
	        "       DESCRIP_STATUS, "+
	         "      URL, "+
	          "     DESC_GARANTIA "+
	          "FROM RUG.V_TRAMITES_PENDIENTES "+
	         "WHERE     ID_PERSONA_LOGIN = ? "+
	          "     AND ID_STATUS = 5 "+
	           "    AND TRAMITE_REASIGNADO = 'F' "+
	            "   AND ID_TIPO_TRAMITE <> 18 "+
	           " AND TO_DATE(FECHA_STATUS, 'DD/MM/RRRR')  BETWEEN TO_DATE(?, 'DD/MM/RRRR') AND TO_DATE(?, 'DD/MM/RRRR') "+
	         " ORDER BY ID_TRAMITE_TEMP DESC ) "+
	 " WHERE RN BETWEEN ? AND ?";
		
		
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setString(2, dateStart);
			ps.setString(3, dateEnd);
			ps.setInt(4, inicio);
			ps.setInt(5, fin);
			rs = ps.executeQuery();
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));
				operacionesTO.setIdTramiteTemporal(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA_PEND")==0){				
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				if (rs.getString("ID_GARANTIA_MODIFICAR")!=null){
					if (rs.getInt("ID_GARANTIA_MODIFICAR")==0){						
						operacionesTO.setIdGarantiaModificar("N/A");
					}else{						
						operacionesTO.setIdGarantiaModificar(rs.getString("ID_GARANTIA_MODIFICAR"));						
					}
				}else{					
					operacionesTO.setIdGarantiaModificar("N/A");					
				}				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));			
				operacionesTO.setPaso(rs.getString("URL"));
				operacionesTO.setDescripcionGeneral(rs.getString("DESC_GARANTIA"));				
				listaPendientesFirma.add(operacionesTO);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesFirma;
	}
	
	public List<OperacionesTO> muestraBusOpPendFirmaByOtorgante(Integer idPersona, Integer inicio, Integer fin, String nomOtorgante){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaPendientesFirma = new ArrayList<OperacionesTO>();
		
		String sql = 
		
		" SELECT ID_TRAMITE_TEMP, "+
	    "   TIPO_TRAMITE, "+
	     "  FECHA_STATUS, "+
	      " ID_GARANTIA_PEND, "+
	       "ID_GARANTIA_MODIFICAR, "+
	       "DESCRIP_STATUS, "+
	       "URL, "+
	       "DESC_GARANTIA "+
	  "FROM (SELECT ROWNUM RN, "+
	   "            ID_TRAMITE_TEMP, "+
	    "           TIPO_TRAMITE, "+
	     "          FECHA_STATUS, "+
	      "         ID_GARANTIA_PEND, "+
	       "        ID_GARANTIA_MODIFICAR, "+
	        "       DESCRIP_STATUS, "+
	         "      URL, "+
	          "     DESC_GARANTIA "+
	          "FROM RUG.V_TRAMITES_PENDIENTES "+
	         "WHERE     ID_PERSONA_LOGIN = ? "+
	          "     AND ID_STATUS = 5 "+
	           "    AND TRAMITE_REASIGNADO = 'F' "+
	            "   AND ID_TIPO_TRAMITE <> 18 "+
	           "AND NOMBRE LIKE trim(upper('%"+nomOtorgante+"%'))"+
	         " ORDER BY ID_TRAMITE_TEMP DESC ) "+
	 " WHERE RN BETWEEN ? AND ?";
		
		
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setInt(2, inicio);
			ps.setInt(3, fin);
			rs = ps.executeQuery();
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));
				operacionesTO.setIdTramiteTemporal(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA_PEND")==0){				
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				if (rs.getString("ID_GARANTIA_MODIFICAR")!=null){
					if (rs.getInt("ID_GARANTIA_MODIFICAR")==0){						
						operacionesTO.setIdGarantiaModificar("N/A");
					}else{						
						operacionesTO.setIdGarantiaModificar(rs.getString("ID_GARANTIA_MODIFICAR"));						
					}
				}else{					
					operacionesTO.setIdGarantiaModificar("N/A");					
				}				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));			
				operacionesTO.setPaso(rs.getString("URL"));
				operacionesTO.setDescripcionGeneral(rs.getString("DESC_GARANTIA"));				
				listaPendientesFirma.add(operacionesTO);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesFirma;
	}
	
	public List<OperacionesTO> muestraBusOpTerminadas(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();
		
		String sql = 
		
		 "SELECT ID_TRAMITE, "+
	      " TIPO_TRAMITE, "+
	       "FECHA_STATUS, "+
	       "ID_GARANTIA, "+
	       "DESCRIP_STATUS, "+
	       "URL_PASO, "+
	       "ID_TIPO_TRAMITE "+
	  "FROM (  SELECT ROWNUM RN, "+
	   "              ID_TRAMITE, "+
	    "             DESCRIPCION AS TIPO_TRAMITE, "+
	     "            FECHA_CREACION AS FECHA_STATUS, "+
	      "           ID_GARANTIA, "+
	       "          DESCRIP_STATUS, "+
	        "         URL_PASO, "+
	         "        ID_TIPO_TRAMITE "+
	          "  FROM RUG.V_TRAMITES_TERMINADOS "+
	           "WHERE ID_PERSONA_LOGIN = ? "+
	            " AND TRAMITE_REASIGNADO = 'F' "+
	             "AND TO_DATE(FECHA_CREACION, 'DD/MM/RRRR') BETWEEN TO_DATE(?, 'DD/MM/RRRR')  AND TO_DATE(?, 'DD/MM/RRRR') "+
	        "ORDER BY FECHA_CREACION DESC) "+
	 "WHERE RN BETWEEN ? AND ?";
		
		
		
		
		
		
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setString(2, dateStart);
			ps.setString(3, dateEnd);
			ps.setInt(4, inicio);
			ps.setInt(5, fin);
			rs = ps.executeQuery();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
				}
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				operacionesTO.setOtorgantes(getOtorganteByTramite(rs.getInt("ID_TRAMITE")));	
				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				operacionesTO.setDescripcionGeneral("");
				listaTerminadas.add(operacionesTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally{
			bd.close(connection,rs,ps);
		}
		MyLogger.Logger.log(Level.INFO, "" + "::::::Tamaño de la lista que regresa: "+listaTerminadas.size());
		MyLogger.Logger.log(Level.INFO, "" + "::::::Consulta por rown"+sql);
		return listaTerminadas;
	}
	
	public List<OperacionesTO> muestraBusOpTermByOtorgante(Integer idPersona, Integer inicio, Integer fin, String nomOtorgante){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs =null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();
		
		String sql = 
				  " SELECT ID_TRAMITE, "+
				"  TIPO_TRAMITE, "+
				"  FECHA_STATUS, "+
				"  ID_GARANTIA, "+
				"  DESCRIP_STATUS, "+
				"  URL_PASO, "+
				"  ID_TIPO_TRAMITE "+
				"  FROM (  SELECT ROWNUM RN, "+
				"            ID_TRAMITE, "+
				"            DESCRIPCION AS TIPO_TRAMITE, "+
				"            FECHA_CREACION AS FECHA_STATUS, "+
				"            ID_GARANTIA, "+
				"            DESCRIP_STATUS, "+
				"            URL_PASO, "+
				"            ID_TIPO_TRAMITE "+
				"       FROM RUG.V_TRAMITES_TERMINADOS V "+
				"      WHERE     V.ID_PERSONA_LOGIN = ? "+
				"            AND UPPER (RUG.FNCONCATOTORGANTE (V.ID_TRAMITE, 1)) LIKE TRIM (UPPER ('%"+nomOtorgante+"%')) "+
				"            AND TRAMITE_REASIGNADO = 'F' "+
				"   ORDER BY FECHA_CREACION DESC) "+
				" WHERE RN BETWEEN ? AND ? ";
		
		
		
		
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
//			ps.setString(2, nomOtorgante);
			ps.setInt(2, inicio);
			ps.setInt(3, fin);
			rs = ps.executeQuery();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
				}
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				operacionesTO.setOtorgantes(getOtorganteByTramite(rs.getInt("ID_TRAMITE")));	
				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				operacionesTO.setDescripcionGeneral("");
				listaTerminadas.add(operacionesTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally{
			bd.close(connection,rs,ps);
		}
		MyLogger.Logger.log(Level.INFO, "" + "::::::Tamaño de la lista que regresa: "+listaTerminadas.size());
		MyLogger.Logger.log(Level.INFO, "" + "::::::Consulta: "+sql);
		return listaTerminadas;
	}
	
	
	
	public List<OperacionesTO> muestraOpPendientesPago(int idPersona){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps =null;
		ResultSet rs =null;
		List <OperacionesTO> listaPendientesPago = new ArrayList<OperacionesTO>();
		
		String sql = "select ID_TRAMITE_TEMP, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, DESCRIP_STATUS,  URL, DESC_GARANTIA from V_TRAMITES_PENDIENTES where ID_PERSONA_LOGIN = ? AND ID_STATUS = 2 AND TRAMITE_REASIGNADO = 'F'";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			MyLogger.Logger.log(Level.WARNING, "la consulta de pendientes de pago en el dao "+ sql);
			rs = ps.executeQuery();
			OperacionesTO operacionesTO;
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				operacionesTO = new OperacionesTO();
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setIdTramiteTemporal(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA_PEND")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				
				
				operacionesTO.setPaso(rs.getString("URL"));
				operacionesTO.setDescripcionGeneral(rs.getString("DESC_GARANTIA"));
				listaPendientesPago.add(operacionesTO);
			}
			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesPago;
	}
	
	public OperacionesTO getById(int idTramiteTemp) throws RugException{
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		OperacionesTO operacionesTO = null;
		
		String sql = "select ID_TRAMITE_TEMP, ID_TIPO_TRAMITE, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, ID_GARANTIA_MODIFICAR,  DESCRIP_STATUS,  URL, DESC_GARANTIA from V_TRAMITES_PENDIENTES where ID_TRAMITE_TEMP=? AND ID_STATUS=5";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);

			ps.setInt(1, idTramiteTemp);
			rs = ps.executeQuery();
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			if(rs.next()){
				
				operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setIdTramiteTemporal(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));
				if (rs.getInt("ID_GARANTIA_PEND")==0){
					
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				if (rs.getString("ID_GARANTIA_MODIFICAR")!=null){
					if (rs.getInt("ID_GARANTIA_MODIFICAR")==0){
						
						operacionesTO.setIdGarantiaModificar("N/A");
					}else{
						
						operacionesTO.setIdGarantiaModificar(rs.getString("ID_GARANTIA_MODIFICAR"));						
					}
				}
				
				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));

				operacionesTO.setPaso(rs.getString("URL"));
				operacionesTO.setDescripcionGeneral(rs.getString("DESC_GARANTIA"));
				
				
			}else{
				MyLogger.Logger.log(Level.WARNING, "No se encontro la operacion");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RugException("Error ",e);
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return operacionesTO;
	}
	
	public List<OperacionesTO> muestraOpPendientesFirma(Integer idPersona){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		List <OperacionesTO> listaPendientesFirma = new ArrayList<OperacionesTO>();
		
		String sql = "select ID_TRAMITE_TEMP, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, " +
				"ID_GARANTIA_MODIFICAR, DESCRIP_STATUS, URL, DESC_GARANTIA " +
				"from V_TRAMITES_PENDIENTES where ID_PERSONA_LOGIN=? AND ID_STATUS=5 " +
				" AND TRAMITE_REASIGNADO = 'F' AND ID_TIPO_TRAMITE <> 18";
		try {
			connection = bd.getConnection();
		ps=	connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));
				operacionesTO.setIdTramiteTemporal(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA_PEND")==0){				
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				if (rs.getString("ID_GARANTIA_MODIFICAR")!=null){
					if (rs.getInt("ID_GARANTIA_MODIFICAR")==0){						
						operacionesTO.setIdGarantiaModificar("N/A");
					}else{						
						operacionesTO.setIdGarantiaModificar(rs.getString("ID_GARANTIA_MODIFICAR"));						
					}
				}else{					
					operacionesTO.setIdGarantiaModificar("N/A");					
				}				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));			
				operacionesTO.setPaso(rs.getString("URL"));
				operacionesTO.setDescripcionGeneral(rs.getString("DESC_GARANTIA"));				
				listaPendientesFirma.add(operacionesTO);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesFirma;
	}
	
	public List<OperacionesTO> muestraOpPendientesFirma(Integer idPersona, Integer inicio, Integer fin){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		List <OperacionesTO> listaPendientesFirma = new ArrayList<OperacionesTO>();
		
		String sql = "SELECT ID_TRAMITE_TEMP, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, ID_GARANTIA_MODIFICAR, DESCRIP_STATUS, URL, DESC_GARANTIA FROM (SELECT ROWNUM RN, ID_TRAMITE_TEMP, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, ID_GARANTIA_MODIFICAR, DESCRIP_STATUS, URL, DESC_GARANTIA FROM RUG.V_TRAMITES_PENDIENTES WHERE ID_PERSONA_LOGIN = ? AND ID_STATUS = 5 AND TRAMITE_REASIGNADO = 'F' AND ID_TIPO_TRAMITE <> 18 ORDER BY ID_TRAMITE_TEMP DESC)  WHERE RN BETWEEN ? AND ?";
		try {
			connection = bd.getConnection();
		ps=	connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setInt(2, inicio);
			ps.setInt(3, fin);
			rs = ps.executeQuery();
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setOtorgantes(altaParteDAO.getDeudorByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));
				operacionesTO.setIdTramiteTemporal(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA_PEND")==0){				
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}
				if (rs.getString("ID_GARANTIA_MODIFICAR")!=null){
					if (rs.getInt("ID_GARANTIA_MODIFICAR")==0){						
						operacionesTO.setIdGarantiaModificar("N/A");
					}else{						
						operacionesTO.setIdGarantiaModificar(rs.getString("ID_GARANTIA_MODIFICAR"));						
					}
				}else{					
					operacionesTO.setIdGarantiaModificar("N/A");					
				}				
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));			
				operacionesTO.setPaso(rs.getString("URL"));
				operacionesTO.setDescripcionGeneral(rs.getString("DESC_GARANTIA"));				
				listaPendientesFirma.add(operacionesTO);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesFirma;
	}
	public List<CargaMasivaResumenTO> muestraOpPendientesFirmaMasiva(int idPersona){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		List <CargaMasivaResumenTO> listaPendientesFirma = new ArrayList<CargaMasivaResumenTO>();
		
		String sql = "select ID_FIRMA_MASIVA,TIPO_TRAMITE,  ID_USUARIO,  NOMBRE_ARCHIVO, CLAVE_RASTREO, TOTAL_EXITO, TOTAL_NO_EXITO from V_CARGA_MASIVA_PENDIENTE_FIRMA where id_usuario=?";
		try {
			connection = bd.getConnection();
			ps=	connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			while(rs.next()){
				CargaMasivaResumenTO masivaResumenTO = new CargaMasivaResumenTO();
				masivaResumenTO.setIdFirmaMasivaTemp(rs.getInt("ID_FIRMA_MASIVA"));
				masivaResumenTO.setTipoTramiteFirma(rs.getString("TIPO_TRAMITE"));
				masivaResumenTO.setNombreArchivo(rs.getString("NOMBRE_ARCHIVO"));
				if(rs.getString("CLAVE_RASTREO")!=null&&!rs.getString("CLAVE_RASTREO").trim().equalsIgnoreCase("")){
				masivaResumenTO.setClaveRastreo(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("CLAVE_RASTREO"),38));
				}else{
					masivaResumenTO.setClaveRastreo("");
					
				}
				masivaResumenTO.setMalas(rs.getInt("TOTAL_NO_EXITO"));
				masivaResumenTO.setBuenas(rs.getInt("TOTAL_EXITO"));
				listaPendientesFirma.add(masivaResumenTO);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesFirma;
	}
	
	public List<CargaMasivaResumenTO> muestraOpPendientesFirmaMasiva(Integer idPersona, Integer inicio, Integer fin){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		List <CargaMasivaResumenTO> listaPendientesFirma = new ArrayList<CargaMasivaResumenTO>();
		
		String sql = "SELECT ID_FIRMA_MASIVA, TIPO_TRAMITE, ID_USUARIO, NOMBRE_ARCHIVO,CLAVE_RASTREO,TOTAL_EXITO, TOTAL_NO_EXITO FROM (SELECT ROWNUM RN, ID_FIRMA_MASIVA, TIPO_TRAMITE, ID_USUARIO, NOMBRE_ARCHIVO,CLAVE_RASTREO,TOTAL_EXITO, TOTAL_NO_EXITO FROM RUG.V_CARGA_MASIVA_PENDIENTE_FIRMA WHERE ID_USUARIO = ? ORDER BY ID_FIRMA_MASIVA DESC) WHERE RN BETWEEN ? AND ?";
		
		
		try {
			connection = bd.getConnection();
			ps=	connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setInt(2, inicio);
			ps.setInt(3, fin);
			rs = ps.executeQuery();
			while(rs.next()){
				CargaMasivaResumenTO masivaResumenTO = new CargaMasivaResumenTO();
				masivaResumenTO.setIdFirmaMasivaTemp(rs.getInt("ID_FIRMA_MASIVA"));
				masivaResumenTO.setTipoTramiteFirma(rs.getString("TIPO_TRAMITE"));
				masivaResumenTO.setNombreArchivo(rs.getString("NOMBRE_ARCHIVO"));
				if(rs.getString("CLAVE_RASTREO")!=null&&!rs.getString("CLAVE_RASTREO").trim().equalsIgnoreCase("")){
				masivaResumenTO.setClaveRastreo(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("CLAVE_RASTREO"),38));
				}else{
					masivaResumenTO.setClaveRastreo("");
					
				}
				masivaResumenTO.setMalas(rs.getInt("TOTAL_NO_EXITO"));
				masivaResumenTO.setBuenas(rs.getInt("TOTAL_EXITO"));
				listaPendientesFirma.add(masivaResumenTO);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesFirma;
	}
	
	public List<CargaMasivaResumenTO> muestraBusOpPenFirmaMasiva(Integer idPersona, Integer inicio, Integer fin, String dateStart, String dateEnd){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		List <CargaMasivaResumenTO> listaPendientesFirma = new ArrayList<CargaMasivaResumenTO>();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT ID_FIRMA_MASIVA, TIPO_TRAMITE, ID_USUARIO, NOMBRE_ARCHIVO, CLAVE_RASTREO, TOTAL_EXITO, TOTAL_NO_EXITO, FECHA_STATUS FROM (SELECT ROWNUM RN, ID_FIRMA_MASIVA, TIPO_TRAMITE, ID_USUARIO, NOMBRE_ARCHIVO, CLAVE_RASTREO, TOTAL_EXITO, TOTAL_NO_EXITO, FECHA_STATUS FROM RUG.V_CARGA_MASIVA_PENDIENTE_FIRMA WHERE ID_USUARIO = ? AND TO_DATE(FECHA_STATUS, 'DD/MM/RRRR') BETWEEN TO_DATE(?, 'DD/MM/RRRR')  AND TO_DATE(?, 'DD/MM/RRRR') ORDER BY ID_FIRMA_MASIVA DESC) WHERE RN BETWEEN ? AND ?");
		
		try {
			connection = bd.getConnection();
			ps=	connection.prepareStatement(sb.toString());
			ps.setInt(1, idPersona);
			ps.setString(2, dateStart);
			ps.setString(3, dateEnd);
			ps.setInt(4, inicio);
			ps.setInt(5, fin);
			rs = ps.executeQuery();
			while(rs.next()){
				CargaMasivaResumenTO masivaResumenTO = new CargaMasivaResumenTO();
				masivaResumenTO.setIdFirmaMasivaTemp(rs.getInt("ID_FIRMA_MASIVA"));
				masivaResumenTO.setTipoTramiteFirma(rs.getString("TIPO_TRAMITE"));
				masivaResumenTO.setNombreArchivo(rs.getString("NOMBRE_ARCHIVO"));
				if(rs.getString("CLAVE_RASTREO")!=null&&!rs.getString("CLAVE_RASTREO").trim().equalsIgnoreCase("")){
				masivaResumenTO.setClaveRastreo(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("CLAVE_RASTREO"),38));
				}else{
					masivaResumenTO.setClaveRastreo("");
					
				}
				masivaResumenTO.setMalas(rs.getInt("TOTAL_NO_EXITO"));
				masivaResumenTO.setBuenas(rs.getInt("TOTAL_EXITO"));
				listaPendientesFirma.add(masivaResumenTO);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesFirma;
	}
	
	public List<CargaMasivaResumenTO> muestraBusOpPenFirMasByClvRastreo(Integer idPersona, Integer inicio, Integer fin, String clvRastreo){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		List <CargaMasivaResumenTO> listaPendientesFirma = new ArrayList<CargaMasivaResumenTO>();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT ID_FIRMA_MASIVA, TIPO_TRAMITE, ID_USUARIO, NOMBRE_ARCHIVO, CLAVE_RASTREO, TOTAL_EXITO, TOTAL_NO_EXITO FROM (SELECT ROWNUM RN, ID_FIRMA_MASIVA, TIPO_TRAMITE, ID_USUARIO, NOMBRE_ARCHIVO, CLAVE_RASTREO, TOTAL_EXITO, TOTAL_NO_EXITO FROM RUG.V_CARGA_MASIVA_PENDIENTE_FIRMA WHERE ID_USUARIO = ? AND ID_FIRMA_MASIVA IN (SELECT DISTINCT (A.ID_FIRMA_MASIVA) FROM RUG.RUG_FIRMA_MASIVA A, RUG.RUG_TRAMITE_RASTREO B WHERE A.ID_TRAMITE_TEMP = B.ID_TRAMITE_TEMP AND CVE_RASTREO = ?) ORDER BY ID_FIRMA_MASIVA DESC) WHERE RN BETWEEN ? AND ?");
		
		try {
			connection = bd.getConnection();
			ps=	connection.prepareStatement(sb.toString());
			ps.setInt(1, idPersona);
			ps.setString(2, clvRastreo);
			ps.setInt(3, inicio);
			ps.setInt(4, fin);
			rs = ps.executeQuery();
			while(rs.next()){
				CargaMasivaResumenTO masivaResumenTO = new CargaMasivaResumenTO();
				masivaResumenTO.setIdFirmaMasivaTemp(rs.getInt("ID_FIRMA_MASIVA"));
				masivaResumenTO.setTipoTramiteFirma(rs.getString("TIPO_TRAMITE"));
				masivaResumenTO.setNombreArchivo(rs.getString("NOMBRE_ARCHIVO"));
				if(rs.getString("CLAVE_RASTREO")!=null&&!rs.getString("CLAVE_RASTREO").trim().equalsIgnoreCase("")){
				masivaResumenTO.setClaveRastreo(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("CLAVE_RASTREO"),38));
				}else{
					masivaResumenTO.setClaveRastreo("");
					
				}
				masivaResumenTO.setMalas(rs.getInt("TOTAL_NO_EXITO"));
				masivaResumenTO.setBuenas(rs.getInt("TOTAL_EXITO"));
				listaPendientesFirma.add(masivaResumenTO);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesFirma;
	}
	
	
	public boolean validaFecha(Integer idGarantia, Integer idTramite){
		boolean regresa = false;		
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "{ ? = call FNVALIDAFECHAS(?,?)}";
		CallableStatement cs = null;		
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setInt(2, idGarantia);
			cs.setInt(3, idTramite);	
			cs.execute();
			int valor = Integer.valueOf(cs.getString(1));
			if (valor == 1){
				regresa = true;
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection, null, cs);
		}
		return regresa;
	}
	
	public List<OperacionesTO> muestraOpPendientesFirma(int idPersona, Integer idAcreedor){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;		
		List <OperacionesTO> listaPendientesFirma = new ArrayList<OperacionesTO>(); 		
		String sql = "select ID_TRAMITE_TEMP, ID_GARANTIA_MODIFICAR, TIPO_TRAMITE, FECHA_STATUS, ID_GARANTIA_PEND, DESCRIP_STATUS, URL, DESC_GARANTIA from V_TRAMITES_PENDIENTES where ID_PERSONA_LOGIN=? AND ID_STATUS=5 AND ID_ACREEDOR = ?  AND TRAMITE_REASIGNADO = 'F'";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setInt(2, idAcreedor);
			rs = ps.executeQuery();
			AltaParteDAO altaParteDAO = new AltaParteDAO();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE_TEMP"));
				operacionesTO.setOtorgantes(altaParteDAO.getOtorganteByInscripcion(rs.getInt("ID_TRAMITE_TEMP")));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaInicio(rs.getDate("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA_PEND")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA_PEND"));
				}				
				operacionesTO.setIdGarantiaModificar(rs.getString("ID_GARANTIA_MODIFICAR"));
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));				
				operacionesTO.setPaso(rs.getString("URL"));
				operacionesTO.setDescripcionGeneral(rs.getString("DESC_GARANTIA"));				
				listaPendientesFirma.add(operacionesTO);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaPendientesFirma;
	}
	
	public List<OperacionesTO> muestraOpTerminadas(Integer idPersona, Integer idAcreedor){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();		
		String sql = "select ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ID_GARANTIA, DESCRIP_STATUS, URL_PASO  from V_TRAMITES_TERMINADOS  where ID_PERSONA_LOGIN=?  AND ID_ACREEDOR = ? ORDER BY FECHA_STATUS DESC";
		try {
			connection = bd.getConnection();
			 ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setInt(2, idAcreedor);
			MyLogger.Logger.log(Level.INFO, "" + ps);
			rs = ps.executeQuery();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
				}
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				operacionesTO.setPaso(rs.getString("URL_PASO"));
				operacionesTO.setDescripcionGeneral("");
				listaTerminadas.add(operacionesTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return listaTerminadas;
	}
	
	public List<OperacionesTO> muestraOpTerminadas(Integer idPersona) throws InfrastructureException{
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();		
		String sql = "select ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS," +
				" ID_GARANTIA, DESCRIP_STATUS, URL_PASO, ID_TIPO_TRAMITE  from RUG.V_TRAMITES_TERMINADOS " +
				" where ID_PERSONA_LOGIN=? AND TRAMITE_REASIGNADO = 'F' ORDER BY FECHA_STATUS DESC";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			MyLogger.Logger.log(Level.INFO, "" + ps);
			rs = ps.executeQuery();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
				}
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				operacionesTO.setOtorgantes(getOtorganteByTramite(rs.getInt("ID_TRAMITE")));	
				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				operacionesTO.setDescripcionGeneral("");
				listaTerminadas.add(operacionesTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InfrastructureException(e);
		} 
		finally{
			bd.close(connection,rs,ps);
		}
		return listaTerminadas;
	}
	
	public List<OperacionesTO> muestraOpTerminadasPag(Integer idPersona){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ");
		sb.append(" ID_GARANTIA, DESCRIP_STATUS, URL_PASO, ID_TIPO_TRAMITE  from RUG.V_TRAMITES_TERMINADOS ");
		sb.append(" where ID_PERSONA_LOGIN = ? AND TRAMITE_REASIGNADO = 'F' ORDER BY FECHA_STATUS DESC ");
//		String sql = "SELECT ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS," +
//				" ID_GARANTIA, DESCRIP_STATUS, URL_PASO, ID_TIPO_TRAMITE  from RUG.V_TRAMITES_TERMINADOS " +
//				" where ID_PERSONA_LOGIN=? AND TRAMITE_REASIGNADO = 'F' ORDER BY FECHA_STATUS DESC";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sb.toString());
			ps.setInt(1, idPersona);
			MyLogger.Logger.log(Level.INFO, "" + ps);
			rs = ps.executeQuery();
//			while(rs.next()){
//				OperacionesTO operacionesTO = new OperacionesTO();
//				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
//				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
//				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
//				if (rs.getInt("ID_GARANTIA")==0){
//					operacionesTO.setNumGarantia("N/A");
//				}else{
//					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
//				}
//				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
//				operacionesTO.setOtorgantes(getOtorganteByTramite(rs.getInt("ID_TRAMITE")));	
//				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
//				operacionesTO.setDescripcionGeneral("");
//				listaTerminadas.add(operacionesTO);
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally{
			bd.close(connection,rs,ps);
		}
		return listaTerminadas;
	}
	
//	public List<OperacionesTO> muestraOpTerminadasPag(Integer idPersona){
//		ConexionBD bd = new ConexionBD();
//		Connection connection = null;
//		ResultSet rs = null;
//		PreparedStatement ps =null;
//		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ");
//		sb.append(" ID_GARANTIA, DESCRIP_STATUS, URL_PASO, ID_TIPO_TRAMITE  from RUG.V_TRAMITES_TERMINADOS ");
//		sb.append(" where ID_PERSONA_LOGIN = ? AND TRAMITE_REASIGNADO = 'F' ORDER BY FECHA_STATUS DESC ");
////		String sql = "SELECT ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS," +
////				" ID_GARANTIA, DESCRIP_STATUS, URL_PASO, ID_TIPO_TRAMITE  from RUG.V_TRAMITES_TERMINADOS " +
////				" where ID_PERSONA_LOGIN=? AND TRAMITE_REASIGNADO = 'F' ORDER BY FECHA_STATUS DESC";
//		try {
//			connection = bd.getConnection();
//			ps = connection.prepareStatement(sb.toString());
//			ps.setInt(1, idPersona);
//			MyLogger.Logger.log(Level.INFO, "" + ps);
//			rs = ps.executeQuery();
//			while(rs.next()){
//				OperacionesTO operacionesTO = new OperacionesTO();
//				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
//				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
//				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
//				if (rs.getInt("ID_GARANTIA")==0){
//					operacionesTO.setNumGarantia("N/A");
//				}else{
//					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
//				}
//				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
//				operacionesTO.setOtorgantes(getOtorganteByTramite(rs.getInt("ID_TRAMITE")));	
//				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
//				operacionesTO.setDescripcionGeneral("");
//				listaTerminadas.add(operacionesTO);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		finally{
//			bd.close(connection,rs,ps);
//		}
//		return listaTerminadas;
//	}
	
	public List<OperacionesTO> muestraOpTerminadasPagInicioFinFiltro(Integer idPersona, String filtro, Integer inicio, Integer fin){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		//Integer anioAct = getCountOpTerminadasMuestra(idPersona);
		String usuariosIn = getUsuariosRelacionados(idPersona);
		
		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();		
		String sql = "SELECT * FROM ( SELECT ROWNUM RN, TT.* FROM ( SELECT ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ID_GARANTIA, DESCRIP_STATUS, ID_TIPO_TRAMITE, ID_PERSONA_LOGIN, USUARIO " +
				"FROM RUG.V_TRAMITES_TERMINADOS_REG WHERE ID_PERSONA_LOGIN IN (" + usuariosIn + ") AND TRAMITE_REASIGNADO = 'F' " +		
			    "AND ( " +
						"(TO_CHAR(ID_TRAMITE) like '%"+ filtro +"%') or " +
						"(DESCRIPCION like '%"+ filtro +"%') or " +
						"(TO_CHAR(ID_GARANTIA) like '%"+ filtro +"%') or " +
						"(TO_CHAR(FECHA_CREACION,'yyyy-mm-dd hh24:mi:ss') like '%"+ filtro +"%') or " +
						"(USUARIO like '%" + filtro +"%') " +
					") " +
					" ORDER BY FECHA_STATUS DESC ) TT ) " +
					" WHERE RN BETWEEN ? AND ? ";
				 				
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			//ps.setString(1, usuariosIn);
			ps.setInt(1, inicio);
			ps.setInt(2, fin);
			MyLogger.Logger.log(Level.INFO, "" + ps);
			rs = ps.executeQuery();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
				}
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				operacionesTO.setOtorgantes(getDeudorByTramite(rs.getInt("ID_TRAMITE")));	
				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				operacionesTO.setDescripcionGeneral("");
				operacionesTO.setUsuario(rs.getString("USUARIO"));
				listaTerminadas.add(operacionesTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally{
			bd.close(connection,rs,ps);
		}
		MyLogger.Logger.log(Level.INFO, "" + "::::::Tamaño de la lista que regresa: "+sql);
		return listaTerminadas;
	}

	public List<OperacionesTO> SoyAcreedorDatos(Integer idPersona, Integer inicio, Integer fin){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		String usuariosIn = getUsuariosRelacionados(idPersona);
		
		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();	
		String query = "SELECT * FROM (SELECT ROWNUM RN, T.* FROM  (select ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ID_GARANTIA, DESCRIP_STATUS, ID_TIPO_TRAMITE, ID_PERSONA_LOGIN, USUARIO AS NOMBRE_PERSONA" +
						"   FROM  RUG.V_TRAMITES_TERMINADOS_REG WHERE ID_TRAMITE IN ( "+ 
						"   SELECT DISTINCT ID_TRAMITE from V_GARANTIA_PARTES WHERE ID_PARTE = 3 AND RFC = (SELECT RFC FROM RUG_PERSONAS where id_persona = "+ idPersona +") " +
						" ) AND TRAMITE_REASIGNADO = 'F' ORDER BY FECHA_STATUS DESC) T) ";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(query);
//			ps.setInt(1, inicio);
//			ps.setInt(2, fin);
			rs = ps.executeQuery();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
				}
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				operacionesTO.setOtorgantes(getDeudorByTramite(rs.getInt("ID_TRAMITE")));	
				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				operacionesTO.setDescripcionGeneral("");
				operacionesTO.setUsuario(rs.getString("NOMBRE_PERSONA"));
				listaTerminadas.add(operacionesTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally{
			bd.close(connection,rs,ps);
		}
	
		return listaTerminadas;
	}

	public List<OperacionesTO> SoyAcreedorDatosFiltro(Integer idPersona, String filtro, Integer inicio, Integer fin){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		String usuariosIn = getUsuariosRelacionados(idPersona);

		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();
		String query = "SELECT * FROM (select ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ID_GARANTIA, DESCRIP_STATUS, ID_TIPO_TRAMITE, ID_PERSONA_LOGIN, USUARIO AS NOMBRE_PERSONA" +
						"   FROM  RUG.V_TRAMITES_TERMINADOS_REG WHERE ID_TRAMITE IN ( "+
						"   SELECT DISTINCT ID_TRAMITE from V_GARANTIA_PARTES WHERE ID_PARTE = 3 AND RFC = (SELECT RFC FROM RUG_PERSONAS where id_persona = "+ idPersona +") " +
						" ) AND TRAMITE_REASIGNADO = 'F' ORDER BY FECHA_STATUS DESC) WHERE ID_GARANTIA = "+ filtro ;
		try {
			System.out.println("Query R  " + query.toString());
			connection = bd.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
				}
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				operacionesTO.setOtorgantes(getDeudorByTramite(rs.getInt("ID_TRAMITE")));
				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				operacionesTO.setDescripcionGeneral("");
				operacionesTO.setUsuario(rs.getString("NOMBRE_PERSONA"));
				listaTerminadas.add(operacionesTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			bd.close(connection,rs,ps);
		}

		return listaTerminadas;
	}
	
	
	public List<OperacionesTO> muestraOpTerminadasPagInicioFin(Integer idPersona, Integer inicio, Integer fin){
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		//Integer anioAct = getCountOpTerminadasMuestra(idPersona);
		String usuariosIn = getUsuariosRelacionados(idPersona);
		
		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();		
		String sql = "SELECT * FROM ( SELECT ROWNUM RN, TT.* " +
				"FROM (  SELECT ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ID_GARANTIA, DESCRIP_STATUS, ID_TIPO_TRAMITE, ID_PERSONA_LOGIN, USUARIO AS NOMBRE_PERSONA " +
				"FROM RUG.V_TRAMITES_TERMINADOS_REG WHERE ID_PERSONA_LOGIN IN (" + usuariosIn + ") AND TRAMITE_REASIGNADO = 'F' " +								
				"ORDER BY FECHA_STATUS DESC ) TT ) WHERE RN BETWEEN ? AND ? ";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			//ps.setString(1, usuariosIn);
			ps.setInt(1, inicio);
			ps.setInt(2, fin);
			MyLogger.Logger.log(Level.INFO, "" + "::::::Query de Terminado "+sql + " inicio " + inicio + " fin  "+ fin);
			rs = ps.executeQuery();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
				}
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				operacionesTO.setOtorgantes(getDeudorByTramite(rs.getInt("ID_TRAMITE")));	
				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				operacionesTO.setDescripcionGeneral("");
				operacionesTO.setUsuario(rs.getString("NOMBRE_PERSONA"));
				listaTerminadas.add(operacionesTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally{
			bd.close(connection,rs,ps);
		}

		return listaTerminadas;
	}
	
	public List<OperacionesTO> muestraOpTerminadasPagInicioFinExcel(Integer idPersona, String filtro){
            MyLogger.Logger.log(Level.INFO, "" + filtro);
		if(filtro == "null"){
                    filtro = "";
                }
                        
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		//Integer anioAct = getCountOpTerminadasMuestra(idPersona);
		String usuariosIn = getUsuariosRelacionados(idPersona);
		
		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();		
		String sql = "SELECT * FROM ( SELECT ROWNUM RN, TT.* " +
				"FROM (  SELECT ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ID_GARANTIA, DESCRIP_STATUS, ID_TIPO_TRAMITE, ID_PERSONA_LOGIN, USUARIO AS NOMBRE_PERSONA " +
				"FROM RUG.V_TRAMITES_TERMINADOS_REG WHERE ID_PERSONA_LOGIN IN (" + usuariosIn + ") AND TRAMITE_REASIGNADO = 'F' " +	
				"AND ( " +
						"(TO_CHAR(ID_TRAMITE) like '%"+ filtro +"%') or " +
						"(DESCRIPCION like '%"+ filtro +"%') or " +
						"(TO_CHAR(ID_GARANTIA) like '%"+ filtro +"%') or " +
						"(TO_CHAR(FECHA_CREACION,'yyyy-mm-dd hh24:mi:ss') like '%"+ filtro +"%') or " +
						"(USUARIO like '%" + filtro +"%') " +
					") " +							
				"ORDER BY FECHA_STATUS DESC ) TT )";
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
				}
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				operacionesTO.setOtorgantes(getDeudorByTramite(rs.getInt("ID_TRAMITE")));	
				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				operacionesTO.setDescripcionGeneral("");
				operacionesTO.setUsuario(rs.getString("NOMBRE_PERSONA"));
				listaTerminadas.add(operacionesTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally{
			bd.close(connection,rs,ps);
		}
		return listaTerminadas;
	}
	
	public List<OperacionesTO> muestraOpTerminadasGeneral(Integer idAcreedor) throws InfrastructureException{
		System.out.println("estoy en el metodo");
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		List <OperacionesTO> listaTerminadas = new ArrayList<OperacionesTO>();
		String sql = "select ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS,"+
				 " ID_GARANTIA, DESCRIP_STATUS, URL_PASO, ID_TIPO_TRAMITE  from V_TRAMITES_TERMINADOS "+
				 " where ID_ACREEDOR=? AND TRAMITE_REASIGNADO = 'V' ORDER BY FECHA_STATUS DESC";
		
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			MyLogger.Logger.log(Level.INFO, "" + ps);
			rs = ps.executeQuery();
			while(rs.next()){
				OperacionesTO operacionesTO = new OperacionesTO();
				operacionesTO.setIdInscripcion(rs.getInt("ID_TRAMITE"));
				operacionesTO.setTipoTransaccion(rs.getString("TIPO_TRAMITE"));
				operacionesTO.setFechaOperacionInicio(rs.getString("FECHA_STATUS"));
				if (rs.getInt("ID_GARANTIA")==0){
					operacionesTO.setNumGarantia("N/A");
				}else{
					operacionesTO.setNumGarantia(rs.getString("ID_GARANTIA"));
				}
				operacionesTO.setEstatus(rs.getString("DESCRIP_STATUS"));
				operacionesTO.setOtorgantes(getOtorganteByTramite(rs.getInt("ID_TRAMITE")));	
				operacionesTO.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				operacionesTO.setDescripcionGeneral("");
				listaTerminadas.add(operacionesTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		finally{
			bd.close(connection,rs,ps);
		}
		return listaTerminadas;
	}	
	
	private boolean getBooleanByTxt(String bandera) {
		boolean regresa = false;
		if (bandera.equals("Y")) {
			regresa = true;
		}
		return regresa;
	}
	
	public String cadenaConSaltoDeLinea(String cadena){
		String regresa =""; 
		try{
			regresa = cadena.replaceAll("<BR>", "\\n");
			regresa = regresa.replaceAll("<br>", "\\n");
		}catch(Exception e){
			regresa = cadena;
		}
		return regresa; 
	}
	
	public List <OtorganteTO> getDeudorByTramite(Integer idTramite) throws InfrastructureException{
		List <OtorganteTO> lista = new ArrayList<OtorganteTO>();
		OtorganteTO otorganteTO = null;
		String sql = "SELECT " +
				" ID_TRAMITE, ID_PERSONA, ID_PARTE, DESC_PARTE, PER_JURIDICA, NOMBRE, FOLIO_MERCANTIL, RFC, CURP" +
				" FROM V_TRAMITES_TERM_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 2";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
//		System.out.println("Id Tramite consulta:" + idTramite);
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			while (rs.next()){
				otorganteTO = new OtorganteTO();				
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				//otorganteTO.setFolioMercantil(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("FOLIO_MERCANTIL"),25));
				otorganteTO.setCurp(rs.getString("CURP"));
				otorganteTO.setRfc(rs.getString("RFC"));
				if(rs.getString("NOMBRE") != null){
					otorganteTO.setNombreCompleto(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("NOMBRE"),25));
				}else{
					otorganteTO.setNombreCompleto("");
				}

				/*String perJur =  rs.getString("PER_JURIDICA");
				String personaJuridica = "Persona Moral";
				if (perJur.equals("PF")){
					personaJuridica ="Persona Fisica";
				}*/
				otorganteTO.setTipoPersona(rs.getString("PER_JURIDICA"));
				lista.add(otorganteTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			MyLogger.Logger.log(Level.SEVERE, "Error otorgante IDTramite: "+idTramite,e);
			e.printStackTrace();
			throw new InfrastructureException();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}
	
	public List <OtorganteTO> getOtorganteByTramite(Integer idTramite) throws InfrastructureException{
		List <OtorganteTO> lista = new ArrayList<OtorganteTO>();
		OtorganteTO otorganteTO = null;
		String sql = "SELECT " +
				" ID_TRAMITE, ID_PERSONA, ID_PARTE, DESC_PARTE, PER_JURIDICA, NOMBRE, FOLIO_MERCANTIL, RFC" +
				" FROM V_TRAMITES_TERM_PARTES " +
				" WHERE ID_TRAMITE = ? AND ID_PARTE = 1";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			while (rs.next()){
				otorganteTO = new OtorganteTO();				
				otorganteTO.setIdOtorgante(rs.getInt("ID_PERSONA"));
				otorganteTO.setFolioMercantil(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("FOLIO_MERCANTIL"),25));
				otorganteTO.setRfc(rs.getString("RFC"));					
				otorganteTO.setNombreCompleto(new CharSetUtil().longitudMaximaPorPalabra(rs.getString("NOMBRE"),25));
				String perJur =  rs.getString("PER_JURIDICA");
				String personaJuridica = "Persona Moral";
				if (perJur.equals("PF")){
					personaJuridica ="Persona Fisica";
				}
				otorganteTO.setTipoPersona(personaJuridica);
				lista.add(otorganteTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			MyLogger.Logger.log(Level.SEVERE, "Error otorgante IDTramite: "+idTramite,e);
			e.printStackTrace();
			throw new InfrastructureException();
		}
		finally{
			bd.close(connection,rs,ps);
		}
		return lista;
	}
	
	public Integer getCountOpPendCap(Integer idPersona){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT COUNT(ID_TRAMITE_TEMP) NUMERO FROM V_TRAMITES_PENDIENTES WHERE ID_PERSONA_LOGIN = ? AND ID_STATUS=1 AND ID_TIPO_TRAMITE = 1 AND TRAMITE_REASIGNADO = 'F'";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	
	public Integer getCountOpPendFirma(Integer idPersona){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "select COUNT(ID_TRAMITE_TEMP) NUMERO from V_TRAMITES_PENDIENTES where ID_PERSONA_LOGIN=? AND ID_STATUS=5 AND TRAMITE_REASIGNADO = 'F' AND ID_TIPO_TRAMITE <> 18";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	
	public Integer getCountOpPendFirmaMasiva(Integer idPersona){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "select COUNT (ID_FIRMA_MASIVA) NUMERO from V_CARGA_MASIVA_PENDIENTE_FIRMA where id_usuario=?";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	
	public Integer getCountOpTerminadasFecha(Integer idPersona){
		int fecha = 0;
		String usuariosIn = getUsuariosRelacionados(idPersona);
		fecha = getCountOpTerminadasTotal(usuariosIn, fecha);
		return fecha;
	}

	public Integer getCantidadTramitesAcreedor(Integer idPersona)
	{
//		String usuario = getUsuariosRelacionados(idPersona);
		return getCantidadTramitesSoyAcreedor(idPersona);
	}

	public Integer getCantidadTramitesAcreedorFiltro(Integer idPersona, String filtro)
	{
		return getCantidadTramitesSoyAcreedorFiltro(idPersona, filtro);
	}
	
	public Integer getCountOpTerminadasFechaFiltro(Integer idPersona, String filtro){
		int total = 0;		
		
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps =null;
		//Integer anioAct = getCountOpTerminadasMuestra(idPersona);
		String usuariosIn = getUsuariosRelacionados(idPersona);
				
		String sql = "SELECT COUNT(ID_TRAMITE) NUMERO " +
				"FROM RUG.V_TRAMITES_TERMINADOS_REG WHERE ID_PERSONA_LOGIN IN (" + usuariosIn + ") AND TRAMITE_REASIGNADO = 'F' " +
				"AND ( " +
						"(TO_CHAR(ID_TRAMITE) like '%"+ filtro +"%') or " +
						"(DESCRIPCION like '%"+ filtro +"%') or " +
						"(TO_CHAR(ID_GARANTIA) like '%"+ filtro +"%') or " +
						"(TO_CHAR(FECHA_CREACION,'yyyy-mm-dd hh24:mi:ss') like '%"+ filtro +"%') or " +
						"(USUARIO like '%" + filtro + "%') " +
						") ";
				//"WHERE RN BETWEEN ? AND ? " +
				//"ORDER BY FECHA_STATUS DESC"; 				
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			//ps.setString(1, usuariosIn);
			//ps.setInt(1, inicio);
			//ps.setInt(2, fin);
			MyLogger.Logger.log(Level.INFO, "" + ps);
			rs = ps.executeQuery();
			if (rs.next()){
				total = rs.getInt("NUMERO");
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally{
			bd.close(connection,rs,ps);
		}
		MyLogger.Logger.log(Level.INFO, "" + "::::::Tamaño de la lista que regresa filtro: "+total);		
		
		return total;
	}
	
	public Integer getCountOpTerminadasMuestra(Integer idPersona){
		int fecha = 0;
		ConexionBD bd = new ConexionBD();
		  String sql = "SELECT MAX(EXTRACT(YEAR FROM FECHA_CREACION)) FECHA " +
		  		"FROM RUG.V_TRAMITES_TERMINADOS_COUNT " +
		  		"WHERE ID_PERSONA_LOGIN =? " +
		  		"AND TRAMITE_REASIGNADO = 'F' ";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()){
				fecha = rs.getInt("FECHA");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return fecha;
	}
	
	public Integer getCountOpTerminadasTotal(String usuariosIn, Integer fecha){
		int total = 0;
		ConexionBD bd = new ConexionBD();
		  String sql = "SELECT COUNT(ID_TRAMITE) NUMERO" +
		  		" FROM RUG.V_TRAMITES_TERMINADOS_COUNT " +
		  		"WHERE ID_PERSONA_LOGIN IN (" + usuariosIn + ")" +
		  		" AND TRAMITE_REASIGNADO = 'F'";
		  		//" AND EXTRACT (YEAR FROM FECHA_CREACION) = "+fecha;
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()){
				total = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return total;
	}

	public Integer getCantidadTramitesSoyAcreedor(Integer usuario)
	{
		Integer total = 0;
		ConexionBD bd = new ConexionBD();
		String query = "SELECT COUNT(*) AS NUMERO FROM (SELECT ROWNUM RN, T.* FROM (SELECT ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ID_GARANTIA, DESCRIP_STATUS, ID_TIPO_TRAMITE, ID_PERSONA_LOGIN, USUARIO AS NOMBRE_PERSONA, ID_ACREEDOR FROM RUG.V_TRAMITES_TERMINADOS_REG WHERE ID_TRAMITE IN(" +
				" SELECT DISTINCT ID_TRAMITE FROM V_GARANTIA_PARTES WHERE ID_PARTE = 3 AND RFC = (SELECT RFC FROM RUG_PERSONAS WHERE ID_PERSONA = " + usuario + ")) AND TRAMITE_REASIGNADO = 'F' ORDER BY FECHA_STATUS DESC)T)";
		System.out.println("Query jj "+ query);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = bd.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()){
				total = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(con, rs, ps);
		}
		return total;
	}
	public Integer getCantidadTramitesSoyAcreedorFiltro(Integer usuario, String filtro)
	{
		Integer total = 0;
		ConexionBD bd = new ConexionBD();
		String query = "SELECT COUNT(*) AS NUMERO FROM (SELECT ROWNUM RN, T.* FROM (SELECT ID_TRAMITE, DESCRIPCION AS TIPO_TRAMITE, FECHA_CREACION AS FECHA_STATUS, ID_GARANTIA, DESCRIP_STATUS, ID_TIPO_TRAMITE, ID_PERSONA_LOGIN, USUARIO AS NOMBRE_PERSONA, ID_ACREEDOR FROM RUG.V_TRAMITES_TERMINADOS_REG WHERE ID_TRAMITE IN(" +
				" SELECT DISTINCT ID_TRAMITE FROM V_GARANTIA_PARTES WHERE ID_PARTE = 3 AND RFC = (SELECT RFC FROM RUG_PERSONAS WHERE ID_PERSONA = " + usuario + ")) AND TRAMITE_REASIGNADO = 'F' ORDER BY FECHA_STATUS DESC)T) WHERE ID_GARANTIA = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = bd.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, filtro);
			rs = ps.executeQuery();
			if(rs.next()){
				total = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			bd.close(con, rs, ps);
		}
		return total;
	}
	
	public Integer getCountBusOpPendCap(Integer idPersona, String dateStart, String dateEnd){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT COUNT (ID_TRAMITE_TEMP) NUMERO FROM RUG.V_TRAMITES_PENDIENTES WHERE ID_PERSONA_LOGIN = ? AND ID_STATUS = 1 AND ID_TIPO_TRAMITE = 1 AND TRAMITE_REASIGNADO = 'F' AND TO_DATE (FECHA_STATUS, 'DD/MM/RRRR') BETWEEN TO_DATE (?,'DD/MM/RRRR') AND TO_DATE (?,'DD/MM/RRRR')";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setString(2, dateStart);
			ps.setString(3, dateEnd);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	
	public Integer getCountBusOpPendCapByOtorgante(Integer idPersona, String nomOtorgante){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT COUNT (ID_TRAMITE_TEMP) NUMERO FROM RUG.V_TRAMITES_PENDIENTES WHERE ID_PERSONA_LOGIN = "+idPersona+" AND ID_STATUS = 1 AND ID_TIPO_TRAMITE = 1 AND TRAMITE_REASIGNADO = 'F' AND NOMBRE LIKE trim(upper('%"+nomOtorgante+"%'))";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
//			ps.setInt(1, idPersona);
//			ps.setString(2, nomOtorgante);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		MyLogger.Logger.log(Level.INFO, "" + "::::::Consulta: "+sql);
		return regresa;
	}
	
	public Integer getCountBusOpPendFirmaByOtorgante(Integer idPersona, String nomOtorgante){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT COUNT(ID_TRAMITE_TEMP) NUMERO FROM RUG.V_TRAMITES_PENDIENTES WHERE ID_PERSONA_LOGIN = "+idPersona+" AND ID_STATUS = 5 AND TRAMITE_REASIGNADO = 'F' AND ID_TIPO_TRAMITE <> 18 AND NOMBRE LIKE trim(upper('%"+nomOtorgante+"%'))";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
//			ps.setInt(1, idPersona);
//			ps.setString(2, nomOtorgante);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		MyLogger.Logger.log(Level.INFO, "" + "::::::Consulta: "+sql);
		return regresa;
	}
	
	public Integer getCountBusOpPendFirma(Integer idPersona, String dateStart, String dateEnd){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT COUNT (ID_TRAMITE_TEMP) NUMERO FROM RUG.V_TRAMITES_PENDIENTES WHERE ID_PERSONA_LOGIN = ? AND ID_STATUS = 5 AND TRAMITE_REASIGNADO = 'F' AND ID_TIPO_TRAMITE <> 18 AND TO_DATE (FECHA_STATUS, 'DD/MM/RRRR') BETWEEN TO_DATE (?, 'DD/MM/RRRR') AND TO_DATE (?, 'DD/MM/RRRR')";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setString(2, dateStart);
			ps.setString(3, dateEnd);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	
	public Integer getCountBusOpTerminadas(Integer idPersona, String dateStart, String dateEnd){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT COUNT (ID_TRAMITE) NUMERO FROM RUG.V_TRAMITES_TERMINADOS WHERE ID_PERSONA_LOGIN = ? AND TRAMITE_REASIGNADO = 'F' AND TO_DATE (FECHA_CREACION, 'DD/MM/RRRR') BETWEEN TO_DATE (?, 'DD/MM/RRRR') AND TO_DATE (?, 'DD/MM/RRRR')";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setString(2, dateStart);
			ps.setString(3, dateEnd);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	
	public Integer getCountBusOpTermByOtorgante(Integer idPersona, String nomOtorgante){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT COUNT (ID_TRAMITE) NUMERO FROM RUG.V_TRAMITES_TERMINADOS V WHERE V.ID_PERSONA_LOGIN = ? AND UPPER (RUG.FNCONCATOTORGANTE (V.ID_TRAMITE, 1)) LIKE UPPER ('%"+nomOtorgante+"%') AND TRAMITE_REASIGNADO = 'F' ";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
//			ps.setString(2, nomOtorgante);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	
	public Integer getCountBusOpPenFirmaMasiva(Integer idPersona, String dateStart, String dateEnd){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT COUNT (ID_FIRMA_MASIVA) NUMERO FROM RUG.V_CARGA_MASIVA_PENDIENTE_FIRMA WHERE ID_USUARIO = ? AND TO_DATE (FECHA_STATUS, 'DD/MM/RRRR') BETWEEN TO_DATE (?, 'DD/MM/RRRR') AND TO_DATE (?, 'DD/MM/RRRR')";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setString(2, dateStart);
			ps.setString(3, dateEnd);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}
	
	public Integer getCountBusOpPenFirMasByClvRastreo(Integer idPersona, String clvRastreo){
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT COUNT (ID_FIRMA_MASIVA) NUMERO FROM RUG.V_CARGA_MASIVA_PENDIENTE_FIRMA WHERE ID_USUARIO = ?  AND ID_FIRMA_MASIVA IN (SELECT DISTINCT (A.ID_FIRMA_MASIVA)  FROM RUG.RUG_FIRMA_MASIVA A, RUG.RUG_TRAMITE_RASTREO B WHERE A.ID_TRAMITE_TEMP = B.ID_TRAMITE_TEMP AND CVE_RASTREO = ?)";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setString(2, clvRastreo);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = rs.getInt("NUMERO");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		return regresa;
	}

	public String getUsuariosRelacionados(int idPersona) {
		// obtener usuarios relacionados
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement psUsuarios = null;
		ResultSet rsUsuarios = null;
		String sqlUsuarios = "SELECT id_persona " + 
				"FROM rug_secu_usuarios " + 
				"WHERE id_persona = ? " + 
				"UNION " + 
				"SELECT id_persona " + 
				"FROM rug_secu_usuarios " + 
				"WHERE cve_usuario_padre = ( " + 
				"	SELECT cve_usuario_padre " + 
				"	FROM rug_secu_usuarios " + 
				"	WHERE id_persona = ? " + 
				") " + 
				"UNION " + 
				"SELECT id_persona " + 
				"FROM rug_secu_usuarios " + 
				"WHERE cve_usuario = ( " + 
				"	SELECT cve_usuario_padre " + 
				"	FROM rug_secu_usuarios " + 
				"	WHERE id_persona = ?" + 
				") " +
				"UNION " + 
				"SELECT id_persona " + 
				"FROM rug_secu_usuarios " + 
				"WHERE cve_usuario_padre = ( " + 
				"	SELECT cve_usuario " + 
				"	FROM rug_secu_usuarios " + 
				"	WHERE id_persona = ?" + 
				")";		

		try {

			connection = bd.getConnection();
			psUsuarios = connection.prepareStatement(sqlUsuarios);
			psUsuarios.setInt(1, idPersona);
			psUsuarios.setInt(2, idPersona);
			psUsuarios.setInt(3, idPersona);
			psUsuarios.setInt(4, idPersona);
			rsUsuarios = psUsuarios.executeQuery();
			StringBuffer sbUsuarios = new StringBuffer();
			System.out.println(" SQL de usuarios: " + sqlUsuarios + " Parametros "+ idPersona);
			while(rsUsuarios.next()) {
				sbUsuarios.append(rsUsuarios.getInt("id_persona") + ",");
			}
			return sbUsuarios.toString().substring(0, sbUsuarios.toString().length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rsUsuarios,psUsuarios);
		}
		
		return String.valueOf(idPersona);
	}
}
