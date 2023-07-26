package gt.gob.rgm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the RUG_PERSONAS database table.
 * 
 */
@Entity
@Table(name="RUG_PERSONAS")
@NamedQuery(name="RugPersonas.findAll", query="SELECT r FROM RugPersonas r")
public class RugPersonas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RUGPERSONAS_IDPERSONA_GENERATOR", sequenceName="SEQ_RUG_ID_PERSONA", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUGPERSONAS_IDPERSONA_GENERATOR")
	@Column(name="ID_PERSONA")
	private long idPersona;

	@Column(name="CODIGO_REGISTRO")
	private BigDecimal codigoRegistro;

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
	private Long idNacionalidad;

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

	//bi-directional many-to-one association to RelUsuAcreedor
	@OneToMany(mappedBy="rugPersona")
	private List<RelUsuAcreedor> relUsuAcreedors;

	//bi-directional one-to-one association to RugPersonasFisica
	@OneToOne(mappedBy="rugPersona")
	private RugPersonasFisicas rugPersonasFisica;

	//bi-directional many-to-one association to RugRelGrupoAcreedor
	@OneToMany(mappedBy="acreedor")
	private List<RugRelGrupoAcreedor> rugRelGrupoAcreedors1;

	//bi-directional many-to-one association to RugRelGrupoAcreedor
	@OneToMany(mappedBy="usuario")
	private List<RugRelGrupoAcreedor> rugRelGrupoAcreedors2;

	//bi-directional many-to-one association to RugRelGrupoAcreedor
	@OneToMany(mappedBy="subUsuario")
	private List<RugRelGrupoAcreedor> rugRelGrupoAcreedors3;

	public RugPersonas() {
	}

	public long getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}

	public BigDecimal getCodigoRegistro() {
		return this.codigoRegistro;
	}

	public void setCodigoRegistro(BigDecimal codigoRegistro) {
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

	public Long getIdNacionalidad() {
		return this.idNacionalidad;
	}

	public void setIdNacionalidad(Long idNacionalidad) {
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

	public List<RelUsuAcreedor> getRelUsuAcreedors() {
		return this.relUsuAcreedors;
	}

	public void setRelUsuAcreedors(List<RelUsuAcreedor> relUsuAcreedors) {
		this.relUsuAcreedors = relUsuAcreedors;
	}

	public RelUsuAcreedor addRelUsuAcreedor(RelUsuAcreedor relUsuAcreedor) {
		getRelUsuAcreedors().add(relUsuAcreedor);
		relUsuAcreedor.setRugPersona(this);

		return relUsuAcreedor;
	}

	public RelUsuAcreedor removeRelUsuAcreedor(RelUsuAcreedor relUsuAcreedor) {
		getRelUsuAcreedors().remove(relUsuAcreedor);
		relUsuAcreedor.setRugPersona(null);

		return relUsuAcreedor;
	}

	public RugPersonasFisicas getRugPersonasFisica() {
		return this.rugPersonasFisica;
	}

	public void setRugPersonasFisica(RugPersonasFisicas rugPersonasFisica) {
		this.rugPersonasFisica = rugPersonasFisica;
	}

	public List<RugRelGrupoAcreedor> getRugRelGrupoAcreedors1() {
		return this.rugRelGrupoAcreedors1;
	}

	public void setRugRelGrupoAcreedors1(List<RugRelGrupoAcreedor> rugRelGrupoAcreedors1) {
		this.rugRelGrupoAcreedors1 = rugRelGrupoAcreedors1;
	}

	public RugRelGrupoAcreedor addRugRelGrupoAcreedors1(RugRelGrupoAcreedor rugRelGrupoAcreedors1) {
		getRugRelGrupoAcreedors1().add(rugRelGrupoAcreedors1);
		rugRelGrupoAcreedors1.setAcreedor(this);

		return rugRelGrupoAcreedors1;
	}

	public RugRelGrupoAcreedor removeRugRelGrupoAcreedors1(RugRelGrupoAcreedor rugRelGrupoAcreedors1) {
		getRugRelGrupoAcreedors1().remove(rugRelGrupoAcreedors1);
		rugRelGrupoAcreedors1.setAcreedor(null);

		return rugRelGrupoAcreedors1;
	}

	public List<RugRelGrupoAcreedor> getRugRelGrupoAcreedors2() {
		return this.rugRelGrupoAcreedors2;
	}

	public void setRugRelGrupoAcreedors2(List<RugRelGrupoAcreedor> rugRelGrupoAcreedors2) {
		this.rugRelGrupoAcreedors2 = rugRelGrupoAcreedors2;
	}

	public RugRelGrupoAcreedor addRugRelGrupoAcreedors2(RugRelGrupoAcreedor rugRelGrupoAcreedors2) {
		getRugRelGrupoAcreedors2().add(rugRelGrupoAcreedors2);
		rugRelGrupoAcreedors2.setUsuario(this);

		return rugRelGrupoAcreedors2;
	}

	public RugRelGrupoAcreedor removeRugRelGrupoAcreedors2(RugRelGrupoAcreedor rugRelGrupoAcreedors2) {
		getRugRelGrupoAcreedors2().remove(rugRelGrupoAcreedors2);
		rugRelGrupoAcreedors2.setUsuario(null);

		return rugRelGrupoAcreedors2;
	}

	public List<RugRelGrupoAcreedor> getRugRelGrupoAcreedors3() {
		return this.rugRelGrupoAcreedors3;
	}

	public void setRugRelGrupoAcreedors3(List<RugRelGrupoAcreedor> rugRelGrupoAcreedors3) {
		this.rugRelGrupoAcreedors3 = rugRelGrupoAcreedors3;
	}

	public RugRelGrupoAcreedor addRugRelGrupoAcreedors3(RugRelGrupoAcreedor rugRelGrupoAcreedors3) {
		getRugRelGrupoAcreedors3().add(rugRelGrupoAcreedors3);
		rugRelGrupoAcreedors3.setSubUsuario(this);

		return rugRelGrupoAcreedors3;
	}

	public RugRelGrupoAcreedor removeRugRelGrupoAcreedors3(RugRelGrupoAcreedor rugRelGrupoAcreedors3) {
		getRugRelGrupoAcreedors3().remove(rugRelGrupoAcreedors3);
		rugRelGrupoAcreedors3.setSubUsuario(null);

		return rugRelGrupoAcreedors3;
	}

}