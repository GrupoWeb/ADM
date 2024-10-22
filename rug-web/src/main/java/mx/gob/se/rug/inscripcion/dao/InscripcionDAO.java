package mx.gob.se.rug.inscripcion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.garantia.to.ActoContratoTO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.garantia.to.ObligacionTO;
import mx.gob.se.rug.inscripcion.to.BienEspecialTO;
import mx.gob.se.rug.inscripcion.to.DeudorTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.CharSetUtil;
import mx.gob.se.rug.util.MyLogger;
import mx.gob.se.rug.util.to.DateUtilRug;

public class InscripcionDAO {
	public boolean actualizaVigencia(InscripcionTO inscripcionTO, Integer idTramite, Integer idEstatus, 
			Integer idPaso, String fechaCelebracion, String banderaFecha){
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		boolean regresa = false;
		try{
			GarantiasDAO dao = new GarantiasDAO();
			dao.actualizaMeses(connection, inscripcionTO);
			dao.altaBitacoraTramiteTX(connection, idTramite, idEstatus, idPaso, fechaCelebracion, banderaFecha);
			regresa = true;		
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			bd.close(connection, null, null);
		}
		return regresa;
	}
	public PlSql insertInscripcion(InscripcionTO inscripcionTO,
			AcreedorTO acreedorTO) {
		PlSql regresa = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		MyLogger.Logger.log(Level.INFO, "RUG-SQL:InscripcionDao.insertInscripcion-------------- Entramos al metodo ::::");
		String sql = "{call RUG.SP_Alta_Tramite_Acreedor_Rep ( "
				+ " ?,?,?,?,?," + " ?,?,?,?,? ) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, inscripcionTO.getIdPersona());			
			cs.setInt(2, inscripcionTO.getIdTipoTramite());
                        System.out.println("inscripcionTO.getIdTipoTramite(): "+inscripcionTO.getIdTipoTramite());
			cs.setInt(3, inscripcionTO.getIdPersona());
			cs.setString(4, "PF");
			cs.setInt(5, 1);
			cs.setNull(6, Types.NULL);
			cs.setNull(7, Types.NULL);
			cs.registerOutParameter(8, Types.INTEGER);
			cs.registerOutParameter(9, Types.INTEGER);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.executeQuery();
			regresa = new PlSql();
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:InscripcionDao.insertInscripcion-------------- Int del Pl ::::"+ cs.getInt(8));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:InscripcionDao.insertInscripcion-------------- Str del Pl ::::" + cs.getString(10));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:InscripcionDao.insertInscripcion-------------- MsjInt del Pl ::::" + cs.getInt(9));
			regresa.setIntPl(cs.getInt(9));
			regresa.setStrPl(cs.getString(10));
			regresa.setResIntPl(cs.getInt(8));			
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage()+", "+ e.getCause());
			regresa.setResIntPl(0);
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:InscripcionDao.insertInscripcion-------------- Sucedio una exception en el metodo  ::::");
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
			
		}
		return regresa;
	}
	public PlSql insertInscripcionClaveRastreo(InscripcionTO inscripcionTO,
			AcreedorTO acreedorTO, String claveRastreo, Integer idArchivo) {
		PlSql regresa = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		MyLogger.Logger.log(Level.INFO, "RUG-SQL:InscripcionDao.insertInscripcion-------------- Entramos al metodo ::::");
		String sql = "{call RUG.SP_ALTA_TRAMITE_RASTREO ( "
				+ " ?,?,?,?,?," + " ?,?,?,?,?," +
						"?,? ) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, inscripcionTO.getIdPersona());
			cs.setInt(2, inscripcionTO.getIdTipoTramite());
			cs.setInt(3, acreedorTO.getIdPersona());
			cs.setString(4, acreedorTO.getTipoPersona());
			cs.setInt(5, 1);
			cs.setNull(6, Types.NULL);
			cs.setNull(7, Types.NULL);
			cs.setString(8, claveRastreo);
			cs.setInt(9, idArchivo);
			cs.registerOutParameter(10, Types.INTEGER);
			cs.registerOutParameter(11, Types.INTEGER);
			cs.registerOutParameter(12, Types.VARCHAR);
			cs.executeQuery();
			regresa = new PlSql();
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:InscripcionDao.insertInscripcion-------------- Int del Pl ::::"+ cs.getInt(10));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:InscripcionDao.insertInscripcion-------------- Str del Pl ::::" + cs.getString(12));
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:InscripcionDao.insertInscripcion-------------- MsjInt del Pl ::::" + cs.getInt(11));
			regresa.setIntPl(cs.getInt(11));
			regresa.setStrPl(cs.getString(12));
			regresa.setResIntPl(cs.getInt(10));			
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage()+", "+ e.getCause());
			regresa.setResIntPl(0);
			MyLogger.Logger.log(Level.INFO, "RUG-SQL:InscripcionDao.insertInscripcion-------------- Sucedio una exception en el metodo  ::::");
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
			
		}
		return regresa;
	}

	public Integer insertInscripcionOtros(InscripcionTO inscripcionTO,
			AcreedorTO acreedorTO) {
		Integer regresa = new Integer(0);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_Alta_Tramite_Acreedor_Rep ( "
				+ " ?,?,?,?,?," + " ?,?,?,?,? ) } ";
		CallableStatement cs =null;
		try {
			 cs = connection.prepareCall(sql);
			cs.setInt(1, inscripcionTO.getIdPersona());
			cs.setInt(2, inscripcionTO.getIdTipoTramite());
			cs.setInt(3, acreedorTO.getIdPersona());
			cs.setString(4, acreedorTO.getTipoPersona());
			cs.setInt(5, 0);
			cs.setNull(6, Types.NULL);
			cs.setNull(7, Types.NULL);
			cs.registerOutParameter(8, Types.INTEGER);
			cs.registerOutParameter(9, Types.INTEGER);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getInt(8);
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Integer Result  = "
					+ cs.getInt(9));
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Varchar Result  = "
					+ cs.getString(10));
			cs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
		return new Integer(regresa);
	}
	
	public List<BienEspecialTO> getListaBienes(Integer idTramite, Integer pQuery){
		List<BienEspecialTO> listaBienes = new ArrayList<BienEspecialTO>();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "";
		
		if(pQuery == 1) {
			sql = "SELECT ID_GARAN_BIEN_PEND,ID_TRAMITE_TEMP,TIPO_BIEN_ESPECIAL, TIPO_IDENTIFICADOR,IDENTIFICADOR,DESCRIPCION_BIEN,SERIE " +
				  "FROM RUG_GARANTIAS_BIENES_PEND " + 
				  "WHERE ID_TRAMITE_TEMP = ?";
		} else {
			sql = "SELECT ID_GARAN_BIEN,ID_TRAMITE,TIPO_BIEN_ESPECIAL, TIPO_IDENTIFICADOR,IDENTIFICADOR,DESCRIPCION_BIEN, SERIE " +
					  "FROM RUG_GARANTIAS_BIENES " + 
					  "WHERE ID_TRAMITE = ?";
		}
		
//		MyLogger.Logger.log(Level.INFO, "pQuery2::" + pQuery);
//		MyLogger.Logger.log(Level.INFO, "sql::" + sql);
		
		ResultSet rs = null;
		PreparedStatement ps =null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramite);
			rs = ps.executeQuery();
			BienEspecialTO bienEspecialTO ;
			while (rs.next()){
				bienEspecialTO = new BienEspecialTO();
				if(pQuery == 1) {
					bienEspecialTO.setIdTramiteGarantia(rs.getInt("ID_GARAN_BIEN_PEND"));
				} else {
					bienEspecialTO.setIdTramiteGarantia(rs.getInt("ID_GARAN_BIEN"));
				}
				bienEspecialTO.setTipoBien(rs.getInt("TIPO_BIEN_ESPECIAL"));
				bienEspecialTO.setTipoIdentificador(rs.getInt("TIPO_IDENTIFICADOR"));
				bienEspecialTO.setIdentificador(rs.getString("IDENTIFICADOR"));
				bienEspecialTO.setDescripcion(rs.getString("DESCRIPCION_BIEN"));
				bienEspecialTO.setSerie(rs.getString("SERIE"));

				listaBienes.add(bienEspecialTO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return listaBienes;
	}
	
	public String eliminarBien(Integer idTramiteGar) {
		String regresa = Constants.FAILED;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_BAJA_GARANTIA_BIENES( ?,?,? ) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramiteGar);					
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getString(3);
			MyLogger.Logger.log(Level.INFO, "InscripcionDAO delete idTramiteGar: Integer Result  = "
					+ cs.getInt(2));
			MyLogger.Logger.log(Level.INFO, "InscripcionDAO delete idTramiteGar: Varchar Result  = "
					+ cs.getString(3));
			cs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
				
		return regresa;
	}
	
	public String registrarBien(BienEspecialTO bienEspecialTO) {
		String regresa = Constants.FAILED;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
    		String sql = "{call RUG.SP_ALTA_GARANTIA_BIENES( ?,?,?,?,?,?,? ,?) } ";
		CallableStatement cs = null;


		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, bienEspecialTO.getIdTramite());
			cs.setInt(2, bienEspecialTO.getTipoBienInt());
			cs.setInt(3, bienEspecialTO.getTipoIdentificadorInt());
			cs.setString(4, bienEspecialTO.getIdentificador());
			cs.setString(5, bienEspecialTO.getDescripcion());
			cs.setString(6, bienEspecialTO.getSerie());
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getString(8);

			MyLogger.Logger.log(Level.INFO, "InscripcionDAO insert bienEspecialTO3: Integer Result  = "
					+ cs.getInt(7));
			MyLogger.Logger.log(Level.INFO, "InscripcionDAO insert bienEspecialTO: Varchar Result  = "
					+ cs.getString(8));
			cs.close();
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.INFO, "error");
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
				
		return regresa;
	}
	
	public String modificarBien(BienEspecialTO bienEspecialTO) {
		String regresa = Constants.FAILED;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_MODIFICA_GARANTIA_BIENES( ?,?,?,?,?,?,?,?) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, bienEspecialTO.getIdTramiteGarantia());
			cs.setInt(2, bienEspecialTO.getTipoBienInt());
			cs.setInt(3, bienEspecialTO.getTipoIdentificadorInt());
			cs.setString(4, bienEspecialTO.getIdentificador());
			cs.setString(5, bienEspecialTO.getDescripcion());
			cs.setString(6, bienEspecialTO.getSerie());
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getString(8);
			MyLogger.Logger.log(Level.INFO, "InscripcionDAO update bienEspecialTO: Integer Result  = "
					+ cs.getInt(7));
			cs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
				
		return regresa;
	}

	public int insert(InscripcionTO inscripcionTO) {
		Integer regresa = new Integer(0);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_Alta_Tramite_Incompleto ( ?,?,?,?,? ) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, inscripcionTO.getIdPersona());
			cs.setInt(2, inscripcionTO.getIdTipoTramite());
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getInt(3);
			MyLogger.Logger.log(Level.INFO, "InscripcionDAO insert inscripcionTO: Integer Result  = "
					+ cs.getInt(4));
			MyLogger.Logger.log(Level.INFO, "InscripcionDAO insert inscripcionTO: Varchar Result  = "
					+ cs.getString(5));
			cs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
		return regresa;

	}

	public Integer insert(Integer idPersona, Integer idTipoTramite) {
		Integer regresa = new Integer(0);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_Alta_Tramite_Incompleto ( ?,?,?,?,?) } ";
		System.out.println("Parametros: Tramite Incompleto Persona " + idPersona + "- Tipo de TRamite " + idTipoTramite );
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idPersona);
			cs.setInt(2, idTipoTramite);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.executeQuery();
			regresa = cs.getInt(3);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
		return regresa;

	}
	
	public List<String> getTextosFormularioByIdGarantia(Integer idTipoGarantia) {
		
		List<String> result = new ArrayList<String>();
		String sql = "SELECT TEXTO FROM RUG_CAT_TEXTO_FORM WHERE ID_TIPO_GARANTIA = ? ORDER BY ID_PARTE ASC";
		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps=connection.prepareStatement(sql);
			ps.setInt(1, idTipoGarantia);
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getString("TEXTO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}
		
		return result;
	}

	public int getTipoTramiteByString(String string) {
		Integer regresa =  new Integer (0);
		String sql = "SELECT ID_TIPO_TRAMITE FROM RUG.RUG_CAT_TIPO_TRAMITE WHERE DESCRIPCION = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
			ps=connection.prepareStatement(sql);
			ps.setString(1, string);
			rs = ps.executeQuery();
			if (rs.next()) {
				regresa = rs.getInt("ID_TIPO_TRAMITE");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}

		return regresa;

	}
	
	public Double getCostoByIdTipoTramiteMasivo(Integer pIdTramiteMasivo) {
		Double regresa =  new Double (0);
		String sql = "SELECT SUM(MAS.COSTO) COSTO FROM (" + 				
				"    SELECT FN_PRECIO_REAL_2023(ID_PERSONA,1,ID_TRAMITE_TEMP,1,null) COSTO " + 
				"    FROM TRAMITES_RUG_INCOMP " + 
				"    WHERE ID_TRAMITE_TEMP IN " + 
				"    (   SELECT ID_TRAMITE_TEMP " + 
				"        FROM RUG_FIRMA_MASIVA " + 
				"        WHERE ID_FIRMA_MASIVA = ? " + 
				"    ) " + 
				") MAS";
                System.out.println("getCostoByIdTipoTramiteMasivo: ");
                System.out.println(sql);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
                    System.out.println("pIdTramiteMasivo: "+pIdTramiteMasivo);
			ps=connection.prepareStatement(sql);
			ps.setInt(1, pIdTramiteMasivo);
			rs = ps.executeQuery();
			if (rs.next()) {
				regresa = rs.getDouble("COSTO");
                                System.out.println("EL COSTO:"+regresa);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}

		return regresa;

	}
        
        /*Agregado por Benjamin*/
        public Double getCostoByIdTipoTramiteMasivo_N(Integer pIdTramiteMasivo) {
		Double regresa =  new Double (0);
		String sql = "SELECT SUM(FN_PRECIO_REAL_2023(A.ID_PERSONA,36,A.ID_TRAMITE_TEMP,1,null)) COSTO" + 				
				"    FROM TRAMITES_RUG_INCOMP  A ,RUG_GARANTIAS_BIENES_PEND  B " + 
				"    WHERE " + 
				"    A.ID_TRAMITE_TEMP = B.ID_TRAMITE_TEMP " + 
				"    AND " + 
				"        A.ID_TRAMITE_TEMP IN " + 
				"        (   SELECT ID_TRAMITE_TEMP FROM RUG_FIRMA_MASIVA WHERE ID_FIRMA_MASIVA = ? ) " ;
                
                
              
                System.out.println("getCostoByIdTipoTramiteMasivo: ");
                System.out.println(sql);
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs =null;
		PreparedStatement ps = null;
		try {
                    System.out.println("pIdTramiteMasivo: "+pIdTramiteMasivo);
			ps=connection.prepareStatement(sql);
			ps.setInt(1, pIdTramiteMasivo);
			rs = ps.executeQuery();
			if (rs.next()) {
				regresa = rs.getDouble("COSTO");
                                System.out.println("EL COSTO:"+regresa);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}

		return regresa;

	}
        /*----------------------*/

	public GarantiaTO getByID(Integer idInscripcion) {
		GarantiaTO garantiaTO = null;
		String sql = "SELECT "
				+ " ID_GARANTIA_PEND, ID_TIPO_GARANTIA, TIPO_GARANTIA,  FECHA_CELEB_ACTO, MONTO_LIMITE, OTROS_TERMINOS_GARANTIA,"
				+ " DESC_BIENES_MUEBLES, TIPO_CONTRATO, FECHA_CELEB_CONTRATO, OTROS_TERMINOS_CONTRATO, VIGENCIA, CAMBIOS_BIENES_MONTO, INSTRUMENTO_PUBLICO, FECHA_FIN_CONTRATO, ID_MONEDA, "
				+ " DESC_MONEDA, CAMBIOS_BIENES_MONTO,NO_GARANTIA_PREVIA_OT, ES_PRIORITARIA, OTROS_REGISTROS, TXT_REGISTROS"
				+ "  FROM RUG.V_GARANTIAS_VALIDACION WHERE ID_GARANTIA_PEND = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idInscripcion);
			rs = ps.executeQuery();
			if (rs.next()) {

				garantiaTO = new GarantiaTO();
				garantiaTO.setIdGarantia(rs.getInt("ID_GARANTIA_PEND"));
				garantiaTO.setIdTipoGarantia(rs.getInt("ID_TIPO_GARANTIA"));
				ActoContratoTO actoContratoTO = new ActoContratoTO();
				// actoContratoTO.setTipoBienes(rs.getString("ID_TIPO_BIEN"));
				actoContratoTO.setTipoGarantia(rs.getString("TIPO_GARANTIA"));
				
				String cambio = rs.getString("CAMBIOS_BIENES_MONTO");
				if (cambio.equals("V")) {
					MyLogger.Logger.log(Level.INFO, "Fue V Verdadero");
					actoContratoTO.setCambiosBienesMonto(true);
				} else {
					MyLogger.Logger.log(Level.INFO, "Fue F Falso");
					actoContratoTO.setCambiosBienesMonto(false);
				}
				
				String noOtG = rs.getString("NO_GARANTIA_PREVIA_OT");
				if (noOtG.equals("V")) {
					MyLogger.Logger.log(Level.INFO, "Fue V Verdadero");
					actoContratoTO.setNoGarantiaPreviaOt(true);
				} else {
					MyLogger.Logger.log(Level.INFO, "Fue F Falso");
					actoContratoTO.setNoGarantiaPreviaOt(false);
				}

				String fechaCele = rs.getString("FECHA_CELEB_ACTO");
				DateUtilRug dateUtilRug = new DateUtilRug();
				MyLogger.Logger.log(Level.INFO, "***" + fechaCele);
				try {
					int idFin = fechaCele.length();
					if (fechaCele.contains(" ")) {
						idFin = fechaCele.indexOf(" ");
					}
					if (idFin < (fechaCele.length() - 1)) {
						fechaCele = dateUtilRug.cambioDeFormato(fechaCele
								.substring(0, idFin));
					} else {
						fechaCele = dateUtilRug.cambioDeFormato(fechaCele
								.substring(0, (fechaCele.length() - 1)));
					}

				} catch (Exception e) {
					fechaCele = rs.getString("FECHA_CELEB_ACTO");
					
				}
				actoContratoTO.setFechaCelebracion(fechaCele);
				if (rs.getString("MONTO_LIMITE")!=null){
					
					actoContratoTO.setMontoMaximo(rs.getString("MONTO_LIMITE"));
				}else{
					
					actoContratoTO.setMontoMaximo("");
				}
				actoContratoTO.setOtrosTerminos(rs
						.getString("OTROS_TERMINOS_GARANTIA"));
				actoContratoTO.setDescripcion(rs
						.getString("DESC_BIENES_MUEBLES"));
				actoContratoTO.setInstrumentoPub(rs
						.getString("INSTRUMENTO_PUBLICO"));
				
				if (rs.getString("CAMBIOS_BIENES_MONTO").equals("V")) {
					MyLogger.Logger.log(Level.INFO, "Fue Verdadero");
					actoContratoTO.setCambiosBienesMonto(true);
				} else {
					MyLogger.Logger.log(Level.WARNING, "No fue Verdadero");
					actoContratoTO.setCambiosBienesMonto(false);
				}

				if (rs.getString("NO_GARANTIA_PREVIA_OT").equals("V")) {
					MyLogger.Logger.log(Level.INFO, "Fue Verdadero");
					actoContratoTO.setNoGarantiaPreviaOt(true);
				} else {
					MyLogger.Logger.log(Level.WARNING, "No fue Verdadero");
					actoContratoTO.setNoGarantiaPreviaOt(false);
				}
				
				//campos nuevos
				if (rs.getString("ES_PRIORITARIA").equals("V")) {				
					actoContratoTO.setGarantiaPrioritaria(true);
				} else {					
					actoContratoTO.setGarantiaPrioritaria(false);
				}
				if (rs.getString("OTROS_REGISTROS").equals("V")) {				
					actoContratoTO.setCpRegistro(true);
					actoContratoTO.setTxtRegistro(rs
							.getString("TXT_REGISTROS"));
				} else {					
					actoContratoTO.setCpRegistro(false);
				}
				
				ObligacionTO obligacionTO = new ObligacionTO();
				fechaCele = rs.getString("FECHA_CELEB_CONTRATO");
				MyLogger.Logger.log(Level.INFO, "***" + fechaCele);
				try {
					int idFin = fechaCele.length();
					if (fechaCele.contains(" ")) {
						idFin = fechaCele.indexOf(" ");
					}

					if (idFin < (fechaCele.length() - 1)) {
						fechaCele = dateUtilRug.cambioDeFormato(fechaCele
								.substring(0, idFin));
					} else {
						fechaCele = dateUtilRug.cambioDeFormato(fechaCele
								.substring(0, (fechaCele.length() - 1)));
					}

				} catch (Exception e) {
					fechaCele = rs.getString("FECHA_CELEB_CONTRATO");
					
				}
				obligacionTO.setFechaCelebracion(fechaCele);
				fechaCele = rs.getString("FECHA_FIN_CONTRATO");
				MyLogger.Logger.log(Level.INFO, "***" + fechaCele);
				try {
					
					if (fechaCele!=null){
						int idFin = fechaCele.length();
						if (fechaCele.contains(" ")) {
							idFin = fechaCele.indexOf(" ");
						}

						if (idFin < (fechaCele.length() - 1)) {
							fechaCele = dateUtilRug.cambioDeFormato(fechaCele
									.substring(0, idFin));
						} else {
							fechaCele = dateUtilRug.cambioDeFormato(fechaCele
									.substring(0, (fechaCele.length() - 1)));
						}

					}
					
				} catch (Exception e) {
					fechaCele = rs.getString("FECHA_FIN_CONTRATO");
					
				}
				obligacionTO.setFechaTerminacion(fechaCele);
				obligacionTO.setOtrosTerminos(rs
						.getString("OTROS_TERMINOS_CONTRATO"));
				obligacionTO.setTipoActoContrato(rs.getString("TIPO_CONTRATO"));
				garantiaTO.setObligacionTO(obligacionTO);
				garantiaTO.setIdMoneda(rs.getInt("ID_MONEDA"));
				garantiaTO.setDescMoneda(rs.getString("DESC_MONEDA"));
				garantiaTO.setActoContratoTO(actoContratoTO);
				garantiaTO.setVigencia(rs.getInt("VIGENCIA"));
				boolean checkBox = true;
				TipoGarantiaDAO dao = new TipoGarantiaDAO();
				MyLogger.Logger.log(Level.INFO, "<"+obligacionTO.getFechaCelebracion()+">["+actoContratoTO.getFechaCelebracion()+"]");
				MyLogger.Logger.log(Level.INFO, "<"+dao.getDescByID(rs.getInt("ID_TIPO_GARANTIA"))+">["+obligacionTO.getTipoActoContrato()+"]");
				MyLogger.Logger.log(Level.INFO, "<"+obligacionTO.getOtrosTerminos()+">["+actoContratoTO.getOtrosTerminos()+"]");
				if (obligacionTO.getFechaCelebracion()!=null){
					
					if (!obligacionTO.getFechaCelebracion().equals(actoContratoTO.getFechaCelebracion())){
						checkBox = false;
					}else if (!dao.getDescByID(rs.getInt("ID_TIPO_GARANTIA")).equals(obligacionTO.getTipoActoContrato())){
						checkBox = false;
						// tipoGarantiaDAO					
					}else{
						if(obligacionTO.getOtrosTerminos()!=null ){
							
							if (!obligacionTO.getOtrosTerminos().equals(actoContratoTO.getOtrosTerminos())){
								checkBox = false;
							}
						}
					}
				}
					
				if (checkBox){
					MyLogger.Logger.log(Level.INFO, "Si se pudo");
					garantiaTO.setChekcBox(true);
				}else{
					MyLogger.Logger.log(Level.INFO, "Ni NO se pudo");
					garantiaTO.setChekcBox(false);
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			bd.close(connection,rs,ps);
		}

		return garantiaTO;
	}

	public PlSql bajaTramiteIncompleto(Integer idTramite) {
		PlSql plSql = new PlSql();
		String sql = "{call RUG.SP_BAJA_TRAMITE_INCOMP ( " + " ?,?,? ) } ";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramite);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();
			plSql.setIntPl(cs.getInt(2));
			plSql.setStrPl(cs.getString(3));
		} catch (SQLException e) {
			plSql.setIntPl(999);
			plSql.setStrPl(e.getMessage());
			e.printStackTrace();
		} finally {
			bd.close(connection,null,cs);
		}
		System.out.println("call SP_BAJA_TRAMITE_INCOMP - Respuesta Str:"+plSql.getStrPl());
		System.out.println("call SP_BAJA_TRAMITE_INCOMP - Respuesta Int:"+plSql.getIntPl());
		return plSql;
	}

}
