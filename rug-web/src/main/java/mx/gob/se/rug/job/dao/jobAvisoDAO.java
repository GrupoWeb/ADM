package mx.gob.se.rug.job.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.rug.dao.BaseRugDao;
import mx.gob.se.rug.dao.ConexionBD;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.job.to.jobAvisoTO;
import mx.gob.se.rug.operaciones.to.OperacionesTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.MyLogger;

public class jobAvisoDAO extends BaseRugDao{
 ///IMC Se modifica retorno de funcion a lista . por el numero de jobs a procesar 
 public List <jobAvisoTO> jobAvisoPrevetivo(/*Integer IdJob*/) throws NoDataFoundException{
	 jobAvisoTO	 jobAvisoTO = null;
	 List <jobAvisoTO> listaJobAvisoPreventivo = new ArrayList<jobAvisoTO>();
		String sql = "SELECT ID, JOB, ESTADO, PERIODICIDAD, ULT_EJECUCION, PROX_EJECUCION , DESCRIPCION FROM V_JOBS_USUARIO";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			//ps.setInt(1, IdJob);
			rs = ps.executeQuery();
			while (rs.next()){
				jobAvisoTO = new jobAvisoTO();
				jobAvisoTO.setIdJob(rs.getInt("ID"));						
				jobAvisoTO.setJobName(rs.getString("JOB"));					
				jobAvisoTO.setEstado(rs.getString("ESTADO"));				
				jobAvisoTO.setPeriodicidad(rs.getString("PERIODICIDAD"));	
				jobAvisoTO.setUltEjecucion(rs.getDate("ULT_EJECUCION"));	
				jobAvisoTO.setProxEjecucion(rs.getDate("PROX_EJECUCION"));
				jobAvisoTO.setDescripcion(rs.getString ("DESCRIPCION"));        
				listaJobAvisoPreventivo.add(jobAvisoTO);//// se agrega lista 
				}
			//}else{
				//		throw new NoDataFoundException("Error en el job::"+IdJob);
			//}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		return listaJobAvisoPreventivo;/// return lista 
	}
 
 
 public List <jobAvisoTO> jobAvisoPrevID(Integer IdJob) throws NoDataFoundException{
	 jobAvisoTO	 jobAvisoTO = null;
	 List <jobAvisoTO> listaJobAvisoPreventivo = new ArrayList<jobAvisoTO>();
		String sql = "SELECT ID, JOB, ESTADO, PERIODICIDAD, ULT_EJECUCION, PROX_EJECUCION , DESCRIPCION FROM V_JOBS_USUARIO " +
					 "WHERE ID =? ";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		PreparedStatement ps = null;	
		ResultSet rs =null;
		try{
			ps = connection.prepareCall(sql);
			ps.setInt(1, IdJob);
			rs = ps.executeQuery();
			while (rs.next()){
				jobAvisoTO = new jobAvisoTO();
				jobAvisoTO.setIdJob(rs.getInt("ID"));						
				jobAvisoTO.setJobName(rs.getString("JOB"));					
				jobAvisoTO.setEstado(rs.getString("ESTADO"));				
				jobAvisoTO.setPeriodicidad(rs.getString("PERIODICIDAD"));	
				jobAvisoTO.setUltEjecucion(rs.getDate("ULT_EJECUCION"));	
				jobAvisoTO.setProxEjecucion(rs.getDate("PROX_EJECUCION"));
				jobAvisoTO.setDescripcion(rs.getString ("DESCRIPCION"));        
				listaJobAvisoPreventivo.add(jobAvisoTO);//// se agrega lista 
				}
			//}else{
				//		throw new NoDataFoundException("Error en el job::"+IdJob);
			//}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			bd.close(connection,rs,ps);
		}
		MyLogger.Logger.log(Level.INFO, "jobAvisoDAO.correrJob");
		MyLogger.Logger.log(Level.INFO,"jobAvisoDAO.jobAvisoPrevID.V_JOBS_USUARIO valores de salida: cantlista="
								+ listaJobAvisoPreventivo.size());
		return listaJobAvisoPreventivo;/// return lista 
	}
 
 
 
 public PlSql correrJob(Integer ID) {
		MyLogger.Logger.log(Level.INFO, "jobAvisoDAO.correrJob");
		PlSql regresa = new PlSql();
		String sql = "{ call RUG.SP_CADUCIDAD_AVISOS_PREV(?, ?) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			regresa.setIntPl(cs.getInt(1));
			regresa.setStrPl(cs.getString(2));
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage());
			MyLogger.Logger.log(Level.SEVERE, "Sucedio un error al ejecutar el PL CADUCIDAD_AVISOS_PREV", e.getStackTrace());
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "jobAvisoDAO.correrJob");
		MyLogger.Logger.log(Level.INFO,"jobAvisoDAO.correrJob.SP_CADUCIDAD_AVISOS_PREV valores de salida: plSql.intPl="
								+ regresa.getIntPl()
								+ ",plSql.intPl="
								+ regresa.getStrPl());
		return regresa;
	}
 
 
 ////// IMC agregar el store procedure   SP_JOB_UPDATE
 
 
 public PlSql actualizaJob(Integer idJob,String statusJob ,String repeatInterval) {
		MyLogger.Logger.log(Level.INFO, "jobAvisoDAO.actualizaJob");
		PlSql regresa = new PlSql();
		String sql = "{ call RUG.SP_JOB_UPDATE (?, ?, ?, ?, ?) }";
		ConexionBD bd = new ConexionBD();
		Connection connection = bd.getConnection();
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1,idJob);
			cs.setString(2,statusJob);
			cs.setString(3,repeatInterval );
			cs.registerOutParameter(4,Types.INTEGER);
			cs.registerOutParameter(5,Types.VARCHAR);
			cs.execute();
			regresa.setIntPl(cs.getInt(4));
			regresa.setStrPl(cs.getString(5));
		} catch (SQLException e) {
			regresa.setIntPl(999);
			regresa.setStrPl(e.getMessage());
			MyLogger.Logger.log(Level.SEVERE, "Sucedio un error al ejecutar el PL SP_JOB_UPDATE", e.getStackTrace());
		} finally {
			bd.close(connection, null, cs);
		}
		MyLogger.Logger.log(Level.INFO, "jobAvisoDAO.actualizaJob");
		MyLogger.Logger.log(Level.INFO,	"jobAvisoDAO.actualizaJob.SP_JOB_UPDATE valores de salida: plSql.intPl="
							+ regresa.getIntPl()+ ",plSql.intPl="+ regresa.getStrPl());
		return regresa;
	}
 
	}