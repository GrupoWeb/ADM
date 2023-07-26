package mx.gob.se.rug.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import mx.gob.se.rug.constants.Constants;

public class MyLogger {
	
	
	public static final Logger Logger = LoggerUtil.createLogger(Constants.getParamValue(Constants.LOG_NAME),Constants.getParamValue(Constants.PATH), 
															  Level.parse(Constants.getParamValue(Constants.LOG_LEVEL)), false);
	
	public static final Logger CargaMasivaLog = LoggerUtil.createLogger(Constants.getParamValue(Constants.LOG_NAME_CM),Constants.getParamValue(Constants.PATH), 
			Level.parse(Constants.getParamValue(Constants.LOG_LEVEL)), false);
	
	public static final Logger ErrorFirma = LoggerUtil.createLogger(Constants.LOG_NAME_EF,"../logs/RugEF.log", Level.parse(Constants.getParamValue(Constants.LOG_LEVEL)), false);
	
	
	

}
