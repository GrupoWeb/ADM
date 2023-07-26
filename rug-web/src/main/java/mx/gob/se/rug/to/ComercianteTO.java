package mx.gob.se.rug.to;

import java.io.Serializable;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castaneda
 * @see FolioMercantilTO
 * @see PersonaTO
 * @version 1.0
 * @category DataTransferObject
 */
public class ComercianteTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private FolioMercantilTO folioMercantil;
	private PersonaTO persona;
	public FolioMercantilTO getFolioMercantil() {
		return folioMercantil;
	}
	public void setFolioMercantil(FolioMercantilTO folioMercantil) {
		this.folioMercantil = folioMercantil;
	}
	public PersonaTO getPersona() {
		return persona;
	}
	public void setPersona(PersonaTO persona) {
		this.persona = persona;
	}
	
	
}
