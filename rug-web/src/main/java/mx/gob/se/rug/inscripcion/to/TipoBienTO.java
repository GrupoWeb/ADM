package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;

public class TipoBienTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer idTipoBien;
	private String descripcion;
	private String descripcionEN;
	private Integer idPadre;
	private String statusReg;
	public Integer getIdTipoBien() {
		return idTipoBien;
	}
	public void setIdTipoBien(Integer idTipoBien) {
		this.idTipoBien = idTipoBien;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDescripcionEN() {
		return descripcionEN;
	}
	public void setDescripcionEN(String descripcionEN) {
		this.descripcionEN = descripcionEN;
	}
	public Integer getIdPadre() {
		return idPadre;
	}
	public void setIdPadre(Integer idPadre) {
		this.idPadre = idPadre;
	}
	public String getStatusReg() {
		return statusReg;
	}
	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}
	
	
	
	
}
