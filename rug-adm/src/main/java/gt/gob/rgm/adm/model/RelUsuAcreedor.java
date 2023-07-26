package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the REL_USU_ACREEDOR database table.
 * 
 */
@Entity
@Table(name="REL_USU_ACREEDOR")
@NamedQuery(name="RelUsuAcreedor.findAll", query="SELECT r FROM RelUsuAcreedor r")
public class RelUsuAcreedor implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RelUsuAcreedorPK id;

	@Column(name="B_FIRMADO")
	private String bFirmado;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Column(name="STATUS_REG")
	private String statusReg;

	public RelUsuAcreedor() {
	}

	public RelUsuAcreedorPK getId() {
		return this.id;
	}

	public void setId(RelUsuAcreedorPK id) {
		this.id = id;
	}

	public String getBFirmado() {
		return this.bFirmado;
	}

	public void setBFirmado(String bFirmado) {
		this.bFirmado = bFirmado;
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