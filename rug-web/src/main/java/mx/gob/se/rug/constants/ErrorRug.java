package mx.gob.se.rug.constants;

import java.util.Map;
import java.util.logging.Level;

import mx.gob.se.rug.constants.dao.ConstantsDAO;
import mx.gob.se.rug.util.MyLogger;

public class ErrorRug {
	
	public static Map<Integer, String> errorRug ;

	public static String getErrorDesc(int idError){
		if(errorRug==null){
			ErrorRug.errorRug= new ConstantsDAO().getErrores();
	      }
		String regresa = "Code error: consvalerror";
		try{
			regresa = new String (ErrorRug.errorRug.get(idError));
		}catch(Exception e){
			MyLogger.Logger.log(Level.WARNING, "No se encontro el id del error "+idError);
			//e.printStackTrace();
		}
		
		return regresa;
	}
	

}
