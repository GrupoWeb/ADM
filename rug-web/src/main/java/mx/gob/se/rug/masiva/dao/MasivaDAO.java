package mx.gob.se.rug.masiva.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.exception.NoDateInfrastructureException;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.exception.StoreProcedureException;
import mx.gob.se.rug.masiva.to.Anotacion;
import mx.gob.se.rug.masiva.to.AnotacionGarantia;
import mx.gob.se.rug.masiva.to.AvisoPreventivo;
import mx.gob.se.rug.masiva.to.Cancelacion;
import mx.gob.se.rug.masiva.to.CargaMasivaProcess;
import mx.gob.se.rug.masiva.to.Ejecucion;
import mx.gob.se.rug.masiva.to.RectificacionPorError;
import mx.gob.se.rug.masiva.to.RenovacionReduccion;
import mx.gob.se.rug.masiva.to.TipoTramiteCargaMasiva;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.to.PersonaTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

/**
 * 
 * @author Abraham Stalin
 * 
 */
public class MasivaDAO extends BaseRugDao {

	public PlSql executeAltaTramiteRestreo(	Tramite tramite) throws StoreProcedureException {
		PlSql regresa = new PlSql();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_ALTA_TRAMITE_RASTREO ( ?,?,? ,?,?,?, ?,?,?, ?,?,?) } ";
	/*
	 * SP_ALTA_TRAMITE_RASTREO(
	 *  1  peIdPersona            IN     NUMBER, -- IDENTIFICADOR DE LA PESONA K SeLOGUEA                                                        
        2  peIdTipoTramite        IN     NUMBER, --INSCRIPCION , ALTA ACREEDORES, AVISO PREVENTIVO
        3  peIdAcreedor    IN     NUMBER, --IDENTIFICADOR UNICO DE LA PERSONA ACREEDOR REPRESENTADO
        4  pePersonaJuridica      IN     VARCHAR2, --IDENTIFICADOR DE LA PERSONA JURIDICA PUEDE SER PF O PM
        5  peIdPaso               IN     NUMBER,
        6  peStatusTram           IN     NUMBER,
        7  peFechaStatus          IN     DATE,
        8  peCveRastreo           IN     VARCHAR2,
        9  peIdArchivo            IN     NUMBER, 
        10 psIdTramiteIncompleto  OUT    NUMBER, --IDENTIFICADOR UNICO DEL REGISTRO
        11 psResult               OUT    INTEGER,   
		12 psTxResult             OUT    VARCHAR2)
	 * 
	 * */	
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, tramite.getIdTipoTramite());
			if(tramite.getIdAcreedor()!=null){
				cs.setInt(3, tramite.getIdAcreedor());
			}else{
				cs.setNull(3, Types.NULL);
			}
			cs.setNull(4, Types.NULL);
			cs.setInt(5, tramite.getIdPaso());
			cs.setInt(6, tramite.getIdEstatus());
			//fechasysdate
			cs.setNull(7, Types.NULL);
			cs.setString(8, tramite.getClaveRastreo());
			cs.setInt(9, tramite.getIdArchivo());

			cs.registerOutParameter(10, Types.INTEGER);
			cs.registerOutParameter(11, Types.INTEGER);
			cs.registerOutParameter(12, Types.VARCHAR);
			
			cs.executeQuery();
			
			regresa.setIntPl(cs.getInt(11));
			regresa.setStrPl(cs.getString(12));
			
			if(regresa.getIntPl().intValue()==0){
				tramite.setIdTramite(cs.getInt(10));
			}else{
				throw new StoreProcedureException(regresa);
			}
			
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite "	+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
			throw new StoreProcedureException(regresa);
			
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO,
				"-MasivaDAO.executeAltaAvisoPreventivo- plSql.intValue:"
						+ regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"-MasivaDAO.executeAltaAvisoPreventivo- plSql.strValue:"
						+ regresa.getStrPl());
		return regresa;
		
	}
	public PlSql executeAltaAnotacionMasiva(Tramite tramite,AnotacionGarantia anotacion) throws StoreProcedureException {
		PlSql regresa = new PlSql();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_ALTA_ANOTACION_MASIVA ( ?,?,? ,?,?,?, ?,?,?, ?,?) } ";
		/*
		SP_ALTA_ANOTACION_MASIVA
(
    1  peIdPersona                 IN     NUMBER, -- IDENTIFICADOR DE LA PESONA K SeLOGUEA    
    2  peIdGarantia                IN     NUMBER,    
    3  peIdArchivo                 IN     NUMBER,
    4  peCveRastreo                IN     VARCHAR2,
    5  peVigencia                  IN     NUMBER,
    6  peAutoridad                 IN     VARCHAR2,
    7  peAnotacion                 IN     CLOB,  
    8  idAcreedor			      OUT     NUMBER,
    9  psIdTramiteIncompleto      OUT     NUMBER,
    10  psResult                   OUT     INTEGER,
    11 psTxResult                 OUT     VARCHAR2
)
		 * 
		 * */	
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, tramite.getIdGarantia());
			cs.setInt(3, tramite.getIdArchivo());
			
			cs.setString(4, tramite.getClaveRastreo());
			cs.setInt(5, anotacion.getVigencia().getMeses().intValue());
			
			cs.setString(6, anotacion.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			cs.setString(7, anotacion.getDatosAnotacion().getContenidoResolucion());
			
			
			cs.registerOutParameter(8, Types.INTEGER);
			cs.registerOutParameter(9, Types.INTEGER);
			cs.registerOutParameter(10, Types.INTEGER);
			cs.registerOutParameter(11, Types.VARCHAR);
			
			cs.executeQuery();
			
			regresa.setIntPl(cs.getInt(10));
			regresa.setStrPl(cs.getString(11));
			
			if(regresa.getIntPl().intValue()==0){
				tramite.setIdTramite(cs.getInt(9));
				tramite.setIdAcreedor(cs.getInt(8));
			}else{
				throw new StoreProcedureException(regresa);
			}
			
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite "	+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
			throw new StoreProcedureException(regresa);
			
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO,
				"-MasivaDAO.executeAltaAvisoPreventivo- plSql.intValue:"
						+ regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"-MasivaDAO.executeAltaAvisoPreventivo- plSql.strValue:"
						+ regresa.getStrPl());
		return regresa;
		
	}
	public PlSql executeAltaAvisoPreventivoMasivo(AvisoPreventivo avisoPreventivo,
			Tramite tramite) {
		PlSql regresa = new PlSql();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_ALTA_AVISO_PREV_MASIVA ( ?,?,? ,?,?,?) } ";
		/*
		 * SP_ALTA_AVISO_PREV_MASIVA
(
  1  psIdTramiteIncompleto   IN  NUMBER,
  2  peIdPersona             IN  NUMBER, 
  3  peAnotacion_Juez        IN  VARCHAR2,
  4  peDescBienes            IN  CLOB,
  5  psResult               OUT  NUMBER,
  6  psTxResult             OUT  VARCHAR2
)*/
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			
			cs.setInt(1, tramite.getIdTramite());
			cs.setInt(2, tramite.getIdUsuario());
			if( avisoPreventivo.getPersonaSolicitaAutoridadInstruyeAsiento()!=null){
				
				cs.setString(3, avisoPreventivo.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			}else{
				cs.setNull(3, Types.NULL);
			}
			cs.setString(4, avisoPreventivo.getDatosAvisoPreventivo().getDescripcionBienesMuebles());
			
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			
			cs.executeQuery();
			
			regresa.setIntPl(cs.getInt(5));
			regresa.setStrPl(cs.getString(6));
			
			
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de Alta Aviso Preventivo  :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
			
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO,
				"-MasivaDAO.executeAltaAvisoPreventivo- plSql.intValue:"
						+ regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"-MasivaDAO.executeAltaAvisoPreventivo- plSql.strValue:"
						+ regresa.getStrPl());
		return regresa;
		
	}
	public PlSql executeAltaAvisoPreventivo(AvisoPreventivo avisoPreventivo,
			Tramite tramite) {
		PlSql regresa = new PlSql();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_ALTA_AVISO_PREVENTIVO ( ?,?,?,?,? ) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdTramite());
			cs.setString(2, avisoPreventivo.getDatosAvisoPreventivo()
					.getDescripcionBienesMuebles());
			cs.setInt(3, tramite.getIdUsuario());
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.executeQuery();
			regresa.setIntPl(cs.getInt(4));
			regresa.setStrPl(cs.getString(5));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de Alta Aviso Preventivo  :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();

		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO,
				"-MasivaDAO.executeAltaAvisoPreventivo- plSql.intValue:"
						+ regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"-MasivaDAO.executeAltaAvisoPreventivo- plSql.strValue:"
						+ regresa.getStrPl());
		return regresa;

	}

	public PlSql executeAltaTramiteAcreedorRep(Tramite tramite,
			AcreedorTO acreedorTO) {
		PlSql regresa = new PlSql();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_Alta_Tramite_Acreedor_Rep ( "
				+ " ?,?,?,?,?," + " ?,?,?,?,? ) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, tramite.getIdTipoTramite());
			cs.setInt(3, tramite.getIdAcreedor());
			cs.setString(4, acreedorTO.getTipoPersona());
			cs.setInt(5, 0);
			cs.setNull(6, Types.NULL);
			cs.setNull(7, Types.NULL);
			cs.registerOutParameter(8, Types.INTEGER);
			cs.registerOutParameter(9, Types.INTEGER);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.executeQuery();
			regresa.setResIntPl(cs.getInt(8));
			regresa.setIntPl(cs.getInt(9));
			regresa.setStrPl(cs.getString(10));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de Alta Aviso Preventivo  :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();

		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "executeAltaTramiteAcreedorRep ID----"
				+ regresa.getIntPl());
		MyLogger.Logger.log(
				Level.INFO,
				"executeAltaTramiteAcreedorRep Mensaje----"
						+ regresa.getStrPl());
		return regresa;
	}

	public PlSql executeAltaAnotacion(Tramite tramite,
			AnotacionGarantia anotacion) {
		PlSql regresa = new PlSql();
		String sql = "{call RUG.SP_ALTA_ANOTACION ( ?,?,?,?,?,?,?,? )} ";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, tramite.getIdTramite());
			cs.setInt(3, anotacion.getIdentificadorGarantia().getIdGarantia().intValue());
			cs.setString(4, anotacion.getDatosAnotacion()
					.getContenidoResolucion());
			cs.setString(5, anotacion.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			cs.setString(6, anotacion.getVigencia().getMeses() + "");
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.executeQuery();
			regresa.setIntPl(cs.getInt(7));
			regresa.setStrPl(cs.getString(8));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de Alta Anotacion  :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();

		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO,
				"executeAltaAnotacion ID----" + regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO, "executeAltaAnotacion Mensaje----"
				+ regresa.getStrPl());
		return regresa;

	}

	public PlSql executeAltaBitacoraTramite2(Integer idTramite,
			Integer idEstatus, Integer idPaso, String fechaCelebracion,
			String banderaFecha) {
            System.out.println("executeAltaBitacoraTramite2");
            System.out.println("executeAltaBitacoraTramite2: idTramite "+idTramite);
            System.out.println("executeAltaBitacoraTramite2: idPaso "+idPaso);
            System.out.println("executeAltaBitacoraTramite2: fechaCelebracion "+fechaCelebracion);
            System.out.println("executeAltaBitacoraTramite2: banderaFecha "+banderaFecha);
		PlSql regresa = new PlSql();
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
			cs.setString(6, banderaFecha);
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			regresa.setIntPl(cs.getInt(7));
			regresa.setStrPl(cs.getString(8));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de Alta Anotacion Sin Garantia :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "executeAltaBitacoraTramite2 ID----"
				+ regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"executeAltaBitacoraTramite2 Mensaje----" + regresa.getStrPl());
		return regresa;

	}

	/**
	 * 
	 * @param idPersona
	 * @param idTipoTramite
	 * @return
	 */
	public PlSql executeAltaTramiteIncompleto(Integer idPersona,
			Integer idTipoTramite) {
            System.out.println("POR AQUI ES: "+idTipoTramite);
		PlSql regresa = new PlSql();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_Alta_Tramite_Incompleto ( ?,?,?,?,?) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idPersona);
			cs.setInt(2, idTipoTramite);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.executeQuery();
			regresa.setIntPl(cs.getInt(4));
			regresa.setStrPl(cs.getString(5));
			regresa.setResIntPl(cs.getInt(3));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de Alta Anotacion Sin Garantia :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "executeAltaTramiteIncompleto ID----"
				+ regresa.getIntPl());
		MyLogger.Logger
				.log(Level.INFO, "executeAltaTramiteIncompleto Mensaje----"
						+ regresa.getStrPl());
		return regresa;

	}

	/**
	 * 
	 * @param anotacion
	 * @param tramite
	 * @return
	 */
	public PlSql executeAltaAnotacionSinGarantia(Anotacion anotacion,
			Tramite tramite) {
		PlSql regresa = new PlSql();
		String sql = "{call RUG.SP_ALTA_ANOTACION_SN_GARANTIA ( ?,?,?,?,?,?,? )} ";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, tramite.getIdTramite());
			cs.setString(3, anotacion.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			cs.setString(4, anotacion.getDatosAnotacion().getContenidoResolucion());
			cs.setInt(5, anotacion.getVigencia().getMeses().intValue());
			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.executeQuery();
			regresa.setIntPl(cs.getInt(6));
			regresa.setStrPl(cs.getString(7));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de Alta Anotacion Sin Garantia :"
					+ e.getMessage());
			regresa.setResIntPl(0);
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO,
				"executeAltaAnotacionSinGarantia ID----" + regresa.getIntPl());
		MyLogger.Logger.log(
				Level.INFO,
				"executeAltaAnotacionSinGarantia Mensaje----"
						+ regresa.getStrPl());
		return regresa;
	}

	/**
	 * 
	 * @param idAcreedor
	 * @param tramite
	 * @return
	 */
	public PlSql cambiaParteAcreedorRepresentado(Integer idAcreedor,
			Tramite tramite) {
		PlSql regresa = new PlSql();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_REL_PARTE_RECTIFICA( ?,?,?,?) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idAcreedor);
			cs.setInt(2, tramite.getIdTramite());
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.executeQuery();
			regresa.setIntPl(cs.getInt(3));
			regresa.setStrPl(cs.getString(4));

		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de prorroga o reduccion de vigencia :"
					+ e.getMessage());
			regresa.setResIntPl(0);
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO,
				"cambiaParteAcreedorRepresentado ID- ---" + regresa.getIntPl());
		MyLogger.Logger.log(
				Level.INFO,
				"cambiaParteAcreedorRepresentado Mensaje- ---"
						+ regresa.getStrPl());
		return regresa;

	}

	/**
	 * 
	 * @param
	 * @return
	 */
	public PlSql actualizaMeses(Tramite tramite, Integer meses) {
		PlSql regresa = new PlSql();
		String sql = "UPDATE RUG_GARANTIAS_PENDIENTES SET VIGENCIA = ? WHERE ID_ULTIMO_TRAMITE = ?";
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, meses);
			ps.setInt(2, tramite.getIdTramite());
			ps.executeUpdate();
			regresa.setIntPl(0);
			regresa.setStrPl("se actualizo correctamente el tramite");
			regresa.setResIntPl(0);
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de prorroga o reduccion de vigencia :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, ps);
		}
		MyLogger.Logger.log(Level.INFO,
				"actualizaMeses ID----" + regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"actualizaMeses Mensaje----" + regresa.getStrPl());
		return regresa;
	}

	/**
	 * 
	 * @param idTramite
	 * @param descripcion
	 * @param idPersona
	 * @return
	 */
	public PlSql insertAviso(Integer idTramite, String descripcion,
			Integer idPersona) {
		PlSql regresa = new PlSql();
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_ALTA_AVISO_PREVENTIVO ( ?,?,?,?,? ) } ";
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, idTramite);
			cs.setString(2, descripcion);
			cs.setInt(3, idPersona);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.executeQuery();
			regresa.setIntPl(cs.getInt(4));
			regresa.setStrPl(cs.getString(5));
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Integer Result  = "
					+ cs.getInt(4));
			MyLogger.Logger.log(Level.INFO, "AcreedoresDAO: Varchar Result  = "
					+ cs.getString(5));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de prorroga o reduccion de vigencia :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO,
				"insertAviso ID- ---" + regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"insertAviso Mensaje- ---" + regresa.getStrPl());
		return regresa;

	}

	/**
	 * 
	 * @param tramite
	 * @param anotacion
	 * @return
	 */
	@Deprecated
	public PlSql executeAltaAnotacionSinGarantia(Tramite tramite,
			Anotacion anotacion) {
		PlSql regresa = null;
		String sql = "{ call RUG.SP_ALTA_ANOTACION_SG_MASIVA- (  ?,?,?,?,?,"
				+ " ?,?,?,?,?, ? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, tramite.getIdAcreedor());
			cs.setString(3, "");
			cs.setInt(4, tramite.getIdArchivo());
			cs.setString(5, anotacion.getIdentificador().getClaveRastreo());
			cs.setInt(6, anotacion.getVigencia().getMeses().intValue());
			cs.setString(7, anotacion.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			cs.setString(8, anotacion.getDatosAnotacion()
					.getContenidoResolucion());
			cs.registerOutParameter(9, Types.INTEGER);
			cs.registerOutParameter(10, Types.INTEGER);
			cs.registerOutParameter(11, Types.VARCHAR);
			cs.executeQuery();
			regresa = new PlSql();
			regresa.setIntPl(cs.getInt(10));
			regresa.setStrPl(cs.getString(11));
			regresa.setResIntPl(cs.getInt(9));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de prorroga o reduccion de vigencia :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO,
				"executeAltaAnotacionSinGarantia ID- ---" + regresa.getIntPl());
		MyLogger.Logger.log(
				Level.INFO,
				"executeAltaAnotacionSinGarantia Mensaje- ---"
						+ regresa.getStrPl());
		return regresa;
	}

	/**
	 * 
	 * @param tramite
	 * @param reduccion
	 * @return
	 */
	public PlSql executeAltaProrrogaMasiva(Tramite tramite,
			RenovacionReduccion reduccion) {
		PlSql regresa = null;
		String sql = "{ call RUG.SP_ALTA_PRORROGA_MASIVA (  ?,?,?,?,?,"
				+ " ?,?,?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, reduccion.getIdentificadorGarantia().getIdGarantia().intValue());
			cs.setInt(3, tramite.getIdAcreedor());
			cs.setInt(4, tramite.getIdArchivo());
			cs.setString(5, reduccion.getIdentificadorGarantia().getClaveRastreo());
			cs.setInt(6, reduccion.getDatosRenovacionReduccion().getVigencia().intValue());
			
			if( reduccion.getPersonaSolicitaAutoridadInstruyeAsiento()!=null){
				
				cs.setString(7, reduccion.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			}else{
				cs.setNull(7, Types.NULL);
			}
			cs.registerOutParameter(8, Types.INTEGER);
			cs.registerOutParameter(9, Types.INTEGER);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.executeQuery();
			regresa = new PlSql();
			regresa.setIntPl(cs.getInt(9));
			regresa.setStrPl(cs.getString(10));
			regresa.setResIntPl(cs.getInt(8));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de prorroga o reduccion de vigencia :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "executeAltaProrrogaMasiva ID- ---"
				+ regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"executeAltaProrrogaMasiva Mensaje- ---" + regresa.getStrPl());
		return regresa;
	}

	/**
	 * 
	 * @param tramite
	 * @param transmicion
	 * @return
	 */
	
	/**
	 * 
	 * @param tramite
	 * @param cancelacion
	 * @return
	 */
	public PlSql executeAltaCancelacionMasiva(Tramite tramite,
			Cancelacion cancelacion) {

		PlSql regresa = null;
		String sql = "{ call RUG.SP_ALTA_CANCELACION_MASIVA ( " + " ?,?,?,?,?,?,"
				+ " ?,?,?,? " + " ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, cancelacion.getIdentificadorGarantia().getIdGarantia().intValue());
			cs.setInt(3, tramite.getIdAcreedor());
			if(cancelacion.getPersonaSolicitaAutoridadInstruyeAsiento()!=null){
				cs.setString(4, cancelacion.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			}else{
				cs.setNull(4, Types.NULL);
			}
			cs.setString(5, tramite.getClaveRastreo());
			cs.setInt(6, tramite.getIdArchivo());
			cs.setString(7, cancelacion.getRazon());
			cs.registerOutParameter(8, Types.INTEGER);
			cs.registerOutParameter(9, Types.INTEGER);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.executeQuery();
			regresa = new PlSql();
			regresa.setIntPl(cs.getInt(9));
			regresa.setStrPl(cs.getString(10));
			regresa.setResIntPl(cs.getInt(8));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de cancelacion :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "executeAltaCancelacionMasiva ID- ---"
				+ regresa.getIntPl());
		MyLogger.Logger.log(
				Level.INFO,
				"executeAltaCancelacionMasiva Mensaje- ---"
						+ regresa.getStrPl());
		return regresa;
	}
	
	public PlSql executeAltaEjecucionMasiva(Tramite tramite,
			Ejecucion ejecucion) {

		PlSql regresa = null;
		String sql = "{ call RUG.SP_ALTA_EJECUCION_MASIVA ( " + " ?,?,?,?,?,?,"
				+ " ?,?,?,? " + " ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, ejecucion.getIdentificadorGarantia().getIdGarantia().intValue());
			cs.setInt(3, tramite.getIdAcreedor());
			if(ejecucion.getPersonaSolicitaAutoridadInstruyeAsiento()!=null){
				cs.setString(4, ejecucion.getPersonaSolicitaAutoridadInstruyeAsiento().getContenidoResolucion());
			}else{
				cs.setNull(4, Types.NULL);
			}
			cs.setString(5, tramite.getClaveRastreo());
			cs.setInt(6, tramite.getIdArchivo());
			cs.setString(7, ejecucion.getRazon());
			cs.registerOutParameter(8, Types.INTEGER);
			cs.registerOutParameter(9, Types.INTEGER);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.executeQuery();
			regresa = new PlSql();
			regresa.setIntPl(cs.getInt(9));
			regresa.setStrPl(cs.getString(10));
			regresa.setResIntPl(cs.getInt(8));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de ejecucion :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "executeAltaEjecucionMasiva ID- ---"
				+ regresa.getIntPl());
		MyLogger.Logger.log(
				Level.INFO,
				"executeAltaEjecucionMasiva Mensaje- ---"
						+ regresa.getStrPl());
		return regresa;
	}
	
	
	/**
	 * 
	 * @param tramite
	 * @param garantia
	 * @return
	 * @throws NoDateInfrastructureException 
	 */
	
	public PlSql executeModificaGarantiaRectifica(Tramite tramite,
			RectificacionPorError rectificacionPorError) throws NoDateInfrastructureException {
		
		PlSql regresa = new PlSql();
		String sql = "{ call RUG.SP_MODIFICA_GARANTIA_RECTIFICA ( " + " ?,?,?,?,?,"+ " ?,?,?,?,?,"+ " ?,?,?,?,?,"
				+ " ?,?,? " + " ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);			
			cs.setInt(1, tramite.getIdTramite());
			cs.setInt(2, rectificacionPorError.getIdentificadorGarantia().getIdGarantia().intValue());
			cs.setInt(3, rectificacionPorError.getDatosGarantia().getIdTipoGarantia().intValue());
			setDateCS(cs, rectificacionPorError.getDatosGarantia().getFechaCelebracion(), 4);
			try{
				cs.setDouble(5, rectificacionPorError.getDatosGarantia().getMontoMaximo());
			}catch(Exception e){
				cs.setNull(5, Types.NULL);
			}
			try{
				cs.setInt(6, rectificacionPorError.getDatosGarantia().getIdMoneda().intValue());
			}catch(Exception e){
				cs.setNull(6, Types.NULL);
			}
			cs.setString(7, getTipoGarantiasStr(rectificacionPorError.getDatosGarantia().getTipoBienMueble()));
			cs.setString(8, rectificacionPorError.getDatosGarantia().getDescripcionBienesMuebles());
			cs.setString(9, rectificacionPorError.getDatosGarantia().isBDatosModificables()==true?"V":"F");
			setStringCS(cs, null, 10);	
			try{
				cs.setString(11, rectificacionPorError.getDatosGarantia().getTerminosCondiciones());
			}catch(Exception e){
				cs.setNull(11, Types.NULL);
			}
			cs.setString(12, rectificacionPorError.getObligacionGarantiza().getActoContrato());
			setDateCS(cs, rectificacionPorError.getObligacionGarantiza().getFechaCelebracion(), 13);
			setDateCS(cs, rectificacionPorError.getObligacionGarantiza().getFechaTerminacion(), 14);
			cs.setString(15, rectificacionPorError.getObligacionGarantiza().getTerminos());			
			cs.setInt(16, tramite.getIdUsuario());			
			cs.registerOutParameter(17, Types.INTEGER);
			cs.registerOutParameter(18, Types.VARCHAR);
			cs.executeQuery();
			regresa.setIntPl(cs.getInt(17));
			regresa.setStrPl(cs.getString(18));
		} catch (SQLException e) {
			
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de cancelacion :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "executeAltaCancelacionMasiva ID- ---"
				+ regresa.getIntPl());
		MyLogger.Logger.log(
				Level.INFO,
				"executeAltaCancelacionMasiva Mensaje- ---"
						+ regresa.getStrPl());
		return regresa;
	}

	/**
	 * 
	 * @param tramite
	 * @return
	 */
	public PlSql executeAltaParteTramIncRast(Tramite tramite) {

		PlSql regresa = null;
		String sql = "{ call RUG.SP_Alta_Parte_Tram_Inc_Rast ( "
				+ " ?,?,?,?,?," + " ?,?,?,? " + " ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, tramite.getIdTipoTramite());
			cs.setInt(3, tramite.getIdGarantia());
			cs.setInt(4, tramite.getIdAcreedor());
			cs.setInt(5, tramite.getIdArchivo());
			cs.setString(6, tramite.getClaveRastreo());
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.INTEGER);
			cs.registerOutParameter(9, Types.VARCHAR);
			cs.executeQuery();
			regresa = new PlSql();
			regresa.setIntPl(cs.getInt(8));
			regresa.setStrPl(cs.getString(9));
			regresa.setResIntPl(cs.getInt(7));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite base :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			MyLogger.Logger
					.log(Level.INFO,
							"RugDAO::MasivaDAO.executeAltaParteTramIncRast------------------- Error al ejecutar el PL executeAltaParteTramIncRast  ");
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "executeAltaParteTramIncRast ID- ---"
				+ regresa.getIntPl());
		MyLogger.Logger
				.log(Level.INFO, "executeAltaParteTramIncRast Mensaje- ---"
						+ regresa.getStrPl());
		return regresa;
	}
	public void altaParteTramIncRast(Tramite tramite) throws InfrastructureException, CargaMasivaException {
		
		String sql = "{ call RUG.SP_Alta_Parte_Tram_Inc_Rast ( "
				+ " ?,?,?,?,?," + " ?,?,?,? " + " ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdUsuario());
			cs.setInt(2, tramite.getIdTipoTramite());
			cs.setInt(3, tramite.getIdGarantia());
			cs.setInt(4, tramite.getIdAcreedor());
			cs.setInt(5, tramite.getIdArchivo());
			cs.setString(6, tramite.getClaveRastreo());
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.INTEGER);
			cs.registerOutParameter(9, Types.VARCHAR);
			cs.executeQuery();
			
			if(cs.getInt(8)==0){
				tramite.setIdTramite(cs.getInt(7));
			}else{
				throw new CargaMasivaException(cs.getString(9),cs.getInt(8));
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE,"SP_Alta_Parte_Tram_Inc_Rast :: ",e);
			throw new InfrastructureException("Ocurrio un Error al intentar Ejecutar SP_Alta_Parte_Tram_Inc_Rast");
		} finally {
			bd.close(connection, null, cs);
		}
	}
	public void spCopiaGarantia(Tramite tramite) throws InfrastructureException, CargaMasivaException {
	/**	PROCEDURE     SP_COPIA_GARANTIA
        (
            peIdTramiteTemp      IN  RUG_REL_TRAM_INC_GARAN.ID_TRAMITE_TEMP%TYPE,
            peIdGarantia         IN  NUMBER,    
            peIdGarantiaPend         OUT  NUMBER,                                                            
            psResult            OUT  INTEGER,   
            psTxResult          OUT  VARCHAR2**/         
		String sql = "{ call RUG.SP_COPIA_GARANTIA ( ?,?,?,?,?) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdTramite());
			cs.setInt(2, tramite.getIdGarantia());
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.executeQuery();
			
			if(cs.getInt(4)!=0){
				throw new CargaMasivaException(cs.getInt(4));
			}else{
				tramite.setIdGarantiaPendiente(cs.getInt(3));
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE,"SP_COPIA_GARANTIA :: ",e);
			throw new InfrastructureException("Ocurrio un Error al intentar Ejecutar SP_COPIA_GARANTIA");
		} finally {
			bd.close(connection, null, cs);
		}
	}

	/**
	 * 
	 * @param idAcreedor
	 * @param idGarantia
	 * @return
	 */
	public boolean isTramite(Integer idAcreedor, Integer idGarantia) {
		return false;
	}

	/**
	 * 
	 * @returnList
	 */
	public List<TipoTramiteCargaMasiva> getListaTipoTramite() {
		List<TipoTramiteCargaMasiva> lista = new ArrayList<TipoTramiteCargaMasiva>();
		String sql = "select ID_TIPO_TRAMITE, DESCRIPCION from V_CATALOGO_CARGA_MASIVA";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			TipoTramiteCargaMasiva tt;
			while (rs.next()) {
				tt = new TipoTramiteCargaMasiva();
				tt.setId(rs.getInt("ID_TIPO_TRAMITE"));
				tt.setTramite(rs.getString("DESCRIPCION"));
				lista.add(tt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return lista;
	}
	public Integer getIdAcreedorFromGarantia(Integer idGarantia) throws InfrastructureException, CargaMasivaException {
		Integer idAcreedor = null;
		String sql = "select ID_TRAMITE, ID_ACREEDOR  from ( "+
						"select id_tramite, id_acreedor "+	
						" from V_TRAMITES_TERMINADOS where id_garantia=? "+
						"order by 1 desc) where rownum = 1";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idGarantia);
			rs = ps.executeQuery();
			if (rs.next()) {
				idAcreedor=new Integer(rs.getInt("ID_ACREEDOR"));
			}else{
				throw new CargaMasivaException(14);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException(e);
		} finally {
			bd.close(connection, rs, ps);
		}
		return idAcreedor;
	}
	
	public String getNombreUsuario(Integer idUsuario) {
		String nombre = "";
		String aPaterno = "";
		String aMaterno = "";
		String nombreCompleto = "";
		String sql = "SELECT NOMBRE_PERSONA,AP_PATERNO,AP_MATERNO FROM RUG_PERSONAS_FISICAS WHERE ID_PERSONA = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1,idUsuario);
			rs = ps.executeQuery();
			if(rs.next()){
				nombre = rs.getString("NOMBRE_PERSONA");
				aPaterno = rs.getString("AP_PATERNO");
				aMaterno = rs.getString("AP_MATERNO");
				nombreCompleto = nombre+" "+aPaterno+" "+aMaterno;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return nombreCompleto;
	}
	
	public UsuarioTO getUsuarioTO(Integer idUsuario) {
		String nombre = "";
		String aPaterno = "";
		String aMaterno = "";
		String nombreCompleto = "";
		UsuarioTO usuarioTO = new UsuarioTO();
		PersonaTO personaTO = new PersonaTO();
		//String sql = "SELECT NOMBRE_PERSONA,AP_PATERNO,AP_MATERNO FROM RUG_PERSONAS_FISICAS WHERE ID_PERSONA = ?";
		
		String sql = "SELECT pf.nombre_persona,pf.ap_paterno,pf.ap_materno,p.cve_usuario " + 
					 "FROM rug_personas_fisicas PF,rug_secu_usuarios P " +
					 "WHERE pf.id_persona = ? AND " +
					 "pf.id_persona = p.id_persona";
		
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1,idUsuario);
			rs = ps.executeQuery();
			if(rs.next()){
				nombre = rs.getString("NOMBRE_PERSONA");
				aPaterno = rs.getString("AP_PATERNO");
				aMaterno = rs.getString("AP_MATERNO");
				nombreCompleto = nombre+" "+aPaterno+" "+aMaterno;
				personaTO.setCorreoElectronico(rs.getString("cve_usuario"));
				personaTO.setIdPersona(idUsuario);
				
				usuarioTO.setNombre(nombreCompleto);
				usuarioTO.setPersona(personaTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return usuarioTO;
	}
	
	public String getNombreArchivo(Integer idArchivo) {
		String nombreArchivo = "";		
		String sql = "SELECT NOMBRE_ARCHIVO FROM RUG_ARCHIVO WHERE ID_ARCHIVO = ?";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1,idArchivo);
			rs = ps.executeQuery();
			if(rs.next()){
				nombreArchivo = rs.getString("NOMBRE_ARCHIVO");				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return nombreArchivo;
	}
	
	public List<Integer> getIdTramitesMasivos(Integer idFirmaMasivaTemporal) throws NoDataFoundException {
		List<Integer> tramites = new ArrayList<Integer>();
		String sql = "SELECT ID_TRAMITE_TEMP FROM RUG_FIRMA_MASIVA WHERE ID_FIRMA_MASIVA = ?";
		
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1,idFirmaMasivaTemporal);
			rs = ps.executeQuery();
			while (rs.next()) {				
				tramites.add(rs.getInt("ID_TRAMITE_TEMP"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return tramites;
	}
	
	public Integer getIdTipoTramiteMasiva(Integer idFirmaMasivaTemporal) throws NoDataFoundException {
		Integer idTipoTramite=0;
		String sql = "SELECT B.ID_TIPO_TRAMITE FROM RUG_FIRMA_MASIVA A, TRAMITES_RUG_INCOMP B WHERE A.ID_FIRMA_MASIVA = ? AND A.STATUS_REG = 'AC' AND A.ID_TRAMITE_TEMP = B.ID_TRAMITE_TEMP AND ROWNUM < 2  ";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1,idFirmaMasivaTemporal);
			rs = ps.executeQuery();
			if (rs.next()) {
				idTipoTramite=rs.getInt("ID_TIPO_TRAMITE");
			}else{
				throw new NoDataFoundException("No se encontro el idTipoTramite de la firma masiva idFirmaMasiva::"+idFirmaMasivaTemporal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return idTipoTramite;
	}
	
	public CargaMasivaProcess getCargaMasiva() throws NoDataFoundException, InfrastructureException {
		CargaMasivaProcess  process= new CargaMasivaProcess();
		String sql = "SELECT ID_ARCHIVO, ID_USUARIO, ID_TIPO_TRAMITE, ID_ACREEDOR,B_TIPO_PROCESO FROM  RUG_CARGA_POOL where ID_STATUS=8 and B_TIPO_PROCESO='D'";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				process.setIdArchivo(rs.getInt("ID_ARCHIVO"));
				process.setIdUsuario(rs.getInt("ID_USUARIO"));
				process.setIdTipoTramite(rs.getInt("ID_TIPO_TRAMITE"));
				process.setIdAcreedor(rs.getInt("ID_ACREEDOR"));
				process.setbTipoProceso(rs.getString("B_TIPO_PROCESO"));
			}else{
				throw new NoDataFoundException("No se encontro Carga Masiva para Procesar");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException(e);
		} finally {
			bd.close(connection, rs, ps);
		}
		return process;
	}
	
	public void insertProcessCarga(CargaMasivaProcess masivaProcess ) throws CargaMasivaException, InfrastructureException {
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
	
                    String sql = "INSERT INTO RUG_CARGA_POOL (ID_ARCHIVO,ID_USUARIO,ID_ARCHIVO_FIRMA,ID_ARCHIVO_RESUMEN,ID_STATUS,ID_TIPO_TRAMITE,B_TIPO_PROCESO,STATUS_REG,FECHA_REG,ID_ACREEDOR) "+
					"VALUES (?,?,null,null,8,?,?,'AC',sysdate,?)";
		
		try {
			ps = connection.prepareStatement(sql);
			
			resetIndexData();
                        System.out.println("masivaProcess.getIdArchivo(): "+masivaProcess.getIdArchivo());
			//SET
			setDataInPreparedStatemet(masivaProcess.getIdArchivo(),ps);
                        System.out.println("masivaProcess.getIdUsuario(): "+masivaProcess.getIdUsuario());
			setDataInPreparedStatemet(masivaProcess.getIdUsuario(),ps);
                        
                        System.out.println("masivaProcess.getIdTipoTramite(): "+masivaProcess.getIdTipoTramite());
			setDataInPreparedStatemet(masivaProcess.getIdTipoTramite(),ps);
                        
                        System.out.println("masivaProcess.getbTipoProceso(): "+masivaProcess.getbTipoProceso());
			setDataInPreparedStatemet(masivaProcess.getbTipoProceso(),ps);
                        
                        System.out.println("masivaProcess.getIdAcreedor(): "+masivaProcess.getIdAcreedor());
			setDataInPreparedStatemet(masivaProcess.getIdAcreedor(),ps);
			
			//Ejecutamos 
			int nInsert=ps.executeUpdate();
			
			if(nInsert<1){//NO Correcto
				throw new InfrastructureException("No se pudo insertar el proceso de carga masiva");
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new InfrastructureException("No se pudo insertar el proceso de carga masiva");
		}  finally {
			bd.close(connection, null, ps);
		}
	}

	public void actualizaProcessCargaIdFirma(CargaMasivaProcess cargaMasivaProcess) throws CargaMasivaException {
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		
		String sql = "Update RUG_CARGA_POOL set ID_ARCHIVO_FIRMA=? where ID_ARCHIVO=?";

		try {
			ps = connection.prepareStatement(sql);
			
			resetIndexData();
			//SET
			setDataInPreparedStatemet(cargaMasivaProcess.getIdArchivoFirma(),ps);
			//WHERE
			setDataInPreparedStatemet(cargaMasivaProcess.getIdArchivo(),ps);
			//Ejecutamos 
			int nUpdate=ps.executeUpdate();
			
			if(nUpdate!=1){//Correcto
			   throw new CargaMasivaException(112);
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(112);
		} catch (InfrastructureException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(112);
		} finally {
			bd.close(connection, null, ps);
		}
	}
	
	public void actualizaProcessCargaIdStatus(CargaMasivaProcess cargaMasivaProcess) throws CargaMasivaException {
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		
		String sql = "Update RUG_CARGA_POOL set ID_STATUS=? where ID_ARCHIVO=?";

		try {
			ps = connection.prepareStatement(sql);
			
			resetIndexData();
			//SET
			setDataInPreparedStatemet(cargaMasivaProcess.getIdStatus(),ps);
			//WHERE
			setDataInPreparedStatemet(cargaMasivaProcess.getIdArchivo(),ps);
			//Ejecutamos 
			int nUpdate=ps.executeUpdate();
			
			if(nUpdate!=1){//Correcto
			   throw new CargaMasivaException(112);
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(112);
		} catch (InfrastructureException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(112);
		} finally {
			bd.close(connection, null, ps);
		}
	}
	public void actualizaProcessCargaIdResumen(CargaMasivaProcess cargaMasivaProcess) throws CargaMasivaException {
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		
		String sql = "Update RUG_CARGA_POOL set ID_ARCHIVO_RESUMEN=? where ID_ARCHIVO=?";
		
		try {
			ps = connection.prepareStatement(sql);
			
			resetIndexData();
			//SET
			setDataInPreparedStatemet(cargaMasivaProcess.getIdArchivoResumen(),ps);
			//WHERE
			setDataInPreparedStatemet(cargaMasivaProcess.getIdArchivo(),ps);			
			//Ejecutamos 
			int nUpdate=ps.executeUpdate();
			
			if(nUpdate!=1){//Correcto
				throw new CargaMasivaException(113);
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(113);
		} catch (InfrastructureException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(113);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			bd.close(connection, null, ps);
		}
	}
	
	public Integer getIdArchivoResDb(Integer idArchivo) throws NoDataFoundException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		int idArchivoDb = 0;
		
		String sql = "SELECT ID_ARCHIVO_RESUMEN FROM RUG_CARGA_POOL WHERE ID_ARCHIVO = ?";
				
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1,idArchivo);
			rs = ps.executeQuery();
			if(rs.next()){
				idArchivoDb = rs.getInt("ID_ARCHIVO_RESUMEN");				
			}else{
				throw new NoDataFoundException("No se encontro el idArchivo "+idArchivo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return idArchivoDb;
	}
	
	public Integer getIdTramiteFirmaDb(Integer idArchivo) throws NoDataFoundException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		int idTramiteFirma = 0;
		
		String sql = "SELECT DISTINCT B.ID_FIRMA_MASIVA, B.ID_ARCHIVO FROM RUG_CARGA_POOL A, RUG_FIRMA_MASIVA B " +
					 "WHERE B.ID_ARCHIVO IN (A.ID_ARCHIVO, A.ID_ARCHIVO_FIRMA) AND " +
					 "A.ID_ARCHIVO =  ? " +
					 "Order by 1 DESC";
				
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1,idArchivo);
			rs = ps.executeQuery();
			if(rs.next()){
				idTramiteFirma = rs.getInt("ID_FIRMA_MASIVA");				
			}else{
				throw new NoDataFoundException("No se encontro el idArchivo "+idArchivo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}
		return idTramiteFirma;
	}

	/**
	 * 
	 * @param tramite
	 * @return
	 */
	public PlSql executeAltaBitacoraTramite(Tramite tramite) {
		PlSql regresa = null;
                System.out.println("executeAltaBitacoraTramite: tramite"+tramite);
		String sql = "{ call RUG.SP_Alta_Bitacora_Tramite2 ( " + " ?, ?, ?,"
				+ " ?, ?, ?, " + " ?, ?" + " ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, tramite.getIdTramite());
			cs.setInt(2, tramite.getIdEstatus());
			cs.setInt(3, 0);
			cs.setString(4, null);
			cs.setString(5, "V");
			cs.setString(6, "");
			cs.registerOutParameter(7, Types.INTEGER);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.execute();
			regresa = new PlSql();
			regresa.setIntPl(cs.getInt(7));
			regresa.setStrPl(cs.getString(8));
		} catch (SQLException e) {
			regresa = new PlSql();
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de actualizar la bitacora :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "executeAltaBitacoraTramite ID- ---"
				+ regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"executeAltaBitacoraTramite Mensaje- ---" + regresa.getStrPl());
		return regresa;

	}

	/**
	 * 
	 * @param idAcreedor
	 * @param idUsuario
	 * @return
	 */
	public HashMap<String, String> getTiposTramite(Integer idAcreedor,
			Integer idUsuario) {
            System.out.println("getTiposTramite: ");
            System.out.println("idAcreedor: "+idAcreedor);
            System.out.println("idUsuario: "+idUsuario);
		HashMap<String, String> mapTipoTramite = new HashMap<String, String>();
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT DESC_PRIVILEGIO, ID_PRIVILEGIO FROM V_USUARIO_ACREEDOR_GRUPOS WHERE ID_ACREEDOR =? AND ID_SUB_USUARIO = ? AND ID_RECURSO = 7 ";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idAcreedor);
			ps.setInt(2, idUsuario);
			rs = ps.executeQuery();
			while (rs.next()) {
				mapTipoTramite.put(rs.getString("ID_PRIVILEGIO"),
						rs.getString("DESC_PRIVILEGIO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			bd.close(connection, rs, ps);
		}

		return mapTipoTramite;
	}
	
	public PlSql bajaParteLogicaMasiva (Integer peIdPersona, Integer peIdParte, Integer peIdTramiteTemp){
		PlSql regresa = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_BAJA_PARTE_LOGICA_MASIVA ( ?,?,?,?,?) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			MyLogger.Logger.log(Level.INFO, "2,"+ peIdPersona);			
			MyLogger.Logger.log(Level.INFO, "3,"+ peIdParte);
			MyLogger.Logger.log(Level.INFO, "1,"+ peIdTramiteTemp);
			    
			cs.setInt(1, peIdTramiteTemp);
			cs.setInt(2, peIdPersona);			
			cs.setInt(3, peIdParte);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);	
			
			 cs.executeQuery();
			 regresa =new PlSql();
			 regresa.setIntPl(cs.getInt(4));
			 regresa.setStrPl(cs.getString(5));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
		
	}	
	public void executeBajaParteLogicaMasiva (Integer peIdPersona, Integer peIdParte, Integer peIdTramiteTemp) throws CargaMasivaException, InfrastructureException{
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call RUG.SP_BAJA_PARTE_LOGICA_MASIVA ( ?,?,?,?,?) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			MyLogger.Logger.log(Level.INFO, "2,"+ peIdPersona);			
			MyLogger.Logger.log(Level.INFO, "3,"+ peIdParte);
			MyLogger.Logger.log(Level.INFO, "1,"+ peIdTramiteTemp);
			
			cs.setInt(1, peIdTramiteTemp);
			cs.setInt(2, peIdPersona);			
			cs.setInt(3, peIdParte);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);	
			
			cs.executeQuery();
			
			if(cs.getInt(4)!=0){
				throw new CargaMasivaException(cs.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException("Error al ejecutar SP_BAJA_PARTE_LOGICA_MASIVA",e);
		}finally{
			bd.close(connection,null,cs);
		}
		
	}	
	
	public void executeBajaClaveRastreo (Integer idAcreedor,Integer idArchivoInicio, String claveRastreo) throws CargaMasivaException, InfrastructureException{
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call SP_BAJA_CLAVE_RASTREO ( ?,?,?,?,?) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			
			cs.setInt(1, idAcreedor);
			cs.setString(2, claveRastreo);	
			if(idArchivoInicio!=null){
			cs.setInt(3, idArchivoInicio);
			}else{
				cs.setNull(3, Types.NULL);
				
			}
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);	
			
			cs.executeQuery();
			
			if(cs.getInt(4)!=0&&cs.getInt(4)!=85){
				//throw new CargaMasivaException(cs.getInt(4));
				MyLogger.Logger.log(Level.INFO, "Error al dar de Baja Clave Rastreo"+cs.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		throw new InfrastructureException("Error al ejecutar SP_BAJA_CLAVE_RASTREO",e);
		}finally{
			bd.close(connection,null,cs);
		}
		
	}	
	
	public void executeBajaClaveRastreoAutoridad (Integer idTramiteTemp, String claveRastreo) throws CargaMasivaException, InfrastructureException{
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "{call SP_BAJA_CLAVE_RASTREO_AUT ( ?,?,?,?) } ";
		CallableStatement cs =null;
		try {
			cs = connection.prepareCall(sql);
			
			cs.setInt(1, idTramiteTemp);
			cs.setString(2, claveRastreo);			
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);	
			
			cs.executeQuery();
			
			if(cs.getInt(3)!=0){
				throw new CargaMasivaException(cs.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InfrastructureException("Error al ejecutar SP_BAJA_CLAVE_RASTREO_AUT",e);
		}finally{
			bd.close(connection,null,cs);
		}
		
	}	
	
	public PlSql actualizaDatosCarga(Integer exitosos,Integer noExitosos,Integer idArchivo) {
		PlSql regresa = new PlSql();
		String sql = "UPDATE RUG_ARCHIVO SET TOTAL_EXITO = ? , TOTAL_NO_EXITO = ? " +
				" WHERE ID_ARCHIVO = ?";
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, exitosos);
			ps.setInt(2, noExitosos);
			ps.setInt(3, idArchivo);
			ps.executeUpdate();
			regresa.setIntPl(0);
			regresa.setStrPl("se actualizo correctamente el tramite");
			regresa.setResIntPl(0);
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl("Error al dar de alta un tramite de carga masiva :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, ps);
		}
		MyLogger.Logger.log(Level.INFO,
				"actualizaMeses ID----" + regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"actualizaMeses Mensaje----" + regresa.getStrPl());
		return regresa;
	}
	
	

	/**
	 * 
	 * @param idTramiteTemporal
	 * @return
	 */
	public boolean isCargaMasiva(Integer idTramiteTemporal) {
		boolean regresa = false;
		ConexionBD bd = new ConexionBD();
		String sql = "SELECT ID_TIPO_TRAMITE  FROM TRAMITES_RUG_INCOMP WHERE ID_TRAMITE_TEMP = ? AND ID_TIPO_TRAMITE = 18";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idTramiteTemporal);
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
	//Modificacion de la garantia
	public PlSql actualizaGarantia(Tramite tramite, Integer meses) {
		PlSql regresa = new PlSql();
		String sql = "UPDATE RUG_GARANTIAS_PENDIENTES SET VIGENCIA = ? WHERE ID_ULTIMO_TRAMITE = ?";
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		try {
			ps = connection.prepareStatement(sql);
			ps.setInt(1, meses);
			ps.setInt(2, tramite.getIdTramite());
			ps.executeUpdate();
			regresa.setIntPl(0);
			regresa.setStrPl("se actualizo correctamente el tramite");
			regresa.setResIntPl(0);
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl(
					"Error al dar de alta un tramite de prorroga o reduccion de vigencia :"
					+ e.getMessage());
			regresa.setResIntPl(0);
			e.printStackTrace();
		} finally {
			bd.close(connection, null, ps);
		}
		MyLogger.Logger.log(Level.INFO,
				"actualizaMeses ID----" + regresa.getIntPl());
		MyLogger.Logger.log(Level.INFO,
				"actualizaMeses Mensaje----" + regresa.getStrPl());
		return regresa;
	}
	//Modificacion Ractificacion Transmision
	public void actualizaDatosGantiaCarga(Integer IdGarantia,String instrumentoPublico, String descGarantia, String terminosCondiciones,
			String infoRegistro, Boolean NoBienesOtorgados, Boolean esPrioritaria, Boolean enOtroRegistro, Boolean isDatosModificables) throws CargaMasivaException {
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		
		String sql = "UPDATE RUG_GARANTIAS_PENDIENTES SET " +
				"INSTRUMENTO_PUBLICO     = NVL(? ,INSTRUMENTO_PUBLICO), "+
				"DESC_GARANTIA     		 = NVL(? ,DESC_GARANTIA), "+
				"OTROS_TERMINOS_GARANTIA = NVL(? ,OTROS_TERMINOS_GARANTIA), "+
				"TXT_REGISTROS 			 = NVL(? ,TXT_REGISTROS), "+
				"NO_GARANTIA_PREVIA_OT   = NVL(? ,NO_GARANTIA_PREVIA_OT), "+
				"ES_PRIORITARIA          = NVL(? ,ES_PRIORITARIA), "+
				"OTROS_REGISTROS         = NVL(? ,OTROS_REGISTROS), "+
				"CAMBIOS_BIENES_MONTO    = NVL(? ,CAMBIOS_BIENES_MONTO) "+
					"WHERE ID_GARANTIA_PEND = ? ";


		try {
			ps = connection.prepareStatement(sql);
			
			resetIndexData();
			//SET
			setDataInPreparedStatemet(instrumentoPublico,ps);
			setDataInPreparedStatemet(descGarantia,ps);
			setDataInPreparedStatemet(terminosCondiciones,ps);
			setDataInPreparedStatemet(infoRegistro,ps);
			setDataInPreparedStatemet(transformBoolean(NoBienesOtorgados),ps);
			setDataInPreparedStatemet(transformBoolean(esPrioritaria),ps);
			setDataInPreparedStatemet(transformBoolean(enOtroRegistro),ps);
			setDataInPreparedStatemet(transformBoolean(isDatosModificables),ps);						
			
			//WHERE
			setDataInPreparedStatemet(IdGarantia,ps);
			
			//Ejecutamos 
			int nUpdate=ps.executeUpdate();
			
			if(nUpdate!=1){//Correcto
			   throw new CargaMasivaException(93);
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(93);
		} catch (InfrastructureException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(93);
		} finally {
			bd.close(connection, null, ps);
		}
	}
	
	private String transformBoolean(Boolean pValor) {
		if(pValor==null) {
			return null;
		} else if(pValor) {
			return "V";
		} else {
			return "F";
		}
	}
	
	public void verificaIdAcreedor(Integer IdUsusario,Integer IdAcreedor) throws CargaMasivaException {
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "SELECT COUNT(*) existe FROM V_USUARIO_ACREEDOR_REP WHERE USUARIO_LOGIN = ? AND ID_PERSONA = ? ";
		try {
			ps = connection.prepareStatement(sql);
			
			resetIndexData();
			//SET
			setDataInPreparedStatemet(IdUsusario,ps);
			setDataInPreparedStatemet(IdAcreedor,ps);
			//Ejecutamos 
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()){//Correcto
				if(rs.getInt("existe")!=1){
					//No existe idAcreedor
					throw new CargaMasivaException(94);
				}
			}else{
				//No existe idAcreedor
				throw new CargaMasivaException(94);
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(999);
		} catch (InfrastructureException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(999);
		} finally {
			bd.close(connection, null, ps);
		}
	}
	
	public void actualizaAcreedor(Integer idAcreedorViejo,Integer idAcreedorNuevo,Integer idTramiteTemp) throws CargaMasivaException {
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		
		String sql = "UPDATE RUG_REL_TRAM_INC_PARTES  SET " +
				"ID_PERSONA = ? " +
				"WHERE ID_PERSONA = ?  AND ID_PARTE = 4  AND ID_TRAMITE_TEMP = ?";


		try {
			ps = connection.prepareStatement(sql);
			
			resetIndexData();
			//SET
			setDataInPreparedStatemet(idAcreedorNuevo,ps);
			//WHERE
			setDataInPreparedStatemet(idAcreedorViejo,ps);
			setDataInPreparedStatemet(idTramiteTemp,ps);
			
			//Ejecutamos 
			int nUpdate=ps.executeUpdate();
			
			if(nUpdate!=1){//NO Correcto
			   throw new CargaMasivaException(95);
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(95);
		} catch (InfrastructureException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(95);
		} finally {
			bd.close(connection, null, ps);
		}
	}
	public void actualizaContrato(Date fechaInicio,Date fechaFin,String otrosTerminosContrato,String tipoContrato,Integer idGarantiaPendiente) throws CargaMasivaException, InfrastructureException {
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		
		String sql = "UPDATE RUG_CONTRATO   SET " +
				"FECHA_INICIO = NVL(?,FECHA_INICIO), " +
				"FECHA_FIN = NVL(?,FECHA_FIN), " +
				"OTROS_TERMINOS_CONTRATO = NVL(?,OTROS_TERMINOS_CONTRATO), " +
				"OBSERVACIONES = NVL(?,OBSERVACIONES) " +
				
				"WHERE ID_GARANTIA_PEND = ? and CLASIF_CONTRATO='OB'";
		
		try {
			ps = connection.prepareStatement(sql);
			
			resetIndexData();
			//SET
			setDataInPreparedStatemet(fechaInicio,ps);
			setDataInPreparedStatemet(fechaFin,ps);
			setDataInPreparedStatemet(otrosTerminosContrato,ps);
			setDataInPreparedStatemet(tipoContrato,ps);
			//WHERE
			setDataInPreparedStatemet(idGarantiaPendiente,ps);
			
			//Ejecutamos 
			int nUpdate=ps.executeUpdate();
			
			if(nUpdate!=1){//NO Correcto
				throw new CargaMasivaException(95);
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(95);
		}  finally {
			bd.close(connection, null, ps);
		}
	}
	public void insertaContratoOConvenio(Date fechaInicio,Date fechaFin,String otrosTerminosContrato,String tipoContrato,Integer idGarantiaPendiente,Integer idTramiteTemporal,UsuarioTO usuario) throws CargaMasivaException, InfrastructureException {
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
	
		String sql =     "INSERT INTO RUG.RUG_CONTRATO (" +
				"ID_CONTRATO, ID_GARANTIA_PEND, CONTRATO_NUM,FECHA_INICIO," +
				"FECHA_FIN, OTROS_TERMINOS_CONTRATO, MONTO_LIMITE," +
				"OBSERVACIONES,TIPO_CONTRATO,ID_TRAMITE_TEMP," +
				"FECHA_REG,STATUS_REG,ID_USUARIO,CLASIF_CONTRATO) " +
				"VALUES (SEQ_CONTRATO.NEXTVAL, ?, NULL, ?, ?, ?, NULL, NULL, ?, ?, SYSDATE, 'AC', ?, 'FU')";
		
		try {
			ps = connection.prepareStatement(sql);
			
			resetIndexData();
			//SET
			setDataInPreparedStatemet(idGarantiaPendiente,ps);

			setDataInPreparedStatemet(fechaInicio,ps);
			setDataInPreparedStatemet(fechaFin,ps);
			setDataInPreparedStatemet(otrosTerminosContrato,ps);
			
			setDataInPreparedStatemet(tipoContrato,ps);
			setDataInPreparedStatemet(idTramiteTemporal,ps);
			
			setDataInPreparedStatemet(usuario.getPersona().getIdPersona(),ps);
			
			
			
			//Ejecutamos 
			int nUpdate=ps.executeUpdate();
			
			if(nUpdate!=1){//NO Correcto
				throw new CargaMasivaException(95);
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new CargaMasivaException(95);
		}  finally {
			bd.close(connection, null, ps);
		}
	}

	
	public void validaCatalogo(Integer idCatalogo, Integer idValidar) throws InfrastructureException, CargaMasivaException {
		/**
		 * 5 RUG_CAT_MONEDAS
 6 RUG_CAT_NACIONALIDADES
10 RUG_CAT_TIPO_BIEN
		 * SP_VALIDA_CATALOGO(
                                                    peIdCatalogo       IN    NUMBER,
                                                    peIdValor          IN    NUMBER,
                                                    psResult          OUT    NUMBER,   
                                                    psTxResult        OUT    VARCHAR2
                                              )
		 * */
		
		String sql = "{ call SP_VALIDA_CATALOGO( ?,?,?,? ) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = bd.getConnection();
			cs = connection.prepareCall(sql);
			cs.setInt(1, idCatalogo);
			cs.setInt(2, idValidar);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			
			if(cs.getInt(3)!=0){
				throw new CargaMasivaException(cs.getInt(3));
			}
			
			
		} catch (SQLException e) {
			throw new InfrastructureException("Error al eejcutar SP_VALIDA_CATALOGO",e);
		} finally {
			bd.close(connection, null, cs);
		}

	}

	/**
	
	SELECT SEQ_BIENES.NEXTVAL
	  FROM DUAL;

	INSERT INTO RUG_REL_GAR_TIPO_BIEN
	VALUES(ID_GARANTIA_PEND, ID_TIPO_BIEN, VAR);
	 * @throws InfrastructureException 
	*/
	
	public Integer getNextRelTipoBien() throws CargaMasivaException, InfrastructureException {
		PreparedStatement ps = null;
		Integer idRelTipoBien= null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		String sql = "SELECT SEQ_BIENES.NEXTVAL idRelTipoBien FROM DUAL";
		try {
			ps = connection.prepareStatement(sql);
			
			//Ejecutamos 
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()){//Correcto
				if(rs.getInt("idRelTipoBien")>0){
					idRelTipoBien=rs.getInt("idRelTipoBien");
				}else{
					throw new InfrastructureException("No se pudo eobtener valor de la secuencia");
				}
			}else{
				throw new InfrastructureException("No se pudo eobtener valor de la secuencia");
			}
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new InfrastructureException("No se pudo eobtener valor de la secuencia",e);
		} finally {
			bd.close(connection, null, ps);
		}
		return idRelTipoBien;
	}
	
	
	public void insertaTipoBienMueble(Integer idGarantiaPendiente,Integer idTipoBien ,Integer idRelacionTipoBien) throws CargaMasivaException, InfrastructureException {
		PreparedStatement ps = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		
		String sql = "INSERT INTO RUG_REL_GAR_TIPO_BIEN (ID_GARANTIA_PEND, ID_TIPO_BIEN, RELACION_BIEN) VALUES(?,?,?)";
		
		try {
			ps = connection.prepareStatement(sql);
			
			resetIndexData();
			//SET
			setDataInPreparedStatemet(idGarantiaPendiente,ps);
			setDataInPreparedStatemet(idTipoBien,ps);
			setDataInPreparedStatemet(idRelacionTipoBien,ps);
			
			//Ejecutamos 
			ps.executeUpdate();
			
			
		} catch (SQLException e) {
			MyLogger.Logger.log(Level.SEVERE, "Error", e);
			throw new InfrastructureException("No se pudo Insertar RUG_REL_GAR_TIPO_BIEN ");
		}  finally {
			bd.close(connection, null, ps);
		}
	}
	
}
