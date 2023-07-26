package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the V_DETALLE_GARANTIA database table.
 * 
 */
@Entity
@Table(name="V_DETALLE_GARANTIA")
@NamedQuery(name="VDetalleGarantia.findAll", query="SELECT v FROM VDetalleGarantia v")
public class VDetalleGarantia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CAMBIOS_BIENES_MONTO")
	private String cambiosBienesMonto;

	@Column(name="CLASIF_CONTRATO")
	private String clasifContrato;

	@Lob
	@Column(name="DESC_BIENES_MUEBLES")
	private String descBienesMuebles;

	@Column(name="DESC_MONEDA")
	private String descMoneda;

	@Column(name="ES_PRIORITARIA")
	private String esPrioritaria;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_CELEB_ACTO")
	private Date fechaCelebActo;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_CELEB_CONTRATO")
	private Date fechaCelebContrato;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_FIN_ACTO")
	private Date fechaFinActo;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_FIN_OB")
	private Date fechaFinOb;

	@Column(name="ID_CONTRATO")
	private BigDecimal idContrato;

	@Id
	@Column(name="ID_GARANTIA")
	private BigDecimal idGarantia;

	@Column(name="ID_MONEDA")
	private BigDecimal idMoneda;

	@Column(name="ID_TIPO_GARANTIA")
	private BigDecimal idTipoGarantia;

	@Column(name="ID_TRAMITE")
	private BigDecimal idTramite;

	@Column(name="INSTRUMENTO_PUBLICO")
	private String instrumentoPublico;

	@Column(name="MONTO_LIMITE")
	private BigDecimal montoLimite;

	@Column(name="NO_GARANTIA_PREVIA_OT")
	private String noGarantiaPreviaOt;

	private String observaciones;

	@Column(name="OTROS_REGISTROS")
	private String otrosRegistros;

	@Lob
	@Column(name="OTROS_TERMINOS_CONTRATO")
	private String otrosTerminosContrato;

	@Lob
	@Column(name="OTROS_TERMINOS_GARANTIA")
	private String otrosTerminosGarantia;

	@Column(name="RELACION_BIEN")
	private BigDecimal relacionBien;

	@Column(name="TIPO_CONTRATO")
	private String tipoContrato;

	@Column(name="TIPO_GARANTIA")
	private String tipoGarantia;

	@Column(name="TXT_REGISTROS")
	private String txtRegistros;

	private Integer vigencia;

	public VDetalleGarantia() {
	}

	public String getCambiosBienesMonto() {
		return this.cambiosBienesMonto;
	}

	public void setCambiosBienesMonto(String cambiosBienesMonto) {
		this.cambiosBienesMonto = cambiosBienesMonto;
	}

	public String getClasifContrato() {
		return this.clasifContrato;
	}

	public void setClasifContrato(String clasifContrato) {
		this.clasifContrato = clasifContrato;
	}

	public String getDescBienesMuebles() {
		return this.descBienesMuebles;
	}

	public void setDescBienesMuebles(String descBienesMuebles) {
		this.descBienesMuebles = descBienesMuebles;
	}

	public String getDescMoneda() {
		return this.descMoneda;
	}

	public void setDescMoneda(String descMoneda) {
		this.descMoneda = descMoneda;
	}

	public String getEsPrioritaria() {
		return this.esPrioritaria;
	}

	public void setEsPrioritaria(String esPrioritaria) {
		this.esPrioritaria = esPrioritaria;
	}

	public Date getFechaCelebActo() {
		return this.fechaCelebActo;
	}

	public void setFechaCelebActo(Date fechaCelebActo) {
		this.fechaCelebActo = fechaCelebActo;
	}

	public Date getFechaCelebContrato() {
		return this.fechaCelebContrato;
	}

	public void setFechaCelebContrato(Date fechaCelebContrato) {
		this.fechaCelebContrato = fechaCelebContrato;
	}

	public Date getFechaFinActo() {
		return this.fechaFinActo;
	}

	public void setFechaFinActo(Date fechaFinActo) {
		this.fechaFinActo = fechaFinActo;
	}

	public Date getFechaFinOb() {
		return this.fechaFinOb;
	}

	public void setFechaFinOb(Date fechaFinOb) {
		this.fechaFinOb = fechaFinOb;
	}

	public BigDecimal getIdContrato() {
		return this.idContrato;
	}

	public void setIdContrato(BigDecimal idContrato) {
		this.idContrato = idContrato;
	}

	public BigDecimal getIdGarantia() {
		return this.idGarantia;
	}

	public void setIdGarantia(BigDecimal idGarantia) {
		this.idGarantia = idGarantia;
	}

	public BigDecimal getIdMoneda() {
		return this.idMoneda;
	}

	public void setIdMoneda(BigDecimal idMoneda) {
		this.idMoneda = idMoneda;
	}

	public BigDecimal getIdTipoGarantia() {
		return this.idTipoGarantia;
	}

	public void setIdTipoGarantia(BigDecimal idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
	}

	public BigDecimal getIdTramite() {
		return this.idTramite;
	}

	public void setIdTramite(BigDecimal idTramite) {
		this.idTramite = idTramite;
	}

	public String getInstrumentoPublico() {
		return this.instrumentoPublico;
	}

	public void setInstrumentoPublico(String instrumentoPublico) {
		this.instrumentoPublico = instrumentoPublico;
	}

	public BigDecimal getMontoLimite() {
		return this.montoLimite;
	}

	public void setMontoLimite(BigDecimal montoLimite) {
		this.montoLimite = montoLimite;
	}

	public String getNoGarantiaPreviaOt() {
		return this.noGarantiaPreviaOt;
	}

	public void setNoGarantiaPreviaOt(String noGarantiaPreviaOt) {
		this.noGarantiaPreviaOt = noGarantiaPreviaOt;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getOtrosRegistros() {
		return this.otrosRegistros;
	}

	public void setOtrosRegistros(String otrosRegistros) {
		this.otrosRegistros = otrosRegistros;
	}

	public String getOtrosTerminosContrato() {
		return this.otrosTerminosContrato;
	}

	public void setOtrosTerminosContrato(String otrosTerminosContrato) {
		this.otrosTerminosContrato = otrosTerminosContrato;
	}

	public String getOtrosTerminosGarantia() {
		return this.otrosTerminosGarantia;
	}

	public void setOtrosTerminosGarantia(String otrosTerminosGarantia) {
		this.otrosTerminosGarantia = otrosTerminosGarantia;
	}

	public BigDecimal getRelacionBien() {
		return this.relacionBien;
	}

	public void setRelacionBien(BigDecimal relacionBien) {
		this.relacionBien = relacionBien;
	}

	public String getTipoContrato() {
		return this.tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public String getTipoGarantia() {
		return this.tipoGarantia;
	}

	public void setTipoGarantia(String tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}

	public String getTxtRegistros() {
		return this.txtRegistros;
	}

	public void setTxtRegistros(String txtRegistros) {
		this.txtRegistros = txtRegistros;
	}

	public Integer getVigencia() {
		return this.vigencia;
	}

	public void setVigencia(Integer vigencia) {
		this.vigencia = vigencia;
	}

}