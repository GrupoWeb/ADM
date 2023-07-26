package mx.gob.se.rug.common.environment.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.logging.Level;

import mx.gob.se.rug.common.environment.dao.EnvironmentDao;
import mx.gob.se.rug.fwk.dao.spring.RugBaseJdbcDao;
import mx.gob.se.rug.util.MyLogger;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class EnvironmentDaoJdbcImpl extends RugBaseJdbcDao implements
		EnvironmentDao {

	@Override
	public String getValue(String name) {
		MyLogger.Logger.log(Level.INFO, "--ENTRA AL METODO GET VALUE--");
		name = name.trim().toUpperCase();
		return (String) getJdbcTemplate().query(
				MessageFormat.format("SELECT VALOR_PARAMETRO"
						+ " FROM V_CAT_PARAMS_GRALES"
						+ " WHERE CVE_PARAMETRO = ''{0}''", name),
				new ResultSetExtractor() {
					@Override
					public Object extractData(ResultSet resultSet)
							throws SQLException, DataAccessException {
						if (resultSet.next()) {
							return resultSet.getString("VALOR_PARAMETRO");
						}
						return null;
					}
				});
	}
}
