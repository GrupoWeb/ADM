package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @version 1.0
 * @category DataTransferObject
 */
public class ContratoTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int Idcontrato;
	private Date fechaCelebracion;
	private Date fechaTermino;
	private String observacion;
	private Double monto;
	
	
	public Date getFechaCelebracion() {
		return fechaCelebracion;
	}
	public void setFechaCelebracion(Date fechaCelebracion) {
		this.fechaCelebracion = fechaCelebracion;
	}
	public Date getFechaTermino() {
		return fechaTermino;
	}
	public void setFechaTermino(Date fechaTermino) {
		this.fechaTermino = fechaTermino;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public void setIdcontrato(int idcontrato) {
		Idcontrato = idcontrato;
	}
	public int getIdcontrato() {
		return Idcontrato;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Double getMonto() {
		return monto;
	}
	
	
}
