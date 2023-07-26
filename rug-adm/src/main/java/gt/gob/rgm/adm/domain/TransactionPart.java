package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class TransactionPart implements Serializable {
	private int idgarantia;
	private int idpersona;
	private String describe;
	private String perjuridica;
	private String nombre;
	private String foliomercantil;
	private String rfc;
	private String curp;
	private String email;
	private String telefono;
	private String ext;
	private String clave;
	
	public TransactionPart() {
	}

	public int getIdgarantia() {
		return idgarantia;
	}

	public void setIdgarantia(int idgarantia) {
		this.idgarantia = idgarantia;
	}

	public int getIdpersona() {
		return idpersona;
	}

	public void setIdpersona(int idpersona) {
		this.idpersona = idpersona;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getPerjuridica() {
		return perjuridica;
	}

	public void setPerjuridica(String perjuridica) {
		this.perjuridica = perjuridica;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFoliomercantil() {
		return foliomercantil;
	}

	public void setFoliomercantil(String foliomercantil) {
		this.foliomercantil = foliomercantil;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
}
