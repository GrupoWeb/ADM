package mx.gob.se.rug.prorroga.serviceimpl;
import java.util.logging.Level;

import mx.gob.se.rug.detallegarantia.dao.DetalleDAO;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.garantia.to.GarantiaTO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.prorroga.dao.ProrrogaDAO;
import mx.gob.se.rug.util.MyLogger;
public class ProrrogaServiceImpl {
	
	public GarantiaTO getVigencia(Integer idGarantia) throws NoDataFoundException{
		MyLogger.Logger.log(Level.INFO, "entra al service impl prorroga para traer la vigencia ");
		GarantiaTO garantiaTO;
		garantiaTO = new ProrrogaDAO().getVigencia(idGarantia);
		return garantiaTO;
	}
	
	public Integer setVigencia (Integer idGarantia,Integer idPersona, Integer idTipoTramite, Integer vigencia){
		Integer idTramite = new InscripcionDAO().insert(idPersona, idTipoTramite);
		MyLogger.Logger.log(Level.INFO, "el id tramite en prorroga serviceImpl : " + idTramite);
		new GarantiasDAO().altaBitacoraTramite(idTramite, 5, 0,null, "V");
		new ProrrogaDAO().altaProrroga(idGarantia, vigencia, idTramite);
		MyLogger.Logger.log(Level.INFO, "despues del alta de prorroga : " + vigencia);
		return idTramite;
	}
}
