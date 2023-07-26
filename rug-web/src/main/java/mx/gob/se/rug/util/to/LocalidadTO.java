package mx.gob.se.rug.util.to;

import java.io.Serializable;

public class LocalidadTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idLocalidad;
	private String descLocalidad;
	
	public int getIdLocalidad() {
		return idLocalidad;
	}
	public void setIdLocalidad(int idLocalidad) {
		this.idLocalidad = idLocalidad;
	}
	public String getDescLocalidad() {
		return descLocalidad;
	}
	public void setDescLocalidad(String descLocalidad) {
		this.descLocalidad = descLocalidad;
	}
	
}
