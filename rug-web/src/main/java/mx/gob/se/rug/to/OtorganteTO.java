package mx.gob.se.rug.to;

import java.util.Date;

public class OtorganteTO {
	private Integer idotorgante;
	private ComercianteTO comerciante;
	private Date fechaAltaOtorgante;
	
	public ComercianteTO getComerciante() {
		return comerciante;
	}
	public void setComerciante(ComercianteTO comerciante) {
		this.comerciante = comerciante;
	}
	public Date getFechaAltaOtorgante() {
		return fechaAltaOtorgante;
	}
	public void setFechaAltaOtorgante(Date fechaAltaOtorgante) {
		this.fechaAltaOtorgante = fechaAltaOtorgante;
	}
	public void setIdotorgante(Integer idotorgante) {
		this.idotorgante = idotorgante;
	}
	public Integer getIdotorgante() {
		return idotorgante;
	}
}
