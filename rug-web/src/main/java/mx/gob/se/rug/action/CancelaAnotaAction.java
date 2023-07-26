package mx.gob.se.rug.action;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.anotacion.serviceimpl.AnotacionSinGarantiaSerImpl;
import mx.gob.se.rug.cancelacionAnotacionSNGarantia.service.CancelacionAnotacionSNGarantiaService;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.to.AnotacionSinGarantiaTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.interceptor.ServletRequestAware;

public class CancelaAnotaAction extends RugBaseAction implements
		ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private String autoridad;
	private Integer idTram;
	
	public String cancelacionAnota() {
		String regresa = "failed";
		if (getAutoridad() != null && !getAutoridad().equals("")) {
			regresa = "success";
			System.out.println("regresa = "+regresa);
		}
		return regresa;
	}
	
	public String iniciaCancelacionAnSinGa() {
		return "success";
	}

	//Ya no va este metod
//	public String cancelacionVal() {
//		MyLogger.Logger.log(Level.INFO,
//				"CancelaAnotaAtion.cancelacionVal: entra al metodo de cancelacion del tramite el primero ");
//		String regresa = "failed";
//		try {
//			CancelacionService cancelacionService = new CancelacionService();
//			UsuarioTO usuario = (UsuarioTO) sessionMap.get(Constants.USUARIO);
//			Integer idPersona = usuario.getPersona().getIdPersona();
//			Integer idTramiteNuevo = cancelacionService.creaTramiteTem(
//					idPersona, 4);
//			sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramiteNuevo);
//			if (idTramiteNuevo.intValue() != 0) {
//				regresa = "success";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			regresa = "failed";
//		}
//
//		return regresa;
//	}

	public String cancelacionTramite() throws NoDataFoundException {
		String regresa="failed";
		MyLogger.Logger.log(Level.INFO,
				"entra al metodo de CancelaAnotaAction.cancelacionTramite() ");
		
		AnotacionSinGarantiaTO anota = new AnotacionSinGarantiaTO();
		
		AnotacionSinGarantiaSerImpl anotacion= new AnotacionSinGarantiaSerImpl();
		
		Integer idTraAnota=(Integer) sessionMap.get(Constants.ID_TRAMITE);
		
		System.out.println("CancelaAnotaAction.cancelacionTramite() : " +idTraAnota);
		
		CancelacionAnotacionSNGarantiaService cancelar = new CancelacionAnotacionSNGarantiaService();
		
	//		anota=anotacion.getDatosAnotacionSinGarantia(idTraAnota);
			
			System.out.println(" " +anota.getIdTramite()+"\n "+
			anota.getIdTramite()+"\n "+
			anota.getDescripcionTram()+"\n "+
			anota.getAnotacion()+"\n "+
			anota.getAutoridad()+"\n "+
			anota.getIdUsuario()+"\n "+
			anota.getTipoPersona()+"\n "+
			anota.getFolioMercantil()+"\n "+
			anota.getAnotacion()+"\n "+
			anota.getNombre()+"\n "+
			anota.getaPaterno()+"\n "+
			anota.getaMaterno()+"\n "+
			anota.getRazonSocial()+"\n "+
			anota.getMeses());
			
			//Integer meses = Integer.parseInt(anota.getMeses());
			Integer meses = (Integer) anota.getMeses();
			Integer idUsua = (Integer) anota.getIdUsuario();
			//tipo tramite = 22
			anota.getIdTramite();
			anota.getAnotacion();
			getAutoridad();
			anota.getMeses();
			
			//cancelar.cancelar(176221, idTramiteTemporal, IdAnotacion, autoInst, Anotacion, VigenciaAnotacion)
			
			if(cancelar.cancelar(idUsua, 22, anota.getIdTramite(), getAutoridad(),anota.getAnotacion(), meses)){
				regresa="success";
			}
			
			System.out.println("El regresa: "+regresa);
		return regresa;
	}

	public String cancelarAnotacion() {
		
		System.out.println("mandar algo aqui");
		return "success";
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

	public Integer getIdTram() {
		return idTram;
	}

	public void setIdTram(Integer idTram) {
		this.idTram = idTram;
	}

	
}
