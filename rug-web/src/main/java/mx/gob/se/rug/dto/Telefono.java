package mx.gob.se.rug.dto;

/**
 * 
 */

import mx.gob.economia.dgi.framework.base.dto.AbstractBaseDTO;

/**
 * @author Alfonso Esquivel
 *
 */
@SuppressWarnings("serial")
public class Telefono extends AbstractBaseDTO {
	
	private String cvePais;
	private String lada;
	private String numero;
	private String extension;
	
	public String getCvePais() {
		return cvePais;
	}
	public void setCvePais(String cvePais) {
		this.cvePais = cvePais;
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
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getExtension() {
		return extension;
	}
	
	public void setTelefono(String value){
		if(value!=null && !value.equals("")){
			if(value.contains("(") &&  value.contains(")")){
				this.lada=value.substring(1, value.indexOf(")"));
				this.numero=value.substring(value.indexOf(")")+1, value.length());
			}else{
				this.numero=value;
			}
		}
	}

}

