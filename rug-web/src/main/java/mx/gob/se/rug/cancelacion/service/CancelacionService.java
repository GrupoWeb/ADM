package mx.gob.se.rug.cancelacion.service;

import java.util.logging.Level;

import mx.gob.se.rug.cancelacion.serviceimpl.CancelacionServiceImpl;
import mx.gob.se.rug.util.MyLogger;

public class CancelacionService {
	
	public Boolean cancelar(Integer idTramite, Integer idGarantia, String observaciones){
		Boolean regresa = false;
		MyLogger.Logger.log(Level.INFO,"entra al metodo de cancelar del service");
		regresa = new CancelacionServiceImpl().cancelar(idTramite, idGarantia, observaciones);
		return regresa;
	}
	
	public Integer creaTramiteTem(Integer idPersona, Integer idTipoTramite){
		MyLogger.Logger.log(Level.INFO, "entra al metodo de crear tramite temp de cancelar del service");
		int idTramite = new CancelacionServiceImpl().creaTramiteTem(idPersona, idTipoTramite);
		return idTramite;
	}
}
