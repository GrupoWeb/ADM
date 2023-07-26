package mx.gob.se.rug.prorroga.service;

import java.util.logging.Level;

import mx.gob.se.rug.detallegarantia.dao.DetalleDAO;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.prorroga.serviceimpl.ProrrogaServiceImpl;
import mx.gob.se.rug.util.MyLogger;

public class ProrrogaService {

	public GarantiaTO getVigencia(Integer idGarantia) throws NoDataFoundException{
		MyLogger.Logger.log(Level.INFO, "entra al service de prorroga para traer vigencia");
		GarantiaTO garantiaTO;
		garantiaTO = new ProrrogaServiceImpl().getVigencia(idGarantia);
		MyLogger.Logger.log(Level.INFO, "garantia TO " + garantiaTO);
		return garantiaTO;
	}
	
	public Integer setVigencia(Integer idGarantia, Integer idPersona, Integer idTipoTramite, Integer vigencia){
		MyLogger.Logger.log(Level.INFO, "entra al service de prorroga para guardar vigencia");
		Integer idTramite=new ProrrogaServiceImpl().setVigencia(idGarantia, idPersona, idTipoTramite, vigencia);
		return idTramite;
	}
}
