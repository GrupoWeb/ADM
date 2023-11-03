package gt.gob.rgm.rug.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


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

	private Timestamp fecha;

	private String operacion;
	
	private long agencia;
	
	@Lob
	private String detalle;

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

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
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

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public long getAgencia() {
		return agencia;
	}

	public void setAgencia(long agencia) {
		this.agencia = agencia;
	}

}