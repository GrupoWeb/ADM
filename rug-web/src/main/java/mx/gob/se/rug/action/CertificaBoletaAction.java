package mx.gob.se.rug.action;

import java.util.logging.Level;

import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.modificacion.service.impl.ModificacionServiceImp;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class CertificaBoletaAction extends RugBaseAction {
	
	private String idpersona;
	

	public void setIdpersona(String idpersona) {
		this.idpersona = idpersona;
	}


	public String getIdpersona() {
		return idpersona;
	}


	public String certificaboleta(){
		String regresa = "failure";
		
		try{				
			Integer idgarantia = (Integer) sessionMap.get("idGarantia");
			Integer tramite = (Integer) sessionMap.get("idTramite");
			sessionMap.put(Constants.ID_TRAMITE, tramite);
			sessionMap.put(Constants.ID_GARANTIA, idgarantia);
			UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");		
			setIdpersona(new Integer(usuario.getPersona().getIdPersona()).toString());
			
			MyLogger.Logger.log(Level.INFO, "dgarantia " + idgarantia);
			MyLogger.Logger.log(Level.INFO, "tramite " + tramite);
			
			ModificacionServiceImp detserv = new ModificacionServiceImp();
			
			Integer tramnuevo = detserv.insertatramiteinc(Integer.valueOf(usuario.getPersona().getIdPersona()),5);
			MyLogger.Logger.log(Level.INFO, "tramite incompleto "+ tramnuevo);
			
			boolean valor = detserv.altaBitacoraTramite(tramnuevo, 5, 0, null, "V");
			MyLogger.Logger.log(Level.INFO, "alta bitacora ."+ valor);
			//setIdTramite(inscripcionTO.getIdInscripcion().toString());
			//setIdTramite(getTramiteinc().toString());
			MyLogger.Logger.log(Level.INFO, "tramiate inc"+ tramnuevo);
			sessionMap.put(Constants.ID_TRAMITE_NUEVO,tramnuevo);	
			regresa="success";
		}catch(Exception e){
			e.printStackTrace();
		}
			
		return regresa;
	}	
}
