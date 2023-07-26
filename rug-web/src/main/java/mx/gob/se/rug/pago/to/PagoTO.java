package mx.gob.se.rug.pago.to;

import java.io.Serializable;

public class PagoTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer idTramite;
	private String tipoTramite;
	private String fechaCelebracion;
	private String fechaTermino;
	private Integer idGarantia;
	private String nombre;
	private String folioMercantil;
	private String status;
	private String descGeneral;
	private Double precio;
	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	
	public String getTipoTramite() {
		return tipoTramite;
	}
	public void setTipoTramite(String tipoTramite) {
		this.tipoTramite = tipoTramite;
	}
	public String getFechaCelebracion() {
		return fechaCelebracion;
	}
	public void setFechaCelebracion(String fechaCelebracion) {
		this.fechaCelebracion = fechaCelebracion;
	}
	public String getFechaTermino() {
		return fechaTermino;
	}
	public void setFechaTermino(String fechaTermino) {
		this.fechaTermino = fechaTermino;
	}
	public Integer getIdGarantia() {
		return idGarantia;
	}
	public void setIdGarantia(Integer idGarantia) {
		this.idGarantia = idGarantia;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFolioMercantil() {
		return folioMercantil;
	}
	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescGeneral() {
		return descGeneral;
	}
	public void setDescGeneral(String descGeneral) {
		this.descGeneral = descGeneral;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
}
