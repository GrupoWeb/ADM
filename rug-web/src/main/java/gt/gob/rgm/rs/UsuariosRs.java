package gt.gob.rgm.rs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import gt.gob.rgm.model.RechazoCuenta;
import gt.gob.rgm.service.RechazoCuentaService;
import mx.gob.se.rug.acreedores.exception.AcreedoresException;
import mx.gob.se.rug.acreedores.service.AcreedoresCatalogosService;
import mx.gob.se.rug.administracion.exception.UsuarioNoGuardadoException;
import mx.gob.se.rug.administracion.service.UsuarioService;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.util.MyLogger;

@Component
@Path("/usuarios")
public class UsuariosRs {
	
	private AcreedoresCatalogosService acreedoresCatalogosService;
	private UsuarioService usuarioService;
	private RechazoCuentaService rechazoService;
	
	private PersonaFisica personaFisica;
	
	@GET
	@Path("/activar")
	public Response activarUsuario(@QueryParam(value="token") String token) throws URISyntaxException {
		acreedoresCatalogosService = (AcreedoresCatalogosService) SpringApplicationContext.getBean("acreedoresCatalogosService");
		usuarioService = (UsuarioService) SpringApplicationContext.getBean("usuarioService");
		String respuesta = token;
		// buscar a la persona por el token
		System.out.println("Token");
		System.out.println("Token" + token);

		try {

			personaFisica = acreedoresCatalogosService.getRegistroByToken(token);
			MyLogger.Logger.log(Level.INFO, "Persona fisica: " + personaFisica);
			if(personaFisica == null || personaFisica.getIdPersona() == null) {
				respuesta = "El token ingresado no existe o ya fue utilizado.";
			} else {
				MyLogger.Logger.log(Level.WARNING, "--no activa--");
				Mensaje mensaje = usuarioService.save(personaFisica);
				MyLogger.Logger.log(Level.INFO, "mensaje DAO ACTIVATION: " + mensaje);
				if (mensaje != null) {
					if (mensaje.getRespuesta() == 0) {
						// Su cuenta ha sido activada exitosamente
//						return Response.temporaryRedirect(new URI("/rug-web/usuario/activated.do")).build();
						return Response.temporaryRedirect(new URI("/Rug/usuario/activated.do")).build();
					} else {
						respuesta = "Ha ocurrido un error al activar su cuenta.";
					}
				}
			}
		} catch (AcreedoresException e) {
			e.printStackTrace();
			respuesta = "El token ingresado no existe o ya fue utilizado.";
		} catch (UsuarioNoGuardadoException e) {
			e.printStackTrace();
			respuesta = "Ha ocurrido un error al activar su cuenta.";
		}
		
		/*
		addActionError(getText("usuario.msg.activacion.error"));
		 */
		MyLogger.Logger.log(Level.INFO, "salio del try");
//		return Response.temporaryRedirect(new URI("/rug-web/usuario/activatedError.do")).build();
		return Response.temporaryRedirect(new URI("/Rug/usuario/activatedError.do")).build();
	}
	
	@GET
	@Path("/rectificar")
	public Response rectificarUsuario(@QueryParam(value="token") String token, @Context HttpServletRequest request) throws URISyntaxException {
		rechazoService = (RechazoCuentaService) SpringApplicationContext.getBean("rechazoCuentaService");
		// obtener la razon de rechazo basada en el token
		RechazoCuenta rechazo = rechazoService.getRechazoByToken(token);

		request.getSession().setAttribute("rechazo", rechazo);
		// redirect a pantalla de rectificacion
		return Response.temporaryRedirect(new URI("/Rug/usuario/ammend.do")).build();
	}

	public AcreedoresCatalogosService getAcreedoresCatalogosService() {
		return acreedoresCatalogosService;
	}

	public void setAcreedoresCatalogosService(AcreedoresCatalogosService acreedoresCatalogosService) {
		this.acreedoresCatalogosService = acreedoresCatalogosService;
	}
}
