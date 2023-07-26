package mx.gob.se.rug.administracion.dto;

import mx.gob.economia.dgi.framework.base.dto.AbstractBaseDTO;
import mx.gob.se.rug.dto.PersonaFisica;

@SuppressWarnings("serial")
public class Perfil extends AbstractBaseDTO {
	
	PersonaFisica personaFisica;
	RegistroUsuario registroUsuario;
	
	public PersonaFisica getPersonaFisica() {
		return personaFisica;
	}
	public void setPersonaFisica(PersonaFisica personaFisica) {
		this.personaFisica = personaFisica;
	}
	public RegistroUsuario getRegistroUsuario() {
		return registroUsuario;
	}
	public void setRegistroUsuario(RegistroUsuario registroUsuario) {
		this.registroUsuario = registroUsuario;
	}
	
}
