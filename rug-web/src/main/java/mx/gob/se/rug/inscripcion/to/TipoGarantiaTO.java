package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;

public class TipoGarantiaTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idTipoGarantia;
	private String descripcion;
	private String descripcionN;
	
	public Integer getIdTipoGarantia() {
		return idTipoGarantia;
	}
	public void setIdTipoGarantia(Integer idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDescripcionN() {
		return descripcionN;
	}
	public void setDescripcionN(String descripcionN) {
		this.descripcionN = descripcionN;
	}
	
	
	

}
