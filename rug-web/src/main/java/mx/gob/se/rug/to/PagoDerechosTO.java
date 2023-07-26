package mx.gob.se.rug.to;

import java.io.Serializable;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castaneda
 * @version 1.0
 * @category DataTransferObject
 */
public class PagoDerechosTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private double costo;

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}
	
}
