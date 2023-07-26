package mx.gob.se.rug.action;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.modificacion.service.impl.ModificacionServiceImp;
import org.apache.struts2.interceptor.ServletRequestAware;

import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.ejecucion.service.EjecucionService;
import mx.gob.se.rug.ejecucion.service.impl.EjecucionServiceImpl;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class EjecucionAction extends RugBaseAction implements ServletRequestAware {
	
	private static final long serialVersionUID = 1L;
	
	private String idGarantia;
	private String observaciones;
	
	public EjecucionAction() {
		super();
	}

	public String inicia(){
		MyLogger.Logger.log(Level.INFO, "--Inicia Ejecucion--");
		String regresa = Constants.FAILED;

		
		try{
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idGarantia =(Integer) sessionMap.get(Constants.ID_GARANTIA);	
			setIdGarantia(idGarantia.toString());
			ModificacionServiceImp detserv = new ModificacionServiceImp();

			MyLogger.Logger.log(Level.INFO, "usuario::"+usuario.getPersona().getIdPersona());
			MyLogger.Logger.log(Level.INFO, "idGarantia::"+idGarantia );
			
			regresa= Constants.SUCCESS;
		}catch(Exception e){
			regresa = Constants.FAILED;
			MyLogger.Logger.log(Level.SEVERE, e.getMessage(),e);
		}
		
		return regresa;
	}
	
	public String ejecucion() {
		String regresa = Constants.FAILED;
		
		try{
			Integer idGarantia =(Integer) sessionMap.get(mx.gob.se.rug.constants.Constants.ID_GARANTIA);
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			
			EjecucionService ejecucionService = new EjecucionServiceImpl();
			Integer idTramiteNuevo =  ejecucionService.setEjecucion(idGarantia, usuario.getPersona().getIdPersona(), 30, getObservaciones());
			sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
//			Garantia para arancel
			sessionMap.put(Constants.ID_GARANTIA_ARANCEL, idGarantia);
			MyLogger.Logger.log(Level.INFO, "id tramite nuevo "+idTramiteNuevo);
			
			if (idTramiteNuevo.intValue() != 0){
				regresa= Constants.SUCCESS;			
			}			
		}catch(Exception e){
			regresa = Constants.FAILED;
			MyLogger.Logger.log(Level.SEVERE, e.getMessage(),e);
		}
		
		return regresa;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}
	
	public String getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(String idGarantia) {
		this.idGarantia = idGarantia;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
