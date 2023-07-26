package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RECHAZO_CUENTA database table.
 * 
 */
@Entity
@Table(name="RECHAZO_CUENTA")
@NamedQuery(name="RechazoCuenta.findAll", query="SELECT r FROM RechazoCuenta r")
public class RechazoCuenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RECHAZO_CUENTA_RECHAZOCUENTAID_GENERATOR", sequenceName="SEQ_RECHAZO_CUENTA", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RECHAZO_CUENTA_RECHAZOCUENTAID_GENERATOR")
	@Column(name="RECHAZO_CUENTA_ID")
	private long rechazoCuentaId;

	private String causa;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name="ID_PERSONA")
	private long idPersona;

	@Column(name="USUARIO_ID")
	private long usuarioId;
	
	private String token;

	public RechazoCuenta() {
	}

	public long getRechazoCuentaId() {
		return this.rechazoCuentaId;
	}

	public void setRechazoCuentaId(long rechazoCuentaId) {
		this.rechazoCuentaId = rechazoCuentaId;
	}

	public String getCausa() {
		return this.causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public long getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}

	public long getUsuarioId() {
		return this.usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
