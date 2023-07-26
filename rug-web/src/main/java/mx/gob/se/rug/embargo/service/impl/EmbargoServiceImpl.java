package mx.gob.se.rug.embargo.service.impl;

import java.util.logging.Level;

import mx.gob.se.rug.embargo.dao.EmbargoDAO;
import mx.gob.se.rug.embargo.service.EmbargoService;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.util.MyLogger;

public class EmbargoServiceImpl implements EmbargoService{

	@Override
	public Integer setEmbargo(Integer idGarantia, Integer idPersona, Integer idTipoTramite, String observaciones) {
		Integer idTramite = new InscripcionDAO().insert(idPersona, idTipoTramite);
		MyLogger.Logger.log(Level.INFO, "el id tramite en embargo serviceImpl : " + idTramite);
		new GarantiasDAO().altaBitacoraTramite(idTramite, 5, 0,null, "V");
		new EmbargoDAO().altaEmbargo(idGarantia, observaciones, idTramite);
		MyLogger.Logger.log(Level.INFO, "despues del alta de embargo : " + observaciones);
		return idTramite;
	}
	
	@Override
	public Boolean isEmbargo(Integer idGarantia) {
		Boolean result = new EmbargoDAO().isEmbargo(idGarantia);
				
		return result;
	}

	@Override
	public GarantiaTO getEmbargo(Integer idGarantia) {
		// TODO Auto-generated method stub
		return null;
	}
}
