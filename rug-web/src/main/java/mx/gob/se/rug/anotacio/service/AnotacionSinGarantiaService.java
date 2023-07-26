package mx.gob.se.rug.anotacio.service;

import java.util.logging.Level;

import mx.gob.se.rug.anotacion.serviceimpl.AnotacionSinGarantiaSerImpl;
import mx.gob.se.rug.util.MyLogger;

public class AnotacionSinGarantiaService {
	
	public Boolean anotacionSinGarantia(int idPersona, String autoridad,String anotacion, Integer idTramite, Integer meses){
		MyLogger.Logger.log(Level.INFO, "entra al service de anotacion");
		AnotacionSinGarantiaSerImpl anotacionSerImpl = new AnotacionSinGarantiaSerImpl();
		Boolean res = anotacionSerImpl.anotacionSinGarantia(idPersona,autoridad,anotacion, idTramite,meses);
		return res;
	}

	public Integer altaTramite(int idPersona,int idTipoTramite){
		return new AnotacionSinGarantiaSerImpl().altaTramite(idPersona, idTipoTramite);
	}
}
