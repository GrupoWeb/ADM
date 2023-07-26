package mx.gob.se.rug.acreedores.to;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class UsuarioTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idTramite;
	private Integer idTramiteTemporal;
	private Integer idPersona;
	private Integer idSubusuario;
	private Integer idAcreedor;
	private String razonSocial;
	private String nombre;
	private String nombreCompleto;
	private String apaterno;
	private String amaterno;
	private Integer idNacionalidad;
	private String sexo;
	private Integer idStatus;
	private String rfc;
	private String curp;
	private String cveUsuario;
	private String perfil;
	private String password;
	private String preguntaRecupera;
	private String respuestaRecupera;
	private String cveInstitucion;
	private String cvePerfil;
	private String cveAplicacion;
	private Integer idGrupo;
	private Date fechaInicio;
	private Date fechaFin;
	private String otrosTerminos;
	private String tipoContrato;
	private boolean bFirmado;
	private String correoElectronico;
	private String confirmacion;
	private String descGrupo;
	private Integer idRepresentanteAcreedor;

	public String getConfirmacion() {
		return confirmacion;
	}

	public void setConfirmacion(String confirmacion) {
		this.confirmacion = confirmacion;
	}

	public Integer getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}

	public Integer getIdTramiteTemporal() {
		return idTramiteTemporal;
	}

	public void setIdTramiteTemporal(Integer idTramiteTemporal) {
		this.idTramiteTemporal = idTramiteTemporal;
	}

	public Integer getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}

	public Integer getIdSubusuario() {
		return idSubusuario;
	}

	public void setIdSubusuario(Integer idSubusuario) {
		this.idSubusuario = idSubusuario;
	}

	public Integer getIdAcreedor() {
		return idAcreedor;
	}

	public void setIdAcreedor(Integer idAcreedor) {
		this.idAcreedor = idAcreedor;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getApaterno() {
		return apaterno;
	}

	public void setApaterno(String apaterno) {
		this.apaterno = apaterno;
	}

	public String getAmaterno() {
		return amaterno;
	}

	public void setAmaterno(String amaterno) {
		this.amaterno = amaterno;
	}

	public Integer getIdNacionalidad() {
		return idNacionalidad;
	}

	public void setIdNacionalidad(Integer idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Integer getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getCveUsuario() {
		return cveUsuario;
	}

	public void setCveUsuario(String cveUsuario) {
		this.cveUsuario = cveUsuario;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPreguntaRecupera() {
		return preguntaRecupera;
	}

	public void setPreguntaRecupera(String preguntaRecupera) {
		this.preguntaRecupera = preguntaRecupera;
	}

	public String getRespuestaRecupera() {
		return respuestaRecupera;
	}

	public void setRespuestaRecupera(String respuestaRecupera) {
		this.respuestaRecupera = respuestaRecupera;
	}

	public String getCveInstitucion() {
		return cveInstitucion;
	}

	public void setCveInstitucion(String cveInstitucion) {
		this.cveInstitucion = cveInstitucion;
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

	// public String getIdGrupo() {
	// return idGrupo;
	// }
	//
	// public void setIdGrupo(String idGrupo) {
	// this.idGrupo = idGrupo;
	// }

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getOtrosTerminos() {
		return otrosTerminos;
	}

	public void setOtrosTerminos(String otrosTerminos) {
		this.otrosTerminos = otrosTerminos;
	}

	public String getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public boolean isbFirmado() {
		return bFirmado;
	}

	public void setbFirmado(boolean bFirmado) {
		this.bFirmado = bFirmado;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setDesGrupo(String desGrupo) {
		this.descGrupo = desGrupo;
	}

	public String getDesGrupo() {
		return descGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public Integer getIdGrupo() {
		return idGrupo;
	}

	public Integer getIdRepresentanteAcreedor() {
		return idRepresentanteAcreedor;
	}

	public void setIdRepresentanteAcreedor(Integer idRepresentanteAcreedor) {
		this.idRepresentanteAcreedor = idRepresentanteAcreedor;
	}

}
