package mx.gob.se.rug.to;

import java.io.Serializable;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castaneda
 * @version 1.0
 * @category DataTransferObject
 */
public class TipoBienTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String descripcion;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
