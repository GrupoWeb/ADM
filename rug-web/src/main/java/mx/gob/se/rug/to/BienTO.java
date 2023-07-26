package mx.gob.se.rug.to;

import java.io.Serializable;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see TipoBienTO
 * @version 1.0
 * @category DataTransferObject
 */
public class BienTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private TipoBienTO tipoBien;
	private String descripcion;
	public TipoBienTO getTipoBien() {
		return tipoBien;
	}
	public void setTipoBien(TipoBienTO tipoBien) {
		this.tipoBien = tipoBien;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
