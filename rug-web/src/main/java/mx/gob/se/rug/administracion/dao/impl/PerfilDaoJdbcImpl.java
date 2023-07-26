package mx.gob.se.rug.administracion.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.sql.DataSource;

import mx.gob.economia.dgi.framework.dao.exception.DaoException;
import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.economia.dgi.framework.spring.jdbc.AbstractBaseStoredProcedure;
import mx.gob.se.rug.administracion.dao.PerfilDao;
import mx.gob.se.rug.administracion.dto.Perfil;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.common.util.DaoUtil;
import mx.gob.se.rug.dto.DatosContacto;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.fwk.dao.spring.RugBaseJdbcDao;
import mx.gob.se.rug.util.MyLogger;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

/**
 * @author Topiltzin Dominguez
 * 
 */
public class PerfilDaoJdbcImpl extends RugBaseJdbcDao implements PerfilDao {

	
	@Override
	public Mensaje update(Perfil perfil, String claveUsuario, PersonaFisica personaFisica) throws DaoException {
		MyLogger.Logger.log(Level.INFO, "-- update dao cambia password--");
		MyLogger.Logger.log(Level.INFO, "perfil: " + perfil);
		MyLogger.Logger.log(Level.INFO, "RFC: "+ personaFisica.getRfc());
		try {
			ExecuteStoredProcedure executeStoredProcedure = new ExecuteStoredProcedure(getDataSource());
			Map<String, Object> map = executeStoredProcedure.execute(perfil, claveUsuario, personaFisica);
			MyLogger.Logger.log(Level.INFO, "map: " + map);
			Mensaje mensaje = DaoUtil.getMensaje(map);
			MyLogger.Logger.log(Level.INFO, "mensaje: " + mensaje);
			return mensaje;
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	public class ExecuteStoredProcedure extends AbstractBaseStoredProcedure {

		private static final String STORED_PROCEDURE = "SP_CAMBIA_PASSWORD";
		private static final String NOMBRE = "peNombre";
		private static final String AP_PATERNO = "peApPaterno";	
		private static final String AP_MATERNO = "peApMaterno";
		private static final String CVE_USUARIO = "peCveUsuario";
		private static final String PASSWORD_ANTERIOR = "pePassAnterior";
		private static final String PASSWORD_NUEVO = "peNuevoPass";
		private static final String RFC = "peRFC";
		private static final String NUM_SERIE = "P_NUM_SERIE";

		public ExecuteStoredProcedure(DataSource dataSource) {
			super(dataSource, STORED_PROCEDURE);
			declareParameter(new SqlParameter(NOMBRE, Types.VARCHAR));
			declareParameter(new SqlParameter(AP_PATERNO, Types.VARCHAR));
			declareParameter(new SqlParameter(AP_MATERNO, Types.VARCHAR));
			declareParameter(new SqlParameter(CVE_USUARIO, Types.VARCHAR));
			declareParameter(new SqlParameter(PASSWORD_ANTERIOR, Types.VARCHAR));
			declareParameter(new SqlParameter(PASSWORD_NUEVO, Types.VARCHAR));
			declareParameter(new SqlParameter(RFC, Types.VARCHAR));
			declareParameter(new SqlParameter(NUM_SERIE, Types.VARCHAR));
			// parametros de salida
			declareParameter(new SqlOutParameter(RESULT, Types.INTEGER));
			declareParameter(new SqlOutParameter(TX_RESULT, Types.VARCHAR));
			compile();
		}

		@SuppressWarnings("unchecked")
		public Map<String, Object> execute(Perfil perfil, String claveUsuario, PersonaFisica  personaFisica) {
			MyLogger.Logger.log(Level.INFO, "-- execute dao cambia passowrd--");
			Map<String, Object> inputs = new HashMap<String, Object>();
			inputs.put(NOMBRE, personaFisica.getNombre());
			inputs.put(AP_PATERNO, personaFisica.getApellidoPaterno());
			inputs.put(AP_MATERNO, personaFisica.getApellidoMaterno());
			inputs.put(CVE_USUARIO, claveUsuario);
			// datos generales de la cuenta
			inputs.put(PASSWORD_ANTERIOR, perfil.getRegistroUsuario().getOldPassword());
			inputs.put(PASSWORD_NUEVO, perfil.getRegistroUsuario().getPassword());
			inputs.put(RFC, personaFisica.getRfc());
			inputs.put(NUM_SERIE, personaFisica.getnSerieCert());
			MyLogger.Logger.log(Level.INFO, "IMPRIME LOS PARAMETROS EVIADOS AL PL"+ inputs);
			return super.execute(inputs);
		}
	}

	@Override
	public PersonaFisica getConsultaRfcUsuario(PersonaFisica personaFisica)
			throws DaoException {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT  RFC as rfc,   ");
		query.append(" CVE_USUARIO as cveUsuario   ");
		query.append(" FROM V_USUARIO_SESION_RUG ");
		query.append(" WHERE RFC = " + "'"+ personaFisica.getRfc() + "'");
		query.append(" AND CVE_USUARIO = " + "'"+ personaFisica.getDatosContacto().getEmailPersonal() + "'");

		try {
			return (PersonaFisica) getJdbcTemplate().query(query.toString(),
					new ConsultaRfcUsuarioResultSetExtractor());
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}
	}

	public class ConsultaRfcUsuarioResultSetExtractor implements ResultSetExtractor {
		@Override
		public Object extractData(ResultSet resultSet) throws SQLException,
				DataAccessException {
			PersonaFisica personaFisica = new PersonaFisica ();
			if (resultSet.next()) {
				DatosContacto datosContacto = new DatosContacto();
				personaFisica.setDatosContacto(datosContacto);
				personaFisica.setRfc(resultSet.getString("rfc"));
				datosContacto.setEmailPersonal(resultSet.getString("rfc"));
			}
			return personaFisica;
		}
	}
}
