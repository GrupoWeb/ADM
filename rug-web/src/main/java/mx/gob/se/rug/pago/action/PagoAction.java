package mx.gob.se.rug.pago.action;

import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.garantia.dao.GarantiasDAO;
import mx.gob.se.rug.pago.dao.PagoDAO;
import mx.gob.se.rug.pago.to.PagoTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.interceptor.ServletRequestAware;

public class PagoAction extends RugBaseAction implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	private static final String SUCCESS = "success";
	private static final String FAILED = "failed";

	private String idTramite;
	private List<PagoTO> pagoTOs;
	private Integer idTipoTramite;
	public String iniciaPago() {
		String regresa = FAILED;
		try {
			PagoDAO pagoDAO = new PagoDAO();
			UsuarioTO usuarioTO = (UsuarioTO) sessionMap.get(Constants.USUARIO);
			MyLogger.Logger.log(Level.INFO, "idtramite var strut:::"+idTramite);
			
			if (idTramite == null) {
				Integer idTramiteNuevo = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
				MyLogger.Logger.log(Level.INFO, "idTramiteNuevo"+idTramiteNuevo);
				setIdTramite(idTramiteNuevo.toString());
			}else{
				sessionMap.put(Constants.ID_TRAMITE_NUEVO,new Integer( idTramite));
			}
			
			if (idTramite == null) {

				setPagoTOs(pagoDAO.getPendientePago(usuarioTO));
				MyLogger.Logger.log(Level.INFO, "no id tramite imprime todo");
			} else {
				MyLogger.Logger.log(Level.WARNING, "Id Tramite a pagar ::" + idTramite);
				PagoTO pagoTO = new PagoTO();
				pagoTO.setIdTramite(new Integer(getIdTramite()));
				setPagoTOs(pagoDAO.getPendientePago(usuarioTO, pagoTO));

			}
			idTramite=null;
			regresa = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public String validarPago() {
		String regresa = FAILED;
		try {
			MyLogger.Logger.log(Level.INFO, "validaPago");
			GarantiasDAO garantiasDAO = new GarantiasDAO();
			Integer idTramiteNuevo = (Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO);
			garantiasDAO.altaBitacoraTramite(new Integer(idTramiteNuevo),
					new Integer(5), new Integer(0), "", "V");
			MyLogger.Logger.log(Level.WARNING, "ID del tramite : " + idTramiteNuevo);
			PagoDAO pagoDAO = new PagoDAO();
			setIdTipoTramite(pagoDAO.getTipoTramiteByIdTramitePendiente((Integer)sessionMap.get(Constants.ID_TRAMITE_NUEVO))+200);
			idTramite=null;
			regresa = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public String validarFirma() {
		String regresa = FAILED;
		MyLogger.Logger.log(Level.WARNING, "validafrima");
		try {
			if(idTramite==null){
				MyLogger.Logger.log(Level.WARNING, "ID del tramite nuevo: "	+ ((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO)).toString());	
			}else{
				sessionMap.put(Constants.ID_TRAMITE_NUEVO, idTramite);
			}
			PagoDAO pagoDAO = new PagoDAO();
			setIdTipoTramite(pagoDAO.getTipoTramiteByIdTramitePendiente((Integer)sessionMap.get(Constants.ID_TRAMITE_NUEVO))+200);
			
			regresa = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}

	public String firma() {
		String regresa = FAILED;
		idTramite=null;
		try {
			Integer idTramiteNuevo = ((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
			MyLogger.Logger.log(Level.WARNING, "ID del tramite firmar: " + idTramiteNuevo);
			
			PagoDAO pagoDAO = new PagoDAO();
			pagoDAO.firmaTramite(new Integer(idTramiteNuevo), "");
			regresa = SUCCESS;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}
	public String firmaApplet() {
		String regresa = FAILED;
		idTramite=null;
		try {
			Integer idTramiteNuevo = ((Integer) sessionMap.get(Constants.ID_TRAMITE_NUEVO));
			MyLogger.Logger.log(Level.WARNING, "ID del tramite firmar: " + idTramiteNuevo);
			regresa = SUCCESS;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regresa;
	}
	// G&S----------------------------

	public List<PagoTO> getPagoTOs() {
		return pagoTOs;
	}

	public void setPagoTOs(List<PagoTO> pagoTOs) {
		this.pagoTOs = pagoTOs;
	}

	public void setIdTramite(String idTramite) {
		this.idTramite = idTramite;
	}

	public String getIdTramite() {
		return idTramite;
	}
	
	

	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}

	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext
				.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext
				.getAttribute(RequestContext.KEY_REMOTEADDR));
	}

}
