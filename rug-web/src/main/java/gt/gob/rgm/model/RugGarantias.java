package gt.gob.rgm.model;

import org.apache.james.mime4j.field.datetime.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="RUG_GARANTIAS")
public class RugGarantias  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ID_GARANTIA")
    private Integer id;

    @Column(name="ID_TIPO_GARANTIA")
    private Integer idTipoGarantia;

    @Column(name="NUM_GARANTIA")
    private Integer numGarantia;

    @Column(name="DESC_GARANTIA")
    private String descGarantia;

    @Column(name="MESES_GARANTIA")
    private Integer mesesGarantia;

    @Column(name="ID_PERSONA")
    private Integer idPersona;

    @Column(name="ID_ANOTACION")
    private Integer idAnotacion;

    @Column(name="ID_RELACION")
    private Integer idRelacion;

    @Column(name="RELACION_BIEN")
    private Integer relacionBien;

    @Column(name="VALOR_BIENES")
    private String valorBien;

    @Column(name="TIPOS_BIENES_MUEBLES")
    private String tipoBienesMuebles;

    @Column(name="UBICACION_BIENES")
    private String ubicacionBienes;

    @Column(name="FOLIO_MERCANTIL")
    private String folioMercantil;

    @Column(name="PATH_DOC_GARANTIA")
    private String pathDocGarantia;

    @Column(name="OTROS_TERMINOS_GARANTIA")
    private String otrosTerminosGarantia;

    @Column(name="FECHA_INSCR")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInsc;

    @Column(name="FECHA_FIN_GAR")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinGarantia;

    @Column(name="VIGENCIA")
    private Float vigencia;

    @Column(name="GARANTIA_CERTIFICADA")
    private String garantiaCertificada;

    @Column(name="GARANTIA_STATUS")
    private String garantiaStatus;

    @Column(name="ID_ULTIMO_TRAMITE")
    private Integer idUltimoTramite;

    @Column(name="B_ULTIMO_TRAMITE")
    private String bUltimoTramite;

    @Column(name="MONTO_MAXIMO_GARANTIZADO")
    private String montoMaximoGarantizado;

    @Column(name="ID_GARANTIA_PEND")
    private Integer idGarantiaPen;

    @Column(name="FECHA_REG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReg;

    @Column(name="STATUS_REG")
    private String statusReg;

    @Column(name="CAMBIOS_BIENES_MONTO")
    private String cambiosBienesMonto;

    @Column(name="INSTRUMENTO_PUBLICO")
    private String instrumentoPublico;

    @Column(name="ID_MONEDA")
    private Integer idModena;

    @Column(name="NO_GARANTIA_PREVIA_OT")
    private String noGarantiaPreviaOT;

    @Column(name="ES_PRIORITARIA")
    private String esPrioritaria;

    @Column(name="OTROS_REGISTROS")
    private String otrosRegistros;

    @Column(name="TXT_REGISTROS")
    private String txtRegistros;

    public RugGarantias(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTipoGarantia() {
        return idTipoGarantia;
    }

    public void setIdTipoGarantia(Integer idTipoGarantia) {
        this.idTipoGarantia = idTipoGarantia;
    }

    public Integer getNumGarantia() {
        return numGarantia;
    }

    public void setNumGarantia(Integer numGarantia) {
        this.numGarantia = numGarantia;
    }

    public String getDescGarantia() {
        return descGarantia;
    }

    public void setDescGarantia(String descGarantia) {
        this.descGarantia = descGarantia;
    }

    public Integer getMesesGarantia() {
        return mesesGarantia;
    }

    public void setMesesGarantia(Integer mesesGarantia) {
        this.mesesGarantia = mesesGarantia;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public Integer getIdAnotacion() {
        return idAnotacion;
    }

    public void setIdAnotacion(Integer idAnotacion) {
        this.idAnotacion = idAnotacion;
    }

    public Integer getIdRelacion() {
        return idRelacion;
    }

    public void setIdRelacion(Integer idRelacion) {
        this.idRelacion = idRelacion;
    }

    public Integer getRelacionBien() {
        return relacionBien;
    }

    public void setRelacionBien(Integer relacionBien) {
        this.relacionBien = relacionBien;
    }

    public String getValorBien() {
        return valorBien;
    }

    public void setValorBien(String valorBien) {
        this.valorBien = valorBien;
    }

    public String getTipoBienesMuebles() {
        return tipoBienesMuebles;
    }

    public void setTipoBienesMuebles(String tipoBienesMuebles) {
        this.tipoBienesMuebles = tipoBienesMuebles;
    }

    public String getUbicacionBienes() {
        return ubicacionBienes;
    }

    public void setUbicacionBienes(String ubicacionBienes) {
        this.ubicacionBienes = ubicacionBienes;
    }

    public String getFolioMercantil() {
        return folioMercantil;
    }

    public void setFolioMercantil(String folioMercantil) {
        this.folioMercantil = folioMercantil;
    }

    public String getPathDocGarantia() {
        return pathDocGarantia;
    }

    public void setPathDocGarantia(String pathDocGarantia) {
        this.pathDocGarantia = pathDocGarantia;
    }

    public String getOtrosTerminosGarantia() {
        return otrosTerminosGarantia;
    }

    public void setOtrosTerminosGarantia(String otrosTerminosGarantia) {
        this.otrosTerminosGarantia = otrosTerminosGarantia;
    }

    public Date getFechaInsc() {
        return fechaInsc;
    }

    public void setFechaInsc(Date fechaInsc) {
        this.fechaInsc = fechaInsc;
    }

    public Date getFechaFinGarantia() {
        return fechaFinGarantia;
    }

    public void setFechaFinGarantia(Date fechaFinGarantia) {
        this.fechaFinGarantia = fechaFinGarantia;
    }

    public Float getVigencia() {
        return vigencia;
    }

    public void setVigencia(Float vigencia) {
        this.vigencia = vigencia;
    }

    public String getGarantiaCertificada() {
        return garantiaCertificada;
    }

    public void setGarantiaCertificada(String garantiaCertificada) {
        this.garantiaCertificada = garantiaCertificada;
    }

    public String getGarantiaStatus() {
        return garantiaStatus;
    }

    public void setGarantiaStatus(String garantiaStatus) {
        this.garantiaStatus = garantiaStatus;
    }

    public Integer getIdUltimoTramite() {
        return idUltimoTramite;
    }

    public void setIdUltimoTramite(Integer idUltimoTramite) {
        this.idUltimoTramite = idUltimoTramite;
    }

    public String getbUltimoTramite() {
        return bUltimoTramite;
    }

    public void setbUltimoTramite(String bUltimoTramite) {
        this.bUltimoTramite = bUltimoTramite;
    }

    public String getMontoMaximoGarantizado() {
        return montoMaximoGarantizado;
    }

    public void setMontoMaximoGarantizado(String montoMaximoGarantizado) {
        this.montoMaximoGarantizado = montoMaximoGarantizado;
    }

    public Integer getIdGarantiaPen() {
        return idGarantiaPen;
    }

    public void setIdGarantiaPen(Integer idGarantiaPen) {
        this.idGarantiaPen = idGarantiaPen;
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public String getStatusReg() {
        return statusReg;
    }

    public void setStatusReg(String statusReg) {
        this.statusReg = statusReg;
    }

    public String getCambiosBienesMonto() {
        return cambiosBienesMonto;
    }

    public void setCambiosBienesMonto(String cambiosBienesMonto) {
        this.cambiosBienesMonto = cambiosBienesMonto;
    }

    public String getInstrumentoPublico() {
        return instrumentoPublico;
    }

    public void setInstrumentoPublico(String instrumentoPublico) {
        this.instrumentoPublico = instrumentoPublico;
    }

    public Integer getIdModena() {
        return idModena;
    }

    public void setIdModena(Integer idModena) {
        this.idModena = idModena;
    }

    public String getNoGarantiaPreviaOT() {
        return noGarantiaPreviaOT;
    }

    public void setNoGarantiaPreviaOT(String noGarantiaPreviaOT) {
        this.noGarantiaPreviaOT = noGarantiaPreviaOT;
    }

    public String getEsPrioritaria() {
        return esPrioritaria;
    }

    public void setEsPrioritaria(String esPrioritaria) {
        this.esPrioritaria = esPrioritaria;
    }

    public String getOtrosRegistros() {
        return otrosRegistros;
    }

    public void setOtrosRegistros(String otrosRegistros) {
        this.otrosRegistros = otrosRegistros;
    }

    public String getTxtRegistros() {
        return txtRegistros;
    }

    public void setTxtRegistros(String txtRegistros) {
        this.txtRegistros = txtRegistros;
    }
}
