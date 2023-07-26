package mx.gob.se.rug.to;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see PersonaTO
 * @see GarantiaTO
 * @version 1.0
 * @category DataTransferObject
 */

public class AccionTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Date fecha;
	private PersonaTO usuario;
	private GarantiaTO garantia;
	private double importe;
	private String fechaRegistro;

	private String valor;

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * 
	 * @return Date
	 * @throws NoDateException
	 * @version 1.1
	 */
	public Date getFecha() {
		return fecha;
	}
	/**
	 * 
	 * @return Date
	 * @throws NoDateException
	 * @version 1.1
	 * @param fecha fecha en que se llevo acabo la accion
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public PersonaTO getUsuario() {
		return usuario;
	}
	public void setUsuario(PersonaTO usuario) {
		this.usuario = usuario;
	}
	public GarantiaTO getGarantia() {
		return garantia;
	}
	public void setGarantia(GarantiaTO garantia) {
		this.garantia = garantia;
	}
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
	}
	
}
