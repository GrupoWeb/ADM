package mx.gob.se.rug.job.service;

import java.util.logging.Level;

import mx.gob.se.rug.job.service.impl.jobAvisoPSerImpl;
import mx.gob.se.rug.util.MyLogger;

public class jobAvisoPService {
	
	public Boolean jobAvisoP(Integer IdJob){
		MyLogger.Logger.log(Level.INFO, "entra al service de jobAvisoP");
		jobAvisoPSerImpl jobAvisoPSerImpl = new jobAvisoPSerImpl();
		MyLogger.Logger.log(Level.INFO, "entra al service de IdJob"+IdJob);
		Boolean res = jobAvisoPSerImpl.jobAvisoPreventivo(IdJob);
		return res;
	}
	
	public Boolean jobModifica (Integer IdJob){
		MyLogger.Logger.log(Level.INFO, "entra al service de jobAvisoP");
		jobAvisoPSerImpl jobAvisoPSerImpl = new jobAvisoPSerImpl();
		MyLogger.Logger.log(Level.INFO, "entra al service de IdJob"+IdJob);
		Boolean res = jobAvisoPSerImpl.jobAvisoPreventivo(IdJob);
		return res;		
	}
	
	
	public Boolean jobBuscaMod (Integer IdJob){
		MyLogger.Logger.log(Level.INFO, "entra al service de jobAvisoP");
		jobAvisoPSerImpl jobAvisoPSerImpl = new jobAvisoPSerImpl();
		MyLogger.Logger.log(Level.INFO, "entra al service de IdJob"+IdJob);
		Boolean res = jobAvisoPSerImpl.jobAvisoPreventivo(IdJob);
		return res;		
	}
	

}
