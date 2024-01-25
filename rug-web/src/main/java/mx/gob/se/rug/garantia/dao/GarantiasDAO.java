package mx.gob.se.rug.garantia.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import javax.json.JsonObject;

import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.dto.Persona;
import mx.gob.se.rug.exception.NoDateInfrastructureException;
import mx.gob.se.rug.garantia.to.BoletaPagoTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.to.AccionTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.TramiteRUGTO;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.to.DateUtilRug;
import gt.gob.rgm.model.RugGarantiasBienesPend;
import java.util.Date;
import java.text.SimpleDateFormat;

public class GarantiasDAO extends BaseRugDao {
	String cuentasRelacionadas;


	public boolean insertarBoleta(Connection connection,
			BoletaPagoTO boletaTO) {
		boolean regresa = false;
		boletaTO.setIdentificador(getCuentaMaestra(boletaTO.getIdPersonaCarga().longValue()).intValue());
		String sql = "INSERT INTO RUG_UTIL.BOLETA(ID, FECHA_HORA, AGENCIA, ID_TRANSACCION, SERIE, NUMERO, CODIGO_TRAMITE, IDENTIFICADOR, MONTO, RESOLUCION, USUARIO, USADA, ID_GARANTIA, BYTES, TIPO_PAGO, ID_PERSONA_CARGA) VALUES (RUG_UTIL.SEQ_BOLETA.NEXTVAL,SYSDATE,?,?,?,?,?,?,?,'N/A','SYSTEM',0,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, boletaTO.getBanco());
			ps.setInt(2, boletaTO.getIdTipoGarantia());
			ps.setString(3, boletaTO.getSerie());
			ps.setString(4, boletaTO.getNumero());
			ps.setInt(5, boletaTO.getIdTipoGarantia());
			ps.setInt(6, boletaTO.getIdentificador());
			ps.setDouble(7, boletaTO.getMonto());
			ps.setInt(8, boletaTO.getIdGarantia());
			ps.setBytes(9, boletaTO.getBoletaBytes());
			ps.setString(10, boletaTO.getFormaPago());
			ps.setInt(11, boletaTO.getIdPersonaCarga());
			ps.executeUpdate();
			regresa = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return regresa;
	}
	
	public Integer getCountBoletasByUsuario(Integer idPersona, Integer estado, String filtro) {
		int regresa = 0;
		ConexionBD bd = new ConexionBD();
		Long cuentaMaestra = getCuentaMaestra(idPersona.longValue());
		String sql = "SELECT COUNT(*) NUMERO FROM RUG_UTIL.BOLETA B, RUG_PERSONAS_FISICAS PF WHERE B.ID_PERSONA_CARGA = PF.ID_PERSONA (+) AND IDENTIFICADOR = ? AND USADA = ? ";
		if(filtro != null && !filtro.equalsIgnoreCase("")) {
			sql += "AND ( " +
					"(TO_CHAR(B.NUMERO) like '%"+ filtro +"%') or " +
					"(NOMBRE_PERSONA like '%"+ filtro +"%') or " +
					"(TO_CHAR(B.MONTO) like '%"+ filtro +"%') " +					
					") ";		
		}
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, cuentaMaestra.toString());
			ps.setInt(2, estado);			
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
	
	public List<BoletaPagoTO> getBoletasByUsuario(Integer idUsuario, Integer estado, String filtro, Integer inicio, Integer fin) {
		// obtener boletas de la cuenta maestra
		Long cuentaMaestra = getCuentaMaestra(idUsuario.longValue());
		String sql = "SELECT * FROM (SELECT ROWNUM as RN, B.NUMERO, B.MONTO, PF.NOMBRE_PERSONA FROM RUG_UTIL.BOLETA B, RUG_PERSONAS_FISICAS PF WHERE B.ID_PERSONA_CARGA = PF.ID_PERSONA (+) AND IDENTIFICADOR = ? AND USADA = ? ";
		if(filtro != null && !filtro.equalsIgnoreCase("")) {
			sql += "AND ( " +
					"(TO_CHAR(B.NUMERO) like '%"+ filtro +"%') or " +
					"(NOMBRE_PERSONA like '%"+ filtro +"%') or " +
					"(TO_CHAR(B.MONTO) like '%"+ filtro +"%') " +					
					") ";		
		}
		sql +=	") WHERE RN BETWEEN ? AND ?";
		List<BoletaPagoTO> lista = new ArrayList<BoletaPagoTO>();
		Connection connection = null;
		ConexionBD bd = new ConexionBD();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			connection = bd.getConnection();
			if (connection != null) {
				ps = connection.prepareStatement(sql);
				ps.setString(1, cuentaMaestra.toString());
				ps.setInt(2, estado);
				ps.setInt(3, inicio);
				ps.setInt(4, fin);
				rs = ps.executeQuery();				
				while (rs.next()) {
					BoletaPagoTO boleta = new BoletaPagoTO();
					boleta.setNumero(rs.getString("NUMERO"));
					boleta.setMonto(rs.getDouble("MONTO"));
					boleta.setNombrePersonaCarga(rs.getString("NOMBRE_PERSONA"));
					lista.add(boleta);
				}
			} else {
				MyLogger.Logger.log(Level.WARNING,
						"No se pudo crear la conexion");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return lista;
	}
	
	public List<BoletaPagoTO> getBoletasByGarantia(Integer idGarantia) {
		String sql = "SELECT NUMERO, MONTO FROM RUG_UTIL.BOLETA WHERE ID_GARANTIA = ?";
		List<BoletaPagoTO> lista = new ArrayList<BoletaPagoTO>();
		Connection connection = null;
		ConexionBD bd = new ConexionBD();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			connection = bd.getConnection();
			if (connection != null) {
				ps = connection.prepareStatement(sql);
				ps.setInt(1, idGarantia);
				rs = ps.executeQuery();				
				while (rs.next()) {
					BoletaPagoTO boleta = new BoletaPagoTO();
					boleta.setNumero(rs.getString("NUMERO"));
					boleta.setMonto(rs.getDouble("MONTO"));
					lista.add(boleta);
				}
			} else {
				MyLogger.Logger.log(Level.WARNING,
						"No se pudo crear la conexion");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return lista;
	}
	
	public Double getValorSaldoByUsuario(String idUsuario) {
		Double saldo = 0.0D;
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "{ ? = call FN_CALCULAR_SALDO(?)}";
		CallableStatement cs = null;		
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.registerOutParameter(1, Types.DOUBLE);
			cs.setString(2, idUsuario);
			cs.execute();
			saldo = cs.getDouble(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);			
		}
				
		return saldo;
	}
	
	public boolean getSaldoByUsuario(String idUsuario, Integer idTipoTramite, Integer idTramite, Integer idGarantia) {
            
               System.out.println(" Valores " +
                           " Usuario " + idUsuario +
                           " Tipo Tramite " + idTipoTramite +
                           " Tramite " + idTramite +
					   		" Garantia " + idGarantia);
		boolean regresa = false;		
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "{ ? = call FN_TIENE_SALDO(?,?,?,?)}";
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, idUsuario);
			cs.setInt(3, idTipoTramite);
			cs.setInt(4, idTramite);
			cs.setInt(5, idGarantia != null ? idGarantia : 0);
			cs.execute();
                        
                        
			int valor = Integer.valueOf(cs.getInt(1));
                        
			if (valor == 1){
				regresa = true;
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection, null, cs);
		}
                System.out.println("getSaldoByUsuario --- Regresa: "+ regresa);
		return regresa;
	}
	
	public boolean getSaldoMasivoByUsuario(String idUsuario, Integer idTipoTramite, Integer idTramite, Integer cantidad) {
		boolean regresa = false;		
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		String sql = "{ ? = call FN_TIENE_SALDO_MASIVO(?,?,?,?)}";
		CallableStatement cs = null;		
		try {
                    Date date = new Date();
                        System.out.println("CALL ENTRA: "+date);
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.registerOutParameter(1, Types.INTEGER);
                        System.out.println("idUsuario: "+idUsuario);
			cs.setString(2, idUsuario);
                        System.out.println("idTipoTramite: "+idTipoTramite);
			cs.setInt(3, idTipoTramite);
                        System.out.println("idTramite: "+idTramite);
			cs.setInt(4, idTramite);
                        System.out.println("cantidad: "+cantidad);
			cs.setInt(5, cantidad);
			cs.execute();
                        Date date1 = new Date();
                        System.out.println("CALL sale: "+date1);
			int valor = Integer.valueOf(cs.getInt(1));
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
	
	public boolean findBoleta(Connection connection,
			BoletaPagoTO boletaTO) {
		boolean regresa = false;
		
		String sql = "SELECT ID FROM RUG_UTIL.BOLETA WHERE NUMERO = ? AND AGENCIA = ? AND USADA IN (0,1)";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, boletaTO.getNumero());
			ps.setString(2, boletaTO.getBanco());			
			rs = ps.executeQuery();	
			if(!rs.isBeforeFirst()) {
				return true;
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return regresa;
	}
	
	public Integer getCountTramitesEfectuados(Integer idPersona, String filtro) {
		int regresa = 0;
		ConexionBD bd = new ConexionBD();		
		String sql = "SELECT COUNT(*) AS NUMERO FROM (SELECT rownum as rn, tt.* FROM ( SELECT SUB.*, PF.NOMBRE_PERSONA FROM ( " + 
				"SELECT ID_GARANTIA, DESCRIPCION, PRECIO, FECHA, ID_PERSONA_LOGIN FROM RUG.V_TRAMITES_PAGADOS TRA " + 
				"WHERE ID_PERSONA_LOGIN IN ( " + 
				"	SELECT id_persona " + 
				"	FROM rug_secu_usuarios " + 
				"	WHERE id_persona = ? " + 
				"	UNION " + 
				"	SELECT id_persona " + 
				"	FROM rug_secu_usuarios " + 
				"	WHERE cve_usuario_padre = ( " + 
				"		SELECT cve_usuario " + 
				"		FROM rug_secu_usuarios " + 
				"		WHERE id_persona = ?" + 
				"	) " + 
				") " + 
				"AND NOT EXISTS ( " +
				"	SELECT 1 " +
				"	FROM homologado_tramite ht " +
				"	WHERE ht.id_tramite = tra.id_tramite " +
				") " +
				"AND PRECIO <> 0 " + 
				"UNION " +
				"SELECT TP.ID_GARANTIA, TP.DESCRIPCION, TP.PRECIO, TP.FECHA, HT.ID_PERSONA " +
				"FROM V_TRAMITES_PAGADOS TP " +
				"JOIN HOMOLOGADO_TRAMITE HT ON HT.ID_TRAMITE = TP.ID_TRAMITE " +
				"WHERE HT.ID_PERSONA IN ( " +
				"	SELECT id_persona " +
				"	FROM rug_secu_usuarios " +  
				"	WHERE id_persona = ? " + 
				"	UNION " +
				"	SELECT id_persona " +  
				"	FROM rug_secu_usuarios " +  
				"	WHERE cve_usuario_padre = ( " +  
				"		SELECT cve_usuario " +
				"		FROM rug_secu_usuarios " +  
				"		WHERE id_persona = ? " +
				"	) " +
				") " +
				") SUB, RUG_PERSONAS_FISICAS PF " + 
				"WHERE SUB.ID_PERSONA_LOGIN = PF.ID_PERSONA) tt ";
		if(filtro != null && !filtro.equalsIgnoreCase("")) {
			sql += "WHERE ( " +
					"(TO_CHAR(tt.id_garantia) like '%"+ filtro +"%') or " +
					"(tt.descripcion like '%"+ filtro +"%') or " +
					"(TO_CHAR(tt.precio) like '%"+ filtro +"%') or " +
					"(tt.fecha like '%"+ filtro +"%') or " +
					"(tt.nombre_persona like '%"+ filtro +"%') " +
					") ";		
		}
		sql += ") ";
		Connection connection =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idPersona);
			ps.setInt(2, idPersona);
			ps.setInt(3, idPersona);
			ps.setInt(4, idPersona);	
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
	
	public List<AccionTO> getTramitesEfectuados(Integer idPersona, String filtro, Integer inicio, Integer fin){
		String sql = "SELECT * FROM ( SELECT rownum as rn, yy.* FROM ( SELECT tt.* FROM ( SELECT SUB.*, PF.NOMBRE_PERSONA FROM ( " + 
				"SELECT ID_GARANTIA, DESCRIPCION, PRECIO, FECHA, ID_PERSONA_LOGIN FROM RUG.V_TRAMITES_PAGADOS TRA " + 
				"WHERE ID_PERSONA_LOGIN IN ( " + 
				"	SELECT id_persona " + 
				"	FROM rug_secu_usuarios " + 
				"	WHERE id_persona = ? " + 
				"	UNION " + 
				"	SELECT id_persona " + 
				"	FROM rug_secu_usuarios " + 
				"	WHERE cve_usuario_padre = ( " + 
				"		SELECT cve_usuario " + 
				"		FROM rug_secu_usuarios " + 
				"		WHERE id_persona = ?" + 
				"	) " + 
				") " + 
				"AND NOT EXISTS ( " +
				"	SELECT 1 " +
				"	FROM homologado_tramite ht " +
				"	WHERE ht.id_tramite = tra.id_tramite " +
				") " +
				"AND PRECIO <> 0 " + 
				"UNION " +
				"SELECT TP.ID_GARANTIA, TP.DESCRIPCION, TP.PRECIO, TP.FECHA, HT.ID_PERSONA " +
				"FROM V_TRAMITES_PAGADOS TP " +
				"JOIN HOMOLOGADO_TRAMITE HT ON HT.ID_TRAMITE = TP.ID_TRAMITE " +
				"WHERE HT.ID_PERSONA IN ( " +
				"	SELECT id_persona " +
				"	FROM rug_secu_usuarios " +  
				"	WHERE id_persona = ? " + 
				"	UNION " +
				"	SELECT id_persona " +  
				"	FROM rug_secu_usuarios " +  
				"	WHERE cve_usuario_padre = ( " +  
				"		SELECT cve_usuario " +
				"		FROM rug_secu_usuarios " +  
				"		WHERE id_persona = ? " +
				"	) " +
				") " +
				") SUB, RUG_PERSONAS_FISICAS PF " + 
				"WHERE SUB.ID_PERSONA_LOGIN = PF.ID_PERSONA) tt ";
		if(filtro != null && !filtro.equalsIgnoreCase("")) {
			sql += "WHERE ( " +
					"(TO_CHAR(tt.id_garantia) like '%"+ filtro +"%') or " +
					"(tt.descripcion like '%"+ filtro +"%') or " +
					"(TO_CHAR(tt.precio) like '%"+ filtro +"%') or " +
					"(tt.fecha like '%"+ filtro +"%') or " +
					"(tt.nombre_persona like '%"+ filtro +"%') " +
					") ";		
		}
		sql += " ORDER BY TO_DATE(TT.FECHA, 'dd/MM/YYYY hh24:mi:ss') DESC ) yy ) where rn BETWEEN ? AND ?";
		List<AccionTO> lista = new ArrayList<AccionTO>();
		Connection connection = null;
		ConexionBD bd = new ConexionBD();
		ResultSet rs = null;
		PreparedStatement ps = null;
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
		try {
			connection = bd.getConnection();
			if (connection != null) {
				ps = connection.prepareStatement(sql);
				ps.setInt(1, idPersona);
				ps.setInt(2, idPersona);
				ps.setInt(3, idPersona);
				ps.setInt(4, idPersona);
				ps.setInt(5, inicio);
				ps.setInt(6, fin);
				rs = ps.executeQuery();				
				while (rs.next()) {
					AccionTO accion = new AccionTO();
					mx.gob.se.rug.to.GarantiaTO garantia = new mx.gob.se.rug.to.GarantiaTO();
					garantia.setIdgarantia(rs.getInt("ID_GARANTIA"));
					garantia.setDescripcion(rs.getString("DESCRIPCION"));
					mx.gob.se.rug.to.PersonaTO persona = new mx.gob.se.rug.to.PersonaTO();
					persona.setNombre(rs.getString("NOMBRE_PERSONA"));
					accion.setGarantia(garantia);
					accion.setUsuario(persona);
					accion.setImporte(rs.getDouble("PRECIO"));	
					try {
						Date date = format.parse(rs.getString("FECHA"));
						accion.setFecha(date);
					} catch(ParseException ex) {
						ex.printStackTrace();
					}
					lista.add(accion);
				}
			} else {
				MyLogger.Logger.log(Level.WARNING,
						"No se pudo crear la conexion");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return lista;
	}
	
	public boolean actualizaMeses(Connection connection,
			InscripcionTO inscripcionTO) {
		boolean regresa = false;
		String sql = "UPDATE RUG_GARANTIAS_PENDIENTES SET VIGENCIA = ? WHERE ID_GARANTIA_PEND = ?";
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(inscripcionTO.getMeses()));
			ps.setInt(2, inscripcionTO.getGarantiaTO().getIdGarantia());
			ps.executeUpdate();
			regresa = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return regresa;
	}

	public boolean actualizaMeses(InscripcionTO inscripcionTO) {
		boolean regresa = false;
		String sql = "UPDATE RUG_GARANTIAS_PENDIENTES SET VIGENCIA = ? WHERE ID_GARANTIA_PEND = ?";
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(inscripcionTO.getMeses()));
			ps.setInt(2, inscripcionTO.getGarantiaTO().getIdGarantia());
			ps.executeUpdate();
			regresa = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection, null, ps);
		}
		return regresa;
	}

	public boolean altaBitacoraTramiteTX(Connection connection,
			Integer idTramite, Integer idEstatus, Integer idPaso,
			String fechaCelebracion, String banderaFecha) {
		System.out.println("Tipo tramite leasing ----- " + idTramite);
		boolean regresa = false;
		String sql = "{ call RUG.SP_Alta_Bitacora_Tramite2 ( " + " ?, ?, ?,"
				+ " ?, ?, ?, " + " ?, ?" + " ) }";

		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramite);
			cs.setInt(2, idEstatus);
			cs.setInt(3, idPaso);
			cs.setString(4, fechaCelebracion);
			cs.setString(5, banderaFecha);
			cs.setString(6, "");
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			regresa = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				cs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return regresa;

	}

	public boolean altaBitacoraTramite(Integer idTramite, Integer idEstatus,
			Integer idPaso, String fechaCelebracion, String banderaFecha) {
		boolean regresa = false;
		System.out.println("Parametros: Tramite leasing ---- " + idTramite + "- estado " + idEstatus + "- Paso " + idPaso + "- fecha " + fechaCelebracion + "- Bandera " + banderaFecha);
		String sql = "{ call RUG.SP_Alta_Bitacora_Tramite2 ( " + " ?, ?, ?,"
				+ " ?, ?, ?, " + " ?, ?" + " ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramite);
			cs.setInt(2, idEstatus);
			cs.setInt(3, idPaso);
			cs.setString(4, fechaCelebracion);
			cs.setString(5, banderaFecha);
			cs.setString(6, "");
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			regresa = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}

		return regresa;

	}

	public PlSql insertGarantia(InscripcionTO inscripcionTO, GarantiaTO garantiaTO) {
		PlSql regresa = null;
		String sql = "{ call RUG.SP_ALTA_GARANTIA ( " + " ?,?,?,?,?,"
													  + " ?,?,?,?,?,"
													  + " ?,?,?,?,?,"
													  + " ?,?,?,?,"
													  + " ?,?,?"
													  + " ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			DateUtilRug dateUtil = new DateUtilRug();
			
			cs.setInt(1, inscripcionTO.getIdPersona());
			cs.setInt(2, inscripcionTO.getIdInscripcion());
                        System.out.println("ESTE ME INTERESA 1: "+inscripcionTO.getIdInscripcion());
			try {
				Integer tipoGar = Integer.valueOf(inscripcionTO.getGarantiaTO().getIdTipoGarantia());
                                System.out.println("ESTE ME INTERESA 2: "+tipoGar);
				cs.setInt(3, tipoGar);
			} catch(Exception e) {
                            System.out.println("ESTE ME INTERESA 3: ");
				cs.setInt(3, 1);// default
			}
			setDateCS(cs, garantiaTO.getActoContratoTO().getFechaCelebracion(),	4);
			setBigDecimalCS(cs,	garantiaTO.getActoContratoTO().getMontoMaximo(), 5);
			
			//String tipoBienes = garantiaTO.getActoContratoTO().getTipoBienes().replaceAll(",", "|");
			String tipoBienes = "1|2";
			
			cs.setString(6, tipoBienes);
			cs.setString(7, garantiaTO.getActoContratoTO().getDescripcion());
			cs.setString(8, garantiaTO.getActoContratoTO().getOtrosTerminos());
			if(garantiaTO.getActoContratoTO().getCambiosBienesMonto()==null) {
				cs.setString(9, "F");
			}else {
				setForV(cs, garantiaTO.getActoContratoTO().getCambiosBienesMonto(),9);
			}			
			if(garantiaTO.getActoContratoTO().getNoGarantiaPreviaOt()==null) {
				cs.setString(10, "F");
			}else {
				setForV(cs, garantiaTO.getActoContratoTO().getNoGarantiaPreviaOt(),10);
			}
			
			
			cs.setString(11, garantiaTO.getActoContratoTO().getInstrumentoPub());
			setStringCS(cs, garantiaTO.getObligacionTO().getTipoActoContrato(),12);
			setDateCS(cs, garantiaTO.getObligacionTO().getFechaCelebracion(),13);
			
			if (garantiaTO.getObligacionTO().getFechaTerminacion() != null && (!garantiaTO.getObligacionTO().getFechaTerminacion().equals(""))) {
				cs.setDate(14, dateUtil.parseToSQLDate(dateUtil.parseStrDate1(garantiaTO.getObligacionTO().getFechaTerminacion())));
			} else {
				cs.setNull(14, Types.NULL);
			}
			
			System.out.println("TIPO BIEN ::::::::::::::::::::::::::::::::::::::::::::::::::::: " + tipoBienes);
			
			//MyLogger.Logger.log(Level.INFO,"Representante......+++ "+ garantiaTO.getObligacionTO().getOtrosTerminos());
			setStringCS(cs, garantiaTO.getObligacionTO().getOtrosTerminos(), 15);
                        
                        
                        //corellana: ver de donde  viene el idTIpoGarantia  esto es para la carga masiva
                        if (garantiaTO.getIdTipoGarantia()!= null && garantiaTO.getIdTipoGarantia() == 16 )
                            setIntCS(cs, garantiaTO.getIdMoneda(), 16); //no se usa, se inicializa a 1
                        else
                            setIntCS(cs, 1, 16); //no se usa, se inicializa a 1
                        
			//campos nuevos
			//Nuevos Campos
			if(garantiaTO.getActoContratoTO().getGarantiaPrioritaria()==null) {
				cs.setString(17, "F");
			}else {
				setForV(cs, garantiaTO.getActoContratoTO().getGarantiaPrioritaria(),17);	
			}			
			if(garantiaTO.getActoContratoTO().getCpRegistro()==null) {
				cs.setString(18, "F");
			}else {
				setForV(cs, garantiaTO.getActoContratoTO().getCpRegistro(),18);
			}			
			setStringCS(cs, garantiaTO.getActoContratoTO().getTxtRegistro(), 19);
			
			cs.registerOutParameter(20, Types.INTEGER);
			cs.registerOutParameter(21, Types.INTEGER);
			cs.registerOutParameter(22, Types.VARCHAR);
				
			try {
				cs.executeQuery();
			} catch (Exception e) {
				System.out.println("Error en el SP " );
				e.printStackTrace();
			}
			regresa = new PlSql();
			
			regresa.setIntPl(cs.getInt(21));
			regresa.setStrPl(cs.getString(22));
			regresa.setResIntPl(cs.getInt(20));
			
			MyLogger.Logger.log(Level.INFO,"RugDAO::GarantiasDAO.insertGarantia-------------------Integer PL  = "+ cs.getInt(20));
			MyLogger.Logger.log(Level.INFO,"RugDAO::GarantiasDAO.insertGarantia-------------------Varchar PL  = "+ cs.getInt(21));
			MyLogger.Logger.log(Level.INFO,"RugDAO::GarantiasDAO.insertGarantia-------------------Integer Result  = "+ cs.getString(22));

			System.out.println("P1 " + inscripcionTO.getIdPersona() + " P2 " + inscripcionTO.getIdInscripcion() + " P3 " + 1 + " P4 " + garantiaTO.getActoContratoTO().getFechaCelebracion() + " P5 " + garantiaTO.getActoContratoTO().getMontoMaximo() + " P6 " + tipoBienes + " P7 " + garantiaTO.getActoContratoTO().getDescripcion() + " P8 " + garantiaTO.getActoContratoTO().getOtrosTerminos() +  " P9 " + garantiaTO.getActoContratoTO().getCambiosBienesMonto() + " P10 " + garantiaTO.getActoContratoTO().getNoGarantiaPreviaOt() +  " P11 " + garantiaTO.getActoContratoTO().getInstrumentoPub() + " P12 " + garantiaTO.getObligacionTO().getTipoActoContrato() + " P13 " + garantiaTO.getObligacionTO().getFechaCelebracion() + " P14 " + 12 + " P15 " + garantiaTO.getObligacionTO().getOtrosTerminos() + " P16 " + garantiaTO.getIdMoneda() + " P17 " +  garantiaTO.getActoContratoTO().getGarantiaPrioritaria() + " P18 " + garantiaTO.getActoContratoTO().getCpRegistro() + " P19 " + garantiaTO.getActoContratoTO().getTxtRegistro() + " P20 " + cs.getInt(20) + " P21 " + cs.getInt(21) + " P22 " + cs.getString(22));
			cs.close();
		} catch (NoDateInfrastructureException e) {
			System.out.println("Error en el SP 2 " );
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al tratar de convertir una cadena a fecha. "
					+ e.getMessage() + ", " + e.getCause());
			regresa.setResIntPl(0);
			MyLogger.Logger.log(Level.INFO,	"RugDAO::GarantiasDAO.insertGarantia------------------- Sucedio una NoDateInrastructureException en el metodo ");
//			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error en el SP 3" );
			// TODO Auto-generated catch block
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl(e.getErrorCode() + "errorcode|" + e.getSQLState()
					+ "sqlsate|" + e.getStackTrace().toString());
			regresa.setResIntPl(0);
			MyLogger.Logger.log(Level.INFO,	"RugDAO::GarantiasDAO.insertGarantia------------------- Sucedio una SQLException en el metodo ");
//			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error en el SP 4" );
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl(e.getCause() + "cause|" + e.getMessage()
					+ "message|" + e.getStackTrace().toString());
			regresa.setResIntPl(0);
			MyLogger.Logger.log(Level.INFO,	"RugDAO::GarantiasDAO.insertGarantia------------------- Sucedio una Exception en el metodo ");
//			e.printStackTrace();
		} finally {

			bd.close(connection, null, cs);
		}
		return regresa;
	}
	

	public Integer actualizaGarantia(InscripcionTO inscripcionTO,
			GarantiaTO garantiaTO, Integer idGarantiaPendiente) {
		int regresa = 0;
		String sql = "{ call  RUG.SP_UPDATE_GARANTIA_PEND ( " 
				+ " ?,?,?,?,?,"
				+ " ?,?,?,?,?,?," 
				+ " ?,?,?,?,?,?,?,?,"
				+ " ?,?,?,?"
				+ " ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			DateUtilRug dateUtil = new DateUtilRug();
			cs = connection.prepareCall(sql);

			cs.setInt(1, inscripcionTO.getIdPersona());

			cs.setInt(2, idGarantiaPendiente);

			cs.setInt(3, inscripcionTO.getIdInscripcion());

			cs.setInt(4, 1); //ya no aplica

			setDateCS(cs, garantiaTO.getObligacionTO().getFechaCelebracion(),5);
						
			setBigDecimalCS(cs,	garantiaTO.getActoContratoTO().getMontoMaximo(), 6);
			
			//String tipoBienes = garantiaTO.getActoContratoTO().getTipoBienes()
					//.replaceAll(",", "|");
			String tipoBienes = "1|2";

			cs.setString(7, tipoBienes);
			cs.setString(8, garantiaTO.getActoContratoTO().getDescripcion());
			cs.setString(9, garantiaTO.getActoContratoTO().getOtrosTerminos());
			// cs.setString(10,
			// garantiaTO.getObligacionTO().getTipoActoContrato());			
			if(garantiaTO.getActoContratoTO().getCambiosBienesMonto()==null) {
				cs.setString(10, "F");
			}else {
				setForV(cs, garantiaTO.getActoContratoTO().getCambiosBienesMonto(),10);
			}
			
						
			if (garantiaTO.getActoContratoTO().getInstrumentoPub() != null) {

				cs.setString(11, garantiaTO.getActoContratoTO()
						.getInstrumentoPub().trim());
			} else {
				cs.setNull(11, Types.NULL);
			}

			setStringCS(cs, garantiaTO.getObligacionTO().getTipoActoContrato(),12);		
			setDateCS(cs, garantiaTO.getObligacionTO().getFechaCelebracion(),13);
			
			MyLogger.Logger.log(Level.INFO, "valor de fterminacion: ["
					+ garantiaTO.getObligacionTO().getFechaTerminacion() + "]");
			if (garantiaTO.getObligacionTO().getFechaTerminacion() != null
					&& !garantiaTO.getObligacionTO().getFechaTerminacion()
							.trim().equals("")) {
				cs.setDate(14, dateUtil.parseToSQLDate(dateUtil
						.parseStrDate1(garantiaTO.getObligacionTO()
								.getFechaTerminacion())));
			} else {
				cs.setNull(14, Types.NULL);
			}

			setStringCS(cs, garantiaTO.getObligacionTO().getOtrosTerminos(), 15);

			if (garantiaTO.getIdMoneda() != null) {
				cs.setInt(16, garantiaTO.getIdMoneda());
			} else {
				MyLogger.Logger.log(Level.INFO, "no tenia id tipo moneda");
				cs.setInt(16, 1);
			}
			
			//Nuevos Campos
			if(garantiaTO.getActoContratoTO().getGarantiaPrioritaria()==null) {
				cs.setString(17, "F");
			}else {
				setForV(cs, garantiaTO.getActoContratoTO().getGarantiaPrioritaria(),17);
			}
			if(garantiaTO.getActoContratoTO().getCpRegistro()==null) {
				cs.setString(18, "F");
			}else {
				setForV(cs, garantiaTO.getActoContratoTO().getCpRegistro(),18);
			}					
			setStringCS(cs, garantiaTO.getActoContratoTO().getTxtRegistro(), 19);
			
			if(garantiaTO.getActoContratoTO().getNoGarantiaPreviaOt()==null) {
				cs.setString(20, "F");
			}else {
				setForV(cs, garantiaTO.getActoContratoTO().getNoGarantiaPreviaOt(),20);
			}
			
			cs.registerOutParameter(21, Types.INTEGER);
			cs.registerOutParameter(22, Types.INTEGER);
			cs.registerOutParameter(23, Types.VARCHAR);
			cs.executeQuery();
			if (cs.getInt(22) == 0) {
				regresa = cs.getInt(21);
			}
			MyLogger.Logger.log(Level.INFO,
					"GarantiasDAO Alta: Integer Result  = " + cs.getInt(22));
			MyLogger.Logger.log(Level.INFO,
					"GarantiasDAO Alta: Varchar Result  = " + cs.getString(23));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoDateInfrastructureException e) {
			MyLogger.Logger
					.log(Level.INFO,
							"RugDAO::GarantiasDAO.actualizaGarantia------------------- Sucedio una NoDateInrastructureException en el metodo ");
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}

		return new Integer(regresa);
	}

	public int insertGarantia(GarantiaTO garantiaTO, Integer idPersona,
			Integer idTramite) {
		int regresa = 0;
		String sql = "{ call RUG.SP_ALTA_GARANTIA ( " + " ?,?,?,?,?,"
				+ " ?,?,?,?,?," + " ?,?,?,?,?" + " ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			DateUtilRug dateUtil = new DateUtilRug();

			cs.setInt(1, idPersona);
			cs.setInt(2, idTramite);
			cs.setInt(3, garantiaTO.getIdTipoGarantia());
			cs.setDate(4, dateUtil.parseToSQLDate(dateUtil
					.parseStrDate1(garantiaTO.getActoContratoTO()
							.getFechaCelebracion())));
			cs.setBigDecimal(5, new BigDecimal(garantiaTO.getActoContratoTO()
					.getMontoMaximo()));

			String tipoBienes = garantiaTO.getActoContratoTO().getTipoBienes()
					.replaceAll(",", "|");
			MyLogger.Logger
					.log(Level.INFO, "tipoBienes a guardar" + tipoBienes);
			cs.setString(6, tipoBienes);
			cs.setString(7, garantiaTO.getActoContratoTO().getDescripcion());
			cs.setString(8, garantiaTO.getActoContratoTO().getOtrosTerminos());
			cs.setString(9, garantiaTO.getObligacionTO().getTipoActoContrato());
			if (garantiaTO.getObligacionTO().getFechaCelebracion() != null
					&& (!garantiaTO.getObligacionTO().getFechaCelebracion()
							.equals(""))) {
				MyLogger.Logger.log(Level.WARNING, "date error"
						+ garantiaTO.getObligacionTO().getFechaCelebracion());
				cs.setDate(10, dateUtil.parseToSQLDate(dateUtil
						.parseStrDate1(garantiaTO.getObligacionTO()
								.getFechaCelebracion())));
			} else {
				cs.setNull(10, Types.NULL);
			}
			MyLogger.Logger.log(Level.INFO, "valor de fterminacion: ["
					+ garantiaTO.getObligacionTO().getFechaTerminacion() + "]");
			if (garantiaTO.getObligacionTO().getFechaTerminacion() != null
					&& (!garantiaTO.getObligacionTO().getFechaTerminacion()
							.equals(""))) {
				cs.setDate(11, dateUtil.parseToSQLDate(dateUtil
						.parseStrDate1(garantiaTO.getObligacionTO()
								.getFechaTerminacion())));
			} else {
				cs.setNull(11, Types.NULL);
			}

			if (garantiaTO.getObligacionTO().getOtrosTerminos() != null
					&& (!garantiaTO.getObligacionTO().getOtrosTerminos()
							.equals(""))) {
				cs.setString(12, garantiaTO.getObligacionTO()
						.getOtrosTerminos());
			} else {
				cs.setNull(12, Types.NULL);
			}

			cs.registerOutParameter(13, Types.INTEGER);
			cs.registerOutParameter(14, Types.INTEGER);
			cs.registerOutParameter(15, Types.VARCHAR);
			cs.executeQuery();
			if (cs.getInt(14) == 0) {
				regresa = cs.getInt(13);
			}
			MyLogger.Logger.log(Level.INFO,
					"GarantiasDAO Alta: Integer Result  = " + cs.getInt(14));
			MyLogger.Logger.log(Level.INFO,
					"GarantiasDAO Alta: Varchar Result  = " + cs.getString(15));
			cs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoDateInfrastructureException e) {
			MyLogger.Logger
					.log(Level.INFO,
							"RugDAO::GarantiasDAO.insertGarantia------------------- Sucedio una NoDateInrastructureException en el metodo ");
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		return regresa;
	}

	public List<String> getGarantias() {
		List<String> lista = new ArrayList<String>();
		Connection connection = null;
		ConexionBD bd = new ConexionBD();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = " SELECT DESC_TIPO_GARANTIA from CAT_TIPO_GARANTIA ";
		try {
			connection = bd.getConnection();
			if (connection != null) {
				ps = connection.prepareStatement(sql);
				rs = ps.executeQuery();
				String cadena;
				while (rs.next()) {
					cadena = rs.getString("DESC_TIPO_GARANTIA");
					lista.add(cadena);
				}
			} else {
				MyLogger.Logger.log(Level.WARNING,
						"No se pudo crear la conexion");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return lista;
	}

	public Integer getIDGarantiaByIdTramite(Integer idTramite) {
		int regresa = 0;
		String sql = "SELECT ID_GARANTIA_PEND FROM RUG.V_TRAMITES_PENDIENTES WHERE ID_TRAMITE_TEMP = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			if (rs.next()) {
				regresa = rs.getInt("ID_GARANTIA_PEND");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		MyLogger.Logger.log(Level.INFO, "regresa" + regresa);
		return regresa;

	}

	public static String seisNumeros(Integer num) {
		String cadena = "";
		if (num < 10) {
			cadena = "12345" + num;
		} else if (num < 100) {
			cadena = "1234" + num;
		} else if (num < 1000) {
			cadena = "123" + num;
		} else if (num < 10000) {
			cadena = "12" + num;
		} else if (num < 100000) {
			cadena = "1" + num;
		} else if (num < 1000000) {
			cadena = num + "";
		}
		return cadena;
	}

	public static void main(String args[]) {
		try {
			MyLogger.Logger.log(Level.INFO,
					"Iniciamos el proceso de crear el archivo");
			FileWriter fw = new FileWriter("C:\\cargaMasiva\\prueba4.xml");
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			pw.println("<carga-masiva xsi:schemaLocation=\"carga-masiva carga-masiva.xsd\" xmlns=\"carga-masiva\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
			for (int x = 1000; x <= 1005; x++) {
				pw.println("<inscripcion>");
				pw.println("<partes>");
				pw.println("	<otorgante apellido-paterno=\"Aguilar\" nombre=\"Abraham Stalin\" tipo-persona=\""
						+ (x % 2 == 0 ? "PF" : "PM")
						+ "\" folio-electronico=\""
						+ (x % 2 == 0 ? "" : "asdff")
						+ "\" denominacion-razon-social=\"String\" apellido-materno=\"Valencia\" id-nacionalidad=\"1\" />");
				pw.println("</partes>");
				pw.println("<garantia>");
				pw.println("	<creacion id-tipo-garantia=\"1\" fecha-celebracion=\"03/12/2010\" descripcion-bienes-muebles=\"String\" datos-instrumento-publico=\"Datos del instrumento publico de la prueba de carga masiva\" terminos-condiciones=\"Terminos y condisiones de la prueba de carga masiva\" monto-maximo=\"4322\" b-datos-modificables=\"true\" lista-tipo-bienes-muebles=\"1,4,5\" id-moneda=\"1\"/>");
				pw.println("	<obligacion fecha-celebracion=\"03/12/2010\" acto-contrato=\"1\" b-misma-creacion=\"true\" terminos=\"\" fecha-terminacion=\"03/12/2012\"/>");
				pw.println("</garantia>");
				pw.println("<vigencia meses=\"12\"/>");
				pw.println("<identificador clave-rastreo=\"PfffRUE-202" + x
						+ "\"/>");
				pw.println("</inscripcion>");
			}
			pw.println("</carga-masiva>");
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public Long getCuentaMaestra(Long idPersona) {
            
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement psUsuarios = null;
		ResultSet rsUsuarios = null;
		String sqlUsuarios = "SELECT id_persona " + 
				"FROM rug_secu_usuarios " + 
				"WHERE cve_usuario = ( " + 
				"	SELECT NVL(cve_usuario_padre, cve_usuario) AS cve_usuario_padre " + 
				"	FROM rug_secu_usuarios " + 
				"	WHERE id_persona = ?" + 
				")";

		try {
			connection = bd.getConnection();
			psUsuarios = connection.prepareStatement(sqlUsuarios);
			psUsuarios.setLong(1, idPersona);
			rsUsuarios = psUsuarios.executeQuery();
			rsUsuarios.next();
			return rsUsuarios.getLong("id_persona");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rsUsuarios,psUsuarios);
		}
		
		return idPersona;
	}
	
	public boolean tienePartes(Integer pIdTramiteTemp) {
	
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement psPartes = null;
		ResultSet rsPartes = null;
		String sqlPartes = "select NVL(sum(decode(id_parte,2,1,0)),0) deudores, " + 
				"       NVL(sum(decode(id_parte,3,1,0)),0) acreedores " + 
				"from RUG_REL_TRAM_INC_PARTES " + 
				"where id_tramite_temp = ?";

		try {
			connection = bd.getConnection();
			psPartes = connection.prepareStatement(sqlPartes);
			psPartes.setLong(1, pIdTramiteTemp);
			rsPartes = psPartes.executeQuery();
			rsPartes.next();
			
			MyLogger.Logger.log(Level.INFO, "deudores" + rsPartes.getInt("deudores"));
			MyLogger.Logger.log(Level.INFO, "acreedores" + rsPartes.getInt("acreedores"));
			
			if(rsPartes.getInt("deudores")>0 && rsPartes.getInt("acreedores")>0) {								
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rsPartes,psPartes);
		}
		
		return false;
	}
	
	public String getCuentasRelacionadas(Long idPersona) {
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
				"	WHERE id_persona = ? " + 
				") " + 
				"UNION " + 
				"SELECT id_persona " + 
				"FROM rug_secu_usuarios " + 
				"WHERE cve_usuario_padre = ( " + 
				"	SELECT cve_usuario " + 
				"	FROM rug_secu_usuarios " + 
				"	WHERE id_persona = ? " + 
				")";

		try {
			connection = bd.getConnection();
			psUsuarios = connection.prepareStatement(sqlUsuarios);
			psUsuarios.setLong(1, idPersona);
			psUsuarios.setLong(2, idPersona);
			psUsuarios.setLong(3, idPersona);
			psUsuarios.setLong(4, idPersona);
			rsUsuarios = psUsuarios.executeQuery();
			StringBuilder sbCuentas = new StringBuilder();
			while(rsUsuarios.next()) {
				sbCuentas.append(rsUsuarios.getLong("id_persona") + ",");
			}
			return sbCuentas.substring(0, sbCuentas.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rsUsuarios,psUsuarios);
		}
		
		return idPersona.toString();
	}
	
	public List<AccionTO> getTramitesCreados(String idPersonaCuentas, JsonObject filtro, Integer inicio, Integer fin) {
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement psTramites = null;
		ResultSet rsTramites = null;
		String sqlTramites = "SELECT * FROM ( " + 
				"SELECT SUB.*, PF.NOMBRE_PERSONA, ROW_NUMBER() OVER (ORDER BY TO_DATE(SUB.FECHA, 'dd/MM/YYYY hh24:mi:ss')) AS row_number FROM ( " + 
				"	SELECT ID_GARANTIA, DESCRIPCION, PRECIO, FECHA, ID_PERSONA_LOGIN " + 
				"	FROM RUG.V_TRAMITES_PAGADOS TRA " + 
				"	WHERE ID_PERSONA_LOGIN IN (" + idPersonaCuentas + ") " + 
				") SUB, RUG_PERSONAS_FISICAS PF " + 
				"WHERE SUB.ID_PERSONA_LOGIN = PF.ID_PERSONA " +
				"AND TO_DATE(SUB.FECHA, 'dd/MM/YYYY hh24:mi:ss') BETWEEN TO_DATE('" + filtro.getString("fechaInicial") + " 00:00:00', 'YYYY-MM-DD hh24:mi:ss') AND TO_DATE('" + filtro.getString("fechaFinal") + " 23:59:59', 'YYYY-MM-DD hh24:mi:ss') order by row_number desc";
		
				if(!filtro.getString("nombre").isEmpty()) {
			sqlTramites += " AND (UPPER(descripcion) LIKE '%" + filtro.getString("nombre").toUpperCase() + "%' " + 
					"OR UPPER(nombre_persona) LIKE '%" + filtro.getString("nombre").toUpperCase() + "%')";
		}
		sqlTramites += ")" + 
				"WHERE ROW_NUMBER BETWEEN ? AND ?";

		try {
			System.out.println(" SQL Tabla Tramites Realizados " + sqlTramites);
			List<AccionTO> lista = new ArrayList<>();
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
			connection = bd.getConnection();
			psTramites = connection.prepareStatement(sqlTramites);
			psTramites.setInt(1, inicio);
			psTramites.setInt(2, fin);
			rsTramites = psTramites.executeQuery();
			while(rsTramites.next()) {
				System.out.println("Row " + rsTramites.getInt("row_number") + " ID GARANTIA " + rsTramites.getInt("ID_GARANTIA") + " descripcion " + rsTramites.getString("DESCRIPCION") +
						" nombre persona " + rsTramites.getString("NOMBRE_PERSONA") + " precio " + rsTramites.getDouble("PRECIO"));
				AccionTO accion = new AccionTO();
				mx.gob.se.rug.to.GarantiaTO garantia = new mx.gob.se.rug.to.GarantiaTO();
				garantia.setRow(rsTramites.getInt("row_number"));
				garantia.setIdgarantia(rsTramites.getInt("ID_GARANTIA"));
				garantia.setDescripcion(rsTramites.getString("DESCRIPCION"));
				mx.gob.se.rug.to.PersonaTO usuario = new mx.gob.se.rug.to.PersonaTO();
				usuario.setNombre(rsTramites.getString("NOMBRE_PERSONA"));
				accion.setGarantia(garantia);
				accion.setUsuario(usuario);
				accion.setImporte(rsTramites.getDouble("PRECIO"));
				try {
					Date date = format.parse(rsTramites.getString("FECHA"));
					accion.setFecha(date);
				} catch(ParseException ex) {
					ex.printStackTrace();
				}
				lista.add(accion);
			}
			return lista;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rsTramites, psTramites);
		}
		
		return new ArrayList<>();
	}
	
	public List<AccionTO> getTramitesEfectuadosOptimizado(Long idPersona, JsonObject filtro, Integer inicio, Integer fin) {
		// buscar cuentas relacionadas
		cuentasRelacionadas = getCuentasRelacionadas(idPersona);
		System.out.println("Cuentas Relacionadas " + cuentasRelacionadas);
		// buscar tramites relacionados a las cuentas
		return getTramitesCreados(cuentasRelacionadas, filtro, inicio, fin);
	}
	
	public Integer getCountTramitesEfectuadosOptimizado(Long idPersona, JsonObject filtro) {
		// buscar cuentas relacionadas
		cuentasRelacionadas = getCuentasRelacionadas(idPersona);
		System.out.println(" Cuentas Relacionadas " + cuentasRelacionadas);
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement psTramites = null;
		ResultSet rsTramites = null;
		String sqlTramites = "SELECT COUNT(1) AS CANTIDAD " + 
				"FROM RUG.V_TRAMITES_PAGADOS TRA, RUG.RUG_PERSONAS_FISICAS PF " + 
				"WHERE ID_PERSONA_LOGIN IN (" + cuentasRelacionadas + ") " +
				"AND TRA.ID_PERSONA_LOGIN = PF.ID_PERSONA " +
				"AND TO_DATE(TRA.FECHA, 'dd/MM/YYYY hh24:mi:ss') BETWEEN TO_DATE('" + filtro.getString("fechaInicial") + " 00:00:00', 'YYYY-MM-DD hh24:mi:ss') AND TO_DATE('" + 
				filtro.getString("fechaFinal") + " 23:59:59', 'YYYY-MM-DD hh24:mi:ss')";
		if(!filtro.getString("nombre").isEmpty()) {
			sqlTramites += " AND (UPPER(descripcion) LIKE '%" + filtro.getString("nombre").toUpperCase() + "%' " + 
					"OR UPPER(nombre_persona) LIKE '%" + filtro.getString("nombre").toUpperCase() + "%')";
		}

		System.out.println(" SQL relacionado " + sqlTramites);
		try {
			connection = bd.getConnection();
			psTramites = connection.prepareStatement(sqlTramites);
			rsTramites = psTramites.executeQuery();
			if(rsTramites.next()) {
				return rsTramites.getInt("CANTIDAD");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rsTramites, psTramites);
		}
		
		return -1;
	}
}
