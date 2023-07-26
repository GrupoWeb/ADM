package mx.gob.se.rug.common.util;

import java.util.logging.Level;

import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.login.service.dao.impl.LoginDaoJdbcImpl;
import mx.gob.se.rug.util.MyLogger;

public class ValidaConexion {
	public String existeConexion() {
                
		MyLogger.Logger.log(Level.INFO, "RUG:INTENTO LOGIN >>>>:" + ++Constants.PETICIONES_lOGIN);
		String regresa = new LoginDaoJdbcImpl().regresaFecha()+"";
                //MyLogger.Logger.log(Level.INFO,"Error = " + regresa);
		return  regresa;
	}
}
