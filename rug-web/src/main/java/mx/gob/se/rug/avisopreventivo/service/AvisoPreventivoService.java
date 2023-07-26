package mx.gob.se.rug.avisopreventivo.service;

import java.util.List;
import java.util.logging.Level;

import mx.gob.se.rug.acreedores.service.impl.AcreedoresServiceImpl;
import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.avisopreventivo.serviceimpl.AvisoPreventivoServiceImpl;
import mx.gob.se.rug.inscripcion.service.impl.InscripcionServiceImpl;
import mx.gob.se.rug.inscripcion.to.AltaParteTO;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.prorroga.serviceimpl.ProrrogaServiceImpl;
import mx.gob.se.rug.util.MyLogger;

public class AvisoPreventivoService {

	public Boolean aviso(Integer idPersona, String desc, Integer idTramite){
		MyLogger.Logger.log(Level.INFO, "entra al sevice");
		return new AvisoPreventivoServiceImpl().aviso(idPersona, desc, idTramite);
	}
	public List<NacionalidadTO> getNacionalidades(){
		return new AcreedoresServiceImpl().getNacionalidades();
	}
	public List <AcreedorTO> getAcreedoresByID(Integer idPersona){
		return new InscripcionServiceImpl().getAcreedoresByID(idPersona);
	}
	
	public Integer altaTramite (Integer idPersona,Integer idTipoTramite, AcreedorTO acreedorTO){
		return new AvisoPreventivoServiceImpl().altaTramite(idPersona, idTipoTramite, acreedorTO);
	}
}
