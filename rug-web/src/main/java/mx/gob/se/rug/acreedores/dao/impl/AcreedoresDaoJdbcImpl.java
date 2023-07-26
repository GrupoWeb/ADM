package mx.gob.se.rug.acreedores.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.dao.AcreedoresDAO;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.acreedores.to.TramitesMasivosTO;
import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.to.DateUtilRug;

public class AcreedoresDaoJdbcImpl extends BaseRugDao implements AcreedoresDAO {
	public PlSql modificarAcreedorRepresentado(AltaParteTO altaParteTO){
		PlSql regresaM = new PlSql();
//		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{ call RUG.SP_MODIFICA_ACREEDOR_REP( " +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?," +
				" ?,?,?,?,?,?) }";
		CallableStatement cs = null;
		/*
                                psIdTramiteTemp       OUT  NUMBER, --ID TRAMITE TEMPORAL RESULTADO DE LA MODIFICACION
                                psResult              OUT  INTEGER,  
                                psTxResult            OUT  VARCHAR2                        
		 */
		try {
			cs = connection.prepareCall(sql);
			
			cs.setInt(1, altaParteTO.getIdPersona());
			cs.setInt(2, altaParteTO.getIdUsuario());	
			cs.setString(3, altaParteTO.getTipoPersona());
			cs.setString(4, altaParteTO.getTipo()); //Sociedad Mercantil 'SM' o Otros 'OT'
			cs.setString(5, altaParteTO.getRazonSocial());
			cs.setString(6, altaParteTO.getRfc());
			cs.setString(7, altaParteTO.getCurp()); //curp
			cs.setString(8, altaParteTO.getFolioMercantil()); //folio mercantil			
			cs.setString(9,altaParteTO.getCalle());
			cs.setString(10,altaParteTO.getNumeroExterior());
			cs.setString(11,altaParteTO.getNumeroInterior());
			cs.setString(12, altaParteTO.getNombre());
			cs.setString(13,altaParteTO.getApellidoPaterno());
			cs.setString(14,altaParteTO.getApellidoMaterno());
			cs.setInt(15, altaParteTO.getIdColonia());
			cs.setInt(16, altaParteTO.getIdLocalidad());
			cs.setInt(17, altaParteTO.getIdNacionalidad());
			DateUtilRug dateUtilRug = new DateUtilRug();
			cs.setDate(18, dateUtilRug.parseToSQLDate(Calendar.getInstance().getTime()));
			cs.setDate(19, dateUtilRug.parseToSQLDate(Calendar.getInstance().getTime()));
			cs.setString(20, "Este es un contrato de modificación.");
			cs.setNull(21, Types.NULL);
//			cs.setString(8, altaParteTO.getClaveLada());
			cs.setString(22, altaParteTO.getTelefono());
			cs.setString(23, altaParteTO.getExtencion());
			cs.setString(24, altaParteTO.getCorreoElectronico());
			cs.setString(25,altaParteTO.getDomicilioUno());
			cs.setString(26,altaParteTO.getDomicilioDos());
			cs.setString(27, altaParteTO.getPoblacion());
			cs.setString(28, altaParteTO.getZonaPostal());
			cs.setInt(29, altaParteTO.getIdPaisResidencia());	
			cs.setBoolean(30, altaParteTO.getAcreedorInscribe());
			cs.setNull(31, Types.NULL);
			cs.setString(32, altaParteTO.getNif());
			cs.registerOutParameter(33, Types.INTEGER);
			cs.registerOutParameter(34, Types.VARCHAR);
			cs.registerOutParameter(35, Types.VARCHAR);
			cs.registerOutParameter(36, Types.INTEGER);
			
			cs.execute();
			
			MyLogger.Logger.log(Level.INFO, "--Int Acreedores dao modifica --" + cs.getInt(33));
			MyLogger.Logger.log(Level.INFO, "--Str Acreedores dao modifica --" + cs.getString(34));
			MyLogger.Logger.log(Level.INFO, "--Int Acreedores dao modifica --" + cs.getString(35));
			MyLogger.Logger.log(Level.INFO, "--Int Acreedores dao modifica --" + cs.getInt(36));
//			if (cs.getInt(32) == 0){
//				regresa = cs.getInt(32);
//			}
			
			regresaM.setIntPl(cs.getInt(33));
			regresaM.setStrPl(cs.getString(34));
			regresaM.setResFolio(cs.getString(35));
			
		} catch (SQLException e) {
			regresaM = new PlSql();
			regresaM.setIntPl(999);
			regresaM.setStrPl("Error de modificación :"+ e.getMessage());
			e.printStackTrace();
		}finally{
			bd.close(connection, null, cs);
		}
		return regresaM;
	}
	
	public boolean altaAcreedor(AcreedorTO acreedorTO, Integer idUsuario){
		boolean regresa = false;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;		
		String sql ="{call RUG.SP_AltaDatosAcreedor (?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,? ) }";		
		try{
			cs = connection.prepareCall(sql);
			cs.setInt(1, idUsuario);
			cs.setString(2, acreedorTO.getTipoPersona());
			cs.setString(3, acreedorTO.getRazonSocial());
			if (acreedorTO.getRfc()==null){
				cs.setString(4, "");
			}else{
				cs.setString(4, acreedorTO.getRfc());
			}
			if (acreedorTO.getCurp()==null){
				cs.setString(5, "");
			}else{
				cs.setString(5, acreedorTO.getCurp());
			}
			cs.setString(6, acreedorTO.getFolioMercantil());
			cs.setString(7, acreedorTO.getCalle());
			cs.setString(8, acreedorTO.getCalleNumero());
			cs.setString(9, acreedorTO.getCalleNumeroInterior());
			cs.setString(10, acreedorTO.getNombre());
			cs.setString(11, acreedorTO.getApellidoPaterno());
			cs.setString(12, acreedorTO.getApellidoMaterno());
			cs.setInt(13, acreedorTO.getIdColonia());
			cs.setInt(14, acreedorTO.getIdLocalidad());
			cs.setInt(15, acreedorTO.getIdNacionalidad());
			cs.registerOutParameter(16, Types.INTEGER);
			cs.registerOutParameter(17, Types.VARCHAR);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Integer Result  = " + cs.getInt(16));
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Varchar Result  = " + cs.getString(17));
			if (cs.getInt(16)==0){
				regresa = true;
			}
		}catch(Exception e){
				e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
	}

	//==========================================
	//Nuevo metodo de altaacreedorrep
	public PlSql altaAcreedorRepFinal(AcreedorTO acreedorTO, Integer idUsuario) {
		PlSql regresa = new PlSql();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		String sql = "{call RUG.SP_ALTA_ACREEDOR_REP_V2 (" + "?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,?,?,?,?," + 
				"?,?,?)}";
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idUsuario);
			cs.setString(2, acreedorTO.getTipoPersona());
			cs.setString(3, acreedorTO.getTipo());
			cs.setString(4, acreedorTO.getRazonSocial());
			if (acreedorTO.getRfc() == null) {
				cs.setString(5, "");
			} else {
				cs.setString(5, acreedorTO.getRfc());
			}
			if (acreedorTO.getCurp() == null) {
				cs.setString(6, "");
			} else {
				cs.setString(6, acreedorTO.getCurp());
			}
			cs.setString(7, acreedorTO.getFolioMercantil());
			cs.setString(8, acreedorTO.getCalle());
			cs.setString(9, acreedorTO.getCalleNumero());
			cs.setString(10, acreedorTO.getCalleNumeroInterior());
			cs.setString(11, acreedorTO.getNombre());
			cs.setString(12, acreedorTO.getApellidoPaterno());
			cs.setString(13, acreedorTO.getApellidoMaterno());
			setIntCS(cs, acreedorTO.getIdColonia(), 14);
			setIntCS(cs, acreedorTO.getIdLocalidad(), 15);
			cs.setInt(16, acreedorTO.getIdNacionalidad());
			DateUtilRug dateUtilRug = new DateUtilRug();
			cs.setDate(17, dateUtilRug.parseToSQLDate(Calendar.getInstance().getTime()));
			cs.setDate(18, dateUtilRug.parseToSQLDate(Calendar.getInstance().getTime()));
			cs.setString(19, "Este es un contrato de acreedor representado");
			cs.setNull(20, Types.NULL);
			cs.setInt(21, acreedorTO.getIdTramitePendiente());
			cs.setString(22, acreedorTO.getTelefono());
			cs.setString(23, acreedorTO.getExtencion());
			cs.setString(24, acreedorTO.getCorreoElectronico());
			setStringCS(cs,acreedorTO.getDomicilioUno(),25);
			setStringCS(cs,acreedorTO.getDomicilioDos(),26);
			setStringCS(cs,acreedorTO.getPoblacion(),27);
			setStringCS(cs,acreedorTO.getZonaPostal(),28);	
			cs.setInt(29, acreedorTO.getIdPaisResidencia());
			cs.setBoolean(30, acreedorTO.getAcreedorInscribe());
			if(acreedorTO.getAfolExiste()==null){
				cs.setBoolean(31, false);
			}else{
				cs.setBoolean(31, acreedorTO.getAfolExiste());
			}			
			cs.setString(32, acreedorTO.getNif());
			// campo nuevos
			cs.setString(33, acreedorTO.getInscrita());
			cs.setString(34, acreedorTO.getFolioInscrito());
			cs.setString(35, acreedorTO.getLibroInscrito());
			cs.setString(36, acreedorTO.getUbicacionInscrito());
			cs.setString(37, acreedorTO.getEdad());
			cs.setString(38, acreedorTO.getEstadoCivil());
			cs.setString(39, acreedorTO.getProfesion());
			cs.registerOutParameter(40, Types.INTEGER);
			cs.registerOutParameter(41, Types.VARCHAR);
			cs.registerOutParameter(42, Types.VARCHAR);
			cs.registerOutParameter(43, Types.INTEGER);
			cs.execute();
			
			System.out.println("getAfolExiste:::::::::::::::::: "+acreedorTO.getAfolExiste());
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AcreedoresDAO.altaAcreedorRep------int ::::" + cs.getInt(40));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AcreedoresDAO.altaAcreedorRep------String ::::" + cs.getString(41));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AcreedoresDAO.altaAcreedorRep------String Folio ::::" + cs.getString(42));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AcreedoresDAO.altaAcreedorRep------Int Id ::::" + cs.getInt(43));
			
			regresa.setIntPl(cs.getInt(40));
			regresa.setStrPl(cs.getString(41));
			regresa.setResFolio(cs.getString(42));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de Alta Anotacion Sin Garantia :"
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}

		return regresa;
	}
	
	//==========================================
	
	public PlSql altaAcreedorRep(AcreedorTO acreedorTO, Integer idUsuario) {
		PlSql regresa = new PlSql();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		String sql = "{call RUG.SP_ALTA_ACREEDOR_REP (" + "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,?) }";
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idUsuario);
			cs.setString(2, acreedorTO.getTipoPersona());
			cs.setString(3, acreedorTO.getRazonSocial());
			if (acreedorTO.getRfc() == null) {
				cs.setString(4, "");
			} else {
				cs.setString(4, acreedorTO.getRfc());
			}
			if (acreedorTO.getCurp() == null) {
				cs.setString(5, "");
			} else {
				cs.setString(5, acreedorTO.getCurp());
			}

			cs.setString(6, acreedorTO.getFolioMercantil());
			cs.setString(7, acreedorTO.getCalle());
			cs.setString(8, acreedorTO.getCalleNumero());
			cs.setString(9, acreedorTO.getCalleNumeroInterior());
			cs.setString(10, acreedorTO.getNombre());
			cs.setString(11, acreedorTO.getApellidoPaterno());
			cs.setString(12, acreedorTO.getApellidoMaterno());
			setIntCS(cs, acreedorTO.getIdColonia(), 13);
			setIntCS(cs, acreedorTO.getIdLocalidad(), 14);
			cs.setInt(15, acreedorTO.getIdNacionalidad());
			DateUtilRug dateUtilRug = new DateUtilRug();
			cs.setDate(16, dateUtilRug.parseToSQLDate(Calendar.getInstance()
					.getTime()));
			cs.setDate(17, dateUtilRug.parseToSQLDate(Calendar.getInstance()
					.getTime()));
			cs.setString(18, "Este es un contrato de acreedor representado");
			cs.setNull(19, Types.NULL);
			cs.setInt(20, acreedorTO.getIdTramitePendiente());
			cs.setString(21, acreedorTO.getTelefono());
			cs.setString(22, acreedorTO.getExtencion());
			cs.setString(23, acreedorTO.getCorreoElectronico());
			setStringCS(cs,acreedorTO.getDomicilioUno(),24);
			setStringCS(cs,acreedorTO.getDomicilioDos(),25);
			setStringCS(cs,acreedorTO.getPoblacion(),26);
			setStringCS(cs,acreedorTO.getZonaPostal(),27);	
			cs.setInt(28, acreedorTO.getIdPaisResidencia());
			cs.registerOutParameter(29, Types.INTEGER);
			cs.registerOutParameter(30, Types.VARCHAR);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AcreedoresDAO.altaAcreedorRep------int ::::" + cs.getInt(29));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:AcreedoresDAO.altaAcreedorRep------String ::::" + cs.getString(30));
			
			regresa.setIntPl(cs.getInt(29));
			regresa.setStrPl(cs.getString(30));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de Alta Anotacion Sin Garantia :"
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}

		return regresa;
	}

	public boolean bajaAcreedor(Integer idPersona, Integer idAcreedor) {
		boolean regresa = false;
		String sql = "{call RUG.SP_BAJA_ACREEDOR_REP" + " (?,?,?,?) }";
		ConexionBD bd = new ConexionBD();
		CallableStatement cs = null;
		Connection connection = bd.getConnection();
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idPersona);
			cs.setInt(2, idAcreedor);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Integer Result  = "
					+ cs.getInt(3));
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Varchar Result  = "
					+ cs.getString(4));
			if (cs.getInt(3)==0){
				regresa = true;
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}

		return regresa;
	}
	
	public boolean bajaTramiteMasivo(Integer idTramiteFirma, Integer idUsuario) {
		boolean regresa = false;
		String sql = "{call RUG.SP_BAJA_ACREEDOR_MASIVA" + " (?,?,?,?) }";
		ConexionBD bd = new ConexionBD();
		CallableStatement cs = null;
		Connection connection = bd.getConnection();
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramiteFirma);
			cs.setInt(2, idUsuario);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Integer Result  = "
					+ cs.getInt(3));
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Varchar Result  = "
					+ cs.getString(4));
			if (cs.getInt(3)==0){
				regresa = true;
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		
		return regresa;
	}

	public boolean tieneTramites(Integer idAcreedor, Integer idUsuario) {
		boolean regresa = false;
		String sql = "SELECT ID_ACREEDOR FROM V_TRAMITES_PENDIENTES_ACREEDOR WHERE ID_ACREEDOR = ? AND ID_PERSONA = ? AND ID_TIPO_TRAMITE != 19";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			ps.setInt(2, idUsuario);
			rs = ps.executeQuery();
			if (rs.next()) {
				regresa = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}

		return regresa;
	}

	public boolean tieneUsuarios(Integer idAcreedor, Integer idUsuario) {
		boolean regresa = false;
		String sql = "SELECT ID_ACREEDOR FROM V_USUARIOS_ACREEDORES WHERE ID_ACREEDOR = ? AND STATUS_REL = 'AC' ";
		ConexionBD bd = new ConexionBD();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = bd.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			rs = ps.executeQuery();
			if (rs.next()) {
				regresa = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return regresa;
	}

	public List<AcreedorTO> getAcreedoresByPersona(Integer idPersona) {
		List<AcreedorTO> lista = new ArrayList<AcreedorTO>();
		AcreedorTO acreedorTO = new AcreedorTO();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		String sql = "SELECT USUARIO_LOGIN,NOMBRE, AP_PATERNO, AP_MATERNO, RAZON_SOCIAL, ID_NACIONALIDAD, DESC_NACIONALIDAD, CLAVE_PAIS, TELEFONO, EXTENSION, ID_PERSONA, RFC, CURP, PER_JURIDICA, FOLIO_MERCANTIL, NOMBRE_ACREEDOR,"
			+ " ID_DOMICILIO, CALLE, CALLE_COLINDANTE_1, CALLE_COLINDANTE_2, LOCALIDAD, NUM_EXTERIOR,"
			+ " NUM_INTERIOR, ID_COLONIA, DESC_COLONIA, ID_LOCALIDAD, DESC_LOCALIDAD, CVE_ESTADO,"
			+ " CVE_PAIS, CVE_MUNICIP_DELEG, CODIGO_POSTAL "
			+ " FROM RUG.V_USUARIO_ACREEDOR_REP WHERE USUARIO_LOGIN = ? ";
		
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			while (rs.next()) {
				acreedorTO = new AcreedorTO();
				acreedorTO.setNombreCompleto(rs.getString("NOMBRE_ACREEDOR"));
				acreedorTO.setNombre(rs.getString("NOMBRE"));
				acreedorTO.setApellidoMaterno(rs.getString("AP_MATERNO"));
				acreedorTO.setApellidoPaterno(rs.getString("AP_PATERNO"));
				acreedorTO.setRazonSocial(rs.getString("RAZON_SOCIAL"));
				acreedorTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				acreedorTO.setTelefono(rs.getString("TELEFONO"));
				acreedorTO.setExtencion(rs.getString("EXTENSION"));
				if (acreedorTO.getRfc() == null) {
					acreedorTO.setRfc(acreedorTO.getCurp());
				}
				acreedorTO.setIdPersona(rs.getInt("ID_PERSONA"));
				MyLogger.Logger.log(Level.INFO, "id Persona" + acreedorTO.getIdPersona());
				acreedorTO.setRfc(rs.getString("RFC"));
				acreedorTO.setCurp(rs.getString("CURP"));
				acreedorTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				acreedorTO.setCalle(rs.getString("CALLE"));
				acreedorTO.setCalleNumero(rs.getString("NUM_EXTERIOR"));
				acreedorTO.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
				MyLogger.Logger.log(Level.INFO, "codigo postal----"
						+ rs.getString("CODIGO_POSTAL"));
				acreedorTO.setCalleNumeroInterior(rs.getString("NUM_INTERIOR"));
				if (rs.getString("ID_COLONIA") != null) {
					acreedorTO.setIdColonia(Integer.valueOf(rs
							.getString("ID_COLONIA")));
				}
				if (rs.getString("ID_LOCALIDAD") != null) {
					acreedorTO.setIdLocalidad(rs.getInt("ID_LOCALIDAD"));
				}

				acreedorTO.setCurp(rs.getString("CURP"));
				if (acreedorTO.getRfc() == null) {
					acreedorTO.setRfc(acreedorTO.getCurp());
				}
				lista.add(acreedorTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return lista;
	}

	// TODO: Hugo D. JimenezJuarez. 02/09/2010
	// Metodo que regresa una lista de tipo AcreedorTO para efectos de la
	// transmision. Devuelve todos los acredores disponibles excepto el acredor
	// que tenga la garantia a transmitir.
	public List<AcreedorTO> getAcreedoresDisponiblesTransmision(
		String idUsuarioLoggeado, String idAcreedorNoDisponible) {
		List<AcreedorTO> lista = new ArrayList<AcreedorTO>();
		AcreedorTO acreedorTO = new AcreedorTO();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;

		String sql = " SELECT ID_PERSONA, NOMBRE_ACREEDOR ";
		sql += " FROM V_USUARIO_ACREEDOR_REP ";
		sql += " WHERE USUARIO_LOGIN = ? ";
		sql += " AND ID_PERSONA <> ? ";

		try {
			ps=connection.prepareStatement(sql);
			ps.setString(1, idUsuarioLoggeado);
			ps.setString(2, idAcreedorNoDisponible);
			rs = ps.executeQuery();
			while (rs.next()) {
				acreedorTO = new AcreedorTO();
				acreedorTO.setNombreCompleto(rs.getString("NOMBRE_ACREEDOR"));
				acreedorTO.setIdAcreedor(rs.getString("ID_PERSONA"));
				lista.add(acreedorTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return lista;
	}

	public boolean sonAsociados(Connection connection, Integer idPersona,
			Integer idAcreedor) {
		boolean regresa = false;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT USUARIO_LOGIN,NOMBRE, AP_PATERNO, AP_MATERNO, RAZON_SOCIAL, ID_NACIONALIDAD, DESC_NACIONALIDAD, CLAVE_PAIS, TELEFONO, EXTENSION, ID_PERSONA, RFC, CURP, PER_JURIDICA, FOLIO_MERCANTIL, NOMBRE_ACREEDOR,"
				+ " ID_DOMICILIO, CALLE, CALLE_COLINDANTE_1, CALLE_COLINDANTE_2, LOCALIDAD, NUM_EXTERIOR,"
				+ " NUM_INTERIOR, ID_COLONIA, DESC_COLONIA, ID_LOCALIDAD, DESC_LOCALIDAD, CVE_ESTADO,"
				+ " CVE_PAIS, CVE_MUNICIP_DELEG, CODIGO_POSTAL "
				+ " FROM RUG.V_USUARIO_ACREEDOR_REP WHERE USUARIO_LOGIN = ? AND ID_PERSONA = ?";

		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setInt(2, idAcreedor);
			rs = ps.executeQuery();
			if (rs.next()) {
				regresa = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return regresa;
	}

	public List<AcreedorTO> getAcreedoresSinFirmaByPersona(Integer idPersona) {
		List<AcreedorTO> lista = new ArrayList<AcreedorTO>();
		MyLogger.Logger.log(Level.WARNING, "Acreedor Representado sin firma : " + idPersona);
		AcreedorTO acreedorTO = new AcreedorTO();
		String sql = "SELECT ID_TRAMITE_TEMP, USUARIO_LOGIN, NOMBRE, AP_PATERNO, AP_MATERNO, RAZON_SOCIAL, ID_PERSONA, RFC, CURP, PER_JURIDICA, FOLIO_MERCANTIL, NOMBRE_ACREEDOR,"
				+ "  ID_DOMICILIO, CALLE, CALLE_COLINDANTE_1, CALLE_COLINDANTE_2, LOCALIDAD, NUM_EXTERIOR,"
				+ " NUM_INTERIOR, ID_COLONIA, DESC_COLONIA, ID_LOCALIDAD, DESC_LOCALIDAD, CVE_ESTADO,"
				+ " CVE_PAIS, CVE_MUNICIP_DELEG, CODIGO_POSTAL "
				+ " FROM V_USUARIO_ACREEDOR_REP_TODOS WHERE USUARIO_LOGIN = ? AND B_FIRMADO  = 'N' ";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			while (rs.next()) {
				acreedorTO = new AcreedorTO();
				acreedorTO.setIdTramitePendiente(rs.getInt("ID_TRAMITE_TEMP"));
				acreedorTO.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
				acreedorTO.setNombreCompleto(rs.getString("NOMBRE_ACREEDOR"));
				acreedorTO.setNombre(rs.getString("NOMBRE"));
				acreedorTO.setApellidoMaterno(rs.getString("AP_MATERNO"));
				acreedorTO.setApellidoPaterno(rs.getString("AP_PATERNO"));
				acreedorTO.setRazonSocial(rs.getString("RAZON_SOCIAL"));
				acreedorTO.setIdPersona(rs.getInt("ID_PERSONA"));
				MyLogger.Logger.log(Level.INFO, "id Persona" + acreedorTO.getIdPersona());
				acreedorTO.setRfc(rs.getString("RFC"));
				acreedorTO.setCurp(rs.getString("CURP"));
				acreedorTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				acreedorTO.setCalle(rs.getString("CALLE"));
				acreedorTO.setCalleNumero(rs.getString("NUM_EXTERIOR"));
				acreedorTO.setCalleNumeroInterior(rs.getString("NUM_INTERIOR"));
				acreedorTO.setIdColonia(rs.getInt("ID_COLONIA"));
				acreedorTO.setIdLocalidad(rs.getInt("ID_LOCALIDAD"));
				acreedorTO.setCurp(rs.getString("CURP"));
				lista.add(acreedorTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return lista;
	}
	public  List<TramitesMasivosTO> getTramitesCargaMasivaSinFirmar(Integer idPersona) {
		List<TramitesMasivosTO> lista = new ArrayList<TramitesMasivosTO>();
		MyLogger.Logger.log(Level.WARNING, "Tramites Masivos sin firma : " + idPersona);
		TramitesMasivosTO tMasivoTO = new TramitesMasivosTO();
		String sql = "SELECT ID_FIRMA_MASIVA, ID_USUARIO, NOMBRE, NOMBRE_ARCHIVO, RFC, TOTAL_EXITO, TOTAL_NO_EXITO "
				+ " FROM V_CARGA_MASIVA_ACREEDORES WHERE ID_USUARIO = ? ";
		
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			while (rs.next()) {
				tMasivoTO = new TramitesMasivosTO();
				tMasivoTO.setIdFirmaMasiva(rs.getInt("ID_FIRMA_MASIVA"));
				tMasivoTO.setIdPersona(rs.getInt("ID_USUARIO"));
				tMasivoTO.setNombre(rs.getString("NOMBRE"));
				tMasivoTO.setNombreArchivo(rs.getString("NOMBRE_ARCHIVO"));
				tMasivoTO.setRfc(rs.getString("RFC"));
				tMasivoTO.setNumCorrectos(rs.getInt("TOTAL_EXITO"));
				tMasivoTO.setNumErroneos(rs.getInt("TOTAL_NO_EXITO"));
				lista.add(tMasivoTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return lista;
	}

	public AcreedorTO getARByID(Integer idPersona) {
		AcreedorTO acreedorTO = new AcreedorTO();
		String sql = "SELECT USUARIO_LOGIN, ID_PERSONA, NOMBRE, AP_PATERNO, AP_MATERNO, RAZON_SOCIAL, RFC, CURP, PER_JURIDICA, FOLIO_MERCANTIL, NOMBRE_ACREEDOR,"
				+ " ID_DOMICILIO, CALLE, CALLE_COLINDANTE_1, CALLE_COLINDANTE_2, LOCALIDAD, NUM_EXTERIOR,"
				+ " NUM_INTERIOR, ID_COLONIA, DESC_COLONIA, ID_LOCALIDAD, DESC_LOCALIDAD, CVE_ESTADO,"
				+ " CVE_PAIS, CVE_MUNICIP_DELEG, CODIGO_POSTAL, ID_NACIONALIDAD, DESC_NACIONALIDAD, CLAVE_PAIS, TELEFONO, EXTENSION, E_MAIL " +
				" , UBICA_DOMICILIO_1, UBICA_DOMICILIO_2, POBLACION, ZONA_POSTAL, ID_PAIS_RESIDENCIA " +
						"FROM RUG.V_USUARIO_ACREEDOR_REP_TODOS WHERE ID_PERSONA = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			MyLogger.Logger.log(Level.INFO, "--ID PERSONA ---" + idPersona);
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()) {
				MyLogger.Logger.log(Level.INFO, "--Nombre ---" + rs.getString("NOMBRE"));
				MyLogger.Logger.log(Level.INFO, "--Apellido p ---" + rs.getString("AP_MATERNO"));
				MyLogger.Logger.log(Level.INFO, "--Apellido m ---" + rs.getString("AP_PATERNO"));
				acreedorTO = new AcreedorTO();
				acreedorTO.setIdAcreedor(idPersona.toString());
				acreedorTO.setDomicilioUno(rs.getString("UBICA_DOMICILIO_1"));
				acreedorTO.setDomicilioDos(rs.getString("UBICA_DOMICILIO_2"));
				acreedorTO.setPoblacion(rs.getString("POBLACION"));
				acreedorTO.setZonaPostal(rs.getString("ZONA_POSTAL"));
				String nombre = rs.getString("NOMBRE_ACREEDOR");
				acreedorTO.setNombreCompleto(nombre);
				acreedorTO.setNombre(rs.getString("NOMBRE"));
				acreedorTO.setApellidoMaterno(rs.getString("AP_MATERNO"));
				acreedorTO.setApellidoPaterno(rs.getString("AP_PATERNO"));
				acreedorTO.setRazonSocial(rs.getString("RAZON_SOCIAL"));
				acreedorTO.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
				acreedorTO.setIdPersona(rs.getInt("ID_PERSONA"));
				MyLogger.Logger.log(Level.INFO, "id Persona" + acreedorTO.getIdPersona());
				acreedorTO.setRfc(rs.getString("RFC"));
				acreedorTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
				acreedorTO.setCalle(rs.getString("CALLE"));
				acreedorTO.setCalleNumero(rs.getString("NUM_EXTERIOR"));
				acreedorTO.setCalleNumeroInterior(rs.getString("NUM_INTERIOR"));
				acreedorTO.setIdColonia(rs.getInt("ID_COLONIA"));
				acreedorTO.setIdLocalidad(rs.getInt("ID_LOCALIDAD"));
				acreedorTO.setTipoPersona(rs.getString("PER_JURIDICA"));
				acreedorTO.setCurp(rs.getString("CURP"));
				acreedorTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
				acreedorTO.setTelefono(rs.getString("TELEFONO"));
				acreedorTO.setExtencion(rs.getString("EXTENSION"));
				acreedorTO.setCorreoElectronico(rs.getString("E_MAIL"));
				acreedorTO.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
				acreedorTO.setIdPaisResidencia(rs.getInt("ID_PAIS_RESIDENCIA"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return acreedorTO;
	}
	
	public AcreedorTO getAcreRepById(Integer idPersona) {
		AcreedorTO acreedorTO = new AcreedorTO();
		String sql = " SELECT DISTINCT "
					+ " DECODE ( "
					+ " RPP.PER_JURIDICA, "
					+ " 'PF', (SELECT    F.NOMBRE_PERSONA "
					+ "          || ' ' "
					+ "       || F.AP_PATERNO "
                    + "    || ' ' "
                    + " || F.AP_MATERNO "
                    + "FROM RUG.RUG_PERSONAS_FISICAS F "
                    + "WHERE F.ID_PERSONA = RPP.ID_PERSONA), "
                    + " 'PM', (SELECT M.RAZON_SOCIAL "
                    + "       FROM RUG.RUG_PERSONAS_MORALES M "
                    + "   WHERE M.ID_PERSONA = RPP.ID_PERSONA)) "
                    + " AS NOMBRE_ACREEDOR, RPP.RFC, "
                    + " RPP.PER_JURIDICA, "
                    + " RPP.ID_PERSONA "
                    + " FROM RUG.RUG_PERSONAS RPP "
                    + " WHERE ID_PERSONA = ? ";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			MyLogger.Logger.log(Level.INFO, "--ID PERSONA ---" + idPersona);
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()) {
//				MyLogger.Logger.log(Level.INFO, "--Nombre ---" + rs.getString("NOMBRE"));
//				MyLogger.Logger.log(Level.INFO, "--Apellido p ---" + rs.getString("AP_MATERNO"));
//				MyLogger.Logger.log(Level.INFO, "--Apellido m ---" + rs.getString("AP_PATERNO"));
//				acreedorTO = new AcreedorTO();
				acreedorTO.setIdAcreedor(idPersona.toString());
//				acreedorTO.setDomicilioUno(rs.getString("UBICA_DOMICILIO_1"));
//				acreedorTO.setDomicilioDos(rs.getString("UBICA_DOMICILIO_2"));
//				acreedorTO.setPoblacion(rs.getString("POBLACION"));
//				acreedorTO.setZonaPostal(rs.getString("ZONA_POSTAL"));
				String nombre = rs.getString("NOMBRE_ACREEDOR");
				acreedorTO.setNombreCompleto(nombre);
//				acreedorTO.setNombre(rs.getString("NOMBRE"));
//				acreedorTO.setApellidoMaterno(rs.getString("AP_MATERNO"));
//				acreedorTO.setApellidoPaterno(rs.getString("AP_PATERNO"));
//				acreedorTO.setRazonSocial(rs.getString("RAZON_SOCIAL"));
//				acreedorTO.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
				acreedorTO.setIdPersona(rs.getInt("ID_PERSONA"));
//				MyLogger.Logger.log(Level.INFO, "id Persona" + acreedorTO.getIdPersona());
				acreedorTO.setRfc(rs.getString("RFC"));
//				acreedorTO.setFolioMercantil(rs.getString("FOLIO_MERCANTIL"));
//				acreedorTO.setCalle(rs.getString("CALLE"));
//				acreedorTO.setCalleNumero(rs.getString("NUM_EXTERIOR"));
//				acreedorTO.setCalleNumeroInterior(rs.getString("NUM_INTERIOR"));
//				acreedorTO.setIdColonia(rs.getInt("ID_COLONIA"));
//				acreedorTO.setIdLocalidad(rs.getInt("ID_LOCALIDAD"));
				acreedorTO.setTipoPersona(rs.getString("PER_JURIDICA"));
//				acreedorTO.setCurp(rs.getString("CURP"));
//				acreedorTO.setIdNacionalidad(rs.getInt("ID_NACIONALIDAD"));
//				acreedorTO.setTelefono(rs.getString("TELEFONO"));
//				acreedorTO.setExtencion(rs.getString("EXTENSION"));
//				acreedorTO.setCorreoElectronico(rs.getString("E_MAIL"));
//				acreedorTO.setCodigoPostal(rs.getString("CODIGO_POSTAL"));
//				acreedorTO.setIdPaisResidencia(rs.getInt("ID_PAIS_RESIDENCIA"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return acreedorTO;
	}
	

	public AcreedorTO getByIDTramite(Integer idTramite) {
		AcreedorTO acreedorTO = null;
		String sql = "SELECT ID_PERSONA FROM V_TRAMITES_INCOMP_PARTES WHERE ID_PARTE = 4 AND ID_TRAMITE = ?";
		ConexionBD bd = new ConexionBD();

		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps =null;
		try {
			 ps=connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			if (rs.next()) {
				MyLogger.Logger.log(Level.INFO, "Encontro al acreedor");
				acreedorTO = getARByID(rs.getInt("ID_PERSONA"));
			} else {
				MyLogger.Logger.log(Level.WARNING, "el tramite no trajo acreedor representado");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return acreedorTO;
	}

	@Override
	public boolean isAutoridad(Integer idPersona) {
		boolean regresa= false;
		String sql = "SELECT ID_PERSONA FROM V_USUARIO_LOGIN_RUG WHERE ID_PERSONA = ? AND CVE_PERFIL = 'AUTORIDAD'";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		
		return regresa;
	}

	@Override
	public long getCuentaMaestra(Integer idPersona) {
		String sql = "SELECT NVL(cve_usuario_padre, 'MAESTRA') AS cve_usuario_padre FROM rug_secu_usuarios WHERE id_persona = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
//		System.out.println("sql = " + sql);
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			rs = ps.executeQuery();
			if (rs.next()){
				String cveUsuarioPadre = rs.getString("cve_usuario_padre");
				if(cveUsuarioPadre.equals("MAESTRA")) {
					return idPersona;
				}
				
				String sqlMaestra = "SELECT id_persona FROM rug_secu_usuarios WHERE cve_usuario = ?";
				PreparedStatement psMaestra = connection.prepareStatement(sqlMaestra);
				psMaestra.setString(1, cveUsuarioPadre);
				ResultSet rsMaestra = psMaestra.executeQuery();
				if(rsMaestra.next()) {
					return rsMaestra.getLong("id_persona");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			bd.close(connection, rs, ps);
		}
		
		return idPersona;
	}
}
