package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;

public class MonedaTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idMoneda;
	private String descMoneda;
	private String moneda;
	
	
	public Integer getIdMoneda() {
		return idMoneda;
	}
	public void setIdMoneda(Integer idMoneda) {
		this.idMoneda = idMoneda;
	}
	public String getDescMoneda() {
		return descMoneda;
	}
	public void setDescMoneda(String descMoneda) {
		this.descMoneda = descMoneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getMoneda() {
		return moneda;
	}
	
	
}
