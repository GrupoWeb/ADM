package mx.gob.se.rug.action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.embargo.service.EmbargoService;
import mx.gob.se.rug.embargo.service.impl.EmbargoServiceImpl;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.to.TipoTo;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class EmbargoAction extends RugBaseAction implements ServletRequestAware {
	
	private static final long serialVersionUID = 1L;
	
	private String idGarantia;
	private String observaciones;
	private List <TipoTo> listaTipoEmbargo;
	private String mdTipoEmbargo;

	public EmbargoAction() {
		super();
	}

	public String inicia(){
		MyLogger.Logger.log(Level.INFO, "--Inicia Embargo--");
		String regresa = Constants.FAILED;
		
		TipoTo tipo0 = new TipoTo();
		tipo0.setIdTipo("1");
		tipo0.setDesTipo("Embargo");
		TipoTo tipo1 = new TipoTo();
		tipo1.setIdTipo("2");
		tipo1.setDesTipo("Levantado de Embargo");
		
		listaTipoEmbargo =  new ArrayList<TipoTo>();
		
		
		try{
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			//Integer pIdGarantia = (Integer) sessionMap.get(Constants.ID_GARANTIA);			
			String pIdGarantia = ServletActionContext.getRequest().getParameter(Constants.ID_GARANTIA);
			MyLogger.Logger.log(Level.INFO, "idGarantia::"+pIdGarantia);
			sessionMap.put(Constants.ID_GARANTIA, new Integer(pIdGarantia));
			setIdGarantia(pIdGarantia.toString());
			
			MyLogger.Logger.log(Level.INFO, "usuario::"+usuario.getPersona().getIdPersona());	
			
			EmbargoService embargoService = new EmbargoServiceImpl();
			Boolean isEmbargo = embargoService.isEmbargo(Integer.valueOf(idGarantia));
			if(isEmbargo) {				
				listaTipoEmbargo.add(tipo1);
			} else {
				listaTipoEmbargo.add(tipo0);
			}
			
			regresa= Constants.SUCCESS;
		}catch(Exception e){
			regresa = Constants.FAILED;
			MyLogger.Logger.log(Level.SEVERE, e.getMessage(),e);
		}
		
		return regresa;
	}
	
	public String embargo() {
		String regresa = Constants.FAILED;
		
		try{
			Integer idGarantia =(Integer) sessionMap.get(mx.gob.se.rug.constants.Constants.ID_GARANTIA);
			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			
			MyLogger.Logger.log(Level.INFO, "tipoEmbargo::"+mdTipoEmbargo);			
			EmbargoService embargoService = new EmbargoServiceImpl();
			Integer idTramiteNuevo = null;
			if(mdTipoEmbargo.equalsIgnoreCase("1"))
				idTramiteNuevo =  embargoService.setEmbargo(idGarantia, usuario.getPersona().getIdPersona(), 13, getObservaciones());
			else 
				idTramiteNuevo =  embargoService.setEmbargo(idGarantia, usuario.getPersona().getIdPersona(), 17, getObservaciones());
			sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
			MyLogger.Logger.log(Level.INFO, "id tramite nuevo "+idTramiteNuevo);
			
			if (idTramiteNuevo.intValue() != 0){
				regresa= Constants.SUCCESS;			
			}			
		}catch(Exception e){
			e.printStackTrace();
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
	
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public List<TipoTo> getListaTipoEmbargo() {
		return listaTipoEmbargo;
	}

	public void setListaTipoEmbargo(List<TipoTo> listaTipoEmbargo) {
		this.listaTipoEmbargo = listaTipoEmbargo;
	}

	public String getMdTipoEmbargo() {
		return mdTipoEmbargo;
	}

	public void setMdTipoEmbargo(String mdTipoEmbargo) {
		this.mdTipoEmbargo = mdTipoEmbargo;
	}

	public String getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(String idGarantia) {
		this.idGarantia = idGarantia;
	}
}
