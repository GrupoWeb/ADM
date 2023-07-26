/**
 * 
 */
package mx.gob.se.rug.administracion.service;

import java.util.List;

import mx.gob.se.rug.administracion.dto.RegistroUsuario;
import mx.gob.se.rug.administracion.exception.UsuarioException;
import mx.gob.se.rug.administracion.exception.UsuarioNoGuardadoException;
import mx.gob.se.rug.administracion.exception.UsuarioPasswordNoRecuperadoException;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.dto.PersonaFisica;
import mx.gob.se.rug.inscripcion.to.NacionalidadTO;

/**
 * @author Alfonso Esquivel
 * 
 */
public interface UsuarioService {
	
	public List<NacionalidadTO> getNacionalidades();

	public List<RegistroUsuario> getPreguntas(String lenguaje) throws UsuarioException;

	public Mensaje save(PersonaFisica personaFisica,
			RegistroUsuario registroUsuario) throws UsuarioNoGuardadoException;
	
	public Mensaje save(PersonaFisica personaFisica) throws UsuarioNoGuardadoException;
	
	public Mensaje recover(PersonaFisica personaFisica, RegistroUsuario registroUsuario) throws UsuarioPasswordNoRecuperadoException;
	
	public RegistroUsuario getPregunta(PersonaFisica personaFisica)throws UsuarioException;
	
	public Mensaje AltaTramite(PersonaFisica personaFisica, int tipoTramite)throws UsuarioException;
	
	public PersonaFisica getConsultaRfc(PersonaFisica personaFisica)throws UsuarioException;
	
	public PersonaFisica getConsultaDocId(PersonaFisica personaFisica) throws UsuarioException;
	
	public int altaExtranjero (PersonaFisica personaFisica, 
			RegistroUsuario registroUsuario);
	
}
