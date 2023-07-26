package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see TramiteRUGTO
 * @see PersonaTO
 * @see GarantiaTO
 * @version 1.0
 * @category DataTransferObject
 */
public class ModificacionGarantiaMTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private TramiteRUGTO tramiteRUG;
	private GarantiaTO garantia;
	private PersonaTO persona;
	private Date fechaModificacion;
	
	public TramiteRUGTO getTramiteRUG() {
		return tramiteRUG;
	}
	public void setTramiteRUG(TramiteRUGTO tramiteRUG) {
		this.tramiteRUG = tramiteRUG;
	}
	public GarantiaTO getGarantia() {
		return garantia;
	}
	public void setGarantia(GarantiaTO garantia) {
		this.garantia = garantia;
	}
	public PersonaTO getPersona() {
		return persona;
	}
	public void setPersona(PersonaTO persona) {
		this.persona = persona;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	
}
