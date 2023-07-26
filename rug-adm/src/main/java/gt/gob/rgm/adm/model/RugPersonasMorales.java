package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_PERSONAS_MORALES database table.
 * 
 */
@Entity
@Table(name="RUG_PERSONAS_MORALES")
@NamedQuery(name="RugPersonasMorales.findAll", query="SELECT r FROM RugPersonasMorales r")
public class RugPersonasMorales implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_PERSONA")
	private long idPersona;

	@Column(name="B_CONSEJO_ADMON")
	private String bConsejoAdmon;

	@Column(name="B_EXCLUSION_EXTRANJEROS")
	private String bExclusionExtranjeros;

	@Column(name="CVE_PAIS_ORIGEN")
	private String cvePaisOrigen;

	@Column(name="CVE_USUARIO_REGISTRO")
	private String cveUsuarioRegistro;

	@Temporal(TemporalType.DATE)
	@Column(name="F_CONSTITUCION")
	private Date fConstitucion;

	@Temporal(TemporalType.DATE)
	@Column(name="F_INI_ACTIVIDAD")
	private Date fIniActividad;

	private String folio;

	@Column(name="ID_PAIS_ORIGEN")
	private BigDecimal idPaisOrigen;

	@Column(name="ID_PERS_REPRESNTE_LEGAL")
	private BigDecimal idPersRepresnteLegal;

	@Column(name="IMP_CAP_SOC_FIJO")
	private BigDecimal impCapSocFijo;

	@Column(name="IMP_CAP_VARIABLE")
	private BigDecimal impCapVariable;

	private String libro;

	@Column(name="NOMBRE_COMERCIAL")
	private String nombreComercial;

	@Column(name="NUM_ACC_CAP_VAR")
	private BigDecimal numAccCapVar;

	@Column(name="NUM_ACCIONES")
	private BigDecimal numAcciones;

	@Column(name="NUM_INSCRITA")
	private String numInscrita;

	@Column(name="RAZON_SOCIAL")
	private String razonSocial;

	@Column(name="SIGLAS_MERCANTIL")
	private String siglasMercantil;

	private String tipo;

	private String ubicada;

	@Column(name="VAL_NOM_ACC_CAP_VAR")
	private BigDecimal valNomAccCapVar;

	@Column(name="VALOR_NOMINAL_ACC")
	private BigDecimal valorNominalAcc;

	public RugPersonasMorales() {
	}

	public long getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}

	public String getBConsejoAdmon() {
		return this.bConsejoAdmon;
	}

	public void setBConsejoAdmon(String bConsejoAdmon) {
		this.bConsejoAdmon = bConsejoAdmon;
	}

	public String getBExclusionExtranjeros() {
		return this.bExclusionExtranjeros;
	}

	public void setBExclusionExtranjeros(String bExclusionExtranjeros) {
		this.bExclusionExtranjeros = bExclusionExtranjeros;
	}

	public String getCvePaisOrigen() {
		return this.cvePaisOrigen;
	}

	public void setCvePaisOrigen(String cvePaisOrigen) {
		this.cvePaisOrigen = cvePaisOrigen;
	}

	public String getCveUsuarioRegistro() {
		return this.cveUsuarioRegistro;
	}

	public void setCveUsuarioRegistro(String cveUsuarioRegistro) {
		this.cveUsuarioRegistro = cveUsuarioRegistro;
	}

	public Date getFConstitucion() {
		return this.fConstitucion;
	}

	public void setFConstitucion(Date fConstitucion) {
		this.fConstitucion = fConstitucion;
	}

	public Date getFIniActividad() {
		return this.fIniActividad;
	}

	public void setFIniActividad(Date fIniActividad) {
		this.fIniActividad = fIniActividad;
	}

	public String getFolio() {
		return this.folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public BigDecimal getIdPaisOrigen() {
		return this.idPaisOrigen;
	}

	public void setIdPaisOrigen(BigDecimal idPaisOrigen) {
		this.idPaisOrigen = idPaisOrigen;
	}

	public BigDecimal getIdPersRepresnteLegal() {
		return this.idPersRepresnteLegal;
	}

	public void setIdPersRepresnteLegal(BigDecimal idPersRepresnteLegal) {
		this.idPersRepresnteLegal = idPersRepresnteLegal;
	}

	public BigDecimal getImpCapSocFijo() {
		return this.impCapSocFijo;
	}

	public void setImpCapSocFijo(BigDecimal impCapSocFijo) {
		this.impCapSocFijo = impCapSocFijo;
	}

	public BigDecimal getImpCapVariable() {
		return this.impCapVariable;
	}

	public void setImpCapVariable(BigDecimal impCapVariable) {
		this.impCapVariable = impCapVariable;
	}

	public String getLibro() {
		return this.libro;
	}

	public void setLibro(String libro) {
		this.libro = libro;
	}

	public String getNombreComercial() {
		return this.nombreComercial;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public BigDecimal getNumAccCapVar() {
		return this.numAccCapVar;
	}

	public void setNumAccCapVar(BigDecimal numAccCapVar) {
		this.numAccCapVar = numAccCapVar;
	}

	public BigDecimal getNumAcciones() {
		return this.numAcciones;
	}

	public void setNumAcciones(BigDecimal numAcciones) {
		this.numAcciones = numAcciones;
	}

	public String getNumInscrita() {
		return this.numInscrita;
	}

	public void setNumInscrita(String numInscrita) {
		this.numInscrita = numInscrita;
	}

	public String getRazonSocial() {
		return this.razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getSiglasMercantil() {
		return this.siglasMercantil;
	}

	public void setSiglasMercantil(String siglasMercantil) {
		this.siglasMercantil = siglasMercantil;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUbicada() {
		return this.ubicada;
	}

	public void setUbicada(String ubicada) {
		this.ubicada = ubicada;
	}

	public BigDecimal getValNomAccCapVar() {
		return this.valNomAccCapVar;
	}

	public void setValNomAccCapVar(BigDecimal valNomAccCapVar) {
		this.valNomAccCapVar = valNomAccCapVar;
	}

	public BigDecimal getValorNominalAcc() {
		return this.valorNominalAcc;
	}

	public void setValorNominalAcc(BigDecimal valorNominalAcc) {
		this.valorNominalAcc = valorNominalAcc;
	}

}