package mx.gob.se.rug.tipogarantia.to;

import java.io.Serializable;

public class TipoGarantiaTO implements Serializable { 
	private static final long serialVersionUID = 1L;
	
	private Integer idtipogarantia;
	private String descripcion;
	private String descripcionen;
	
	public Integer getIdtipogarantia() {
		return idtipogarantia;
	}
	public void setIdtipogarantia(Integer idtipogarantia) {
		this.idtipogarantia = idtipogarantia;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDescripcionen() {
		return descripcionen;
	}
	public void setDescripcionen(String descripcionen) {
		this.descripcionen = descripcionen;
	}
	
	

}
