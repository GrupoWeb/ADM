package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the RUG_REL_TRAM_INC_PARTES database table.
 * 
 */
@Entity
@Table(name="RUG_REL_TRAM_INC_PARTES")
@NamedQuery(name="RugRelTramIncParte.findAll", query="SELECT r FROM RugRelTramIncParte r")
public class RugRelTramIncParte implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RugRelTramIncPartePK id;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Column(name="PER_JURIDICA")
	private String perJuridica;

	@Column(name="STATUS_REG")
	private String statusReg;

	public RugRelTramIncParte() {
	}

	public RugRelTramIncPartePK getId() {
		return this.id;
	}

	public void setId(RugRelTramIncPartePK id) {
		this.id = id;
	}

	public Date getFechaReg() {
		return this.fechaReg;
	}

	public void setFechaReg(Date fechaReg) {
		this.fechaReg = fechaReg;
	}

	public String getPerJuridica() {
		return this.perJuridica;
	}

	public void setPerJuridica(String perJuridica) {
		this.perJuridica = perJuridica;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

}