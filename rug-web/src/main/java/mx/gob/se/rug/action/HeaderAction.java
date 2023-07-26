package mx.gob.se.rug.action;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.interceptor.ServletRequestAware;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

public class HeaderAction extends RugBaseAction implements ServletRequestAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nomUSuario;
	
	public String inicioLogo(){
		String regresa = "error";//se va a login
		try{
			if (sessionMap.get("usuario")!= null){
				regresa = "success";//se va a busqueda
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}
	
	public String closeSession(){
		String regresa = "error";
		try{
			sessionMap.remove("usuario");
			regresa = "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		return regresa;
	}

	public String inicia(){
		String regresa= "error";
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		MyLogger.Logger.log(Level.INFO, "el nombre del usuario en session: " +usuario.getNombre());
		setNomUSuario(usuario.getNombre());
		regresa="error";
		return regresa;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}

	public String getNomUSuario() {
		return nomUSuario;
	}

	public void setNomUSuario(String nomUSuario) {
		this.nomUSuario = nomUSuario;
	}
	
}
