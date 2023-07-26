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
public class DeudorTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer iddeudor;
	private ComercianteTO comerciante;
	private Date fechaAltaDeudor;
	public ComercianteTO getComerciante() {
		return comerciante;
	}
	public void setComerciante(ComercianteTO comerciante) {
		this.comerciante = comerciante;
	}
	public Date getFechaAltaDeudor() {
		return fechaAltaDeudor;
	}
	public void setFechaAltaDeudor(Date fechaAltaDeudor) {
		this.fechaAltaDeudor = fechaAltaDeudor;
	}
	public void setIddeudor(Integer iddeudor) {
		this.iddeudor = iddeudor;
	}
	public Integer getIddeudor() {
		return iddeudor;
	}
	
	
}
