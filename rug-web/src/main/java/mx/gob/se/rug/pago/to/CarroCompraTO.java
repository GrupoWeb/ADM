package mx.gob.se.rug.pago.to;

import java.io.Serializable;

public class CarroCompraTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer idTipoTramite; 
	private String descripcionTramite; 
	private Double precioUnitario; 
	private Integer cantidad;
	private Double precioTotal; 
	
	public Double getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(Double precioTotal) {
		this.precioTotal = precioTotal;
	}
	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}
	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	public String getDescripcionTramite() {
		return descripcionTramite;
	}
	public void setDescripcionTramite(String descripcionTramite) {
		this.descripcionTramite = descripcionTramite;
	}
	public Double getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	} 

}
