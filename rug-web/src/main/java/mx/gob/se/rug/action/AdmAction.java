package mx.gob.se.rug.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.context.annotation.Scope;

import gt.gob.rgm.model.Boleta;
import gt.gob.rgm.service.BoletaService;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;

@Scope("prototype")
public class AdmAction extends RugBaseAction implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	
	List<Boleta> boletas;
	
	BoletaService boletaSvc;

	public String contador(){
		String regresa = "success";
		boletas = boletaSvc.listBoletas();
		System.out.println("Boletas encontradas: " + boletas.size());
		
		return regresa;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}

	public BoletaService getBoletaSvc() {
		return boletaSvc;
	}

	public void setBoletaSvc(BoletaService boletaSvc) {
		this.boletaSvc = boletaSvc;
	}

	public List<Boleta> getBoletas() {
		return boletas;
	}

	public void setBoletas(List<Boleta> boletas) {
		this.boletas = boletas;
	}
}
