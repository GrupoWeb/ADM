package mx.gob.se.rug.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.se.rug.to.UsuarioTO;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

@SuppressWarnings("serial")
public class RugLoginInterceptor  extends AbstractInterceptor{
	
	protected Log logger =LogFactory.getLog(RugLoginInterceptor.class);
	 
	public String intercept(ActionInvocation invocation) throws Exception	{
		
		logger.debug("INTERCEPTOR RUG LOGIN");

		final ActionContext context = invocation.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) context.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession(true);
		UsuarioTO usuario = (UsuarioTO) session.getAttribute("usuario");
		logger.debug("Usuario: "+ usuario );
		if (usuario == null) {
		
			logger.debug("REGRESA RUG LOGIN:" + Action.LOGIN);
			return Action.LOGIN;
		}
		return invocation.invoke();
	}
	
	
}