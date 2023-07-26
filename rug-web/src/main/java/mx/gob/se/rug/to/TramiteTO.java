package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;

import mx.gob.se.rug.garantia.to.GarantiaTO;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see PagoDerechosTO
 * @version 1.0
 * @category DataTransferObject
 */
public class TramiteTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private GarantiaTO garantiaTO;
	private PagoDerechosTO pagoDerechos;
	private Date fechaTramite;
	
	public PagoDerechosTO getPagoDerechos() {
		return pagoDerechos;
	}
	public void setPagoDerechos(PagoDerechosTO pagoDerechos) {
		this.pagoDerechos = pagoDerechos;
	}
	public Date getFechaTramite() {
		return fechaTramite;
	}
	public void setFechaTramite(Date fechaTramite) {
		this.fechaTramite = fechaTramite;
	}
	public void setGarantiaTO(GarantiaTO garantiaTO) {
		this.garantiaTO = garantiaTO;
	}
	public GarantiaTO getGarantiaTO() {
		return garantiaTO;
	}
	
	
}
