package mx.gob.se.rug.rol.action;

import java.util.logging.Level;

import mx.gob.economia.dgi.framework.uuid.UUIDProvider;
import mx.gob.se.rug.administracion.dto.RegistroUsuario;
import mx.gob.se.rug.administracion.service.UsuarioService;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.constants.Constants;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.fwk.action.RugBaseAction;
import mx.gob.se.rug.util.MyLogger;

@SuppressWarnings("serial")
public class RolesAction extends RugBaseAction {
	
	private static final String OPERACION_I = "I";
	private RegistroUsuario registroUsuario;
	private UsuarioService usuarioService;
	private PersonaFisica personaFisica;
	
	
	public String captura() {
		return SUCCESS;
	}
	
	public String save() throws Exception {
		
		
		MyLogger.Logger.log(Level.INFO,"-- save Roles --");
		
		registroUsuario.setTipoOperacion(OPERACION_I);
		Mensaje mensaje = usuarioService.save(getPersonaFisica(),
				getRegistroUsuario());
		
		//ENVIAR MAIL
//		mailRegistroService.sendMailRegistro(personaFisica, registroUsuario);
		
		
		MyLogger.Logger.log(Level.INFO, "mensaje: " + mensaje);
		
		if (mensaje != null) {
			if (mensaje.getRespuesta() == 0) {
				addActionMessage(getText("usuario.msg.registro.success"));
				
				return SUCCESS;
			} else {
				addActionError(mensaje.getMensaje());
				return ERROR;
			}
		}
		addActionError(getText("usuario.msg.registro.error"));
	
		return ERROR;
	}

	public void setRegistroUsuario(RegistroUsuario registroUsuario) {
		this.registroUsuario = registroUsuario;
	}

	public RegistroUsuario getRegistroUsuario() {
		return registroUsuario;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setPersonaFisica(PersonaFisica personaFisica) {
		this.personaFisica = personaFisica;
	}

	public PersonaFisica getPersonaFisica() {
		return personaFisica;
	}


}
