package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see TramiteTO
 * @see ConstanciaRegistroGMTO
 * @see AccionTO
 * @version 1.0
 * @category DataTransferObject
 */
public class TramiteRUGTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private TramiteTO tramite;
	private ConstanciaRegistroGMTO constancia;
	private Date fechaInscripcion;
	private Date fechaPreInscripcion;
	private AccionTO accion;
	public TramiteTO getTramite() {
		return tramite;
	}
	public void setTramite(TramiteTO tramite) {
		this.tramite = tramite;
	}
	public ConstanciaRegistroGMTO getConstancia() {
		return constancia;
	}
	public void setConstancia(ConstanciaRegistroGMTO constancia) {
		this.constancia = constancia;
	}
	public Date getFechaInscripcion() {
		return fechaInscripcion;
	}
	public void setFechaInscripcion(Date fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}
	public Date getFechaPreInscripcion() {
		return fechaPreInscripcion;
	}
	public void setFechaPreInscripcion(Date fechaPreInscripcion) {
		this.fechaPreInscripcion = fechaPreInscripcion;
	}
	public AccionTO getAccion() {
		return accion;
	}
	public void setAccion(AccionTO accion) {
		this.accion = accion;
	}
	
	
}
