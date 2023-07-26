/**
 * 
 */
package mx.gob.se.rug.fwk.listener;

import java.util.logging.Level;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import mx.gob.economia.dgi.framework.security.user.dto.User;
import mx.gob.se.rug.bitacora.dto.BitacoraLogin;
import mx.gob.se.rug.common.constants.Constants;
import mx.gob.se.rug.util.MyLogger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Alfonso Esquivel
 * 
 */
public class RugSessionListener implements HttpSessionListener {

	public static final String KEY_REMOTEADDR;
	public static final String KEY_REQUESTURI;

	protected Log logger = LogFactory.getLog(RugSessionListener.class);
//	private BitacoraLoginService bitacoraLoginService;

	static {
		String base=RugSessionListener.class.getName();
		KEY_REMOTEADDR=base + "_remoteaddr";
		KEY_REQUESTURI=base + "_requesturi";
	}

//	public void setBitacoraLoginService(
//			BitacoraLoginService bitacoraLoginService) {
//		this.bitacoraLoginService = bitacoraLoginService;
//	}

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		logger.debug("Sesión creada");		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		MyLogger.Logger.log(Level.INFO, "Session destruida");
		logger.debug("Sesión destruida");
		HttpSession session = sessionEvent.getSession();
		User user = (User) session.getAttribute(Constants.SESSION_USER);

		if (user != null) {
//			WebApplicationContext applicationContext = ContextLoader
//					.getCurrentWebApplicationContext();
//			bitacoraLoginService = (BitacoraLoginService) applicationContext
//					.getBean("bitacoraLoginService");
			BitacoraLogin bitacoraLogin = new BitacoraLogin();
			bitacoraLogin.setCveUsuario(user.getCredentials().getPrincipal()
					.toString());
			bitacoraLogin.setComentario("Login de usuario");
			bitacoraLogin.setIdEvento(1);
			String remoteAddress=(String)session.getAttribute(KEY_REMOTEADDR);
			String requestUri=(String)session.getAttribute(KEY_REQUESTURI);
			bitacoraLogin.setRequestAddress(remoteAddress!=null? remoteAddress: "none");
			bitacoraLogin.setRequestURI(requestUri!=null? requestUri: "auto");

//			try {
//				logger.debug("bitacoraLoginService: " + bitacoraLoginService);
//				boolean guardado = bitacoraLoginService
//						.bitacoraLogin(bitacoraLogin);
//				if (guardado) {
//					logger.debug("Se guardo el login de usuario");
//				} else {
//					logger.warn("No se guardo el login de usuario");
//				}
//
//			} catch (UsuarioNoBitacoradoException e) {
//				logger.error("No se pudo guardar el login de usuario." + e);
//			}
		}
	}

}
