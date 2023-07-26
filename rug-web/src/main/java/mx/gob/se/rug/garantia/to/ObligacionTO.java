package mx.gob.se.rug.garantia.to;

import java.io.Serializable;

public class ObligacionTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String tipoGarantia;
	private String fechaCelebracion;
	private String fechaTerminacion;
	private String otrosTerminos;
	private String tipoActoContrato;
	
	
	public String getTipoGarantia() {
		return tipoGarantia;
	}
	public void setTipoGarantia(String tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}
	public String getFechaCelebracion() {
		return fechaCelebracion;
	}
	public void setFechaCelebracion(String fechaCelebracion) {
		this.fechaCelebracion = fechaCelebracion;
	}
	public String getFechaTerminacion() {
		return fechaTerminacion;
	}
	public void setFechaTerminacion(String fechaTerminacion) {
		this.fechaTerminacion = fechaTerminacion;
	}
	public String getOtrosTerminos() {
		return otrosTerminos;
	}
	public void setOtrosTerminos(String otrosTerminos) {
		this.otrosTerminos = otrosTerminos;
	}
	public void setTipoActoContrato(String tipoActoContrato) {
		this.tipoActoContrato = tipoActoContrato;
	}
	public String getTipoActoContrato() {
		return tipoActoContrato;
	}
	
	
}
