package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the RUG_SECU_PERFILES_USUARIO database table.
 * 
 */
@Entity
@Table(name="RUG_SECU_PERFILES_USUARIO")
@NamedQuery(name="RugSecuPerfilesUsuario.findAll", query="SELECT r FROM RugSecuPerfilesUsuario r")
public class RugSecuPerfilesUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_PERSONA")
	private long idPersona;

	@Column(name="B_BLOQUEADO")
	private String bBloqueado;

	@Column(name="CVE_APLICACION")
	private String cveAplicacion;

	@Column(name="CVE_INSTITUCION")
	private String cveInstitucion;

	@Column(name="CVE_PERFIL")
	private String cvePerfil;

	@Column(name="CVE_USUARIO")
	private String cveUsuario;

	public RugSecuPerfilesUsuario() {
	}

	public long getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}

	public String getBBloqueado() {
		return this.bBloqueado;
	}

	public void setBBloqueado(String bBloqueado) {
		this.bBloqueado = bBloqueado;
	}

	public String getCveAplicacion() {
		return this.cveAplicacion;
	}

	public void setCveAplicacion(String cveAplicacion) {
		this.cveAplicacion = cveAplicacion;
	}

	public String getCveInstitucion() {
		return this.cveInstitucion;
	}

	public void setCveInstitucion(String cveInstitucion) {
		this.cveInstitucion = cveInstitucion;
	}

	public String getCvePerfil() {
		return this.cvePerfil;
	}

	public void setCvePerfil(String cvePerfil) {
		this.cvePerfil = cvePerfil;
	}

	public String getCveUsuario() {
		return this.cveUsuario;
	}

	public void setCveUsuario(String cveUsuario) {
		this.cveUsuario = cveUsuario;
	}

}