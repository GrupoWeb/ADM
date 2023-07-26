package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the SD_INCIDENTES database table.
 * 
 */
@Entity
@Table(name="SD_INCIDENTES")
@NamedQuery(name="Incidente.findAll", query="SELECT i FROM Incidente i")
public class Incidente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INCIDENTES_ID_GENERATOR", sequenceName="SEQ_INCIDENTES", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INCIDENTES_ID_GENERATOR")
	@Column(name="INCIDENTE_ID")
	private long incidenteId;

	
	private String asunto;

	private BigDecimal categoria;

	@Lob
	private String descripcion;

	@Column(name="DIAS_PAUSA")
	private BigDecimal diasPausa;

	private BigDecimal estado;

	@Column(name="FECHA_CREACION")
	@Temporal(TemporalType.DATE)	
	private Date fechaCreacion;

	@Column(name="FECHA_FIN")
	@Temporal(TemporalType.DATE)	
	private Date fechaFin;

	private BigDecimal impacto;

	@Column(name="MODO_INGRESO")
	private BigDecimal modoIngreso;

	private BigDecimal prioridad;

	@Lob
	private String resolucion;

	@Column(name="TIPO_SOLICITUD")
	private BigDecimal tipoSolicitud;

	private BigDecimal urgencia;

	@Column(name="USUARIO_ID")
	private BigDecimal usuarioId;

	@Column(name="USUARIO_SOLICITANTE")
	private String usuarioSolicitante;

	public Incidente() {
	}

	public long getIncidenteId() {
		return this.incidenteId;
	}

	public void setIncidenteId(long incidenteId) {
		this.incidenteId = incidenteId;
	}

	
	public String getAsunto() {
		return this.asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public BigDecimal getCategoria() {
		return this.categoria;
	}

	public void setCategoria(BigDecimal categoria) {
		this.categoria = categoria;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getDiasPausa() {
		return this.diasPausa;
	}

	public void setDiasPausa(BigDecimal diasPausa) {
		this.diasPausa = diasPausa;
	}

	public BigDecimal getEstado() {
		return this.estado;
	}

	public void setEstado(BigDecimal estado) {
		this.estado = estado;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public BigDecimal getImpacto() {
		return this.impacto;
	}

	public void setImpacto(BigDecimal impacto) {
		this.impacto = impacto;
	}

	public BigDecimal getModoIngreso() {
		return this.modoIngreso;
	}

	public void setModoIngreso(BigDecimal modoIngreso) {
		this.modoIngreso = modoIngreso;
	}

	public BigDecimal getPrioridad() {
		return this.prioridad;
	}

	public void setPrioridad(BigDecimal prioridad) {
		this.prioridad = prioridad;
	}

	public String getResolucion() {
		return this.resolucion;
	}

	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	public BigDecimal getTipoSolicitud() {
		return this.tipoSolicitud;
	}

	public void setTipoSolicitud(BigDecimal tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public BigDecimal getUrgencia() {
		return this.urgencia;
	}

	public void setUrgencia(BigDecimal urgencia) {
		this.urgencia = urgencia;
	}

	public BigDecimal getUsuarioId() {
		return this.usuarioId;
	}

	public void setUsuarioId(BigDecimal usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getUsuarioSolicitante() {
		return this.usuarioSolicitante;
	}

	public void setUsuarioSolicitante(String usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

}