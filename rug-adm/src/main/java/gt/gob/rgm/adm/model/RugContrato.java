package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_CONTRATO database table.
 * 
 */
@Entity
@Table(name="RUG_CONTRATO")
@NamedQuery(name="RugContrato.findAll", query="SELECT r FROM RugContrato r")
public class RugContrato implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RUG_CONTRATO_IDCONTRATO_GENERATOR", sequenceName="SEQ_CONTRATO", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUG_CONTRATO_IDCONTRATO_GENERATOR")
	@Column(name="ID_CONTRATO")
	private long idContrato;

	@Column(name="CLASIF_CONTRATO")
	private String clasifContrato;

	@Column(name="CONTRATO_NUM")
	private BigDecimal contratoNum;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_FIN")
	private Date fechaFin;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_INICIO")
	private Date fechaInicio;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Column(name="ID_GARANTIA_PEND")
	private Long idGarantiaPend;

	@Column(name="ID_TRAMITE_TEMP")
	private Long idTramiteTemp;

	@Column(name="ID_USUARIO")
	private BigDecimal idUsuario;

	@Column(name="MONTO_LIMITE")
	private double montoLimite;

	private String observaciones;
//
//	@Lob
	@Column(name="OTROS_TERMINOS_CONTRATO")
	private String otrosTerminosContrato;

       
        
	@Column(name="STATUS_REG")
	private String statusReg;

	@Column(name="TIPO_CONTRATO")
	private String tipoContrato;

	public RugContrato() {
	}

        
        
	public long getIdContrato() {
		return this.idContrato;
	}

	public void setIdContrato(long idContrato) {
		this.idContrato = idContrato;
	}

	public String getClasifContrato() {
		return this.clasifContrato;
	}

	public void setClasifContrato(String clasifContrato) {
		this.clasifContrato = clasifContrato;
	}

	public BigDecimal getContratoNum() {
		return this.contratoNum;
	}

	public void setContratoNum(BigDecimal contratoNum) {
		this.contratoNum = contratoNum;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaReg() {
		return this.fechaReg;
	}

	public void setFechaReg(Date fechaReg) {
		this.fechaReg = fechaReg;
	}

	public Long getIdGarantiaPend() {
		return this.idGarantiaPend;
	}

	public void setIdGarantiaPend(Long idGarantiaPend) {
		this.idGarantiaPend = idGarantiaPend;
	}

	public Long getIdTramiteTemp() {
		return this.idTramiteTemp;
	}

	public void setIdTramiteTemp(Long idTramiteTemp) {
		this.idTramiteTemp = idTramiteTemp;
	}

	public BigDecimal getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(BigDecimal idUsuario) {
		this.idUsuario = idUsuario;
	}

	public double getMontoLimite() {
		return this.montoLimite;
	}

	public void setMontoLimite(double montoLimite) {
		this.montoLimite = montoLimite;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getOtrosTerminosContrato() {
		return this.otrosTerminosContrato;
	}

	public void setOtrosTerminosContrato(String otrosTerminosContrato) {
		this.otrosTerminosContrato = otrosTerminosContrato;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

	public String getTipoContrato() {
		return this.tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

}