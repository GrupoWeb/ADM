package gt.gob.rgm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the RUG_PERSONAS_FISICAS database table.
 * 
 */
@Entity
@Table(name="RUG_PERSONAS_FISICAS")
@NamedQuery(name="RugPersonasFisicas.findAll", query="SELECT r FROM RugPersonasFisicas r")
public class RugPersonasFisicas implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_PERSONA")
	private long idPersona;

	@Column(name="AP_MATERNO")
	private String apMaterno;

	@Column(name="AP_PATERNO")
	private String apPaterno;

	private String curp;

	@Column(name="CVE_ESCOLARIDAD")
	private String cveEscolaridad;

	@Column(name="CVE_ESTADO_NACIM")
	private String cveEstadoNacim;

	@Column(name="CVE_MUN_DEL_NACIM")
	private BigDecimal cveMunDelNacim;

	@Column(name="CVE_PAIS_NACIM")
	private String cvePaisNacim;

	private String edad;

	@Column(name="ESTADO_CIVIL")
	private String estadoCivil;

	@Temporal(TemporalType.DATE)
	@Column(name="F_NACIMIENTO")
	private Date fNacimiento;

	@Column(name="FOLIO_DOCTO_MIGRAT")
	private String folioDoctoMigrat;

	@Column(name="ID_CALIDAD_MIGRAT")
	private BigDecimal idCalidadMigrat;

	@Column(name="ID_PAIS_NACIM")
	private BigDecimal idPaisNacim;

	@Column(name="LUGAR_NAC_PERS_EXT")
	private String lugarNacPersExt;

	@Column(name="NOMBRE_PERSONA")
	private String nombrePersona;

	@Column(name="NUM_SERIE")
	private String numSerie;

	@Column(name="OCUPACION_ACTUAL")
	private String ocupacionActual;

	private String sexo;

	//bi-directional many-to-one association to RelUsuAcreedor
	@OneToMany(mappedBy="rugPersonasFisica")
	private List<RelUsuAcreedor> relUsuAcreedors;

	//bi-directional one-to-one association to RugPersona
	@OneToOne
	@JoinColumn(name="ID_PERSONA")
	private RugPersonas rugPersona;

	//bi-directional one-to-one association to RugSecuUsuario
	@OneToOne(mappedBy="personaFisica")
	private RugSecuUsuarios rugSecuUsuario;

	public RugPersonasFisicas() {
	}

	public long getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
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

	public String getCveEscolaridad() {
		return this.cveEscolaridad;
	}

	public void setCveEscolaridad(String cveEscolaridad) {
		this.cveEscolaridad = cveEscolaridad;
	}

	public String getCveEstadoNacim() {
		return this.cveEstadoNacim;
	}

	public void setCveEstadoNacim(String cveEstadoNacim) {
		this.cveEstadoNacim = cveEstadoNacim;
	}

	public BigDecimal getCveMunDelNacim() {
		return this.cveMunDelNacim;
	}

	public void setCveMunDelNacim(BigDecimal cveMunDelNacim) {
		this.cveMunDelNacim = cveMunDelNacim;
	}

	public String getCvePaisNacim() {
		return this.cvePaisNacim;
	}

	public void setCvePaisNacim(String cvePaisNacim) {
		this.cvePaisNacim = cvePaisNacim;
	}

	public String getEdad() {
		return this.edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public String getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Date getFNacimiento() {
		return this.fNacimiento;
	}

	public void setFNacimiento(Date fNacimiento) {
		this.fNacimiento = fNacimiento;
	}

	public String getFolioDoctoMigrat() {
		return this.folioDoctoMigrat;
	}

	public void setFolioDoctoMigrat(String folioDoctoMigrat) {
		this.folioDoctoMigrat = folioDoctoMigrat;
	}

	public BigDecimal getIdCalidadMigrat() {
		return this.idCalidadMigrat;
	}

	public void setIdCalidadMigrat(BigDecimal idCalidadMigrat) {
		this.idCalidadMigrat = idCalidadMigrat;
	}

	public BigDecimal getIdPaisNacim() {
		return this.idPaisNacim;
	}

	public void setIdPaisNacim(BigDecimal idPaisNacim) {
		this.idPaisNacim = idPaisNacim;
	}

	public String getLugarNacPersExt() {
		return this.lugarNacPersExt;
	}

	public void setLugarNacPersExt(String lugarNacPersExt) {
		this.lugarNacPersExt = lugarNacPersExt;
	}

	public String getNombrePersona() {
		return this.nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	public String getNumSerie() {
		return this.numSerie;
	}

	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}

	public String getOcupacionActual() {
		return this.ocupacionActual;
	}

	public void setOcupacionActual(String ocupacionActual) {
		this.ocupacionActual = ocupacionActual;
	}

	public String getSexo() {
		return this.sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public List<RelUsuAcreedor> getRelUsuAcreedors() {
		return this.relUsuAcreedors;
	}

	public void setRelUsuAcreedors(List<RelUsuAcreedor> relUsuAcreedors) {
		this.relUsuAcreedors = relUsuAcreedors;
	}

	public RelUsuAcreedor addRelUsuAcreedor(RelUsuAcreedor relUsuAcreedor) {
		getRelUsuAcreedors().add(relUsuAcreedor);
		relUsuAcreedor.setRugPersonasFisica(this);

		return relUsuAcreedor;
	}

	public RelUsuAcreedor removeRelUsuAcreedor(RelUsuAcreedor relUsuAcreedor) {
		getRelUsuAcreedors().remove(relUsuAcreedor);
		relUsuAcreedor.setRugPersonasFisica(null);

		return relUsuAcreedor;
	}

	public RugPersonas getRugPersona() {
		return this.rugPersona;
	}

	public void setRugPersona(RugPersonas rugPersona) {
		this.rugPersona = rugPersona;
	}

	public RugSecuUsuarios getRugSecuUsuario() {
		return this.rugSecuUsuario;
	}

	public void setRugSecuUsuario(RugSecuUsuarios rugSecuUsuario) {
		this.rugSecuUsuario = rugSecuUsuario;
	}

}