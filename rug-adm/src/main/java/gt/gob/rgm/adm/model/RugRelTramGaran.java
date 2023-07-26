package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the RUG_REL_TRAM_GARAN database table.
 * 
 */
@Entity
@Table(name="RUG_REL_TRAM_GARAN")
@NamedQuery(name="RugRelTramGaran.findAll", query="SELECT r FROM RugRelTramGaran r")
public class RugRelTramGaran implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RugRelTramGaranPK id;

	@Column(name="B_TRAMITE_COMPLETO")
	private String bTramiteCompleto;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Column(name="STATUS_REG")
	private String statusReg;
	
	@OneToOne
	@JoinColumn(name="ID_GARANTIA", referencedColumnName="ID_GARANTIA", insertable=false, updatable=false)
	private RugGarantias garantia;
	
	@OneToOne
	@JoinColumn(name="ID_TRAMITE", referencedColumnName="ID_TRAMITE", insertable=false, updatable=false)
	private Tramites tramite;

	public RugRelTramGaran() {
	}

	public RugRelTramGaranPK getId() {
		return this.id;
	}

	public void setId(RugRelTramGaranPK id) {
		this.id = id;
	}

	public String getBTramiteCompleto() {
		return this.bTramiteCompleto;
	}

	public void setBTramiteCompleto(String bTramiteCompleto) {
		this.bTramiteCompleto = bTramiteCompleto;
	}

	public Date getFechaReg() {
		return this.fechaReg;
	}

	public void setFechaReg(Date fechaReg) {
		this.fechaReg = fechaReg;
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

	public Tramites getTramite() {
		return tramite;
	}

	public void setTramite(Tramites tramite) {
		this.tramite = tramite;
	}
}