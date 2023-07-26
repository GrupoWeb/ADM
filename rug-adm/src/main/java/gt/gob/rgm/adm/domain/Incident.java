package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class Incident implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long incidenteId;
	private String asunto;
	private Long categoria;
	private String descripcion;
	private Long diasPausa;
	private Long estado;
	private String fechaCreacion;
	private String fechaFin;
	private Long impacto;
	private Long modoIngreso;
	private Long prioridad;
	private String resolucion;
	private Long tipoSolicitud;
	private Long urgencia;
	private Long usuarioId;
	private String usuarioSolicitante;
	private String texto;
	
	public Long getIncidenteId() {
		return incidenteId;
	}
	public void setIncidenteId(Long incidenteId) {
		this.incidenteId = incidenteId;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public Long getCategoria() {
		return categoria;
	}
	public void setCategoria(Long categoria) {
		this.categoria = categoria;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getDiasPausa() {
		return diasPausa;
	}
	public void setDiasPausa(Long diasPausa) {
		this.diasPausa = diasPausa;
	}
	public Long getEstado() {
		return estado;
	}
	public void setEstado(Long estado) {
		this.estado = estado;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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
	public Long getModoIngreso() {
		return modoIngreso;
	}
	public void setModoIngreso(Long modoIngreso) {
		this.modoIngreso = modoIngreso;
	}
	public Long getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(Long prioridad) {
		this.prioridad = prioridad;
	}
	public String getResolucion() {
		return resolucion;
	}
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}
	public Long getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(Long tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	public Long getUrgencia() {
		return urgencia;
	}
	public void setUrgencia(Long urgencia) {
		this.urgencia = urgencia;
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
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}

}
