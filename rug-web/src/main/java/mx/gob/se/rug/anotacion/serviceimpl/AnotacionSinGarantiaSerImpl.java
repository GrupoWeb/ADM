package mx.gob.se.rug.anotacion.serviceimpl;

import java.util.logging.Level;

import mx.gob.se.rug.anotacion.dao.AnotacionDAO;
import mx.gob.se.rug.boleta.dao.BoletaDAO;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.to.AnotacionSinGarantiaTO;
import mx.gob.se.rug.util.MyLogger;

public class AnotacionSinGarantiaSerImpl {
	
	public Boolean anotacionSinGarantia (int idPersona, String autoridad,String anotacion, Integer idTramite,Integer meses){
		MyLogger.Logger.log(Level.INFO, "entra al sevice imple anotacion sin garantia");
		MyLogger.Logger.log(Level.INFO, "el id tramite q trae el dao :" + idTramite);
		new AnotacionDAO().insert(idPersona, idTramite, autoridad, anotacion, meses);
		new GarantiasDAO().altaBitacoraTramite(idTramite, 5, 0,null, "V");
		return true;
	}
	
	public Integer altaTramite(int idPersona,int idTipoTramite){
		MyLogger.Logger.log(Level.INFO, "entra al sevice imple de alta tramite");
		Integer idTramite = new InscripcionDAO().insert(idPersona, idTipoTramite);
		MyLogger.Logger.log(Level.INFO, "el id tramite q trae el dao :" + idTramite);
		return idTramite;
	}
	
	public AnotacionSinGarantiaTO getDatosAnotacionSinGarantia(Integer idTramite) throws NoDataFoundException{
		return new BoletaDAO().getDatosAnotacionSinGarantia(idTramite);
	}
}

