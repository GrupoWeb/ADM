package mx.gob.se.rug.modificacion.to;

import java.io.Serializable;

public class AutoridadInstruye implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String anotacionJuez;
	private Integer idTipoTramite;
	private String etiquetaTipoTramite;
	public String getAnotacionJuez() {
		return anotacionJuez;
	}
	public void setAnotacionJuez(String anotacionJuez) {
		this.anotacionJuez = anotacionJuez;
	}
	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}
	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	public String getEtiquetaTipoTramite() {
		
		switch(idTipoTramite){
		case 1: etiquetaTipoTramite="Persona que solicita o Autoridad que instruye la Inscripción y contenido de la Resolución ";break;
		case 2: etiquetaTipoTramite="Persona que solicita o Autoridad que instruye la Anotación";break;
		case 4: etiquetaTipoTramite="Persona que solicita o Autoridad que instruye la Cancelación y Resolución Judicial ";break;
		case 6: etiquetaTipoTramite="Persona que solicita o Autoridad que instruye la Rectificación y contenido de la Resolución";break;
		case 7: etiquetaTipoTramite="Persona que solicita o Autoridad que instruye la Modificación y contenido de la Resolución";break;
		case 8: etiquetaTipoTramite="Persona que solicita o Autoridad que instruye la Transmisión y contenido de la Resolución";break;
		case 9: etiquetaTipoTramite="Persona que solicita o Autoridad que instruye la Renovación o Reducción de la Vigencia ";break;
		default:etiquetaTipoTramite ="";break;
		}
		
		return etiquetaTipoTramite;
	}

	
	
	
	
	
	
	
	

}
