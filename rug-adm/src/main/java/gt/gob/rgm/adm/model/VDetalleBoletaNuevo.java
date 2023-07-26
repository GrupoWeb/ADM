package gt.gob.rgm.adm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="V_DETALLE_BOLETA_NUEVO")
@NamedQuery(name="VDetalleBoletaNuevo.findAll", query="SELECT v FROM VDetalleBoletaNuevo v")
public class VDetalleBoletaNuevo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name="CAMBIOS_BIENES_MONTO")
	private String cambiosBienesMonto;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_ACTO_CONVENIO")
	private Date fechaActoConvenio;

	@Id
	@Column(name="ID_GARANTIA")
	private BigDecimal idGarantia;

	@Column(name="ID_TIPO_GARANTIA")
	private BigDecimal idTipoGarantia;

	@Column(name="ID_TRAMITE")
	private BigDecimal idTramite;
	
	@Column(name="ID_TIPO_TRAMITE")
	private BigDecimal idTipoTramite;

	@Column(name="INSTRUMENTO_PUBLICO")
	private String instrumentoPublico;

	@Column(name="MONTO_MAXIMO_GARANTIZADO")
	private String montoMaximoGar;

	@Column(name="NO_GARANTIA_PREVIA_OT")
	private String noGarantiaPreviaOt;

	private String observaciones;

	@Lob
	@Column(name="OTROS_TERMINOS_CONTRATO")
	private String otrosTerminosContrato;

	@Lob
	@Column(name="OTROS_TERMINOS_GARANTIA")
	private String otrosTerminosGarantia;

	@Column(name="TIPO_CONTRATO")
	private String tipoContrato;

	@Column(name="TIPO_GARANTIA")
	private String tipoGarantia;

	private Integer vigencia;
	
	@Lob
	@Column(name="CONTENIDO_RESOL")
	private String contenidoResol;
	
	@Column(name="ANOTACION_JUEZ")
	private String anotacionJuez;
	
	@Column(name="TIPO_TRAMITE")
	private String tipoTramite;
	
	@Column(name="TIPOS_BIENES_GARANTIA")
	private String tiposBienesGarantia;
	
	@Lob
	@Column(name="DESC_GARANTIA")
	private String descGarantia;
	
	@Column(name="ID_USUARIO")
	private BigDecimal idUsuario;
	
	@Column(name="CVE_PERFIL")
	private String cvePerfil;
	
	@Column(name="NOMBRE_USUARIO")
	private String nombreUsuario;
	
	@Column(name="FOLIO_OTORGANTE")
	private String folioOtorgante;
	
	@Column(name="TIPO_CONTRATO_FU")
	private String tipoContratoFU;
	
	@Column(name="OTROS_TERMINOS_CONTRATO_FU")
	private String otrosTerminosContratoFU;
	
	@Column(name="FECHA_CREACION")
	private String fechaCreacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_FIN_CONTRATO")
	private Date fechaFinContrato;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_INICIO_CONTRATO")
	private Date fechaInicioContrato;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_INICIO_CONTRATO_FU")
	private Date fechaInicioContratoFU;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_FIN_CONTRATO_FU")
	private Date fechaFinContratoFU;
	
	public VDetalleBoletaNuevo() {	
	}

	public String getCambiosBienesMonto() {
		return cambiosBienesMonto;
	}

	public void setCambiosBienesMonto(String cambiosBienesMonto) {
		this.cambiosBienesMonto = cambiosBienesMonto;
	}

	public Date getFechaActoConvenio() {
		return fechaActoConvenio;
	}

	public void setFechaActoConvenio(Date fechaActoConvenio) {
		this.fechaActoConvenio = fechaActoConvenio;
	}

	public BigDecimal getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(BigDecimal idGarantia) {
		this.idGarantia = idGarantia;
	}

	public BigDecimal getIdTipoGarantia() {
		return idTipoGarantia;
	}

	public void setIdTipoGarantia(BigDecimal idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
	}

	public BigDecimal getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(BigDecimal idTramite) {
		this.idTramite = idTramite;
	}

	public String getInstrumentoPublico() {
		return instrumentoPublico;
	}

	public void setInstrumentoPublico(String instrumentoPublico) {
		this.instrumentoPublico = instrumentoPublico;
	}

	public String getMontoMaximoGar() {
		return montoMaximoGar;
	}

	public void setMontoMaximoGar(String montoMaximoGar) {
		this.montoMaximoGar = montoMaximoGar;
	}

	public String getNoGarantiaPreviaOt() {
		return noGarantiaPreviaOt;
	}

	public void setNoGarantiaPreviaOt(String noGarantiaPreviaOt) {
		this.noGarantiaPreviaOt = noGarantiaPreviaOt;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getOtrosTerminosContrato() {
		return otrosTerminosContrato;
	}

	public void setOtrosTerminosContrato(String otrosTerminosContrato) {
		this.otrosTerminosContrato = otrosTerminosContrato;
	}

	public String getOtrosTerminosGarantia() {
		return otrosTerminosGarantia;
	}

	public void setOtrosTerminosGarantia(String otrosTerminosGarantia) {
		this.otrosTerminosGarantia = otrosTerminosGarantia;
	}

	public String getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public String getTipoGarantia() {
		return tipoGarantia;
	}

	public void setTipoGarantia(String tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}

	public Integer getVigencia() {
		return vigencia;
	}

	public void setVigencia(Integer vigencia) {
		this.vigencia = vigencia;
	}

	public String getContenidoResol() {
		return contenidoResol;
	}

	public void setContenidoResol(String contenidoResol) {
		this.contenidoResol = contenidoResol;
	}

	public String getAnotacionJuez() {
		return anotacionJuez;
	}

	public void setAnotacionJuez(String anotacionJuez) {
		this.anotacionJuez = anotacionJuez;
	}

	public String getTipoTramite() {
		return tipoTramite;
	}

	public void setTipoTramite(String tipoTramite) {
		this.tipoTramite = tipoTramite;
	}

	public String getTiposBienesGarantia() {
		return tiposBienesGarantia;
	}

	public void setTiposBienesGarantia(String tiposBienesGarantia) {
		this.tiposBienesGarantia = tiposBienesGarantia;
	}

	public String getDescGarantia() {
		return descGarantia;
	}

	public void setDescGarantia(String descGarantia) {
		this.descGarantia = descGarantia;
	}

	public BigDecimal getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(BigDecimal idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getCvePerfil() {
		return cvePerfil;
	}

	public void setCvePerfil(String cvePerfil) {
		this.cvePerfil = cvePerfil;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getFolioOtorgante() {
		return folioOtorgante;
	}

	public void setFolioOtorgante(String folioOtorgante) {
		this.folioOtorgante = folioOtorgante;
	}

	public String getTipoContratoFU() {
		return tipoContratoFU;
	}

	public void setTipoContratoFU(String tipoContratoFU) {
		this.tipoContratoFU = tipoContratoFU;
	}

	public String getOtrosTerminosContratoFU() {
		return otrosTerminosContratoFU;
	}

	public void setOtrosTerminosContratoFU(String otrosTerminosContratoFU) {
		this.otrosTerminosContratoFU = otrosTerminosContratoFU;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaFinContrato() {
		return fechaFinContrato;
	}

	public void setFechaFinContrato(Date fechaFinContrato) {
		this.fechaFinContrato = fechaFinContrato;
	}

	public Date getFechaInicioContrato() {
		return fechaInicioContrato;
	}

	public void setFechaInicioContrato(Date fechaInicioContrato) {
		this.fechaInicioContrato = fechaInicioContrato;
	}

	public Date getFechaInicioContratoFU() {
		return fechaInicioContratoFU;
	}

	public void setFechaInicioContratoFU(Date fechaInicioContratoFU) {
		this.fechaInicioContratoFU = fechaInicioContratoFU;
	}

	public Date getFechaFinContratoFU() {
		return fechaFinContratoFU;
	}

	public void setFechaFinContratoFU(Date fechaFinContratoFU) {
		this.fechaFinContratoFU = fechaFinContratoFU;
	}

	public BigDecimal getIdTipoTramite() {
		return idTipoTramite;
	}

	public void setIdTipoTramite(BigDecimal idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	
}
