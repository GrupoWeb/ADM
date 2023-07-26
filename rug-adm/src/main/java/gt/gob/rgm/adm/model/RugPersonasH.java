package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the RUG_PERSONAS_H database table.
 * 
 */
@Entity
@Table(name="RUG_PERSONAS_H")
@NamedQuery(name="RugPersonasH.findAll", query="SELECT r FROM RugPersonasH r")
public class RugPersonasH implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RugPersonasHPK id;

	@Column(name="AP_MATERNO")
	private String apMaterno;

	@Column(name="AP_PATERNO")
	private String apPaterno;

	private String curp;

	@Column(name="CURP_DOC")
	private String curpDoc;

	@Column(name="CVE_PERFIL")
	private String cvePerfil;

	@Column(name="DESC_PARTE")
	private String descParte;

	@Column(name="E_MAIL")
	private String eMail;

	@Column(name="FOLIO_MERCANTIL")
	private String folioMercantil;

	@Column(name="ID_NACIONALIDAD")
	private Integer idNacionalidad;

	@Column(name="NOMBRE_PERSONA")
	private String nombrePersona;

	@Column(name="PER_JURIDICA")
	private String perJuridica;

	@Column(name="RAZON_SOCIAL")
	private String razonSocial;

	private String rfc;

	public RugPersonasH() {
	}

	public RugPersonasHPK getId() {
		return this.id;
	}

	public void setId(RugPersonasHPK id) {
		this.id = id;
	}

	public String getApMaterno() {
		return this.apMaterno;
	}

	public void setApMaterno(String apMaterno) {
		this.apMaterno = apMaterno;
	}

	public String getApPaterno() {
		return this.apPaterno;
	}

	public void setApPaterno(String apPaterno) {
		this.apPaterno = apPaterno;
	}

	public String getCurp() {
		return this.curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getCurpDoc() {
		return this.curpDoc;
	}

	public void setCurpDoc(String curpDoc) {
		this.curpDoc = curpDoc;
	}

	public String getCvePerfil() {
		return this.cvePerfil;
	}

	public void setCvePerfil(String cvePerfil) {
		this.cvePerfil = cvePerfil;
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

	public String getFolioMercantil() {
		return this.folioMercantil;
	}

	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}

	public Integer getIdNacionalidad() {
		return this.idNacionalidad;
	}

	public void setIdNacionalidad(Integer idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}

	public String getNombrePersona() {
		return this.nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	public String getPerJuridica() {
		return this.perJuridica;
	}

	public void setPerJuridica(String perJuridica) {
		this.perJuridica = perJuridica;
	}

	public String getRazonSocial() {
		return this.razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRfc() {
		return this.rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

}