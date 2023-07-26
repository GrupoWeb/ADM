package mx.gob.se.rug.login.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;


import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.login.service.dao.LoginDAO;
import mx.gob.se.rug.util.MyLogger;

public class LoginDaoJdbcImpl implements LoginDAO{

	@Override
	public String regresaFecha(){
		String regresa = "0";
		String sql = "select sysdate from dual";
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConexionBD bd = new ConexionBD();
		Connection connection = null;
		try{
			connection = bd.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()){
				regresa ="1";
			}				
		}catch(Exception e){
			e.printStackTrace();				
		}finally{
			bd.close(connection, rs, ps);
		}
		if (regresa.equals("0")){
			MyLogger.Logger.log(Level.WARNING, "RUG:ASAV:>>>>ERRORES EN LA CONEXION::"+ ++Constants.PETICIONES_FALLIDAS_BD);
		}
		return regresa;
	}

	private String getCustomStackTrace(Throwable aThrowable) {
	    //add the class name and any message passed to constructor
	    final StringBuilder result = new StringBuilder( "Exception New Page: " );
	    result.append(aThrowable.toString());
	    final String NEW_LINE = System.getProperty("line.separator");
	    result.append(NEW_LINE);

	    //add each element of the stack trace
	    for (StackTraceElement element : aThrowable.getStackTrace() ){
	      result.append( element );
	      result.append( NEW_LINE );
	    }
	    return result.toString();
	  }


}
