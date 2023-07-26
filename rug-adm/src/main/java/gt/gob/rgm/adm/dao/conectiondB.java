package gt.gob.rgm.adm.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//import mx.gob.se.rug.util.LoggerUtil;

public class conectiondB implements Serializable {
	/**
	 * 
	 */
	// private static  long idConexion = 1;
	// private static  long nConexionAbiertas = 0;
	// private long idConexionInstancia = 1;
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private DataSource ds = null;
	private Context ctx = null;

	public conectiondB() {
		// this.connection = getConnection();
	}

	public Connection getConnection() {

		try {
			
			this.ctx =null;
			this.ds=null;
			this.ctx = new InitialContext();
			this.ds = (DataSource) this.ctx.lookup("jdbc/rugDS");
			this.connection = this.ds.getConnection();
			
			//monitoreo conexiones
			//traza();
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.connection;
	}

//	private void traza(){
//		idConexionInstancia=++idConexion;
//		
//		LoggerUtil.ConnectionLog.log(Level.INFO,"Abierta:,"+idConexion +" Abiertas:"+ ++nConexionAbiertas);
//		LoggerUtil.ConnectionLog.log(Level.INFO,"Traza:::::::::");
//		
//		StackTraceElement[] stackTraceElement=	Thread.currentThread().getStackTrace();
//		StringBuffer stringBuffer= new StringBuffer();
//
//		for (StackTraceElement stackTraceElement2 : stackTraceElement) {
//			
//			stringBuffer.append(stackTraceElement2.getMethodName()+", ");
//		}
//		LoggerUtil.ConnectionLog.log(Level.INFO,stringBuffer.toString());
//
//	}

	public void close(Connection connection, ResultSet rs, Statement stmt) {
			
		
		
		if (rs != null) {
			try {
				if (!rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		if (stmt != null) {
			try {
				if (!stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
