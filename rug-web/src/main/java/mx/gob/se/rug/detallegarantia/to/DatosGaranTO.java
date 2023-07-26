package mx.gob.se.rug.detallegarantia.to;

import java.util.Date;

public class DatosGaranTO {
	private Date fechaInsc;
	private Date fechUltAsiento;
	private Date fechaAsiento;
	private String tipoAsiento;
	private Integer idTipoAsiento;
	public Date getFechaInsc() {
		return fechaInsc;
	}
	public void setFechaInsc(Date fechaInsc) {
		this.fechaInsc = fechaInsc;
	}
	public Date getFechUltAsiento() {
		return fechUltAsiento;
	}
	public void setFechUltAsiento(Date fechUltAsiento) {
		this.fechUltAsiento = fechUltAsiento;
	}
	public Date getFechaAsiento() {
		return fechaAsiento;
	}
	public void setFechaAsiento(Date fechaAsiento) {
		this.fechaAsiento = fechaAsiento;
	}
	public String getTipoAsiento() {
		return tipoAsiento;
	}
	public void setTipoAsiento(String tipoAsiento) {
		this.tipoAsiento = tipoAsiento;
	}
	public Integer getIdTipoAsiento() {
		return idTipoAsiento;
	}
	public void setIdTipoAsiento(Integer idTipoAsiento) {
		this.idTipoAsiento = idTipoAsiento;
	}
	
	

}
