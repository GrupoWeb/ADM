package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the V_GARANTIA_PARTES database table.
 * 
 */
@Entity
@Table(name="V_GARANTIA_PARTES")
@NamedQuery(name="VGarantiaParte.findAll", query="SELECT v FROM VGarantiaParte v")
public class VGarantiaParte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CLAVE_PAIS")
	private String clavePais;

	private String curp;

	@Column(name="DESC_PARTE")
	private String descParte;

	@Column(name="E_MAIL")
	private String eMail;

	private String extension;

	@Column(name="FOLIO_MERCANTIL")
	private String folioMercantil;

	@Column(name="ID_GARANTIA")
	private BigDecimal idGarantia;

	@Column(name="ID_NACIONALIDAD")
	private BigDecimal idNacionalidad;

	@Column(name="ID_PARTE")
	private BigDecimal idParte;

	@Id
	@Column(name="ID_PERSONA")
	private BigDecimal idPersona;

	@Column(name="ID_TRAMITE")
	private BigDecimal idTramite;

	private String nombre;

	@Column(name="NUM_INSCRITA")
	private String numInscrita;

	@Column(name="PER_JURIDICA")
	private String perJuridica;

	private String rfc;

	private String telefono;

	@Column(name="UBICA_DOMICILIO_1")
	private String ubicaDomicilio1;

	public VGarantiaParte() {
	}

	public String getClavePais() {
		return this.clavePais;
	}

	public void setClavePais(String clavePais) {
		this.clavePais = clavePais;
	}

	public String getCurp() {
		return this.curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getDescParte() {
		return this.descParte;
	}

	public void setDescParte(String descParte) {
		this.descParte = descParte;
	}

	public String getEMail() {
		return this.eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFolioMercantil() {
		return this.folioMercantil;
	}

	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}

	public BigDecimal getIdGarantia() {
		return this.idGarantia;
	}

	public void setIdGarantia(BigDecimal idGarantia) {
		this.idGarantia = idGarantia;
	}

	public BigDecimal getIdNacionalidad() {
		return this.idNacionalidad;
	}

	public void setIdNacionalidad(BigDecimal idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}

	public BigDecimal getIdParte() {
		return this.idParte;
	}

	public void setIdParte(BigDecimal idParte) {
		this.idParte = idParte;
	}

	public BigDecimal getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(BigDecimal idPersona) {
		this.idPersona = idPersona;
	}

	public BigDecimal getIdTramite() {
		return this.idTramite;
	}

	public void setIdTramite(BigDecimal idTramite) {
		this.idTramite = idTramite;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNumInscrita() {
		return this.numInscrita;
	}

	public void setNumInscrita(String numInscrita) {
		this.numInscrita = numInscrita;
	}

	public String getPerJuridica() {
		return this.perJuridica;
	}

	public void setPerJuridica(String perJuridica) {
		this.perJuridica = perJuridica;
	}

	public String getRfc() {
		return this.rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUbicaDomicilio1() {
		return this.ubicaDomicilio1;
	}

	public void setUbicaDomicilio1(String ubicaDomicilio1) {
		this.ubicaDomicilio1 = ubicaDomicilio1;
	}

}