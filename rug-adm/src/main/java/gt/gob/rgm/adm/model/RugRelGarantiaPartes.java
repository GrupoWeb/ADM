package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_REL_GARANTIA_PARTES database table.
 * 
 */
@Entity
@Table(name="RUG_REL_GARANTIA_PARTES")
@NamedQuery(name="RugRelGarantiaPartes.findAll", query="SELECT r FROM RugRelGarantiaPartes r")
public class RugRelGarantiaPartes implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RugRelGarantiaPartesPK id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Column(name="STATUS_REG")
	private String statusReg;

	public RugRelGarantiaPartes() {
	}

	public RugRelGarantiaPartesPK getId() {
		return id;
	}

	public void setId(RugRelGarantiaPartesPK id) {
		this.id = id;
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

}