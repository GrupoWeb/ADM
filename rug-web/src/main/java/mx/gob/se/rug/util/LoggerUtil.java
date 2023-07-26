package mx.gob.se.rug.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import mx.gob.se.rug.constants.Constants;

import com.lowagie.text.Utilities;

public class LoggerUtil {
	
	public static final Logger ConnectionLog = LoggerUtil.createLogger("Conexion","../logs/RugConnection.log",Level.ALL, false);
	
	public static Logger createLogger(final String loggerName, final String stringPath, final Level level, final boolean asXML) {
	       Logger logger = null;
	       try {
	           logger =  java.util.logging.Logger.getLogger(loggerName);
	           logger.setLevel(level);
	           Handler fHandler = new FileHandler(stringPath, 1024 * 1024 * 10, 10, true);
	           //Handler fHandler = new FileHandler(stringPath, true);
	           if (!asXML) {
	               fHandler.setFormatter(new SimpleFormatter());
	           }
	           /*En caso que no pueda anexar la siquiente propiedad regresa un logger apuntando a la salida convencional*/
	           logger.addHandler(fHandler);
	       } catch (IOException ex) {
	    	   java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
	       } catch (SecurityException ex) {
	    	   java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
	       }
	       return logger;
	   }
	public static Logger createLogger(final String loggerName,final Level level) {
	       Logger logger = null;
	       try {
	           logger = java.util.logging.Logger.getLogger(loggerName);
	           logger.setLevel(level);
	           }
	          
	       catch (SecurityException ex) {
	           java.util.logging.Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
	       } 
	       return logger;
	   }

}
