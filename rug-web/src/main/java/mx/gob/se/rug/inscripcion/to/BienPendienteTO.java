package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;

public class BienPendienteTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer idGarntiaPendiente;
	private Integer idTipoBien;
	private String descripcion;
	public Integer getIdGarntiaPendiente() {
		return idGarntiaPendiente;
	}
	public void setIdGarntiaPendiente(Integer idGarntiaPendiente) {
		this.idGarntiaPendiente = idGarntiaPendiente;
	}
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
	
	
}
