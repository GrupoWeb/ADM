package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the SD_ADJUNTOS database table.
 * 
 */
@Entity
@Table(name="SD_ADJUNTOS")
@NamedQuery(name="Adjunto.findAll", query="SELECT s FROM Adjunto s")
public class Adjunto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SD_ADJUNTOS_ADJUNTOID_GENERATOR", sequenceName="SEQ_ADJUNTOS", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SD_ADJUNTOS_ADJUNTOID_GENERATOR")
	@Column(name="ADJUNTO_ID")
	private long adjuntoId;

	@Lob
	private byte[] adjunto;

	private String descripcion;

	@Column(name="INCIDENTE_ID")
	private BigDecimal incidenteId;

	private String tipo;

	public Adjunto() {
	}

	public long getAdjuntoId() {
		return this.adjuntoId;
	}

	public void setAdjuntoId(long adjuntoId) {
		this.adjuntoId = adjuntoId;
	}

	public byte[] getAdjunto() {
		return this.adjunto;
	}

	public void setAdjunto(byte[] adjunto) {
		this.adjunto = adjunto;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getIncidenteId() {
		return this.incidenteId;
	}

	public void setIncidenteId(BigDecimal incidenteId) {
		this.incidenteId = incidenteId;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}