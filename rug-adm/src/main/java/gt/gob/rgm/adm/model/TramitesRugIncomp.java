package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TRAMITES_RUG_INCOMP database table.
 * 
 */
@Entity
@Table(name="TRAMITES_RUG_INCOMP")
@NamedQuery(name="TramitesRugIncomp.findAll", query="SELECT t FROM TramitesRugIncomp t")
public class TramitesRugIncomp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_TRAMITE_TEMP")
	private long idTramiteTemp;

	@Column(name="B_CARGA_MASIVA")
	private Integer bCargaMasiva;

	@Temporal(TemporalType.DATE)
	@Column(name="FECH_PRE_INSCR")
	private Date fechPreInscr;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_INSCR")
	private Date fechaInscr;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_STATUS")
	private Date fechaStatus;

	@Column(name="ID_PASO")
	private Integer idPaso;

	@Column(name="ID_PERSONA")
	private Long idPersona;

	@Column(name="ID_STATUS_TRAM")
	private Integer idStatusTram;

	@Column(name="ID_TIPO_TRAMITE")
	private Integer idTipoTramite;

	@Column(name="STATUS_REG")
	private String statusReg;

	public TramitesRugIncomp() {
	}

	public long getIdTramiteTemp() {
		return this.idTramiteTemp;
	}

	public void setIdTramiteTemp(long idTramiteTemp) {
		this.idTramiteTemp = idTramiteTemp;
	}

	public Integer getBCargaMasiva() {
		return this.bCargaMasiva;
	}

	public void setBCargaMasiva(Integer bCargaMasiva) {
		this.bCargaMasiva = bCargaMasiva;
	}

	public Date getFechPreInscr() {
		return this.fechPreInscr;
	}

	public void setFechPreInscr(Date fechPreInscr) {
		this.fechPreInscr = fechPreInscr;
	}

	public Date getFechaInscr() {
		return this.fechaInscr;
	}

	public void setFechaInscr(Date fechaInscr) {
		this.fechaInscr = fechaInscr;
	}

	public Date getFechaStatus() {
		return this.fechaStatus;
	}

	public void setFechaStatus(Date fechaStatus) {
		this.fechaStatus = fechaStatus;
	}

	public Integer getIdPaso() {
		return this.idPaso;
	}

	public void setIdPaso(Integer idPaso) {
		this.idPaso = idPaso;
	}

	public Long getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Integer getIdStatusTram() {
		return this.idStatusTram;
	}

	public void setIdStatusTram(Integer idStatusTram) {
		this.idStatusTram = idStatusTram;
	}

	public Integer getIdTipoTramite() {
		return this.idTipoTramite;
	}

	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

}