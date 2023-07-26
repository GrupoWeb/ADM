package mx.gob.se.rug.anotacion.tramites.to;

import java.io.Serializable;

public class AnotacionSnGarantiaTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String anotacion;
	private String origenTramite;
	private String autoridadAutorizaTramite;
	private Integer vigencia;
	private String autoridadAutoriza;
	
	
	private String vigenciaStr;
	private Integer vigenciaNueva;
	private Integer personaAnotacion;
	
	private String otorganteNombre;
	private String otorganteAPaterno;
	private String otorganteAMaterno;
	private String otorganteDenominacion;
	private String otorganteFolioElectronico;
	private Integer otorganteId;
	
	private String resolucion;

	private Integer idUsuario;
	private String  nombreUsario;
	private String  perfil;
	private String  foliomercantil;
	
	private Integer idUsuarioFirmo;
	
	private Integer idTramitePadre;
	private Integer idTramiteNuevo;
	private Integer idTramiteFinal;
	private Integer idTipoTramite;
	
	private String fechaFirma;
	private String fechaAnotacion;
	
	private String tipoTramiteStr;
	
	
	private Integer resultado;
	private String mensaje;
	                                                                                                                                             
	
	
	public String getTipoTramiteStr() {
		return tipoTramiteStr;
	}
	public void setTipoTramiteStr(String tipoTramiteStr) {
		this.tipoTramiteStr = tipoTramiteStr;
	}
	public String getFechaAnotacion() {
		return fechaAnotacion;
	}
	public void setFechaAnotacion(String fechaAnotacion) {
		this.fechaAnotacion = fechaAnotacion;
	}
	public Integer getVigenciaNueva() {
		return vigenciaNueva;
	}
	public void setVigenciaNueva(Integer vigenciaNueva) {
		this.vigenciaNueva = vigenciaNueva;
	}
	public String getFoliomercantil() {
		return foliomercantil;
	}
	public void setFoliomercantil(String foliomercantil) {
		this.foliomercantil = foliomercantil;
	}
	public String getNombreUsario() {
		return nombreUsario;
	}
	public void setNombreUsario(String nombreUsario) {
		this.nombreUsario = nombreUsario;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public String getFechaFirma() {
		return fechaFirma;
	}
	public void setFechaFirma(String fechaFirma) {
		this.fechaFirma = fechaFirma;
	}
	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}
	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	public String getVigenciaStr() {
		return vigenciaStr;
	}
	public void setVigenciaStr(String vigenciaStr) {
		this.vigenciaStr = vigenciaStr;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getResultado() {
		return resultado;
	}
	public void setResultado(Integer resultado) {
		this.resultado = resultado;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getAnotacion() {
		return anotacion;
	}
	public void setAnotacion(String anotacion) {
		this.anotacion = anotacion;
	}
	public String getOrigenTramite() {
		return origenTramite;
	}
	public void setOrigenTramite(String origenTramite) {
		this.origenTramite = origenTramite;
	}
	public Integer getVigencia() {
		return vigencia;
	}
	public void setVigencia(Integer vigencia) {
		this.vigencia = vigencia;
	}
	public String getAutoridadAutoriza() {
		return autoridadAutoriza;
	}
	public void setAutoridadAutoriza(String autoridadAutoriza) {
		this.autoridadAutoriza = autoridadAutoriza;
	}
	public Integer getPersonaAnotacion() {
		return personaAnotacion;
	}
	public void setPersonaAnotacion(Integer personaAnotacion) {
		this.personaAnotacion = personaAnotacion;
	}
	public String getResolucion() {
		return resolucion;
	}
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}
	public Integer getIdTramitePadre() {
		return idTramitePadre;
	}
	public void setIdTramitePadre(Integer idTramitePadre) {
		this.idTramitePadre = idTramitePadre;
	}
	public Integer getIdTramiteNuevo() {
		return idTramiteNuevo;
	}
	public void setIdTramiteNuevo(Integer idTramiteNuevo) {
		this.idTramiteNuevo = idTramiteNuevo;
	}
	public Integer getIdTramiteFinal() {
		return idTramiteFinal;
	}
	public void setIdTramiteFinal(Integer idTramiteFinal) {
		this.idTramiteFinal = idTramiteFinal;
	}
	public String getAutoridadAutorizaTramite() {
		return autoridadAutorizaTramite;
	}
	public void setAutoridadAutorizaTramite(String autoridadAutorizaTramite) {
		this.autoridadAutorizaTramite = autoridadAutorizaTramite;
	}
	public String getOtorganteNombre() {
		return otorganteNombre;
	}
	public void setOtorganteNombre(String otorganteNombre) {
		this.otorganteNombre = otorganteNombre;
	}
	public String getOtorganteAPaterno() {
		return otorganteAPaterno;
	}
	public void setOtorganteAPaterno(String otorganteAPaterno) {
		this.otorganteAPaterno = otorganteAPaterno;
	}
	public String getOtorganteAMaterno() {
		return otorganteAMaterno;
	}
	public void setOtorganteAMaterno(String otorganteAMaterno) {
		this.otorganteAMaterno = otorganteAMaterno;
	}
	public String getOtorganteDenominacion() {
		return otorganteDenominacion;
	}
	public void setOtorganteDenominacion(String otorganteDenominacion) {
		this.otorganteDenominacion = otorganteDenominacion;
	}
	public Integer getOtorganteId() {
		return otorganteId;
	}
	public void setOtorganteId(Integer otorganteId) {
		this.otorganteId = otorganteId;
	}
	public String getOtorganteFolioElectronico() {
		return otorganteFolioElectronico;
	}
	public void setOtorganteFolioElectronico(String otorganteFolioElectronico) {
		this.otorganteFolioElectronico = otorganteFolioElectronico;
	}
	public Integer getIdUsuarioFirmo() {
		return idUsuarioFirmo;
	}
	public void setIdUsuarioFirmo(Integer idUsuarioFirmo) {
		this.idUsuarioFirmo = idUsuarioFirmo;
	}
	
	
	
	
	
}

