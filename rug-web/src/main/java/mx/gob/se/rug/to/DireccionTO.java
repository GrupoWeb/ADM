package mx.gob.se.rug.to;

import java.io.Serializable;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @version 1.0
 * @category DataTransferObject
 */

public class DireccionTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String calle;
	private String ciudad;
	private String codidoPostal;
	private String colonia;
	private String estado;
	private String minucipio;
	private String numeroExt;
	private String numeroInt;
	private PaisTO pais;
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getCodidoPostal() {
		return codidoPostal;
	}
	public void setCodidoPostal(String codidoPostal) {
		this.codidoPostal = codidoPostal;
	}
	public String getColonia() {
		return colonia;
	}
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getMinucipio() {
		return minucipio;
	}
	public void setMinucipio(String minucipio) {
		this.minucipio = minucipio;
	}
	public String getNumeroExt() {
		return numeroExt;
	}
	public void setNumeroExt(String numeroExt) {
		this.numeroExt = numeroExt;
	}
	public String getNumeroInt() {
		return numeroInt;
	}
	public void setNumeroInt(String numeroInt) {
		this.numeroInt = numeroInt;
	}
	public PaisTO getPais() {
		return pais;
	}
	public void setPais(PaisTO pais) {
		this.pais = pais;
	}
	
	
}
