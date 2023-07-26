package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_GARANTIAS_H database table.
 * 
 */
@Entity
@Table(name="RUG_GARANTIAS_H")
@NamedQueries({
	@NamedQuery(name="RugGarantiasH.findAll", query="SELECT r FROM RugGarantiasH r"),
	@NamedQuery(name="RugGarantiasH.findByTramite", query="SELECT r FROM RugGarantiasH r WHERE r.idUltimoTramite = :idUltimoTramite")
})
public class RugGarantiasH implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RUG_GARANTIAS_H_IDREGISTRO_GENERATOR", sequenceName="SEQ_REG_GARANTIA_H")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUG_GARANTIAS_H_IDREGISTRO_GENERATOR")
	@Column(name="ID_REGISTRO")
	private BigDecimal idRegistro;

	@Column(name="B_ULTIMO_TRAMITE")
	private String bUltimoTramite;

	@Column(name="CAMBIOS_BIENES_MONTO")
	private String cambiosBienesMonto;

	@Lob
	@Column(name="DESC_GARANTIA")
	private String descGarantia;

	@Column(name="ES_PRIORITARIA")
	private String esPrioritaria;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_FIN_GAR")
	private Date fechaFinGar;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_INSCR")
	private Date fechaInscr;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_MODIF_REG")
	private Date fechaModifReg;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Column(name="FOLIO_MERCANTIL")
	private String folioMercantil;

	@Column(name="GARANTIA_CERTIFICADA")
	private String garantiaCertificada;

	@Column(name="GARANTIA_STATUS")
	private String garantiaStatus;

	@Column(name="ID_ANOTACION")
	private BigDecimal idAnotacion;

	@Column(name="ID_GARANTIA")
	private Long idGarantia;

	@Column(name="ID_GARANTIA_PEND")
	private Long idGarantiaPend;

	@Column(name="ID_MONEDA")
	private Integer idMoneda;

	@Column(name="ID_PERSONA")
	private Long idPersona;

	@Column(name="ID_RELACION")
	private Long idRelacion;

	@Column(name="ID_TIPO_GARANTIA")
	private Integer idTipoGarantia;

	@Column(name="ID_ULTIMO_TRAMITE")
	private Long idUltimoTramite;

	@Column(name="INSTRUMENTO_PUBLICO")
	private String instrumentoPublico;

	@Column(name="MESES_GARANTIA")
	private Integer mesesGarantia;

	@Column(name="MONTO_MAXIMO_GARANTIZADO")
	private BigDecimal montoMaximoGarantizado;

	@Column(name="NO_GARANTIA_PREVIA_OT")
	private String noGarantiaPreviaOt;

	@Column(name="NUM_GARANTIA")
	private Long numGarantia;

	@Column(name="OTROS_REGISTROS")
	private String otrosRegistros;

	@Lob
	@Column(name="OTROS_TERMINOS_GARANTIA")
	private String otrosTerminosGarantia;

	@Column(name="PATH_DOC_GARANTIA")
	private String pathDocGarantia;

	@Column(name="RELACION_BIEN")
	private BigDecimal relacionBien;

	@Column(name="STATUS_REG")
	private String statusReg;

	@Column(name="TIPOS_BIENES_MUEBLES")
	private String tiposBienesMuebles;

	@Column(name="TXT_REGISTROS")
	private String txtRegistros;

	@Column(name="UBICACION_BIENES")
	private String ubicacionBienes;

	@Column(name="VALOR_BIENES")
	private double valorBienes;

	private Integer vigencia;

	public RugGarantiasH() {
	}

	public String getBUltimoTramite() {
		return this.bUltimoTramite;
	}

	public void setBUltimoTramite(String bUltimoTramite) {
		this.bUltimoTramite = bUltimoTramite;
	}

	public String getCambiosBienesMonto() {
		return this.cambiosBienesMonto;
	}

	public void setCambiosBienesMonto(String cambiosBienesMonto) {
		this.cambiosBienesMonto = cambiosBienesMonto;
	}

	public String getDescGarantia() {
		return this.descGarantia;
	}

	public void setDescGarantia(String descGarantia) {
		this.descGarantia = descGarantia;
	}

	public String getEsPrioritaria() {
		return this.esPrioritaria;
	}

	public void setEsPrioritaria(String esPrioritaria) {
		this.esPrioritaria = esPrioritaria;
	}

	public Date getFechaFinGar() {
		return this.fechaFinGar;
	}

	public void setFechaFinGar(Date fechaFinGar) {
		this.fechaFinGar = fechaFinGar;
	}

	public Date getFechaInscr() {
		return this.fechaInscr;
	}

	public void setFechaInscr(Date fechaInscr) {
		this.fechaInscr = fechaInscr;
	}

	public Date getFechaModifReg() {
		return this.fechaModifReg;
	}

	public void setFechaModifReg(Date fechaModifReg) {
		this.fechaModifReg = fechaModifReg;
	}

	public Date getFechaReg() {
		return this.fechaReg;
	}

	public void setFechaReg(Date fechaReg) {
		this.fechaReg = fechaReg;
	}

	public String getFolioMercantil() {
		return this.folioMercantil;
	}

	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}

	public String getGarantiaCertificada() {
		return this.garantiaCertificada;
	}

	public void setGarantiaCertificada(String garantiaCertificada) {
		this.garantiaCertificada = garantiaCertificada;
	}

	public String getGarantiaStatus() {
		return this.garantiaStatus;
	}

	public void setGarantiaStatus(String garantiaStatus) {
		this.garantiaStatus = garantiaStatus;
	}

	public BigDecimal getIdAnotacion() {
		return this.idAnotacion;
	}

	public void setIdAnotacion(BigDecimal idAnotacion) {
		this.idAnotacion = idAnotacion;
	}

	public Long getIdGarantia() {
		return this.idGarantia;
	}

	public void setIdGarantia(Long idGarantia) {
		this.idGarantia = idGarantia;
	}

	public Long getIdGarantiaPend() {
		return this.idGarantiaPend;
	}

	public void setIdGarantiaPend(Long idGarantiaPend) {
		this.idGarantiaPend = idGarantiaPend;
	}

	public Integer getIdMoneda() {
		return this.idMoneda;
	}

	public void setIdMoneda(Integer idMoneda) {
		this.idMoneda = idMoneda;
	}

	public Long getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public BigDecimal getIdRegistro() {
		return this.idRegistro;
	}

	public void setIdRegistro(BigDecimal idRegistro) {
		this.idRegistro = idRegistro;
	}

	public Long getIdRelacion() {
		return this.idRelacion;
	}

	public void setIdRelacion(Long idRelacion) {
		this.idRelacion = idRelacion;
	}

	public Integer getIdTipoGarantia() {
		return this.idTipoGarantia;
	}

	public void setIdTipoGarantia(Integer idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
	}

	public Long getIdUltimoTramite() {
		return this.idUltimoTramite;
	}

	public void setIdUltimoTramite(Long idUltimoTramite) {
		this.idUltimoTramite = idUltimoTramite;
	}

	public String getInstrumentoPublico() {
		return this.instrumentoPublico;
	}

	public void setInstrumentoPublico(String instrumentoPublico) {
		this.instrumentoPublico = instrumentoPublico;
	}

	public Integer getMesesGarantia() {
		return this.mesesGarantia;
	}

	public void setMesesGarantia(Integer mesesGarantia) {
		this.mesesGarantia = mesesGarantia;
	}

	public BigDecimal getMontoMaximoGarantizado() {
		return this.montoMaximoGarantizado;
	}

	public void setMontoMaximoGarantizado(BigDecimal montoMaximoGarantizado) {
		this.montoMaximoGarantizado = montoMaximoGarantizado;
	}

	public String getNoGarantiaPreviaOt() {
		return this.noGarantiaPreviaOt;
	}

	public void setNoGarantiaPreviaOt(String noGarantiaPreviaOt) {
		this.noGarantiaPreviaOt = noGarantiaPreviaOt;
	}

	public Long getNumGarantia() {
		return this.numGarantia;
	}

	public void setNumGarantia(Long numGarantia) {
		this.numGarantia = numGarantia;
	}

	public String getOtrosRegistros() {
		return this.otrosRegistros;
	}

	public void setOtrosRegistros(String otrosRegistros) {
		this.otrosRegistros = otrosRegistros;
	}

	public String getOtrosTerminosGarantia() {
		return this.otrosTerminosGarantia;
	}

	public void setOtrosTerminosGarantia(String otrosTerminosGarantia) {
		this.otrosTerminosGarantia = otrosTerminosGarantia;
	}

	public String getPathDocGarantia() {
		return this.pathDocGarantia;
	}

	public void setPathDocGarantia(String pathDocGarantia) {
		this.pathDocGarantia = pathDocGarantia;
	}

	public BigDecimal getRelacionBien() {
		return this.relacionBien;
	}

	public void setRelacionBien(BigDecimal relacionBien) {
		this.relacionBien = relacionBien;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

	public String getTiposBienesMuebles() {
		return this.tiposBienesMuebles;
	}

	public void setTiposBienesMuebles(String tiposBienesMuebles) {
		this.tiposBienesMuebles = tiposBienesMuebles;
	}

	public String getTxtRegistros() {
		return this.txtRegistros;
	}

	public void setTxtRegistros(String txtRegistros) {
		this.txtRegistros = txtRegistros;
	}

	public String getUbicacionBienes() {
		return this.ubicacionBienes;
	}

	public void setUbicacionBienes(String ubicacionBienes) {
		this.ubicacionBienes = ubicacionBienes;
	}

	public double getValorBienes() {
		return this.valorBienes;
	}

	public void setValorBienes(double valorBienes) {
		this.valorBienes = valorBienes;
	}

	public Integer getVigencia() {
		return this.vigencia;
	}

	public void setVigencia(Integer vigencia) {
		this.vigencia = vigencia;
	}

}