/*
 * PerfilAction.java        29/10/2009
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140, 
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y contiene información perteneciente
 * a la Secretaría de Economía.
 * 
 */
package mx.gob.se.rug.administracion.action;

import java.util.List;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.cipher.service.Cipher;
import mx.gob.economia.dgi.framework.common.domicilios.dto.Domicilio;
import mx.gob.economia.dgi.framework.regexp.Regexp;
import mx.gob.economia.dgi.framework.security.user.dto.User;
import mx.gob.economia.dgi.framework.util.ValidationUtils;
import mx.gob.se.rug.acreedores.service.AcreedoresCatalogosService;
import mx.gob.se.rug.administracion.dto.Perfil;
import mx.gob.se.rug.administracion.service.PerfilService;
import mx.gob.se.rug.administracion.service.UsuarioService;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.common.service.HomeService;
import mx.gob.se.rug.common.util.NullsFree;
import mx.gob.se.rug.fwk.action.RugBaseAction;
//import mx.gob.economia.dgi.tuempresa.administracion.dto.Auxiliar;
//import mx.gob.economia.dgi.tuempresa.administracion.dto.Perfil;
//import mx.gob.economia.dgi.tuempresa.administracion.service.PerfilService;
import mx.gob.se.rug.common.constants.Constants;
import mx.gob.se.rug.dto.PersonaFisica;
//import mx.gob.economia.dgi.tuempresa.common.dto.Mensaje;
//import mx.gob.economia.dgi.tuempresa.common.exception.UserException;
//import mx.gob.economia.dgi.tuempresa.common.service.HomeService;
//import mx.gob.economia.dgi.tuempresa.common.util.NullsFree;
//import mx.gob.economia.dgi.tuempresa.fedatario.exception.AuxiliaresException;
//import mx.gob.economia.dgi.tuempresa.fedatario.service.AuxiliaresService;
//import mx.gob.economia.dgi.tuempresa.fwk.action.TuEmpresaBaseAction;
import mx.gob.se.rug.to.UsuarioTO;
import mx.gob.se.rug.util.MyLogger;

/**
 * 
 * 
 * @version 0.1
 * @author Alfonso Esquivel
 * 
 */
@SuppressWarnings("serial")
public class PerfilAction extends RugBaseAction {

	private Perfil perfil;

	private Regexp regexp;
	private PerfilService perfilService;
	// private AuxiliaresService auxiliaresService;
	private HomeService homeService;
	private Cipher cipher;
	private PersonaFisica personaFisica;
	private AcreedoresCatalogosService acreedoresCatalogosService;
	private PersonaFisica personaFisicaRfc;
	private PersonaFisica personaFisicaRfcUsuario;
	private UsuarioService usuarioService;
	private String errorFirma;

	public void setPerfilService(PerfilService perfilService) {
		this.perfilService = perfilService;
	}

	public void setHomeService(HomeService homeService) {
		this.homeService = homeService;
	}

	public void setCipher(Cipher cipher) {
		this.cipher = cipher;
	}

	public void setRegexp(Regexp regexp) {
		this.regexp = regexp;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public String edit() throws Exception {
		MyLogger.Logger.log(Level.INFO, "-- edit --");
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		MyLogger.Logger.log(Level.INFO, "-- clave del usuario: --" + usuario.getNombre());
		personaFisica = getAcreedoresCatalogosService().getRegistro(
				usuario.getNombre());
		MyLogger.Logger.log(Level.INFO, "rfc: " + personaFisica.getRfc());
		return SUCCESS;
	}
	public String update() throws Exception {
		UsuarioTO usuario = (UsuarioTO) sessionMap.get("usuario");
		MyLogger.Logger.log(Level.INFO, "-- update --");
		MyLogger.Logger.log(Level.INFO, "perfil: " + perfil);
		MyLogger.Logger.log(Level.INFO, "clave usuario: " + usuario.getNombre());

		MyLogger.Logger.log(Level.INFO, "Nombre: " + personaFisica.getNombre());
		MyLogger.Logger.log(Level.INFO, "Ap Paterno: " + personaFisica.getApellidoPaterno());
		MyLogger.Logger.log(Level.INFO, "Ap Materno: " + personaFisica.getApellidoMaterno());

		String oldPassword = perfil.getRegistroUsuario().getOldPassword();
		String password = perfil.getRegistroUsuario().getPassword();
		MyLogger.Logger.log(Level.INFO, "oldpassword" + oldPassword);
		MyLogger.Logger.log(Level.INFO, "password" + password);
		// if (oldPassword.equals("")) {
		// MyLogger.Logger.log(Level.INFO,"ENTRA NUEVA VALIDACION");
		// addActionError(getText("perfil.password.formulario"));
		// return ERROR;
		// }

		if (!ValidationUtils.isNull(password)
				&& !ValidationUtils.isEmpty(password)) {
			// // cifra la cadena
			// perfil.getRegistroUsuario().setOldPassword(cipher.getCipherString(oldPassword));
			// perfil.getRegistroUsuario().setPassword(cipher.getCipherString(password));
			perfil.getRegistroUsuario().setOldPassword(oldPassword);
			perfil.getRegistroUsuario().setPassword(password);
			//
		} else {
			// asigna null
			perfil.getRegistroUsuario().setOldPassword(null);
			perfil.getRegistroUsuario().setPassword(null);
		}

		MyLogger.Logger.log(Level.INFO, "perfil: " + perfil);
		String claveUsuario = usuario.getNombre();

		MyLogger.Logger.log(Level.INFO, "--rfc enviado--: " + personaFisica.getRfc());
		MyLogger.Logger.log(Level.INFO, "--Email enviado--: "
				+ personaFisica.getDatosContacto().getEmailPersonal());

		personaFisicaRfcUsuario = perfilService
				.getConsultaRfcUsuario(personaFisica);
		personaFisicaRfc = usuarioService.getConsultaRfc(personaFisica);

		if (personaFisicaRfcUsuario.getRfc() != null) {
			// Se valida que exista el mismo registro con el rfc
			Mensaje mensaje = perfilService.update(perfil, claveUsuario,
					personaFisica);
			MyLogger.Logger.log(Level.INFO, "" + mensaje);
			if (mensaje.getRespuesta() == 0) {

				User user = homeService.getUser(principal.getUserPrincipal()
						.toString());
				sessionMap.put(Constants.SESSION_USER, user);
				addActionMessage(getText("perfil.msg.update.success"));
				return SUCCESS;
			}
			addActionError(mensaje.getMensaje());
			return ERROR;

		} else {
			// Se valida que exista otro NO exista este registro con rfc
			if (personaFisicaRfc.getRfc() != null) {
				// Se valida que exista otro registro direfente con el rfc
				addActionError(getText("usuario.msg.registro.rfc.error"));
			} else {
				Mensaje mensaje = perfilService.update(perfil, claveUsuario,
						personaFisica);
				MyLogger.Logger.log(Level.INFO, "" + mensaje);
				if (mensaje.getRespuesta() == 0) {

					User user = homeService.getUser(principal
							.getUserPrincipal().toString());
					sessionMap.put(Constants.SESSION_USER, user);
					addActionMessage(getText("perfil.msg.update.success"));
					return SUCCESS;
				}
				addActionError(mensaje.getMensaje());
				return ERROR;

			}
		}
		return ERROR;
	}

	public void validateUpdate() throws Exception {
		MyLogger.Logger.log(Level.INFO, "-- validaUpdate --");
		String oldPassword = NullsFree.getNotNullValue(
				perfil.getRegistroUsuario().getOldPassword()).trim();
		String password = NullsFree.getNotNullValue(
				perfil.getRegistroUsuario().getPassword()).trim();
		String confirmacion = NullsFree.getNotNullValue(
				perfil.getRegistroUsuario().getConfirmacion()).trim();

		if (!((oldPassword.equals("")) && (password.equals("")) && (confirmacion
				.equals("")))) {

			MyLogger.Logger.log(Level.WARNING, "Hay cambios en los passwords");

			// si hay cambios, verifica su validez a continuacion
			// *** Password anterior ***
			if (oldPassword.equals("")) {
				MyLogger.Logger.log(Level.WARNING, "Su contraseña anterior es requerida");
				addFieldError("perfil.registroUsuario.oldPassword",
						getText("perfil.password.anterior.required"));
			}

			// *** Password nuevo diferente al anterior ***
			if (oldPassword.equals(password)) {
				logger.debug("La nueva contraseña debe ser diferente a su nueva contraseña anterior");
				addFieldError("perfil.registroUsuario.password",
						getText("perfil.password.anterior.nueva.diferent"));
			}

			// *** Password ***
			if (!regexp.matches(Constants.REGEX, password)) {
				logger.debug("Su nueva contraseña debe tener una longitud de entre 8 y 16 "
						+ "caracteres, debe incluir al menos una letra mayúscula y al menos un número");
				addFieldError("perfil.registroUsuario.password",
						getText("perfil.password.nueva.invalid"));
			}

			// *** ConfPassword ***
			if (!password.trim().equals(confirmacion)) {
				logger.debug("La confirmacion de contraseña no es igual a su nueva contraseña.");
				addFieldError("perfil.registroUsuario.confirmacion",
						getText("perfil.password.nueva.confirmacion.diferent"));
			}
		} else {
			MyLogger.Logger.log(Level.WARNING, "no hay cambios en las contraseñas");
		}
	}

	public void setPersonaFisica(PersonaFisica personaFisica) {
		this.personaFisica = personaFisica;
	}

	public PersonaFisica getPersonaFisica() {
		return personaFisica;
	}

	public void setAcreedoresCatalogosService(
			AcreedoresCatalogosService acreedoresCatalogosService) {
		this.acreedoresCatalogosService = acreedoresCatalogosService;
	}

	public AcreedoresCatalogosService getAcreedoresCatalogosService() {
		return acreedoresCatalogosService;
	}

	public void setPersonaFisicaRfc(PersonaFisica personaFisicaRfc) {
		this.personaFisicaRfc = personaFisicaRfc;
	}

	public PersonaFisica getPersonaFisicaRfc() {
		return personaFisicaRfc;
	}

	public void setPersonaFisicaRfcUsuario(PersonaFisica personaFisicaRfcUsuario) {
		this.personaFisicaRfcUsuario = personaFisicaRfcUsuario;
	}

	public PersonaFisica getPersonaFisicaRfcUsuario() {
		return personaFisicaRfcUsuario;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setErrorFirma(String errorFirma) {
		this.errorFirma = errorFirma;
	}

	public String getErrorFirma() {
		return errorFirma;
	}

}
