package gt.gob.rgm.adm.rs;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gt.gob.rgm.adm.annotation.SecuredResource;
import gt.gob.rgm.adm.domain.Principal;
import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;
import gt.gob.rgm.adm.model.Usuario;
import gt.gob.rgm.adm.service.BitacoraOperacionesService;
import gt.gob.rgm.adm.service.RugParametroConfService;
import gt.gob.rgm.adm.service.SecurityService;
import gt.gob.rgm.adm.service.UsuarioService;
import gt.gob.rgm.adm.util.Constants;
import gt.gob.rgm.adm.util.MailUtils;

@Path("/usuarios")
@RequestScoped
public class UsuariosRs {
	@Inject
	SecurityService securityService;
	
	@Inject
	UsuarioService usuarioService;
	
	@Inject
	BitacoraOperacionesService bitacoraService;
	
	@Inject
	RugParametroConfService parametroService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public List<Usuario> getAll() {
		return usuarioService.listUsuarios("A");
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@SecuredResource
	public Usuario create(@HeaderParam("Authorization") String authorization, Usuario usuario) {
		try {
			usuario = usuarioService.save(usuario);
			// enviar correo de cuenta creada
			String rol = usuario.getRol().equals("R") ? "Registrador(a)" : (usuario.getRol().equals("V") ? "Validador(a) de cuentas" : "Contador(a)");
			int idTipoMensaje = 1;
			int idAccountSmtp = Integer.valueOf(parametroService.getParam(Constants.ID_SMTP_MAIL_REGISTRO_USUARIOS).getValorParametro());
			String to = usuario.getEmail();
			String cc = null;
			String cco = null;
			String subject = parametroService.getParam(Constants.MAIL_SUBJECT_CUENTA_ADM).getValorParametro();
			String message = parametroService.getParam(Constants.MAIL_THEME_CUENTA_ADM).getValorParametro();
			subject = subject.replace("@nombreCompleto", usuario.getNombre());
			message = message.replace("@nombreCompleto", usuario.getNombre());
			message = message.replace("@email", usuario.getEmail());
			message = message.replace("@rol", rol);
			message = message.replace("@password", usuario.getPassword());
			try {
				MailUtils.getMailServiceInstance().sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco, subject, message);
			} catch(Exception e) {
				// si el envio del correo falla, se captura la excepcion pero continua el proceso
		    	e.printStackTrace();
			}
			usuario.setPassword("");
			
			// agregar a bitacora
			String token = authorization.split(" ")[1];
			bitacoraService.createEntry(token, "Se ha creado el usuario [" + usuario.getUsuarioId() + "] " + usuario.getNombre() + ", " + usuario.getEmail());
		} catch(EntityAlreadyExistsException e) {
			Response response = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
			throw new WebApplicationException(response);
		}
		return usuario;
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Usuario update(@HeaderParam("Authorization") String authorization, @PathParam(value="id") Long id, Usuario usuario) {
		String emailAnterior = usuarioService.getUsuario(id).getEmail();
		int updated = usuarioService.update(id, usuario);
		// si el correo es diferente, enviar correo de actualizacion
		if(usuario.getEmail() != null && !emailAnterior.equals(usuario.getEmail())) {
			String rol = usuario.getRol().equals("R") ? "Registrador(a)" : (usuario.getRol().equals("V") ? "Validador(a) de cuentas" : "Contador(a)");
			int idTipoMensaje = 1;
			int idAccountSmtp = Integer.valueOf(parametroService.getParam(Constants.ID_SMTP_MAIL_REGISTRO_USUARIOS).getValorParametro());
			String to = usuario.getEmail();
			String cc = null;
			String cco = null;
			String subject = parametroService.getParam(Constants.MAIL_SUBJECT_CUENTA_ADM).getValorParametro();
			String message = parametroService.getParam(Constants.MAIL_THEME_CUENTA_ADM).getValorParametro();
			subject = subject.replace("@nombreCompleto", usuario.getNombre());
			message = message.replace("@nombreCompleto", usuario.getNombre());
			message = message.replace("@email", usuario.getEmail());
			message = message.replace("@rol", rol);
			message = message.replace("@password", usuario.getPassword());
			try {
				MailUtils.getMailServiceInstance().sendMail(idTipoMensaje, idAccountSmtp, to, cc, cco, subject, message);
			} catch(Exception e) {
				// si el envio del correo falla, se captura la excepcion pero continua el proceso
		    	e.printStackTrace();
			}
		}
		// agregar a bitacora
		String token = authorization.split(" ")[1];
		bitacoraService.createEntry(token, "Se ha modificado al usuario [" + usuario.getUsuarioId() + "] " + usuario.getNombre() + ", " + usuario.getEmail());
		return usuario;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Usuario getUsuario(@PathParam(value="id") Long id) {
		return usuarioService.getUsuario(id);
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Principal login(Principal principal) {
		return securityService.login(principal);
	}
	
	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Principal logout(Principal principal) {
		// TODO: implementar cierre de sesion
		return null;
	}	
}
