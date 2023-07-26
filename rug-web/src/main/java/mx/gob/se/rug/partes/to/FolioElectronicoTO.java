package mx.gob.se.rug.partes.to;

import java.io.Serializable;

public class FolioElectronicoTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer idPersona;
	private String folioElectronico;
	
	public Integer getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	public String getFolioElectronico() {
		return folioElectronico;
	}
	public void setFolioElectronico(String folioElectronico) {
		this.folioElectronico = folioElectronico;
	}
	
	
}
