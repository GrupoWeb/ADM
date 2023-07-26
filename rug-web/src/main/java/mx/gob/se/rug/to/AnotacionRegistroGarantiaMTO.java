package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see TramiteTO
 * @see GarantiaTO
 * @see PersonaTO
 * @see TramiteRUGTO
 * @version 1.0
 * @category DataTransferObject
 */

public class AnotacionRegistroGarantiaMTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private TramiteRUGTO tramiteRUG;
	private GarantiaTO garantia;
	private Date fechaAnotacion;
	private PersonaTO persona;
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
	public Date getFechaAnotacion() {
		return fechaAnotacion;
	}
	public void setFechaAnotacion(Date fechaAnotacion) {
		this.fechaAnotacion = fechaAnotacion;
	}
	public PersonaTO getPersona() {
		return persona;
	}
	public void setPersona(PersonaTO persona) {
		this.persona = persona;
	}
	
	
}
