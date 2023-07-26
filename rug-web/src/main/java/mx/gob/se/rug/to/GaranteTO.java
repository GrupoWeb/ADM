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

public class GaranteTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer Idgarante;
	private ComercianteTO comerciante;
	private Date fechaAltaGarante;
	
	public ComercianteTO getComerciante() {
		return comerciante;
	}
	public void setComerciante(ComercianteTO comerciante) {
		this.comerciante = comerciante;
	}
	public Date getFechaAltaGarante() {
		return fechaAltaGarante;
	}
	public void setFechaAltaGarante(Date fechaAltaGarante) {
		this.fechaAltaGarante = fechaAltaGarante;
	}
	public void setIdgarante(Integer idgarante) {
		Idgarante = idgarante;
	}
	public Integer getIdgarante() {
		return Idgarante;
	}

	
	
}
