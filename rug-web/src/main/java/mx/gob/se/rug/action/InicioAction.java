package mx.gob.se.rug.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;

public class InicioAction extends RugBaseAction implements ServletRequestAware {
	
	private static final String SUCCESS = "success";
	
	public InicioAction(){
		super();
	}
	
	public String login() {
		return SUCCESS;
	}
	
	public String inicio(){
		String regresa = "error";//se va a login
		try{
			if (sessionMap.get("usuario")!= null){
				regresa = "success";//se va a inicio
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return regresa;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}
}
