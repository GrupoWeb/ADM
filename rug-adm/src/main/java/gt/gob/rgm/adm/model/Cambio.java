package gt.gob.rgm.adm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the SD_INCIDENTES database table.
 * 
 */
@Entity
@Table(name="SD_CAMBIOS")
@NamedQuery(name="Cambio.findAll", query="SELECT i FROM Cambio i")
public class Cambio implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="CAMBIOS_ID_GENERATOR", sequenceName="SEQ_CAMBIOS", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CAMBIOS_ID_GENERATOR")
	@Column(name="CAMBIOS_ID")
	private long cambioId;

	@Column(name="USUARIO_ID")
	private BigDecimal usuarioId;

	@Column(name="USUARIO_SOLICITANTE")
	private String usuarioSolicitante;
	
	private String sistema;
	
	private String version;

	@Lob
	private String descripcion;

	private BigDecimal estado;

	@Column(name="FECHA_REGISTRO")
	@Temporal(TemporalType.DATE)	
	private Date fechaRegistro;
	
	@Column(name="FECHA_INICIO")
	@Temporal(TemporalType.DATE)	
	private Date fechaInicio;

	@Column(name="FECHA_FIN")
	@Temporal(TemporalType.DATE)	
	private Date fechaFin;

	private BigDecimal impacto;

	@Lob
	private String observaciones;

	public Cambio() {
		
	}
	
	public long getCambioId() {
		return cambioId;
	}

	public void setCambioId(long cambioId) {
		this.cambioId = cambioId;
	}

	public BigDecimal getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(BigDecimal usuarioId) {
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

	public BigDecimal getEstado() {
		return estado;
	}

	public void setEstado(BigDecimal estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

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

	public BigDecimal getImpacto() {
		return impacto;
	}

	public void setImpacto(BigDecimal impacto) {
		this.impacto = impacto;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
}
