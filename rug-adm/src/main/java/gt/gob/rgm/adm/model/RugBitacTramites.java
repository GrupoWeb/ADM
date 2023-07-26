package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_BITAC_TRAMITES database table.
 * 
 */
@Entity
@Table(name="RUG_BITAC_TRAMITES")
@NamedQueries({
	@NamedQuery(name="RugBitacTramites.findAll", query="SELECT r FROM RugBitacTramites r"),
	@NamedQuery(name="RugBitacTramites.findByTramiteAndStatus", query="SELECT r FROM RugBitacTramites r WHERE r.id.idTramiteTemp = :idTramiteTemp AND r.id.idStatus = :idStatus")
})
public class RugBitacTramites implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private RugBitacTramitesPK id;
	
	@OneToOne
	@JoinColumn(name="ID_TRAMITE_TEMP")
	private TramitesRugIncomp tramiteIncomp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_STATUS")
	private Date fechaStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	public RugBitacTramites() {
	}

	public RugBitacTramitesPK getId() {
		return id;
	}

	public void setId(RugBitacTramitesPK id) {
		this.id = id;
	}

	public Date getFechaStatus() {
		return this.fechaStatus;
	}

	public void setFechaStatus(Date fechaStatus) {
		this.fechaStatus = fechaStatus;
	}
	
	public Date getFechaReg() {
		return this.fechaReg;
	}

	public void setFechaReg(Date fechaReg) {
		this.fechaReg = fechaReg;
	}

	public TramitesRugIncomp getTramiteIncomp() {
		return tramiteIncomp;
	}

	public void setTramiteIncomp(TramitesRugIncomp tramiteIncomp) {
		this.tramiteIncomp = tramiteIncomp;
	}
}