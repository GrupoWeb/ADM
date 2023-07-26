package gt.gob.rgm.model;

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

	//bi-directional many-to-one association to RugPersona
	@ManyToOne
	@JoinColumn(name="ID_ACREEDOR", insertable=false, updatable=false)
	private RugPersonas rugPersona;

	//bi-directional many-to-one association to RugPersonasFisica
	@ManyToOne
	@JoinColumn(name="ID_USUARIO", insertable=false, updatable=false)
	private RugPersonasFisicas rugPersonasFisica;

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

	public RugPersonas getRugPersona() {
		return this.rugPersona;
	}

	public void setRugPersona(RugPersonas rugPersona) {
		this.rugPersona = rugPersona;
	}

	public RugPersonasFisicas getRugPersonasFisica() {
		return this.rugPersonasFisica;
	}

	public void setRugPersonasFisica(RugPersonasFisicas rugPersonasFisica) {
		this.rugPersonasFisica = rugPersonasFisica;
	}

}