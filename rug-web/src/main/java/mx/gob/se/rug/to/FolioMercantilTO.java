package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @version 1.0
 * @category DataTransferObject
 */

public class FolioMercantilTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Date fechaInscripcion;
	private String folioMercantil;
	public Date getFechaInscripcion() {
		return fechaInscripcion;
	}
	public void setFechaInscripcion(Date fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}
	public String getFolioMercantil() {
		return folioMercantil;
	}
	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}
	
}
