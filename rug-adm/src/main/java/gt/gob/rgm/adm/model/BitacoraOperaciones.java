package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the BITACORA_OPERACIONES database table.
 * 
 */
@Entity
@Table(name="BITACORA_OPERACIONES")
@NamedQuery(name="BitacoraOperaciones.findAll", query="SELECT b FROM BitacoraOperaciones b")
public class BitacoraOperaciones implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="BITACORA_OPERACIONES_BITACORAID_GENERATOR", sequenceName="SEQ_BITACORA_OPERACIONES", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BITACORA_OPERACIONES_BITACORAID_GENERATOR")
	@Column(name="BITACORA_ID")
	private long bitacoraId;

	@Temporal(value=TemporalType.TIMESTAMP)
	private Date fecha;

	private String operacion;

	@Column(name="USUARIO_ID")
	private long usuarioId;

	public BitacoraOperaciones() {
	}

	public long getBitacoraId() {
		return this.bitacoraId;
	}

	public void setBitacoraId(long bitacoraId) {
		this.bitacoraId = bitacoraId;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getOperacion() {
		return this.operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public long getUsuarioId() {
		return this.usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

}