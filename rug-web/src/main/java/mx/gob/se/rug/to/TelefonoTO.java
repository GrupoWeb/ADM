package mx.gob.se.rug.to;

import java.io.Serializable;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @version 1.0
 * @category DataTransferObject
 */
public class TelefonoTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String codigoPais;
	private String extension;
	private String lada;
	private String numero;
	public String getCodigoPais() {
		return codigoPais;
	}
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getLada() {
		return lada;
	}
	public void setLada(String lada) {
		this.lada = lada;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
}
