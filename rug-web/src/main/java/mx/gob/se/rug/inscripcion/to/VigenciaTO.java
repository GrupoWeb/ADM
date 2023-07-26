package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;

public class VigenciaTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer meses;
	private String fechaInicio;
	private String fechaFin;
	
	public Integer getMeses() {
		return meses;
	}
	public void setMeses(Integer meses) {
		this.meses = meses;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	
}
