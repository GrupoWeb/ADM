package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see TramiteRUGTO
 * @see TipoConsultaTO
 * @see GarantiaTO
 * @see PersonaTO
 * @version 1.0
 * @category DataTransferObject
 */
public class ConsultaGarantiaMTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private TramiteRUGTO tramiteRUG;
	private TipoConsultaTO tipoConsulta;
	private List <GarantiaTO> garantias;
	private Date fechaConsulta;
	private PersonaTO persona;
	
	public TramiteRUGTO getTramiteRUG() {
		return tramiteRUG;
	}
	public void setTramiteRUG(TramiteRUGTO tramiteRUG) {
		this.tramiteRUG = tramiteRUG;
	}
	public TipoConsultaTO getTipoConsulta() {
		return tipoConsulta;
	}
	public void setTipoConsulta(TipoConsultaTO tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	public List<GarantiaTO> getGarantias() {
		return garantias;
	}
	public void setGarantias(List<GarantiaTO> garantias) {
		this.garantias = garantias;
	}
	public Date getFechaConsulta() {
		return fechaConsulta;
	}
	public void setFechaConsulta(Date fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}
	public PersonaTO getPersona() {
		return persona;
	}
	public void setPersona(PersonaTO persona) {
		this.persona = persona;
	}
	
	
}
