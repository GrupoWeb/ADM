package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the V_GARANTIA_UTRAM database table.
 * 
 */
@Entity
@Table(name="V_GARANTIA_UTRAM")
@NamedQuery(name="VGarantiaUtram.findAll", query="SELECT v FROM VGarantiaUtram v")
public class VGarantiaUtram implements Serializable {
	private static final long serialVersionUID = 1L;

	private String descripcion;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_CREACION")
	private Date fechaCreacion;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_INSCRIPCION")
	private Date fechaInscripcion;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_ULTIMO")
	private Date fechaUltimo;

	@Column(name="ID_GARANTIA")
	private BigDecimal idGarantia;

	@Column(name="ID_TIPO_TRAMITE")
	private BigDecimal idTipoTramite;

	@Id
	@Column(name="ID_TRAMITE")
	private BigDecimal idTramite;

	public VGarantiaUtram() {
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaInscripcion() {
		return this.fechaInscripcion;
	}

	public void setFechaInscripcion(Date fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}

	public Date getFechaUltimo() {
		return this.fechaUltimo;
	}

	public void setFechaUltimo(Date fechaUltimo) {
		this.fechaUltimo = fechaUltimo;
	}

	public BigDecimal getIdGarantia() {
		return this.idGarantia;
	}

	public void setIdGarantia(BigDecimal idGarantia) {
		this.idGarantia = idGarantia;
	}

	public BigDecimal getIdTipoTramite() {
		return this.idTipoTramite;
	}

	public void setIdTipoTramite(BigDecimal idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public BigDecimal getIdTramite() {
		return this.idTramite;
	}

	public void setIdTramite(BigDecimal idTramite) {
		this.idTramite = idTramite;
	}

}