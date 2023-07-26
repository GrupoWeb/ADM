package mx.gob.se.rug.to;

import java.io.Serializable;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castaneda
 * @see GarantiaTO
 * @see CreditoTO
 * @see BienTO
 * @version 1.0
 * @category DataTransferObject
 */
public class ConstanciaRegistroGMTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private GarantiaTO garantia;
	private CreditoTO credito;
	private BienTO bien;
	
	public GarantiaTO getGarantia() {
		return garantia;
	}
	public void setGarantia(GarantiaTO garantia) {
		this.garantia = garantia;
	}
	public CreditoTO getCredito() {
		return credito;
	}
	public void setCredito(CreditoTO credito) {
		this.credito = credito;
	}
	public BienTO getBien() {
		return bien;
	}
	public void setBien(BienTO bien) {
		this.bien = bien;
	}
	
	
	
}
