package gt.gob.rgm.domain;

import java.io.Serializable;

public class Usuario implements Serializable {
	Long idPersona;
	String rfc;
	Long idNacionalidad;
	String tipoPersona;
	String inscritoComo;
	String docId;
	String nombre;
	String cveInstitucion;
	String cveUsuario;
	String password;
	String confirmacion;
	String pregRecupera;
	String respRecupera;
	String cveUsuarioPadre;
	Long idPersonaPadre;
	String cveAcreedor;
	Long idGrupo;
	String firmado;
	String token;
	String cvePerfil;
	String cveAplicacion;
	String mensajeError;
	String sitUsuario;
	String isJudicial;
	String isAdmin;

	String isAdminButton;
	
	public Usuario() {
	}


	public String getIsAdminButton() {
		return isAdminButton;
	}

	public void setIsAdminButton(String isAdminButton) {
		this.isAdminButton = isAdminButton;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public Long getIdNacionalidad() {
		return idNacionalidad;
	}

	public void setIdNacionalidad(Long idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getInscritoComo() {
		return inscritoComo;
	}

	public void setInscritoComo(String inscritoComo) {
		this.inscritoComo = inscritoComo;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCveInstitucion() {
		return cveInstitucion;
	}

	public void setCveInstitucion(String cveInstitucion) {
		this.cveInstitucion = cveInstitucion;
	}

	public String getCveUsuario() {
		return cveUsuario;
	}

	public void setCveUsuario(String cveUsuario) {
		this.cveUsuario = cveUsuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmacion() {
		return confirmacion;
	}

	public void setConfirmacion(String confirmacion) {
		this.confirmacion = confirmacion;
	}

	public String getPregRecupera() {
		return pregRecupera;
	}

	public void setPregRecupera(String pregRecupera) {
		this.pregRecupera = pregRecupera;
	}

	public String getRespRecupera() {
		return respRecupera;
	}

	public void setRespRecupera(String respRecupera) {
		this.respRecupera = respRecupera;
	}

	public String getCveUsuarioPadre() {
		return cveUsuarioPadre;
	}

	public void setCveUsuarioPadre(String cveUsuarioPadre) {
		this.cveUsuarioPadre = cveUsuarioPadre;
	}

	public Long getIdPersonaPadre() {
		return idPersonaPadre;
	}

	public void setIdPersonaPadre(Long idPersonaPadre) {
		this.idPersonaPadre = idPersonaPadre;
	}

	public String getCveAcreedor() {
		return cveAcreedor;
	}

	public void setCveAcreedor(String cveAcreedor) {
		this.cveAcreedor = cveAcreedor;
	}

	public Long getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Long idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getFirmado() {
		return firmado;
	}

	public void setFirmado(String firmado) {
		this.firmado = firmado;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCvePerfil() {
		return cvePerfil;
	}

	public void setCvePerfil(String cvePerfil) {
		this.cvePerfil = cvePerfil;
	}

	public String getCveAplicacion() {
		return cveAplicacion;
	}

	public void setCveAplicacion(String cveAplicacion) {
		this.cveAplicacion = cveAplicacion;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public String getSitUsuario() {
		return sitUsuario;
	}

	public void setSitUsuario(String sitUsuario) {
		this.sitUsuario = sitUsuario;
	}

	public String getIsJudicial() {
		return isJudicial;
	}

	public void setIsJudicial(String isJudicial) {
		this.isJudicial = isJudicial;
	}
}
