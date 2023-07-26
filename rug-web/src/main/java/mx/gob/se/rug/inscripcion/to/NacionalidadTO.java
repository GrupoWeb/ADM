package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;

public class NacionalidadTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idNacionalidad;
	private String descNacionalidad;
	private String cvePais;
	public Integer getIdNacionalidad() {
		return idNacionalidad;
	}
	public void setIdNacionalidad(Integer idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}
	public String getDescNacionalidad() {
		return descNacionalidad;
	}
	public void setDescNacionalidad(String descNacionalidad) {
		this.descNacionalidad = descNacionalidad;
	}
	public void setCvePais(String cvePais) {
		this.cvePais = cvePais;
	}
	public String getCvePais() {
		return cvePais;
	}
	
	
	

}
