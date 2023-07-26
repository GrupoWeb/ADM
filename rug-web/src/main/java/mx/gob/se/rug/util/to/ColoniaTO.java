package mx.gob.se.rug.util.to;

import java.io.Serializable;

public class ColoniaTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idColonia;
	private String descColonia;
	
	public Integer getIdColonia() {
		return idColonia;
	}
	public void setIdColonia(Integer idColonia) {
		this.idColonia = idColonia;
	}
	public String getDescColonia() {
		return descColonia;
	}
	public void setDescColonia(String descColonia) {
		this.descColonia = descColonia;
	}
	
}
