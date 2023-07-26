package mx.gob.se.rug.util.to;

import java.io.Serializable;

public class MunicipioTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idMunicipio;
	private String claveEstado;
	private String descMunicipio;
	private String clavePais;
	
	public Integer getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public String getDescMunicipio() {
		return descMunicipio;
	}
	public void setDescMunicipio(String descMunicipio) {
		this.descMunicipio = descMunicipio;
	}
	public void setClaveEstado(String claveEstado) {
		this.claveEstado = claveEstado;
	}
	public String getClaveEstado() {
		return claveEstado;
	}
	public void setClavePais(String clavePais) {
		this.clavePais = clavePais;
	}
	public String getClavePais() {
		return clavePais;
	}
	
	
}
