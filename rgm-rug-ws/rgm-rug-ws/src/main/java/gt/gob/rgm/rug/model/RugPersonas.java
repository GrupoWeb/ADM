package gt.gob.rgm.rug.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_PERSONAS database table.
 * 
 */
@Entity
@Table(name="RUG_PERSONAS")
@NamedQuery(name="RugPersona.findAll", query="SELECT r FROM RugPersonas r")
public class RugPersonas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_PERSONA")
	private long idPersona;

	@Column(name="CODIGO_REGISTRO")
	private Long codigoRegistro;

	@Column(name="CURP_DOC")
	private String curpDoc;

	@Column(name="CVE_NACIONALIDAD")
	private String cveNacionalidad;

	@Column(name="E_MAIL")
	private String eMail;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_INSCR_CC")
	private Date fechaInscrCc;

	@Temporal(TemporalType.DATE)
	@Column(name="FH_REGISTRO")
	private Date fhRegistro;

	@Column(name="FOLIO_MERCANTIL")
	private String folioMercantil;

	@Column(name="ID_DOMICILIO")
	private BigDecimal idDomicilio;

	@Column(name="ID_NACIONALIDAD")
	private BigDecimal idNacionalidad;

	@Column(name="ID_PERSONA_MODIFICAR")
	private BigDecimal idPersonaModificar;

	private String inscrito;

	private String nifp;

	@Column(name="PER_JURIDICA")
	private String perJuridica;

	private String procedencia;

	private String procesado;

	@Column(name="REG_TERMINADO")
	private String regTerminado;

	private String rfc;

	@Column(name="SIT_PERSONA")
	private String sitPersona;

	public RugPersonas() {
	}

	public long getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}

	public Long getCodigoRegistro() {
		return this.codigoRegistro;
	}

	public void setCodigoRegistro(Long codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}

	public String getCurpDoc() {
		return this.curpDoc;
	}

	public void setCurpDoc(String curpDoc) {
		this.curpDoc = curpDoc;
	}

	public String getCveNacionalidad() {
		return this.cveNacionalidad;
	}

	public void setCveNacionalidad(String cveNacionalidad) {
		this.cveNacionalidad = cveNacionalidad;
	}

	public String getEMail() {
		return this.eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public Date getFechaInscrCc() {
		return this.fechaInscrCc;
	}

	public void setFechaInscrCc(Date fechaInscrCc) {
		this.fechaInscrCc = fechaInscrCc;
	}

	public Date getFhRegistro() {
		return this.fhRegistro;
	}

	public void setFhRegistro(Date fhRegistro) {
		this.fhRegistro = fhRegistro;
	}

	public String getFolioMercantil() {
		return this.folioMercantil;
	}

	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}

	public BigDecimal getIdDomicilio() {
		return this.idDomicilio;
	}

	public void setIdDomicilio(BigDecimal idDomicilio) {
		this.idDomicilio = idDomicilio;
	}

	public BigDecimal getIdNacionalidad() {
		return this.idNacionalidad;
	}

	public void setIdNacionalidad(BigDecimal idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}

	public BigDecimal getIdPersonaModificar() {
		return this.idPersonaModificar;
	}

	public void setIdPersonaModificar(BigDecimal idPersonaModificar) {
		this.idPersonaModificar = idPersonaModificar;
	}

	public String getInscrito() {
		return this.inscrito;
	}

	public void setInscrito(String inscrito) {
		this.inscrito = inscrito;
	}

	public String getNifp() {
		return this.nifp;
	}

	public void setNifp(String nifp) {
		this.nifp = nifp;
	}

	public String getPerJuridica() {
		return this.perJuridica;
	}

	public void setPerJuridica(String perJuridica) {
		this.perJuridica = perJuridica;
	}

	public String getProcedencia() {
		return this.procedencia;
	}

	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

	public String getProcesado() {
		return this.procesado;
	}

	public void setProcesado(String procesado) {
		this.procesado = procesado;
	}

	public String getRegTerminado() {
		return this.regTerminado;
	}

	public void setRegTerminado(String regTerminado) {
		this.regTerminado = regTerminado;
	}

	public String getRfc() {
		return this.rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getSitPersona() {
		return this.sitPersona;
	}

	public void setSitPersona(String sitPersona) {
		this.sitPersona = sitPersona;
	}

}