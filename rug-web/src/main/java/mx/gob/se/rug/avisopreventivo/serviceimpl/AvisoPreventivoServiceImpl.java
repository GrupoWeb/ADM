package mx.gob.se.rug.avisopreventivo.serviceimpl;

import java.util.logging.Level;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.avisopreventivo.dao.AvisoPreventivoDAO;
import mx.gob.se.rug.detallegarantia.dao.DetalleDAO;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.inscripcion.dao.AltaParteDAO;
import mx.gob.se.rug.inscripcion.dao.InscripcionDAO;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.InscripcionTO;
import mx.gob.se.rug.util.MyLogger;

public class AvisoPreventivoServiceImpl {
	
	public boolean aviso(Integer idPersona, String desc, Integer idTramite){
		MyLogger.Logger.log(Level.INFO, "entra al sevice imple aviso");
		boolean regresa = false;
		try{
			MyLogger.Logger.log(Level.INFO, "entra al sevice imple aviso" + idTramite);
			MyLogger.Logger.log(Level.INFO, "entra al sevice imple aviso" + desc);
			MyLogger.Logger.log(Level.INFO, "entra al sevice imple aviso" + idPersona);
			new AvisoPreventivoDAO().insertAviso(idTramite, desc, idPersona);
			MyLogger.Logger.log(Level.INFO, "aun en el aviso" + idPersona);
			new GarantiasDAO().altaBitacoraTramite(idTramite, 2, 0,null, "V");
			MyLogger.Logger.log(Level.INFO, "aun en alta bitacora tramite" + idPersona);
			regresa = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return regresa;
	}
	
	public Integer altaTramite (int idPersona,int idTipoTramite, AcreedorTO acreedorTO){
		MyLogger.Logger.log(Level.INFO, "entra al sevice imple metodo alta tramite");
		int regresa = 0;
		try{
			InscripcionTO inscripcionTO = new InscripcionTO();
			inscripcionTO.setIdTipoTramite(idTipoTramite);
			inscripcionTO.setIdPersona(idPersona);
			int idTramite = new InscripcionDAO().insertInscripcionOtros(inscripcionTO, acreedorTO);
			regresa = idTramite;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return regresa;
	}
}

