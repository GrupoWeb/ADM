package gt.gob.rgm.adm.domain;

import java.io.Serializable;
import java.util.List;

public class Transaction implements Serializable {

    private Long idTramite;
    private Integer bCargaMasiva;
    private String fechaCreacion;
    private Integer idStatusTramite;
    private Long idTramiteTemp;
    private String statusReg;
    private Integer idTipoTramite;
    private String descripcion;
//    private String contrato;
    private Double precio;
    private ExternalUser solicitante;
    private Guarantee guarantee;
    private List<ExternalUser> deudores;
    private List<ExternalUser> acreedores;
    private List<String> controlCambios;
    private Bienes bienLista;

    public Transaction() {
    }

//    public String getContrato() {
//        return contrato;
//    }
//
//    public void setContrato(String contrato) {
//        this.contrato = contrato;
//    }

    public Long getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Long idTramite) {
        this.idTramite = idTramite;
    }

    public Integer getbCargaMasiva() {
        return bCargaMasiva;
    }

    public void setbCargaMasiva(Integer bCargaMasiva) {
        this.bCargaMasiva = bCargaMasiva;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdStatusTramite() {
        return idStatusTramite;
    }

    public void setIdStatusTramite(Integer idStatusTramite) {
        this.idStatusTramite = idStatusTramite;
    }

    public Long getIdTramiteTemp() {
        return idTramiteTemp;
    }

    public void setIdTramiteTemp(Long idTramiteTemp) {
        this.idTramiteTemp = idTramiteTemp;
    }

    public String getStatusReg() {
        return statusReg;
    }

    public void setStatusReg(String statusReg) {
        this.statusReg = statusReg;
    }

    public Integer getIdTipoTramite() {
        return idTipoTramite;
    }

    public void setIdTipoTramite(Integer idTipoTramite) {
        this.idTipoTramite = idTipoTramite;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public ExternalUser getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(ExternalUser solicitante) {
        this.solicitante = solicitante;
    }

    public Guarantee getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(Guarantee guarantee) {
        this.guarantee = guarantee;
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

    public List<String> getControlCambios() {
        return controlCambios;
    }

    public void setControlCambios(List<String> controlCambios) {
        this.controlCambios = controlCambios;
    }

    /**
     * @return the bienLista
     */
    public Bienes getBienLista() {
        return bienLista;
    }

    /**
     * @param bienLista the bienLista to set
     */
    public void setBienLista(Bienes bienLista) {
        this.bienLista = bienLista;
    }
}
