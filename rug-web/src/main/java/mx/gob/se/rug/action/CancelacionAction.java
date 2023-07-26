package mx.gob.se.rug.action;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.cancelacion.service.CancelacionService;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.juez.service.JuezService;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.interceptor.ServletRequestAware;

public class CancelacionAction extends RugBaseAction implements
		ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private String autoridad;
	private String idGarantia;
	private String observaciones;

	public String cancelacion() {
		setAutoridad("");
		return Constants.SUCCESS;
	}

	public String cancelacionVal() {
		MyLogger.Logger.log(Level.INFO,
				"entra al metodo de cancelacion del tramite el primero cancelacionVal() ");
		String regresa = "failed";
		try {
			CancelacionService cancelacionService = new CancelacionService();
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idPersona = usuario.getPersona().getIdPersona();
			Integer idTramiteNuevo = cancelacionService.creaTramiteTem(idPersona, 4);
			sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
			if (idTramiteNuevo.intValue() != 0) {
				regresa = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
			regresa = "failed";
		}

		return regresa;
	}

	public String cancelacionTramite() {
		MyLogger.Logger.log(Level.INFO,
				"entra al metodo de cancelacion del tramite 10:14, comprobando cambios");

		String regresa = cancelacionVal();
		if (regresa.equals("success")) {
			regresa = "failed";
			Integer idGarantia = (Integer) sessionMap
					.get(Constants.ID_GARANTIA);
			MyLogger.Logger.log(Level.INFO,
					"id Garantia : "+ idGarantia);
			Integer idTramiteNuevo = (Integer) sessionMap
					.get(Constants.ID_TRAMITE_NUEVO);
			MyLogger.Logger.log(Level.INFO,
					"Id tramite nuevo : "+idTramiteNuevo);
			try {
				CancelacionService cancelacionService = new CancelacionService();
				cancelacionService.cancelar(idTramiteNuevo, idGarantia, getObservaciones());
				if (getAutoridad() != null && !getAutoridad().equals("")) {
					JuezService juezService = new JuezService();
					MyLogger.Logger.log(Level.WARNING,
							"entra al action cancelacion: " + idTramiteNuevo
									+ "la autoridad" + getAutoridad());
					juezService.insertJuez(idTramiteNuevo, getAutoridad());
				}
				regresa = "success";
			} catch (Exception e) {
				regresa = "failed";
				e.printStackTrace();
			}
			MyLogger.Logger.log(Level.INFO,
					"sale del metodo antes de regresar " + regresa);
		}

		return regresa;
	}

	public String iniciaCancelacion() {
		MyLogger.Logger.log(Level.INFO, "--Inicia Cancelacion--");
		String regresa = Constants.FAILED;
		
		try{
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			Integer idGarantia =(Integer) sessionMap.get(Constants.ID_GARANTIA);	
			setIdGarantia(idGarantia.toString());
			
			MyLogger.Logger.log(Level.INFO, "usuario::"+usuario.getPersona().getIdPersona());
			MyLogger.Logger.log(Level.INFO, "idGarantia::"+idGarantia);
			
			regresa= Constants.SUCCESS;
		}catch(Exception e){
			regresa = Constants.FAILED;
			MyLogger.Logger.log(Level.SEVERE, e.getMessage(),e);
		}
		
		return regresa;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI,
				RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR,
				RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}

	public String getAutoridad() {
		return autoridad;
	}

	public void setAutoridad(String autoridad) {
		this.autoridad = autoridad;
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
