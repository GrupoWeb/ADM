package mx.gob.se.rug.job.service.impl;

import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.job.dao.jobAvisoDAO;
import mx.gob.se.rug.job.to.jobAvisoTO;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.util.MyLogger;

public class jobAvisoPSerImpl {
	
	public Boolean jobAvisoPreventivo (Integer IdJob){
		MyLogger.Logger.log(Level.INFO, "entra al sevice imple jobAvisoP");
		MyLogger.Logger.log(Level.INFO, "el id tramite q trae el dao :" + IdJob);
		return true;
	}
	////IMC se modifica retorno de funcion para obtener lista de jobs 
	public List <jobAvisoTO>getDatosjobAvisoPreventivo(/*Integer IdJob*/) throws NoDataFoundException{
		return new jobAvisoDAO().jobAvisoPrevetivo(/*IdJob*/);
	}
	
	
	///IMC se agrega la funcion para ejecutar el job 
	public PlSql ejecutarJobAvisoPreventivo (Integer IdJob) throws NoDataFoundException {
		return new jobAvisoDAO().correrJob(IdJob);
	}
	
	
	////IMC se agrega  funcion para actualizar el job a ejecutar 
	public PlSql actualizaJobAvisoPreventivo (Integer idJob, String statusJob,String repeatInterval ){
		
		return new jobAvisoDAO().actualizaJob(idJob, statusJob, repeatInterval);
	}
	
	
	////IMC buscar aviso preventivo por id job 
	public List <jobAvisoTO> buscarJobAvisoPrev (Integer idJob )throws NoDataFoundException{
		
		return new  jobAvisoDAO().jobAvisoPrevID( idJob);
	}
		

	
}

