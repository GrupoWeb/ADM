package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see GarantiaTO
 * @see PersonaTO
 * @see TramiteRUGTO
 * @version 1.0
 * @category DataTransferObject
 */
public class CancelacionGarantiaMTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private TramiteRUGTO tramiteRUG;
	private Date fechaCancelacion;
	private GarantiaTO garantia;
	private PersonaTO persona;
	public TramiteRUGTO getTramiteRUG() {
		return tramiteRUG;
	}
	public void setTramiteRUG(TramiteRUGTO tramiteRUG) {
		this.tramiteRUG = tramiteRUG;
	}
	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}
	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
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
	
	
}
