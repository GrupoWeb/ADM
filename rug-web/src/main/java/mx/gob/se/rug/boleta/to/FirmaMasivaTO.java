package mx.gob.se.rug.boleta.to;

import java.io.Serializable;
import java.util.List;

public class FirmaMasivaTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String acreedor;
	private String personaFirmo;
	private String asientos;
	private String nInscripciones;
	private List<Integer> listaTramites;
	
	private Integer idTramite;
	
	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public String getAcreedor() {
		return acreedor;
	}
	public void setAcreedor(String acreedor) {
		this.acreedor = acreedor;
	}
	public String getPersonaFirmo() {
		return personaFirmo;
	}
	public void setPersonaFirmo(String personaFirmo) {
		this.personaFirmo = personaFirmo;
	}
	public String getAsientos() {
		return asientos;
	}
	public void setAsientos(String asientos) {
		this.asientos = asientos;
	}
	public String getnInscripciones() {
		return nInscripciones;
	}
	public void setnInscripciones(String nInscripciones) {
		this.nInscripciones = nInscripciones;
	}
	public void setListaTramites(List<Integer> listaTramites) {
		this.listaTramites = listaTramites;
	}
	public List<Integer> getListaTramites() {
		return listaTramites;
	}
	
	
}
