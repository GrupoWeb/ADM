package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_PERSONAS_FISICAS database table.
 * 
 */
@Entity
@Table(name="RUG_PERSONAS_FISICAS")
@NamedQuery(name="RugPersonasFisica.findAll", query="SELECT r FROM RugPersonasFisicas r")
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

	@Override
	public String toString() {
		return "RugPersonasFisicas{" +
				"idPersona=" + idPersona +
				", apMaterno='" + apMaterno + '\'' +
				", apPaterno='" + apPaterno + '\'' +
				", curp='" + curp + '\'' +
				", cveEscolaridad='" + cveEscolaridad + '\'' +
				", cveEstadoNacim='" + cveEstadoNacim + '\'' +
				", cveMunDelNacim=" + cveMunDelNacim +
				", cvePaisNacim='" + cvePaisNacim + '\'' +
				", edad='" + edad + '\'' +
				", estadoCivil='" + estadoCivil + '\'' +
				", fNacimiento=" + fNacimiento +
				", folioDoctoMigrat='" + folioDoctoMigrat + '\'' +
				", idCalidadMigrat=" + idCalidadMigrat +
				", idPaisNacim=" + idPaisNacim +
				", lugarNacPersExt='" + lugarNacPersExt + '\'' +
				", nombrePersona='" + nombrePersona + '\'' +
				", numSerie='" + numSerie + '\'' +
				", ocupacionActual='" + ocupacionActual + '\'' +
				", sexo='" + sexo + '\'' +
				'}';
	}
}