package mx.gob.se.rug.administracion.dao;

import java.util.List;

import mx.gob.economia.dgi.framework.dao.exception.DaoException;
import mx.gob.se.rug.administracion.dto.RegistroUsuario;
import mx.gob.se.rug.administracion.exception.UsuarioException;
import mx.gob.se.rug.common.dto.Mensaje;
import mx.gob.se.rug.dto.PersonaFisica;

/**
 * @author Erika Astorga
 * 
 */
public interface UsuarioDao {

	public List<RegistroUsuario> getPreguntas(String lenguaje)
			throws DaoException;

	public Mensaje save(PersonaFisica personaFisica,
			RegistroUsuario registroUsuario) throws DaoException;

	public Mensaje save(PersonaFisica personaFisica) throws DaoException;

	public Mensaje recover(PersonaFisica personaFisica,
			RegistroUsuario registroUsuario) throws DaoException;

	public RegistroUsuario getPregunta(PersonaFisica personaFisica);
	
	public Mensaje AltaTramite(PersonaFisica personaFisica, int tipoTramite)throws UsuarioException;
	
	public PersonaFisica getConsultaRfc(PersonaFisica personaFisica)throws DaoException;
	
	public PersonaFisica getConsultaDocId(PersonaFisica personaFisica) throws DaoException;
	
	public int altaExtranjero(PersonaFisica personaFisica, RegistroUsuario registroUsuario);
	
}
