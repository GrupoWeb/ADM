package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_CERTIFICACIONES database table.
 * 
 */
@Entity
@Table(name="RUG_CERTIFICACIONES")
@NamedQuery(name="RugCertificaciones.findAll", query="SELECT r FROM RugCertificaciones r")
public class RugCertificaciones implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_TRAMITE_CERT")
	private long idTramiteCert;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_CERT")
	private Date fechaCert;

	@Column(name="ID_GARANTIA")
	private BigDecimal idGarantia;

	@Column(name="ID_PERSONA_LOGIN")
	private BigDecimal idPersonaLogin;

	@Column(name="ID_TIPO_TRAMITE")
	private BigDecimal idTipoTramite;

	@Column(name="ID_TRAMITE")
	private BigDecimal idTramite;

	private BigDecimal precio;

	@Column(name="STATUS_REG")
	private String statusReg;
	
	@OneToOne
	@JoinColumn(name="ID_GARANTIA", referencedColumnName="ID_GARANTIA", insertable=false, updatable=false)
	private RugGarantias garantia;

	public RugCertificaciones() {
	}

	public long getIdTramiteCert() {
		return this.idTramiteCert;
	}

	public void setIdTramiteCert(long idTramiteCert) {
		this.idTramiteCert = idTramiteCert;
	}

	public Date getFechaCert() {
		return this.fechaCert;
	}

	public void setFechaCert(Date fechaCert) {
		this.fechaCert = fechaCert;
	}

	public BigDecimal getIdGarantia() {
		return this.idGarantia;
	}

	public void setIdGarantia(BigDecimal idGarantia) {
		this.idGarantia = idGarantia;
	}

	public BigDecimal getIdPersonaLogin() {
		return this.idPersonaLogin;
	}

	public void setIdPersonaLogin(BigDecimal idPersonaLogin) {
		this.idPersonaLogin = idPersonaLogin;
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

	public BigDecimal getPrecio() {
		return this.precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

	public RugGarantias getGarantia() {
		return garantia;
	}

	public void setGarantia(RugGarantias garantia) {
		this.garantia = garantia;
	}
}