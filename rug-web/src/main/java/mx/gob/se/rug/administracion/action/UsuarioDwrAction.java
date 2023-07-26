package mx.gob.se.rug.administracion.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gt.gob.rgm.domain.Usuario;
import gt.gob.rgm.model.RugGruposRoles;
import gt.gob.rgm.service.UsuariosService;
import mx.gob.economia.dgi.framework.dwr.action.AbstractBaseDwrAction;
import mx.gob.se.rug.administracion.dto.RegistroUsuario;
import mx.gob.se.rug.administracion.exception.UsuarioException;
import mx.gob.se.rug.administracion.service.UsuarioService;
import mx.gob.se.rug.dto.DatosContacto;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.to.MessageDwr;
import java.util.Date;

public class UsuarioDwrAction extends AbstractBaseDwrAction {
	
	private UsuarioService usuarioService;
	
	UsuariosService usuariosBusinessService;

	
	public MessageDwr getSecurityQuestion(String email) {
		MessageDwr response = new MessageDwr();
		
		PersonaFisica personaFisica = new PersonaFisica();
		DatosContacto datosContacto = new DatosContacto();
		datosContacto.setEmailPersonal(email);
		personaFisica.setDatosContacto(datosContacto);
		
		RegistroUsuario registroUsuarioRecupera;
		try {
			registroUsuarioRecupera = usuarioService.getPregunta(personaFisica);
			if(registroUsuarioRecupera == null || registroUsuarioRecupera.getTipoCuenta() == null) {
				response.setCodeError(1);
				response.setMessage("El usuario no es válido, verifique la cuenta de correo.");
			} else {
				if(registroUsuarioRecupera.getTipoCuenta().equals("P")) {
					// es cuenta principal
					if (registroUsuarioRecupera.getPregunta() != null) {
						response.setCodeError(0);
						response.setMessage("Es un usuario válido, puede continuar.");
						ObjectMapper mapper = new ObjectMapper();
						
						try {
							response.setData(mapper.writeValueAsString(registroUsuarioRecupera.getPregunta()));
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
					} else {
						response.setCodeError(1);
						response.setMessage("El usuario no es válido, verifique la cuenta de correo.");
					}
				} else {
					// es subcuenta
					response.setCodeError(0);
					response.setMessage("Es un usuario válido, puede continuar.");
					ObjectMapper mapper = new ObjectMapper();
					
					try {
						response.setData(mapper.writeValueAsString("Escriba el correo de la cuenta principal"));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (UsuarioException e1) {
			e1.printStackTrace();
			response.setCodeError(1);
			response.setMessage("Hubo un error en la validacion del usuario.");
		}

		return response;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setUsuariosBusinessService(UsuariosService usuariosBusinessService) {
		this.usuariosBusinessService = usuariosBusinessService;
	}

	public MessageDwr guardaSubusuario(String isAdmin, Long idPersona, String nombre, String docId, String email, String password, String pregunta, String otraPregunta, String respuesta) {
		// obtener el usuario de la persona logeada
		Usuario usuarioLogeado = usuariosBusinessService.getUsuario(idPersona);
		
		Usuario usuario = new Usuario();
		usuario.setIdNacionalidad(1L);
		usuario.setTipoPersona("PF");
		usuario.setInscritoComo("N");
		usuario.setDocId(docId);
		usuario.setNombre(nombre);
		usuario.setCveInstitucion("SE");
		usuario.setCveUsuario(email);
		usuario.setPassword(password);
		usuario.setIsAdmin(isAdmin);
		//usuario.setPregRecupera(pregunta);
		//usuario.setRespRecupera(respuesta);
		if(usuarioLogeado.getIsAdmin().equals("Administrador")){
			usuario.setCveUsuarioPadre(usuarioLogeado.getCveUsuarioPadre());
		}else{
			usuario.setCveUsuarioPadre(usuarioLogeado.getCveUsuario());
		}
		usuario.setCveAcreedor(usuarioLogeado.getCveUsuario());
		if(usuarioLogeado.getIsJudicial()!=null && usuarioLogeado.getIsJudicial().equalsIgnoreCase("1")) {
			usuario.setIdGrupo(13L); // grupo SUBDELEGADO JUDICIAL
			usuario.setIsJudicial(usuarioLogeado.getIsJudicial());
		}

		usuario.setFirmado("Y");
		usuario.setCvePerfil("CIUDADANO");
		usuario.setCveAplicacion("RugPortal");
		usuario.setIdPersonaPadre(idPersona);
		
		Usuario usuarioCreado = usuariosBusinessService.add(usuario);
		
		MessageDwr response = new MessageDwr();
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			if(usuarioCreado.getMensajeError() != null && !usuarioCreado.getMensajeError().isEmpty()) {
				response.setCodeError(-1);
				response.setMessage(usuarioCreado.getMensajeError());
			}
			if(isAdmin.equals("1")){
				usuarioCreado.setIsAdmin("Administrador");
			}else{
				usuarioCreado.setIsAdmin("SubUsuario");
			}
			response.setData(mapper.writeValueAsString(usuarioCreado));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public MessageDwr actualizaSubusuario(String isAdmin, Long idPersona, Long usuarioModificar, String nombre, String docId, String email, String password, String pregunta, String otraPregunta, String respuesta) {
		// obtener el usuario de la persona logeada
		//Usuario usuarioLogeado = usuariosBusinessService.getUsuario(idPersona);
		
		Usuario usuario = new Usuario();
		usuario.setDocId(docId);
		usuario.setNombre(nombre);
		usuario.setCveUsuario(email);
		usuario.setPassword(password);
		usuario.setSitUsuario("AC");
		usuario.setIdPersona(usuarioModificar);
		usuario.setIsAdmin(isAdmin);

		
		int resultado = usuariosBusinessService.update(usuarioModificar, usuario);
//		int resultado = 0;
//		System.out.println("Resultado " + resultado);
		
		MessageDwr response = new MessageDwr();
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			if(resultado == 0) {
				response.setCodeError(-1);
				response.setMessage("No se pudo modificar al usuario");
			}
			usuario.setIsAdmin(usuario.getIsAdmin().isEmpty() ? "SubUsuario": "Administrador");
			response.setData(mapper.writeValueAsString(usuario));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public MessageDwr eliminaSubusuario(Long idPersona) {
		Usuario usuarioEliminar = usuariosBusinessService.getUsuario(idPersona);
		usuarioEliminar.setSitUsuario("IN");
		
		int resultado = usuariosBusinessService.updateState(idPersona, usuarioEliminar.getSitUsuario());
		
		MessageDwr response = new MessageDwr();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(resultado == 0) {
				response.setCodeError(-1);
				response.setMessage("No se pudo eliminar al usuario");
			}
			response.setData(mapper.writeValueAsString(usuarioEliminar));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public String getUsuario(Long id) {
		ObjectMapper mapper = new ObjectMapper();
		String strUsuario = "";
		
		try {
			strUsuario = mapper.writeValueAsString(usuariosBusinessService.getUsuario(id));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return strUsuario;
	}
}
