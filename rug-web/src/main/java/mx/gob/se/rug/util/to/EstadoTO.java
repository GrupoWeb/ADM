package mx.gob.se.rug.util.to;

import java.io.Serializable;

public class EstadoTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idEstado;
	private String descEstado;
	
	public String getDescEstado() {
		return descEstado;
	}
	public void setDescEstado(String descEstado) {
		this.descEstado = descEstado;
	}
	public String getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	
	
	
}
