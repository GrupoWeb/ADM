package mx.gob.se.rug.action;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.economia.dgi.framework.security.user.dto.User;
import mx.gob.se.rug.acceso.dao.AccesoDAO;
import mx.gob.se.rug.common.constants.Constants;
import mx.gob.se.rug.common.exception.UserException;
import mx.gob.se.rug.common.service.HomeService;
import mx.gob.se.rug.common.util.RequestContext;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.fwk.listener.RugSessionListener;
import mx.gob.se.rug.to.PersonaTO;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;


@SuppressWarnings("serial")
public class LayoutAction extends RugBaseAction implements ServletRequestAware {
	
	private static final String SUCCESS = "success";
	private static final String LOGEADO ="logeado";
	private static final String ERROR ="error";
	private HomeService homeService;
	public LayoutAction(){
		super();		
	}
	
	public String  portada() throws UserException{

		UsuarioTO u = new UsuarioTO();
		PersonaTO persona = new PersonaTO();
		AccesoDAO udao = new AccesoDAO();
		
		
		//NUEVA FUNCIONALIDAD
		
		User user = homeService.getUser(principal.getUserPrincipal().toString());
		
		MyLogger.Logger.log(Level.INFO, "IMPIRME EL NOMBRE 001: "+user.getProfile().getNombre());
		u.setNombre(principal.getUserPrincipal().toString());
		
		// ----------------- cookies ------------
//		Cookie myCookie = new Cookie(Constants.COOKIE_LOGEADO, "true");
//		myCookie.setMaxAge(-1);
//		response.addCookie(myCookie);
//		logger.debug("Subio la cookie: " + myCookie.getName());
		// ----------------- cookies ------------
		logger.debug(sessionMap);

		
		if(principal.getUserPrincipal().toString().equals(u.getNombre())){
			MyLogger.Logger.log(Level.INFO, " logeado ");
			PersonaFisica personaFisica = udao.getIdPersona(u.getNombre());
			
			persona.setIdPersona(personaFisica.getIdPersona());
			persona.setPerfil(personaFisica.getPerfil());
			persona.setCorreoElectronico(u.getNombre());
			
			u.setPersona(persona);
			sessionMap.put(mx.gob.se.rug.constants.Constants.USUARIO,u);
			MyLogger.Logger.log(Level.INFO, "IMPRIME NOMBE 4"+persona.getIdPersona());
			MyLogger.Logger.log(Level.INFO, "IMPRIME NOMBE 6"+u.getNombre());
			user.getProfile().setEmail(u.getNombre());
			sessionMap.put(Constants.SESSION_USER, user);
//			sessionMap.put("usuario", user.getProfile().getNombre());
			// buscar si la cuenta logeada es una cuenta maestra
			Boolean esAsmin = udao.checkUserAdmin(personaFisica.getIdPersona());
			if(esAsmin){
				sessionMap.put(Constants.MASTER_USER, esAsmin);
			}else{
				Boolean esMaestra = udao.getCuentaMaestra(personaFisica.getIdPersona());
				sessionMap.put(Constants.MASTER_USER, esMaestra);
			}
			
			return LOGEADO;
		}		
		return SUCCESS;		
		
	}
	
	public String logout() throws Exception {
		MyLogger.Logger.log(Level.INFO, "Invalidando la sesion.");
	

		if (principal != null && principal.getUserPrincipal() != null) {
			String userName = principal.getUserPrincipal().toString();
			MyLogger.Logger.log(Level.INFO, "userName: " + userName);
		}

		// ----------------- cookies ------------
//		Cookie myCookie = searchCookie(Constants.COOKIE_LOGEADO);
//		if (myCookie != null) {
//			myCookie.setMaxAge(0);
//			myCookie.setValue(null);
//			response.addCookie(myCookie);
//			MyLogger.Logger.log(Level.INFO,"Se elimino la cookie: " + myCookie);
//		}
		// ----------------- cookies ------------

		((SessionMap<String, Object>) sessionMap).invalidate();
		MyLogger.Logger.log(Level.INFO, "Sesion invalidada");
		return SUCCESS;
	}
	
//	private Cookie searchCookie(String cookieName) {
//		MyLogger.Logger.log(Level.INFO,"cookieName: " + cookieName);
//		MyLogger.Logger.log(Level.INFO,"__ cookies __");
//		HttpServletRequest request = ServletActionContext.getRequest();
//		MyLogger.Logger.log(Level.INFO,"cookies: " + request.getCookies());
//		for (Cookie cookie : request.getCookies()) {
//			MyLogger.Logger.log(Level.INFO,"cookie: " + cookie.getName());
//			if (cookieName.equals(cookie.getName())) {
//				MyLogger.Logger.log(Level.INFO,"regresara cookie: " + cookie.getName());
//				return cookie;
//			}
//		}
//		MyLogger.Logger.log(Level.INFO,"__ cookies __");
//		return null;
//	}
	
	public String  errorlogin(){
		return SUCCESS;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute(RugSessionListener.KEY_REQUESTURI, RequestContext.getAttribute(RequestContext.KEY_REQUESTURI));
		session.setAttribute(RugSessionListener.KEY_REMOTEADDR, RequestContext.getAttribute(RequestContext.KEY_REMOTEADDR));
	}

	public void setHomeService(HomeService homeService) {
		this.homeService = homeService;
	}

	public HomeService getHomeService() {
		return homeService;
	}

}
