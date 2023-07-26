package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;

/**
 * The persistent class for the RUG_SECU_USUARIOS database table.
 * 
 */
@Entity
@Table(name="RUG_SECU_USUARIOS")
@NamedQueries({
	@NamedQuery(name="RugSecuUsuario.findAll", query="SELECT r FROM RugSecuUsuario r"),
	@NamedQuery(name="RugSecuUsuario.findNotMigracion", query="SELECT r FROM RugSecuUsuario r WHERE r.cveUsuario NOT LIKE :email AND r.cveInstitucion <> :institucion ORDER BY r.persona.codigoRegistro, r.persona.perJuridica"),
	@NamedQuery(name="RugSecuUsuario.countAll", query="SELECT COUNT(r) FROM RugSecuUsuario r ORDER BY r.persona.codigoRegistro, r.persona.perJuridica"),
	@NamedQuery(name="RugSecuUsuario.countNotMigracion", query="SELECT COUNT(r) FROM RugSecuUsuario r WHERE r.cveUsuario NOT LIKE :email AND r.cveInstitucion <> :institucion ORDER BY r.persona.codigoRegistro, r.persona.perJuridica")
})
public class RugSecuUsuario implements Serializable {
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FH_REGISTRO")
	private Date fhRegistro;

	@Temporal(TemporalType.DATE)
	@Column(name="FH_ULT_ACCESO")
	private Date fhUltAcceso;

	@Temporal(TemporalType.DATE)
	@Column(name="FH_ULT_ACTUALIZACION")
	private Date fhUltActualizacion;

	@Column(name="ID_GRUPO")
	private Integer idGrupo;

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
	
	@ManyToOne
	@JoinColumn(name="ID_PERSONA", insertable=false, updatable=false)
	private RugPersonasFisicas personaFisica;
	
	@ManyToOne
	@JoinColumn(name="ID_PERSONA", insertable=false, updatable=false)
	private RugPersonas persona;
	
	@OneToMany
	@JoinColumn(name="OBJETO_ID")
	private List<Archivo> adjuntos;
	
	@OneToMany
	@JoinColumn(name="DESTINATARIO", referencedColumnName="CVE_USUARIO")
	private List<RugMailPool> correos;
	
	/*@Column(name="CORREOS_ERROR")
	private Long correosError;
	
	@Column(name="CORREOS_NO_ENVIADOS")
	private Long correosNoEnviados;*/
	
	@Column(name="IS_JUDICIAL")
	private String isJudicial;

	public RugSecuUsuario() {
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

	public Integer getIdGrupo() {
		return this.idGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
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

	public RugPersonasFisicas getPersonaFisica() {
		return personaFisica;
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

	public List<Archivo> getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(List<Archivo> adjuntos) {
		this.adjuntos = adjuntos;
	}

	public List<RugMailPool> getCorreos() {
		return correos;
	}

	public void setCorreos(List<RugMailPool> correos) {
		this.correos = correos;
	}

	public String getIsJudicial() {
		return isJudicial;
	}

	public void setIsJudicial(String isJudicial) {
		this.isJudicial = isJudicial;
	}

	@Override
	public String toString() {
		return "RugSecuUsuario{" +
				"idPersona=" + idPersona +
				", bFirmado='" + bFirmado + '\'' +
				", cveAcreedor='" + cveAcreedor + '\'' +
				", cveInstitucion='" + cveInstitucion + '\'' +
				", cveUsuAutenticacion='" + cveUsuAutenticacion + '\'' +
				", cveUsuario='" + cveUsuario + '\'' +
				", cveUsuarioPadre='" + cveUsuarioPadre + '\'' +
				", fAsignaPsw=" + fAsignaPsw +
				", fProxCambPsw=" + fProxCambPsw +
				", fVencePsw=" + fVencePsw +
				", fhBaja=" + fhBaja +
				", fhRegistro=" + fhRegistro +
				", fhUltAcceso=" + fhUltAcceso +
				", fhUltActualizacion=" + fhUltActualizacion +
				", idGrupo=" + idGrupo +
				", nomAlias='" + nomAlias + '\'' +
				", numErroresPsw=" + numErroresPsw +
				", numLogInvalido=" + numLogInvalido +
				", password='" + password + '\'' +
				", pregRecuperaPsw='" + pregRecuperaPsw + '\'' +
				", respRecuperaPsw='" + respRecuperaPsw + '\'' +
				", sitUsuario='" + sitUsuario + '\'' +
				", token='" + token + '\'' +
				", personaFisica=" + personaFisica.toString() +
				", persona=" + persona +
				", adjuntos=" + adjuntos +
				", correos=" + correos +
				", isJudicial='" + isJudicial + '\'' +
				'}';
	}

	/*public Long getCorreosError() {
		return correosError;
	}

	public void setCorreosError(Long correosError) {
		this.correosError = correosError;
	}

	public Long getCorreosNoEnviados() {
		return correosNoEnviados;
	}

	public void setCorreosNoEnviados(Long correosNoEnviados) {
		this.correosNoEnviados = correosNoEnviados;
	}*/
}
