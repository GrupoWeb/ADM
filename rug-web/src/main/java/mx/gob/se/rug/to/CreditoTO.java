package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see ContratoTO
 * @see AcreedorTO
 * @see DeudorTO
 * @version 1.0
 * @category DataTransferObject
 */
public class CreditoTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private ContratoTO contrato;
	private double monto;
	private Date plazoVencimiento;
	private AcreedorTO acreedor;
	private DeudorTO deudor;
	
	public ContratoTO getContrato() {
		return contrato;
	}
	public void setContrato(ContratoTO contrato) {
		this.contrato = contrato;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public Date getPlazoVencimiento() {
		return plazoVencimiento;
	}
	public void setPlazoVencimiento(Date plazoVencimiento) {
		this.plazoVencimiento = plazoVencimiento;
	}
	public AcreedorTO getAcreedor() {
		return acreedor;
	}
	public void setAcreedor(AcreedorTO acreedor) {
		this.acreedor = acreedor;
	}
	public DeudorTO getDeudor() {
		return deudor;
	}
	public void setDeudor(DeudorTO deudor) {
		this.deudor = deudor;
	}
	
	
}
