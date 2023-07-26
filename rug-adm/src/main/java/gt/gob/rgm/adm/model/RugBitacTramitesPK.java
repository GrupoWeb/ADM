package gt.gob.rgm.adm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class RugBitacTramitesPK implements Serializable {
	@Column(name="ID_TRAMITE_TEMP", insertable=false, updatable=false)
	private Long idTramiteTemp;
	
	@Column(name="ID_STATUS")
	private Integer idStatus;
	
	@Column(name="ID_PASO")
	private Integer idPaso;
	
	@Column(name="ID_TIPO_TRAMITE")
	private Integer idTipoTramite;

	@Column(name="STATUS_REG")
	private String statusReg;

	public Long getIdTramiteTemp() {
		return this.idTramiteTemp;
	}

	public void setIdTramiteTemp(Long idTramiteTemp) {
		this.idTramiteTemp = idTramiteTemp;
	}

	public Integer getIdPaso() {
		return this.idPaso;
	}

	public void setIdPaso(Integer idPaso) {
		this.idPaso = idPaso;
	}

	public Integer getIdStatus() {
		return this.idStatus;
	}

	public Integer getIdTipoTramite() {
		return this.idTipoTramite;
	}

	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}
	
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	public int hashCode() {
		return super.hashCode();
	}
}
