package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class Change implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long cambioId;
	private Long usuarioId;
	private String usuarioSolicitante;	
	private String sistema;
	private String version;
	private String descripcion;
	private Long estado;
	private String fechaRegistro;
	private String fechaInicio;
	private String fechaFin;
	private Long impacto;	
	private String observaciones;	
	private String texto;
	
	public Change() {
		super();
	}

	public Long getCambioId() {
		return cambioId;
	}

	public void setCambioId(Long cambioId) {
		this.cambioId = cambioId;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(String usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getEstado() {
		return estado;
	}

	public void setEstado(Long estado) {
		this.estado = estado;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Long getImpacto() {
		return impacto;
	}

	public void setImpacto(Long impacto) {
		this.impacto = impacto;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
}
