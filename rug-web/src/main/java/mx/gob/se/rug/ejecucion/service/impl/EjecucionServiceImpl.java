package mx.gob.se.rug.ejecucion.service.impl;

import java.util.logging.Level;

import mx.gob.se.rug.ejecucion.dao.EjecucionDAO;
import mx.gob.se.rug.ejecucion.service.EjecucionService;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.util.MyLogger;

public class EjecucionServiceImpl implements EjecucionService {

	@Override
	public Integer setEjecucion(Integer idGarantia, Integer idPersona, Integer idTipoTramite, String observaciones) {
		Integer idTramite = new InscripcionDAO().insert(idPersona, idTipoTramite);
		MyLogger.Logger.log(Level.INFO, "el id tramite en ejecucion serviceImpl : " + idTramite);
		new GarantiasDAO().altaBitacoraTramite(idTramite, 5, 0,null, "V");
		new EjecucionDAO().altaEjecucion(idGarantia, observaciones, idTramite);
		MyLogger.Logger.log(Level.INFO, "despues del alta de ejecucion : " + observaciones);
		return idTramite;
	}

	@Override
	public GarantiaTO getEjecucion(Integer idGarantia) {
		// TODO Auto-generated method stub
		return null;
	}

}
