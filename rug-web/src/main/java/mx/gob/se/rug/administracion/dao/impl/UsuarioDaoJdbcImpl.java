/**
 * 
 */
package mx.gob.se.rug.administracion.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.sql.DataSource;

import mx.gob.economia.dgi.framework.dao.exception.DaoException;
import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.economia.dgi.framework.spring.jdbc.AbstractBaseStoredProcedure;
import mx.gob.se.rug.administracion.dao.UsuarioDao;
import mx.gob.se.rug.administracion.dto.RegistroUsuario;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.common.util.DaoUtil;
import mx.gob.se.rug.common.util.StringUtil;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.fwk.dao.spring.RugBaseJdbcDao;
import mx.gob.se.rug.util.MyLogger;
import oracle.jdbc.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

/**
 * @author Alfonso Esquivel
 * 
 */
public class UsuarioDaoJdbcImpl extends RugBaseJdbcDao implements
		UsuarioDao {

	private static final String SE = "SE";
	private static final String CIUDADANO_PORTAL = "RugPortal";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroUsuario> getPreguntas(String lenguaje) throws DaoException {
		logger.debug("-- getPreguntas --");
		System.out.println("*******************"+lenguaje);
		try{
			StringBuffer sqlQuery = new StringBuffer("");
			sqlQuery.append("SELECT TX_PREGUNTA as pregunta ");
			sqlQuery.append("FROM V_PREG_RECUPERA_PSW ");
			sqlQuery.append("WHERE CVE_IDIOMA = ?");
			
			Object[] params = new Object[] { StringUtil.toUpperCase(lenguaje) };
			
			logger.debug("sqlQuery: " + sqlQuery);
			logger.debug("lenguaje: " + lenguaje);
		
			return (List<RegistroUsuario>) getJdbcTemplate().query(
					sqlQuery.toString(), params, new RegistroUsuarioRowMapper());
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	public class RegistroUsuarioResultSetExtractor implements
			ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			RegistroUsuario registroUsuario = new RegistroUsuario();
			registroUsuario.setPregunta(resultSet.getString("pregunta"));
			return registroUsuario;
		}
	}

	public class RegistroUsuarioRowMapper implements RowMapper {
		@Override
		public Object mapRow(ResultSet resultSet, int line) throws SQLException {
			RegistroUsuarioResultSetExtractor extractor = new RegistroUsuarioResultSetExtractor();
			return extractor.extractData(resultSet);
		}
	}

	// Ejemplo de Stored Procedure, CREACION DE USUARIO PRINCIPAL
	@Override
	public Mensaje save(PersonaFisica personaFisica,
			RegistroUsuario registroUsuario) throws DaoException {
		MyLogger.Logger.log(Level.INFO, "-- save Dao --");
		MyLogger.Logger.log(Level.INFO, "personaFisica: " + personaFisica);
		MyLogger.Logger.log(Level.INFO, "registroUsuario: " + registroUsuario);
		try {
			// Creamos la instancia de la clase que lo ejecuta.
			ExecuteStoredProcedure executeStoredProcedure = new ExecuteStoredProcedure(
					getDataSource());
			// Invocamos el metodo execute.
			Map<String, Object> map = executeStoredProcedure.execute(
					personaFisica, registroUsuario);
			MyLogger.Logger.log(Level.INFO, "map dao: " + map);
			// Usamos la clase DaoUtil para convertir el mapa en objeto Mensaje. 
			Mensaje mensaje = DaoUtil.getMensaje(map);
			MyLogger.Logger.log(Level.INFO, "mensaje DAO METODO PRICIPAL: " + mensaje);
			return mensaje;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JdbcDaoException(e);
		}
	}

	// Creamos la clase que compila el Stored Procedure
	// Debe extender de AbstractBaseStoredProcedure
	public class ExecuteStoredProcedure extends AbstractBaseStoredProcedure {
		
		// Creamos como constantes los nombres del stored procedure y de los parametros. 
		private static final String STORED_PROCEDURE = "SP_REGISTRO_USUARIO_RUG";
		
		private static final String TIPO_OPERACION = "peTipoOperacion";
		private static final String NOMBRE = "penombre";
		private static final String AP_PATERNO = "peApPaterno";
		private static final String AP_MATERNO = "peApMaterno";
		private static final String ID_PAIS = "peIdPais";
		private static final String SEXO = "peSexo";
		private static final String RFC = "peRFC";
		private static final String CVE_USUARIO = "peCveUsuario";
		private static final String CVE_USUARIO_PADRE = "peCveUsuarioPadre";
		private static final String CVE_PASSWORD = "pePassword";
		private static final String FRASE_CVE = "peFraseCve";
		private static final String RESP_FRASE_CVE = "peRespFraseCve";
		private static final String CVE_GPO_EMPRESA = "peCveGpoEmpresa";
		private static final String CVE_PERFIL = "peCvePerfil";
		private static final String CVE_APLICACION = "peCveAplicacion";
		private static final String ID_GRUPO = "peIdGrupo";
		
		//NUEVOS PARAMETROS
		private static final String ID_TRAM_TEMP = "peIdTram";
		private static final String FECHA_INICIO = "peFechaInico";
		private static final String FECHA_FIN = "peFechaFin";
		private static final String OTROS_TERM = "peOtrosTerm";
		private static final String TIPO_CONTRATO = "peTipoContrato";
		private static final String ID_USUARIO = "peIdUsuario";
		//private static final String NUM_SERIE= "P_NUM_SERIE";
		private static final String TOKEN = "P_TOKEN";
		private static final String TIPO_PERSONA = "P_TIPO_PERSONA";
		private static final String INSCRITO = "P_INSCRITO";
		private static final String DOCID = "P_DOCID";
		
		//NUEVOS PARAMETROS
		
		private static final String PERFIL_DEFAULT = "CIUDADANO";
		private static final String CIUDADANO_PORTAL = "RugPortal";
		private static final String IS_JUDICIAL = "peIsJudicial";
		

		// Creamos el contructor
		// Y declaramos los parametros de entrada y salida
		public ExecuteStoredProcedure(DataSource dataSource) {

			super(dataSource, STORED_PROCEDURE);
			
			declareParameter(new SqlParameter(TIPO_OPERACION, Types.VARCHAR));
			declareParameter(new SqlParameter(NOMBRE, Types.VARCHAR));
			declareParameter(new SqlParameter(AP_PATERNO, Types.VARCHAR));
			declareParameter(new SqlParameter(AP_MATERNO, Types.VARCHAR));
			declareParameter(new SqlParameter(ID_PAIS, Types.INTEGER));
			declareParameter(new SqlParameter(SEXO, Types.VARCHAR));
			declareParameter(new SqlParameter(RFC, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_USUARIO, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_USUARIO_PADRE, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_PASSWORD, Types.VARCHAR));
			declareParameter(new SqlParameter(FRASE_CVE, Types.VARCHAR));
			declareParameter(new SqlParameter(RESP_FRASE_CVE, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_GPO_EMPRESA, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_PERFIL, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_APLICACION, Types.VARCHAR));
			declareParameter(new SqlParameter(ID_GRUPO, Types.VARCHAR));
			
			//NUEVOS PARAMETROS
			declareParameter(new SqlParameter(ID_TRAM_TEMP, Types.VARCHAR));
			declareParameter(new SqlParameter(FECHA_INICIO, Types.VARCHAR));
			declareParameter(new SqlParameter(FECHA_FIN, Types.VARCHAR));
			declareParameter(new SqlParameter(OTROS_TERM, Types.VARCHAR));
			declareParameter(new SqlParameter(TIPO_CONTRATO, Types.VARCHAR));
			declareParameter(new SqlParameter(ID_USUARIO, Types.VARCHAR));
			//declareParameter(new SqlParameter(NUM_SERIE, Types.VARCHAR));
			declareParameter(new SqlParameter(IS_JUDICIAL, Types.VARCHAR));
			declareParameter(new SqlParameter(TOKEN, Types.VARCHAR));
			declareParameter(new SqlParameter(TIPO_PERSONA, Types.VARCHAR));
			declareParameter(new SqlParameter(INSCRITO, Types.VARCHAR));
			declareParameter(new SqlParameter(DOCID, Types.VARCHAR));			
			//NUEVOS PARAMETROS
			
			declareParameter(new SqlOutParameter(RESULT, Types.INTEGER));
			declareParameter(new SqlOutParameter(TX_RESULT, Types.VARCHAR));
			compile();
		}
		
		// Implementamos el metodo execute con los parametros necesarios.
		@SuppressWarnings("unchecked")
		public Map<String, Object> execute(PersonaFisica personaFisica,
				RegistroUsuario registroUsuario) {
			logger.debug("-- execute --");
			Map<String, Object> inputs = new HashMap<String, Object>();
			inputs.put(TIPO_OPERACION, registroUsuario.getTipoOperacion());
			inputs.put(NOMBRE, personaFisica.getNombre());
			inputs.put(AP_PATERNO, personaFisica.getApellidoPaterno());
			inputs.put(AP_MATERNO, personaFisica.getApellidoMaterno());
			inputs.put(ID_PAIS, 1);
			inputs.put(SEXO, null);
			inputs.put(RFC, personaFisica.getRfc());
			inputs.put(CVE_USUARIO, personaFisica.getDatosContacto().getEmailPersonal());
			inputs.put(CVE_USUARIO_PADRE, personaFisica.getClaveUsuarioPadre());
			inputs.put(CVE_PASSWORD, registroUsuario.getPassword());
			inputs.put(FRASE_CVE, registroUsuario.getPregunta());
			inputs.put(RESP_FRASE_CVE, registroUsuario.getRespuesta());
			//inputs.put(NUM_SERIE, personaFisica.getnSerieCert());
			/** parametro para saber si es organismo judicial **/
			inputs.put(IS_JUDICIAL, registroUsuario.getJudicial()?"1":"");
			inputs.put(TOKEN, personaFisica.getToken());
			inputs.put(TIPO_PERSONA, personaFisica.getTipoPersona());
			inputs.put(INSCRITO, personaFisica.getNacionalidadInscrito());
			inputs.put(DOCID, personaFisica.getDocumentoIdentificacion());
			
			inputs.put(CVE_GPO_EMPRESA, SE);
			MyLogger.Logger.log(Level.INFO, "IMPRIME  EL PERFIL DAOSITA: "+personaFisica.getPerfil());
			
			if(personaFisica.getPerfil()==null){
				MyLogger.Logger.log(Level.INFO, "perfil es null osea CIUDADANO");
				inputs.put(CVE_PERFIL, PERFIL_DEFAULT);
				inputs.put(ID_GRUPO, 2);
			}else{
				MyLogger.Logger.log(Level.INFO, "no es ciudadano es administrador u operativo");
				inputs.put(CVE_PERFIL, "ACREEDOR");
				if(!personaFisica.getPerfil().equals("ADMINISTRADOR")){
				
					MyLogger.Logger.log(Level.INFO, "--operativo--");
					inputs.put(ID_GRUPO, personaFisica.getGrupo().getId());
				}
				else{
					MyLogger.Logger.log(Level.INFO, "--administrador--");
					inputs.put(ID_GRUPO, 3);
				}
			}

			inputs.put(CVE_APLICACION, CIUDADANO_PORTAL);

			//NUEVOS PARAMETROS
			inputs.put(ID_TRAM_TEMP, registroUsuario.getIdTramiteNuevo());
			inputs.put(FECHA_INICIO, null);
			inputs.put(FECHA_FIN, null);
			inputs.put(OTROS_TERM, null);
			inputs.put(TIPO_CONTRATO, null);
			inputs.put(ID_USUARIO, null);
			//NUEVOS PARAMETROS
			
			
			MyLogger.Logger.log(Level.INFO, "imprime los parametros del metodo execute: " + inputs);
			return super.execute(inputs);
		}
	}
	
	@Override
	public Mensaje save(PersonaFisica personaFisica) throws DaoException {
		MyLogger.Logger.log(Level.INFO, "-- save Dao --");
		MyLogger.Logger.log(Level.INFO, "personaFisica: " + personaFisica);
		
		try {
			// Creamos la instancia de la clase que lo ejecuta.
			ExecuteActivaStoredProcedure executeActivaStoredProcedure = new ExecuteActivaStoredProcedure(
					getDataSource());
			// Invocamos el metodo execute.
			Map<String, Object> map = executeActivaStoredProcedure.execute(
					personaFisica);
			MyLogger.Logger.log(Level.INFO, "map: " + map);
			// Usamos la clase DaoUtil para convertir el mapa en objeto Mensaje. 
			Mensaje mensaje = DaoUtil.getMensaje(map);
			MyLogger.Logger.log(Level.INFO, "mensaje SOBRESCRITO DAO: " + mensaje);
			return mensaje;
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	// Creamos la clase que compila el Stored Procedure
	// Debe extender de AbstractBaseStoredProcedure
	public class ExecuteActivaStoredProcedure extends AbstractBaseStoredProcedure {
		
		// Creamos como constantes los nombres del stored procedure y de los parametros. 
		private static final String STORED_PROCEDURE = "SP_REGISTRO_USUARIO_RUG";
		
		private static final String TIPO_OPERACION = "peTipoOperacion";
		private static final String NOMBRE = "penombre";
		private static final String AP_PATERNO = "peApPaterno";
		private static final String AP_MATERNO = "peApMaterno";
		private static final String ID_PAIS = "peIdPais";
		private static final String SEXO = "peSexo";
		private static final String RFC = "peRFC";
		private static final String CVE_USUARIO = "peCveUsuario";
		private static final String CVE_USUARIO_PADRE = "peCveUsuarioPadre";
		private static final String CVE_PASSWORD = "pePassword";
		private static final String FRASE_CVE = "peFraseCve";
		private static final String RESP_FRASE_CVE = "peRespFraseCve";
		private static final String CVE_GPO_EMPRESA = "peCveGpoEmpresa";
		private static final String CVE_PERFIL = "peCvePerfil";
		private static final String CVE_APLICACION = "peCveAplicacion";
		private static final String ID_GRUPO = "peIdGrupo";
		
		//NUEVOS PARAMETROS
		private static final String ID_TRAM_TEMP = "peIdTram";
		private static final String FECHA_INICIO = "peFechaInico";
		private static final String FECHA_FIN = "peFechaFin";
		private static final String OTROS_TERM = "peOtrosTerm";
		private static final String TIPO_CONTRATO = "peTipoContrato";
		private static final String ID_USUARIO = "peIdUsuario";
		private static final String NUM_SERIE= "P_NUM_SERIE";
		private static final String TOKEN = "TOKEN";
		private static final String TIPO_PERSONA = "P_TIPO_PERSONA";
		private static final String INSCRITO = "P_INSCRITO";
		private static final String DOCID = "P_DOCID";
		//NUEVOS PARAMETROS
		
		private static final String PERFIL_DEFAULT = "Default";
		private static final String CIUDADANO_PORTAL = "RugPortal";
		

		// Creamos el contructor
		// Y declaramos los parametros de entrada y salida
		public ExecuteActivaStoredProcedure(DataSource dataSource) {

			super(dataSource, STORED_PROCEDURE);
			
			declareParameter(new SqlParameter(TIPO_OPERACION, Types.VARCHAR));
			declareParameter(new SqlParameter(NOMBRE, Types.VARCHAR));
			declareParameter(new SqlParameter(AP_PATERNO, Types.VARCHAR));
			declareParameter(new SqlParameter(AP_MATERNO, Types.VARCHAR));
			declareParameter(new SqlParameter(ID_PAIS, Types.INTEGER));
			declareParameter(new SqlParameter(SEXO, Types.VARCHAR));
			declareParameter(new SqlParameter(RFC, Types.VARCHAR));		
			declareParameter(new SqlParameter(CVE_USUARIO, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_USUARIO_PADRE, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_PASSWORD, Types.VARCHAR));
			declareParameter(new SqlParameter(FRASE_CVE, Types.VARCHAR));
			declareParameter(new SqlParameter(RESP_FRASE_CVE, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_GPO_EMPRESA, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_PERFIL, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_APLICACION, Types.VARCHAR));
			declareParameter(new SqlParameter(ID_GRUPO, Types.VARCHAR));
			
			//NUEVOS PARAMETROS
			declareParameter(new SqlParameter(ID_TRAM_TEMP, Types.VARCHAR));
			declareParameter(new SqlParameter(FECHA_INICIO, Types.VARCHAR));
			declareParameter(new SqlParameter(FECHA_FIN, Types.VARCHAR));
			declareParameter(new SqlParameter(OTROS_TERM, Types.VARCHAR));
			declareParameter(new SqlParameter(TIPO_CONTRATO, Types.VARCHAR));
			declareParameter(new SqlParameter(ID_USUARIO, Types.VARCHAR));
			declareParameter(new SqlParameter(NUM_SERIE, Types.VARCHAR));
			declareParameter(new SqlParameter(TOKEN, Types.VARCHAR));
			declareParameter(new SqlParameter(TIPO_PERSONA, Types.VARCHAR));
			declareParameter(new SqlParameter(INSCRITO, Types.VARCHAR));
			declareParameter(new SqlParameter(DOCID, Types.VARCHAR));
			//NUEVOS PARAMETROS
			
			declareParameter(new SqlOutParameter(RESULT, Types.INTEGER));
			declareParameter(new SqlOutParameter(TX_RESULT, Types.VARCHAR));
			compile();
		}
		
		// Implementamos el metodo execute con los parametros necesarios.
		@SuppressWarnings("unchecked")
		public Map<String, Object> execute(PersonaFisica personaFisica) {
			logger.debug("-- execute --");
			Map<String, Object> inputs = new HashMap<String, Object>();
			
			inputs.put(TIPO_OPERACION, "U");
			inputs.put(NOMBRE, personaFisica.getNombre());
			inputs.put(AP_PATERNO, personaFisica.getApellidoPaterno());
			inputs.put(AP_MATERNO, personaFisica.getApellidoMaterno());
			inputs.put(ID_PAIS, 1);
			inputs.put(SEXO, null);
			inputs.put(RFC, null);
			inputs.put(CVE_USUARIO, personaFisica.getDatosContacto().getEmailPersonal());
			inputs.put(CVE_USUARIO_PADRE, personaFisica.getClaveUsuarioPadre());
			inputs.put(NUM_SERIE, personaFisica.getnSerieCert());
			inputs.put(TOKEN, null);
			inputs.put(TIPO_PERSONA, null);
			inputs.put(INSCRITO, null);
			inputs.put(DOCID, null);
			
			inputs.put(CVE_PASSWORD, null);
			inputs.put(FRASE_CVE, null);
			inputs.put(RESP_FRASE_CVE, null);
			inputs.put(CVE_GPO_EMPRESA, SE);
			
			if(personaFisica.getPerfil()==null){
				inputs.put(CVE_PERFIL, PERFIL_DEFAULT);
			}else{
				inputs.put(CVE_PERFIL, personaFisica.getPerfil());
			}
			
			inputs.put(CVE_APLICACION, CIUDADANO_PORTAL);
			if(personaFisica.getGrupo()==null){
				MyLogger.Logger.log(Level.INFO, "grupo" +personaFisica.getGrupo());
				inputs.put(ID_GRUPO, 0);
			}else{
				MyLogger.Logger.log(Level.INFO, "grupo" +personaFisica.getGrupo());
				inputs.put(ID_GRUPO, personaFisica.getGrupo().getId());
			}
			//NUEVOS PARAMETROS
			inputs.put(ID_TRAM_TEMP, null);
			inputs.put(FECHA_INICIO, null);
			inputs.put(FECHA_FIN, null);
			inputs.put(OTROS_TERM, null);
			inputs.put(TIPO_CONTRATO, null);
			inputs.put(ID_USUARIO, null);
			//NUEVOS PARAMETROS
			
			MyLogger.Logger.log(Level.INFO, "imprime los parametros del metodo execute: " + inputs);
			return super.execute(inputs);
		}
	}

	@Override
	public Mensaje recover(PersonaFisica personaFisica, RegistroUsuario registroUsuario) throws DaoException {

		try {
			// Creamos la instancia de la clase que lo ejecuta.
			RecoverStoredProcedure recoverStoredProcedure = new RecoverStoredProcedure(getDataSource());
			// Invocamos el metodo execute.
			Map<String, Object> map = recoverStoredProcedure.execute(personaFisica, registroUsuario);
			// Usamos la clase DaoUtil para convertir el mapa en objeto Mensaje.
			Mensaje mensaje = DaoUtil.getMensaje(map);
			return mensaje;
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}
	
	public class RecoverStoredProcedure extends AbstractBaseStoredProcedure {

		private static final String STORED_PROCEDURE = "RUG.SP_RECUPERA_PASSWORD";
		private static final String USUARIO = "peCveUsuario";
		private static final String RESP_FRASE = "peRespPreg";             
		private static final String NEW_PASSWORD = "peNuevoPass";

		public RecoverStoredProcedure(DataSource dataSource) {
			super(dataSource, STORED_PROCEDURE);
			declareParameter(new SqlParameter(USUARIO, Types.VARCHAR));
			declareParameter(new SqlParameter(RESP_FRASE, Types.VARCHAR));
			declareParameter(new SqlParameter(NEW_PASSWORD, Types.VARCHAR));
			declareParameter(new SqlOutParameter(RESULT, Types.INTEGER));
			declareParameter(new SqlOutParameter(TX_RESULT, Types.VARCHAR));
			compile();
		}
		
		@SuppressWarnings("unchecked")
		public Map<String, Object> execute(PersonaFisica personaFisica, RegistroUsuario registroUsuario) {
			Map<String, Object> inputs = new HashMap<String, Object>();
			inputs.put(USUARIO, personaFisica.getDatosContacto().getEmailPersonal());
			inputs.put(RESP_FRASE, registroUsuario.getRespuesta());
			inputs.put(NEW_PASSWORD, registroUsuario.getPassword());
			return super.execute(inputs);
		}
	}
	

	//RECUPERA PREGUNTA 
	public RegistroUsuario getPregunta(PersonaFisica personaFisica) throws DaoException {
		
		StringBuffer query = new StringBuffer();
		query.append(" SELECT CVE_USUARIO as cveUsuario, PREG_RECUPERA_PSW as pregunta,   ");
		query.append(" SIT_USUARIO as situacion, ");
		query.append(" NOMBRE_PERSONA as nombre, ");
		query.append(" COALESCE(CVE_USUARIO_PADRE, 'P') AS tipo_cuenta");
		query.append(" FROM V_USUARIO_SESION_RUG ");
		query.append(" WHERE CVE_USUARIO = " + "'"+ personaFisica.getDatosContacto().getEmailPersonal() + "'");
		query.append(" AND SIT_USUARIO = " + "'AC'");

		try {
			return (RegistroUsuario) getJdbcTemplate().query(query.toString(),
					new PreguntaResultSetExtractor());
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	public class PreguntaResultSetExtractor implements ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			RegistroUsuario registroUsuario = new RegistroUsuario ();
			if (resultSet.next()) {
				registroUsuario.setPregunta(resultSet.getString("pregunta"));
				registroUsuario.setNombreAcreedor(resultSet.getString("nombre"));
				registroUsuario.setTipoCuenta(resultSet.getString("tipo_cuenta"));
			}
			return registroUsuario;
		}
	}

//	@Override
//	public Mensaje AltaTramite(PersonaFisica personaFisica)
//			throws UsuarioException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	
	//=========================================================================================================================================
	@Override
	public Mensaje AltaTramite(PersonaFisica personaFisica, int tipoTramite)
			throws DaoException {
		try {
			// Creamos la instancia de la clase que lo ejecuta.
			AltaTramiteStoredProcedure altaTramiteStoredProcedure = new AltaTramiteStoredProcedure(
					getDataSource());
			// Invocamos el metodo execute.
			Map<String, Object> map = altaTramiteStoredProcedure.execute(personaFisica,tipoTramite);
			
			logger.debug("map: " + map);
			// Usamos la clase DaoUtil para convertir el mapa en objeto Mensaje.
			Mensaje mensaje = new Mensaje();

			if (map != null && !map.isEmpty()) {
			
				mensaje.setId(map.get("peIdTramiteIncompleto").toString());
				mensaje.setRespuesta(Integer.parseInt(map.get("psResult")
						.toString()));
				mensaje.setMensaje(map.get("psTxResult").toString());
			}
			
			//TEMPORAL SIMULANDO RESPUESTA DEL PL
			//mensaje.setId("1001");
			//mensaje.setRespuesta(0);
			//mensaje.setMensaje("EL REGISTRO HA SIDO INSERTADO, JOJO");
			logger.debug("IMPRIME EL MENSAJE: "+ mensaje);
			
			//TEMPORAL SIMULANDO RESPUESTA DEL PL
			return mensaje;
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}
	
	public class AltaTramiteStoredProcedure extends
	AbstractBaseStoredProcedure {

		// Creamos como constantes los nombres del stored procedure y de los
		// parametros.
		private static final String STORED_PROCEDURE = "RUG.SP_ALTA_TRAMITE_INCOMPLETO";
		private static final String ID_PERSONA = "peIdPersona";
		private static final String ID_TIPO_TRAMITE = "peIdTipoTramite";
		private static final String ID_TRAMITE_INCOMPLETO = "peIdTramiteIncompleto";
		private static final String RESULT = "psResult";
		private static final String TX_RESULT = "psTxResult";

		// Creamos el contructor
		// Y declaramos los parametros de entrada y salida
		public AltaTramiteStoredProcedure(DataSource dataSource) {
			super(dataSource, STORED_PROCEDURE);
			declareParameter(new SqlParameter(ID_PERSONA, Types.INTEGER));
			declareParameter(new SqlParameter(ID_TIPO_TRAMITE, Types.INTEGER));
			declareParameter(new SqlOutParameter(ID_TRAMITE_INCOMPLETO, Types.INTEGER));
			declareParameter(new SqlOutParameter(RESULT, Types.INTEGER));
			declareParameter(new SqlOutParameter(TX_RESULT, Types.VARCHAR));
		}

		// Implementamos el metodo execute con los parametros necesarios.

		@SuppressWarnings("unchecked")
		public Map<String, Object> execute(PersonaFisica personaFisica, int tipoTramite  ) {
			logger.debug("-- execute --");
			Map<String, Object> inputs = new HashMap<String, Object>();
			inputs.put(ID_PERSONA, personaFisica.getIdPersona());
			inputs.put(ID_TIPO_TRAMITE, 14);
			logger.debug("PARAMETROS: "+inputs);
			return super.execute(inputs);
		}
}
	//============================================================================================================================

	@Override
	public PersonaFisica getConsultaRfc(PersonaFisica personaFisica)
			throws DaoException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT  RFC as rfc   ");
		query.append(" FROM V_USUARIO_SESION_RUG ");
		query.append(" WHERE RFC = " + "'"+ personaFisica.getRfc() + "'");

		try {
			return (PersonaFisica) getJdbcTemplate().query(query.toString(),
					new ConsultaRfcResultSetExtractor());
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}
	
	@Override
	public PersonaFisica getConsultaDocId(PersonaFisica personaFisica)
			throws DaoException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT CURP_DOC as docid   ");
		query.append(" FROM V_USUARIO_SESION_RUG ");
		query.append(" WHERE CURP_DOC = " + "'"+ personaFisica.getDocumentoIdentificacion() + "'");

		try {
			return (PersonaFisica) getJdbcTemplate().query(query.toString(), new ConsultaDocIdResultSetExtractor());
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	public class ConsultaRfcResultSetExtractor implements ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			PersonaFisica personaFisica = new PersonaFisica ();
			if (resultSet.next()) {
				personaFisica.setRfc(resultSet.getString("rfc"));
				
			}
			return personaFisica;
		}
	}
	
	public class ConsultaDocIdResultSetExtractor implements ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			PersonaFisica personaFisica = new PersonaFisica ();
			if (resultSet.next()) {
				personaFisica.setDocumentoIdentificacion(resultSet.getString("docid"));				
			}
			return personaFisica;
		}
	}
	
	@Override
	public int altaExtranjero(PersonaFisica personaFisica, RegistroUsuario registroUsuario) {
		int regresa = -1;
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;		
		String sql ="{call RUG.SP_REGISTRO_USUARIO_RUG (?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,?,?,?,?," +
				"?,?,?,?,? ) }";		
		try{
			cs = connection.prepareCall(sql);
			cs.setString(1, registroUsuario.getTipoOperacion());
			cs.setString(2, personaFisica.getNombre());
			cs.setString(3, personaFisica.getApellidoPaterno());
			if (personaFisica.getApellidoMaterno()==null){
				cs.setString(4, "");
			}else{
				cs.setString(4, personaFisica.getApellidoMaterno());
			}
			cs.setInt(5, personaFisica.getIdPais());
			cs.setNull(6, OracleTypes.VARCHAR);
			if (personaFisica.getRfc()== null) {
				cs.setString(7, "");
			} else {
				cs.setString(7, personaFisica.getRfc());
			}			
			cs.setString(8, personaFisica.getDatosContacto().getEmailPersonal());
			cs.setString(9, personaFisica.getClaveUsuarioPadre());
			cs.setString(10, registroUsuario.getPassword());
			cs.setString(11, registroUsuario.getPregunta());
			cs.setString(12, registroUsuario.getRespuesta());
			cs.setString(13, SE);
			cs.setString(14, registroUsuario.getTipoUsuario());
			cs.setString(15, CIUDADANO_PORTAL);
			cs.setString(16, registroUsuario.getIdGrupo());
			cs.setNull(17, OracleTypes.NUMBER);			
			cs.setDate(18, null);
			cs.setDate(19, null);
			cs.setNull(20, OracleTypes.VARCHAR);
			cs.setInt(21, 0);
			cs.setInt(22, 0);
			cs.setString(23, personaFisica.getnSerieCert());
			cs.registerOutParameter(24, Types.INTEGER);
			cs.registerOutParameter(25, Types.VARCHAR);
			cs.execute();
			MyLogger.Logger.log(Level.INFO, "UsuarioDAO: Integer Result  = " + cs.getInt(24));
			MyLogger.Logger.log(Level.INFO, "UsuarioDAO: Varchar Result  = " + cs.getString(25));
		//	if (cs.getInt(23)==0){
		//		regresa = true;
		//	}
			regresa = cs.getInt(24);
		}catch(Exception e){
				e.printStackTrace();
		}finally{
			bd.close(connection,null,cs);
		}
		return regresa;
	}
	
}
