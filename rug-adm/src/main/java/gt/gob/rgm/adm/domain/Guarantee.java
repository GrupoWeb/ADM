package gt.gob.rgm.adm.domain;

import java.io.Serializable;
import java.util.List;

public class Guarantee implements Serializable {

    private Long idGarantia;
    private String descGarantia;
    private String esPrioritaria;
    private String fechaFinGar;
    private String fechaInscr;
    private String fechaReg;
    private String folioMercantil;
    private String garantiaStatus;
    private Integer idTipoGarantia;
    private String instrumentoPublico;
    private Integer mesesGarantia;
    private Long numGarantia;
    private String otrosTerminosGarantia;
    private String statusReg;
    private String tiposBienesMuebles;
    private String txtRegistros;
    private Double valorBienes;
    private Integer vigencia;
    private ExternalUser solicitante;
    private List<ExternalUser> deudores;
    private List<ExternalUser> acreedores;
    /*contrato*/
    private String tipoContrato;
    private String otrosTerminos;
    private String clasifContrato;
    private Long idContrato;
    private int cantidad;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
    
    
    private String original;

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }


    
    public String getOtrosTerminos() {
        return otrosTerminos;
    }
    

    public String getClasifContrato() {
        return clasifContrato;
    }

    public void setClasifContrato(String clasifContrato) {
        this.clasifContrato = clasifContrato;
    }

    public void setOtrosTerminos(String otrosTerminos) {
        this.otrosTerminos = otrosTerminos;
    }
    
    

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }


    public Guarantee() {
    }

    public Long getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(Long idGarantia) {
        this.idGarantia = idGarantia;
    }

    public String getDescGarantia() {
        return descGarantia;
    }

    public void setDescGarantia(String descGarantia) {
        this.descGarantia = descGarantia;
    }

    public String getEsPrioritaria() {
        return esPrioritaria;
    }

    public void setEsPrioritaria(String esPrioritaria) {
        this.esPrioritaria = esPrioritaria;
    }

    public String getFechaFinGar() {
        return fechaFinGar;
    }

    public void setFechaFinGar(String fechaFinGar) {
        this.fechaFinGar = fechaFinGar;
    }

    public String getFechaInscr() {
        return fechaInscr;
    }

    public void setFechaInscr(String fechaInscr) {
        this.fechaInscr = fechaInscr;
    }

    public String getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(String fechaReg) {
        this.fechaReg = fechaReg;
    }

    public String getFolioMercantil() {
        return folioMercantil;
    }

    public void setFolioMercantil(String folioMercantil) {
        this.folioMercantil = folioMercantil;
    }

    public String getGarantiaStatus() {
        return garantiaStatus;
    }

    public void setGarantiaStatus(String garantiaStatus) {
        this.garantiaStatus = garantiaStatus;
    }

    public Integer getIdTipoGarantia() {
        return idTipoGarantia;
    }

    public void setIdTipoGarantia(Integer idTipoGarantia) {
        this.idTipoGarantia = idTipoGarantia;
    }

    public String getInstrumentoPublico() {
        return instrumentoPublico;
    }

    public void setInstrumentoPublico(String instrumentoPublico) {
        this.instrumentoPublico = instrumentoPublico;
    }

    public Integer getMesesGarantia() {
        return mesesGarantia;
    }

    public void setMesesGarantia(Integer mesesGarantia) {
        this.mesesGarantia = mesesGarantia;
    }

    public Long getNumGarantia() {
        return numGarantia;
    }

    public void setNumGarantia(Long numGarantia) {
        this.numGarantia = numGarantia;
    }

    public String getOtrosTerminosGarantia() {
        return otrosTerminosGarantia;
    }

    public void setOtrosTerminosGarantia(String otrosTerminosGarantia) {
        this.otrosTerminosGarantia = otrosTerminosGarantia;
    }

    public String getStatusReg() {
        return statusReg;
    }

    public void setStatusReg(String statusReg) {
        this.statusReg = statusReg;
    }

    public String getTiposBienesMuebles() {
        return tiposBienesMuebles;
    }

    public void setTiposBienesMuebles(String tiposBienesMuebles) {
        this.tiposBienesMuebles = tiposBienesMuebles;
    }

    public String getTxtRegistros() {
        return txtRegistros;
    }

    public void setTxtRegistros(String txtRegistros) {
        this.txtRegistros = txtRegistros;
    }

    public Double getValorBienes() {
        return valorBienes;
    }

    public void setValorBienes(Double valorBienes) {
        this.valorBienes = valorBienes;
    }

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public ExternalUser getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(ExternalUser solicitante) {
        this.solicitante = solicitante;
    }

    public List<ExternalUser> getDeudores() {
        return deudores;
    }

    public void setDeudores(List<ExternalUser> deudores) {
        this.deudores = deudores;
    }

    public List<ExternalUser> getAcreedores() {
        return acreedores;
    }

    public void setAcreedores(List<ExternalUser> acreedores) {
        this.acreedores = acreedores;
    }
}
