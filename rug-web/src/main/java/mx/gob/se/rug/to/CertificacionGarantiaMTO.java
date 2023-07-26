
package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castaneda
 * @see GarantiaTO
 * @see PersonaTO
 * @see TramiteRUGTO
 * @version 1.0
 * @category DataTransferObject
 */

public class CertificacionGarantiaMTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private TramiteRUGTO tramiteRUG;
	private String firmaElectronica;
	private GarantiaTO garantia;
	private Date fechaCertificacion;
	private PersonaTO persona;
	public TramiteRUGTO getTramiteRUG() {
		return tramiteRUG;
	}
	public void setTramiteRUG(TramiteRUGTO tramiteRUG) {
		this.tramiteRUG = tramiteRUG;
	}
	public String getFirmaElectronica() {
		return firmaElectronica;
	}
	public void setFirmaElectronica(String firmaElectronica) {
		this.firmaElectronica = firmaElectronica;
	}
	public GarantiaTO getGarantia() {
		return garantia;
	}
	public void setGarantia(GarantiaTO garantia) {
		this.garantia = garantia;
	}
	public Date getFechaCertificacion() {
		return fechaCertificacion;
	}
	public void setFechaCertificacion(Date fechaCertificacion) {
		this.fechaCertificacion = fechaCertificacion;
	}
	public PersonaTO getPersona() {
		return persona;
	}
	public void setPersona(PersonaTO persona) {
		this.persona = persona;
	}
	
	
}
