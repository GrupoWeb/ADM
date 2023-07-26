package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see ComercianteTO
 * @version 1.0
 * @category DataTransferObject
 */
public class AcreedorTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private ComercianteTO comerciante;
	private Date fechaAltaAcreedor;
	public ComercianteTO getComerciante() {
		return comerciante;
	}
	public void setComerciante(ComercianteTO comerciante) {
		this.comerciante = comerciante;
	}
	public Date getFechaAltaAcreedor() {
		return fechaAltaAcreedor;
	}
	public void setFechaAltaAcreedor(Date fechaAltaAcreedor) {
		this.fechaAltaAcreedor = fechaAltaAcreedor;
	}
	
	
}
