package mx.gob.se.rug.bitacora.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.dao.AbstractBaseJdbcDao;
import mx.gob.economia.dgi.framework.dao.exception.DaoException;
import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.bitacora.dao.BitacoraLoginDao;
import mx.gob.se.rug.bitacora.dto.BitacoraLogin;
import mx.gob.se.rug.common.constants.Constants;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.util.MyLogger;

@SuppressWarnings("deprecation")
public class BitacoraLoginDaoJdbcImpl extends AbstractBaseJdbcDao implements
		BitacoraLoginDao {

	private static final String COMMIT = "V";

	public BitacoraLoginDaoJdbcImpl() {
		super(Constants.DATA_SOURCE);
	}

	@Override
	public boolean bitacoraLogin(BitacoraLogin bitacoraLogin)
			throws DaoException {

		MyLogger.Logger.log(Level.INFO, "bitacoraLogin: " + bitacoraLogin);
		Map<Integer, String> parametros = new HashMap<Integer, String>();
		parametros.put(1, bitacoraLogin.getCveUsuario()); // peCveUsuario
		parametros.put(2, String.valueOf(bitacoraLogin.getIdEvento())); // peIdEvento
		parametros.put(3, bitacoraLogin.getComentario()); // peTxComentario

		parametros.put(4, bitacoraLogin.getRequestAddress()); // peIpEvento
		parametros.put(5, bitacoraLogin.getRequestURI()); // peUrlEvento
		parametros.put(6, ""); // peDatosAdic

		logger.debug("parametros: " + parametros);
		return executePl(parametros);
	}

	private boolean executePl(Map<Integer, String> parametros) throws DaoException{
		Mensaje mensaje = new Mensaje();

		StringBuffer sqlQuery = new StringBuffer(
				"begin PKGSE_INFRA.SPInsBitcoraEvento(?,?,?,?,?,?,?,?,?); end;");

		Connection connection = null;
		CallableStatement callStatement = null;

		try {
			connection = getConnection();
			callStatement = connection.prepareCall(sqlQuery.toString());

			for (int i = 1; i <= parametros.size(); i++) {
				callStatement.setString(i, parametros.get(i));
			}
			callStatement.setString(7, COMMIT);// peBcommit
			callStatement.registerOutParameter(8, java.sql.Types.INTEGER);// psResult
			callStatement.registerOutParameter(9, java.sql.Types.VARCHAR);// psTxResult

			callStatement.execute();

			mensaje.setRespuesta(callStatement.getInt(8));
			mensaje.setMensaje(callStatement.getString(9));

		} catch (Exception e) {
			logger.error("execute: ", e);
			throw new JdbcDaoException(e);
		} finally {
			closeResources(connection, callStatement, null);
		}
		MyLogger.Logger.log(Level.INFO, "Bitacora el login del usuario execute, mensaje: "
				+ mensaje);

		if ((mensaje != null) && (mensaje.getRespuesta() == 0)) {
			return true;
		} else {
			return false;
		}

	}
}
