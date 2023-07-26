package gt.gob.rgm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_SECU_USUARIOS database table.
 * 
 */
@Entity
@Table(name="RUG_SECU_USUARIOS")
@NamedQueries({
		@NamedQuery(name="RugSecuUsuarios.findAll", query="SELECT r FROM RugSecuUsuarios r"),
		@NamedQuery(name="RugSecuUsuarios.countIsAdmin", query="SELECT COUNT(r.isAdmin) FROM RugSecuUsuarios r WHERE r.cveUsuarioPadre = :cveAdmin")
})
public class RugSecuUsuarios implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_PERSONA")
	private long idPersona;

	@Column(name="B_FIRMADO")
	private String bFirmado;

	@Column(name="CVE_ACREEDOR")
	private String cveAcreedor;

	@Column(name="CVE_INSTITUCION")
	private String cveInstitucion;

	@Column(name="CVE_USU_AUTENTICACION")
	private String cveUsuAutenticacion;

	@Column(name="CVE_USUARIO")
	private String cveUsuario;

	@Column(name="CVE_USUARIO_PADRE")
	private String cveUsuarioPadre;

	@Temporal(TemporalType.DATE)
	@Column(name="F_ASIGNA_PSW")
	private Date fAsignaPsw;

	@Temporal(TemporalType.DATE)
	@Column(name="F_PROX_CAMB_PSW")
	private Date fProxCambPsw;

	@Temporal(TemporalType.DATE)
	@Column(name="F_VENCE_PSW")
	private Date fVencePsw;

	@Temporal(TemporalType.DATE)
	@Column(name="FH_BAJA")
	private Date fhBaja;

	@Temporal(TemporalType.DATE)
	@Column(name="FH_REGISTRO")
	private Date fhRegistro;

	@Temporal(TemporalType.DATE)
	@Column(name="FH_ULT_ACCESO")
	private Date fhUltAcceso;

	@Temporal(TemporalType.DATE)
	@Column(name="FH_ULT_ACTUALIZACION")
	private Date fhUltActualizacion;

	@Column(name="NOM_ALIAS")
	private String nomAlias;

	@Column(name="NUM_ERRORES_PSW")
	private BigDecimal numErroresPsw;

	@Column(name="NUM_LOG_INVALIDO")
	private BigDecimal numLogInvalido;

	private String password;

	@Column(name="PREG_RECUPERA_PSW")
	private String pregRecuperaPsw;

	@Column(name="RESP_RECUPERA_PSW")
	private String respRecuperaPsw;

	@Column(name="SIT_USUARIO")
	private String sitUsuario;

	private String token;
	
	@Column(name="IS_JUDICIAL")
	private String isJudicial;

	@Column(name="IS_ADMIN")
	private String isAdmin;

	//bi-directional one-to-one association to RugSecuPerfilesUsuario
	@OneToOne(mappedBy="rugSecuUsuario")
	private RugSecuPerfilesUsuario perfilUsuario;
	
	@Column(name="ID_GRUPO")
	private long idGrupo;

	//bi-directional many-to-one association to RugGrupo
	@ManyToOne
	@JoinColumn(name="ID_GRUPO", insertable=false, updatable=false)
	private RugGrupos grupo;

	//bi-directional one-to-one association to RugPersonasFisica
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_PERSONA")
	private RugPersonasFisicas personaFisica;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_PERSONA")
	private RugPersonas persona;

	public RugSecuUsuarios() {
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public long getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}

	public String getBFirmado() {
		return this.bFirmado;
	}

	public void setBFirmado(String bFirmado) {
		this.bFirmado = bFirmado;
	}

	public String getCveAcreedor() {
		return this.cveAcreedor;
	}

	public void setCveAcreedor(String cveAcreedor) {
		this.cveAcreedor = cveAcreedor;
	}

	public String getCveInstitucion() {
		return this.cveInstitucion;
	}

	public void setCveInstitucion(String cveInstitucion) {
		this.cveInstitucion = cveInstitucion;
	}

	public String getCveUsuAutenticacion() {
		return this.cveUsuAutenticacion;
	}

	public void setCveUsuAutenticacion(String cveUsuAutenticacion) {
		this.cveUsuAutenticacion = cveUsuAutenticacion;
	}

	public String getCveUsuario() {
		return this.cveUsuario;
	}

	public void setCveUsuario(String cveUsuario) {
		this.cveUsuario = cveUsuario;
	}

	public String getCveUsuarioPadre() {
		return this.cveUsuarioPadre;
	}

	public void setCveUsuarioPadre(String cveUsuarioPadre) {
		this.cveUsuarioPadre = cveUsuarioPadre;
	}

	public Date getFAsignaPsw() {
		return this.fAsignaPsw;
	}

	public void setFAsignaPsw(Date fAsignaPsw) {
		this.fAsignaPsw = fAsignaPsw;
	}

	public Date getFProxCambPsw() {
		return this.fProxCambPsw;
	}

	public void setFProxCambPsw(Date fProxCambPsw) {
		this.fProxCambPsw = fProxCambPsw;
	}

	public Date getFVencePsw() {
		return this.fVencePsw;
	}

	public void setFVencePsw(Date fVencePsw) {
		this.fVencePsw = fVencePsw;
	}

	public Date getFhBaja() {
		return this.fhBaja;
	}

	public void setFhBaja(Date fhBaja) {
		this.fhBaja = fhBaja;
	}

	public Date getFhRegistro() {
		return this.fhRegistro;
	}

	public void setFhRegistro(Date fhRegistro) {
		this.fhRegistro = fhRegistro;
	}

	public Date getFhUltAcceso() {
		return this.fhUltAcceso;
	}

	public void setFhUltAcceso(Date fhUltAcceso) {
		this.fhUltAcceso = fhUltAcceso;
	}

	public Date getFhUltActualizacion() {
		return this.fhUltActualizacion;
	}

	public void setFhUltActualizacion(Date fhUltActualizacion) {
		this.fhUltActualizacion = fhUltActualizacion;
	}

	public String getNomAlias() {
		return this.nomAlias;
	}

	public void setNomAlias(String nomAlias) {
		this.nomAlias = nomAlias;
	}

	public BigDecimal getNumErroresPsw() {
		return this.numErroresPsw;
	}

	public void setNumErroresPsw(BigDecimal numErroresPsw) {
		this.numErroresPsw = numErroresPsw;
	}

	public BigDecimal getNumLogInvalido() {
		return this.numLogInvalido;
	}

	public void setNumLogInvalido(BigDecimal numLogInvalido) {
		this.numLogInvalido = numLogInvalido;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPregRecuperaPsw() {
		return this.pregRecuperaPsw;
	}

	public void setPregRecuperaPsw(String pregRecuperaPsw) {
		this.pregRecuperaPsw = pregRecuperaPsw;
	}

	public String getRespRecuperaPsw() {
		return this.respRecuperaPsw;
	}

	public void setRespRecuperaPsw(String respRecuperaPsw) {
		this.respRecuperaPsw = respRecuperaPsw;
	}

	public String getSitUsuario() {
		return this.sitUsuario;
	}

	public void setSitUsuario(String sitUsuario) {
		this.sitUsuario = sitUsuario;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public RugSecuPerfilesUsuario getPerfilUsuario() {
		return this.perfilUsuario;
	}

	public void setPerfilUsuario(RugSecuPerfilesUsuario perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}

	public long getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(long idGrupo) {
		this.idGrupo = idGrupo;
	}

	public RugGrupos getRugGrupo() {
		return this.grupo;
	}

	public void setRugGrupo(RugGrupos grupo) {
		this.grupo = grupo;
	}

	public RugPersonasFisicas getPersonaFisica() {
		return this.personaFisica;
	}

	public void setPersonaFisica(RugPersonasFisicas personaFisica) {
		this.personaFisica = personaFisica;
	}

	public RugPersonas getPersona() {
		return persona;
	}

	public void setPersona(RugPersonas persona) {
		this.persona = persona;
	}

	public String getIsJudicial() {
		return isJudicial;
	}

	public void setIsJudicial(String isJudicial) {
		this.isJudicial = isJudicial;
	}
}