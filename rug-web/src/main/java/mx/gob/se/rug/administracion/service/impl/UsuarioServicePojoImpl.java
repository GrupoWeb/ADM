/**
 * 
 */
package mx.gob.se.rug.administracion.service.impl;

import java.util.List;
import java.util.logging.Level;

import mx.gob.economia.dgi.framework.base.service.AbstractBaseService;
import mx.gob.economia.dgi.framework.dao.exception.DaoException;
import mx.gob.economia.dgi.framework.exception.ExceptionMessage;
import mx.gob.se.rug.administracion.dao.UsuarioDao;
import mx.gob.se.rug.administracion.dto.RegistroUsuario;
import mx.gob.se.rug.administracion.exception.UsuarioException;
import mx.gob.se.rug.administracion.exception.UsuarioNoGuardadoException;
import mx.gob.se.rug.administracion.exception.UsuarioPasswordNoRecuperadoException;
import mx.gob.se.rug.administracion.service.UsuarioService;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.inscripcion.dao.NacionalidadDAO;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;
import mx.gob.se.rug.util.MyLogger;

/**
 * @author Alfonso Esquivel
 * 
 */
public class UsuarioServicePojoImpl extends AbstractBaseService implements
		UsuarioService {

	private UsuarioDao usuarioDao;

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public List<NacionalidadTO> getNacionalidades() {
		return new NacionalidadDAO().getNacionalidades();
	}

	@Override
	public List<RegistroUsuario> getPreguntas(String lenguaje)
			throws UsuarioException {
		try {
			return usuarioDao.getPreguntas(lenguaje);
		} catch (DaoException daoe) {
			throw new UsuarioException(
					new ExceptionMessage(
							"Ocurrio un error al tratar de obtener la lista de preguntas."),
					daoe);
		}
	}

	@Override
	public Mensaje save(PersonaFisica personaFisica,
			RegistroUsuario registroUsuario) throws UsuarioNoGuardadoException {
		try {
			return usuarioDao.save(personaFisica, registroUsuario);
		} catch (DaoException daoe) {
			throw new UsuarioNoGuardadoException(
					new ExceptionMessage(
							"Ocurrio un error al tratar de guardar los datos del usuario."),
					daoe);
		}
	}

	@Override
	public Mensaje save(PersonaFisica personaFisica)
			throws UsuarioNoGuardadoException {
		try {
			return usuarioDao.save(personaFisica);
		} catch (DaoException daoe) {
			throw new UsuarioNoGuardadoException(
					new ExceptionMessage(
							"Ocurrio un error al tratar de guardar los datos del usuario."),
					daoe);
		}
	}

	@Override
	public Mensaje recover(PersonaFisica personaFisica,
			RegistroUsuario registroUsuario)
			throws UsuarioPasswordNoRecuperadoException {
		try {
			return usuarioDao.recover(personaFisica, registroUsuario);
		} catch (DaoException daoe) {
			throw new UsuarioPasswordNoRecuperadoException(
					new ExceptionMessage(
							"Ocurrio un error al tratar de recuperar el password del usuario."),
					daoe);
		}
	}

	@Override
	public RegistroUsuario getPregunta(PersonaFisica personaFisica)
			throws UsuarioException {
		try {
			MyLogger.Logger.log(Level.INFO, "IMPRIME PREGUNTA : --"
					+ usuarioDao.getPregunta(personaFisica));
			return usuarioDao.getPregunta(personaFisica);
		} catch (DaoException gpte) {
			throw new UsuarioException(new ExceptionMessage(
					"Ocurrio un error al obtener la pregunta secreta"), gpte);
		}
	}

	@Override
	public Mensaje AltaTramite(PersonaFisica personaFisica, int tipoTramite)
			throws UsuarioException {
		try {
			MyLogger.Logger.log(Level.INFO, "IMPRIME PREGUNTA : --"
					+ usuarioDao.AltaTramite(personaFisica, tipoTramite));
			return usuarioDao.AltaTramite(personaFisica, tipoTramite);
		} catch (DaoException gpte) {
			throw new UsuarioException(new ExceptionMessage(
					"Ocurrio un error al obtener la pregunta secreta"), gpte);
		}
	}

	@Override
	public PersonaFisica getConsultaRfc(PersonaFisica personaFisica)
			throws UsuarioException {
		MyLogger.Logger.log(Level.INFO, "--metodo get consultaRfc --");
		try {
			return usuarioDao.getConsultaRfc(personaFisica);
		} catch (DaoException crfce) {
			throw new UsuarioException(new ExceptionMessage(
					"Ocurrio un error al obtener el rfc consultado"), crfce);
		}
	}
	
	@Override
	public PersonaFisica getConsultaDocId(PersonaFisica personaFisica) throws UsuarioException {
		MyLogger.Logger.log(Level.INFO, "--metodo get consultaDocId --");
		try {
			return usuarioDao.getConsultaDocId(personaFisica);
		} catch (DaoException crfce) {
			throw new UsuarioException(new ExceptionMessage("Ocurrio un error al obtener el Documento de identificacion consultado"), crfce);
		}
	}

	@Override
	public int altaExtranjero(PersonaFisica personaFisica,
			RegistroUsuario registroUsuario) {
		int regresa = usuarioDao.altaExtranjero(personaFisica, registroUsuario);
		if (regresa==0) {
			registroUsuario.setTipoOperacion("U");
			regresa = usuarioDao.altaExtranjero(personaFisica, registroUsuario);
		}
		return regresa;

	}

}
