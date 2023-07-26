/*
 * RegistroUsuario.java        29/10/2009
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140, 
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y contiene información perteneciente
 * a la Secretaría de Economía.
 * 
 */
package mx.gob.se.rug.administracion.dto;

import mx.gob.economia.dgi.framework.base.dto.AbstractBaseDTO;

/**
 * 
 * 
 * @version		0.1
 * @author 		Alfonso Esquivel
 *
 */
@SuppressWarnings("serial")
public class RegistroUsuario extends AbstractBaseDTO {

	private String password;
	private String confirmacion;
	private String oldPassword;
	private String pregunta;
	private String respuesta;
	private boolean terminos;
	private String tipoOperacion;
	private String uri;
	private Integer  idTramiteNuevo;
	private String nombreAcreedor;
	private String tipoUsuario;
	private String idGrupo;
	private String tipoCuenta;
	private boolean judicial;
	
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
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public void setTerminos(boolean terminos) {
		this.terminos = terminos;
	}
	public boolean isTerminos() {
		return terminos;
	}
	public boolean getTerminos() {
		return terminos;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getUri() {
		return uri;
	}
	public Integer getIdTramiteNuevo() {
		return idTramiteNuevo;
	}
	public void setIdTramiteNuevo(Integer idTramiteNuevo) {
		this.idTramiteNuevo = idTramiteNuevo;
	}
	public void setNombreAcreedor(String nombreAcreedor) {
		this.nombreAcreedor = nombreAcreedor;
	}
	public String getNombreAcreedor() {
		return nombreAcreedor;
	}
	public String getTipoUsuario() {
		return tipoUsuario;
	}
	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	public String getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}
	public String getTipoCuenta() {
		return tipoCuenta;
	}
	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
	public boolean isJudicial() {
		return judicial;
	}
	public boolean getJudicial() {
		return judicial;
	}
	public void setJudicial(boolean judicial) {
		this.judicial = judicial;
	}	
}
