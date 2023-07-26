package mx.gob.se.rug.BusquedaCertificacion.to;

import java.util.Date;
import java.util.List;

import mx.gob.se.rug.inscripcion.to.OtorganteTO;

public class BusquedaCertTO {
	private Integer idTramTem;
	private Integer numOperacion;
	private String tipoOp;
	private Date fechaInsc;	
	private Double precio;
	private Integer numGarantia;
	private List<OtorganteTO> otorgantes;	
	
	public Integer getNumGarantia() {
		return numGarantia;
	}
	public void setNumGarantia(Integer numGarantia) {
		this.numGarantia = numGarantia;
	}
	public Integer getIdTramTem() {
		return idTramTem;
	}
	public void setIdTramTem(Integer idTramTem) {
		this.idTramTem = idTramTem;
	}
	public Integer getNumOperacion() {
		return numOperacion;
	}
	public void setNumOperacion(Integer numOperacion) {
		this.numOperacion = numOperacion;
	}
	public String getTipoOp() {
		return tipoOp;
	}
	public void setTipoOp(String tipoOp) {
		this.tipoOp = tipoOp;
	}
	public Date getFechaInsc() {
		return fechaInsc;
	}
	public void setFechaInsc(Date fechaInsc) {
		this.fechaInsc = fechaInsc;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public void setOtorgantes(List<OtorganteTO> otorgantes) {
		this.otorgantes = otorgantes;
	}
	public List<OtorganteTO> getOtorgantes() {
		return otorgantes;
	}

}
