package mx.gob.se.rug.modificacion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.dao.exception.JdbcDaoException;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.modificacion.to.TipoBienesTO;
import mx.gob.se.rug.util.MyLogger;

public class TipobienesDAO {
	
	public List <TipoBienesTO> getTipoBienes() {
		///logger.debug("-- getPreguntas --");
		TipoBienesTO tipo = new TipoBienesTO();
		List <TipoBienesTO> lista  = new ArrayList<TipoBienesTO>();		
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sqlQuery = "";
			sqlQuery = "select ID_TIPO_BIEN, DESC_TIPO_BIEN, DESC_TIPO_BIEN_EN, ID_PADRE, STATUS_REG from RUG_CAT_TIPO_BIEN where STATUS_REG = 'AC' order by ID_TIPO_BIEN";
			ps = connection.prepareStatement(sqlQuery);
			MyLogger.Logger.log(Level.INFO, "sql v" + ps);
			 rs = ps.executeQuery();
			while(rs.next()){
				tipo = new TipoBienesTO();
				tipo.setIdbien(rs.getInt("ID_TIPO_BIEN"));
				tipo.setDescrbien(rs.getString("DESC_TIPO_BIEN"));
				lista.add(tipo);
			}
			
		} catch (Exception e) {
			throw new JdbcDaoException(e);
		}finally{
			bd.close(connection, rs, ps);
		}
		return lista;
	}
}
