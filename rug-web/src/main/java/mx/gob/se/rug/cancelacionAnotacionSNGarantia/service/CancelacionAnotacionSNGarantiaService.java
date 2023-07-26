package mx.gob.se.rug.cancelacionAnotacionSNGarantia.service;

import java.util.logging.Level;

import mx.gob.se.rug.cancelacionAnotacionSNGarantia.serviceimpl.CancelacionASNGarantiaServiceImpl;
import mx.gob.se.rug.util.MyLogger;

public class CancelacionAnotacionSNGarantiaService {
	
//	  peIdUsuario          IN  NUMBER,
//    peIdTipoTramite      IN  NUMBER,
//    peIdAnotacion        IN  NUMBER,                                             
//    peAutoridadInstruye  IN  VARCHAR2,
//    peAnotacion          IN  CLOB,                                             
//    peVigenciaAnotacion  IN  NUMBER,
	
	public Boolean cancelar(Integer IdUsuario, Integer idTipoTram, Integer IdAnotacion, String autoInst,String Anotacion, Integer VigenMes){
		Boolean regresa = false;
		System.out.println("anotacion texto:**"+Anotacion);
		MyLogger.Logger.log(Level.INFO,"entra al metodo de cancelar del service");
		regresa = new CancelacionASNGarantiaServiceImpl().cancelar(IdUsuario, idTipoTram, IdAnotacion, autoInst,Anotacion, VigenMes);
		System.out.println("regresa en CancelacionAnotacionSNGarantia es... :"+regresa);
		return regresa;
	}
	
//	public Integer creaTramiteTem(Integer IdUsuario, Integer idTramiteTemporal, Integer IdAnotacion, String Anotacion, Integer VigenciaAnotacion){
//		MyLogger.Logger.log(Level.INFO, "entra al metodo de crear tramite temp de cancelar del service");
//		int idTramite = new CancelacionASNGarantiaServiceImpl().creaTramiteTem(IdUsuario, idTramiteTemporal, IdAnotacion, Anotacion, VigenciaAnotacion);
//		return idTramite;
//	}
}
