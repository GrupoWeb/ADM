package gt.gob.rgm.adm.domain;

import java.io.Serializable;
import java.util.Date;

public class Garantia implements Serializable {
    
    private Integer idGarantia;
    private Integer movs;
    private Integer inscr;
    private Integer cert;
    private Integer modif;
    private Integer cancelado;
    private Integer ejec;

    public Integer getProrr() {
        return prorr;
    }

    public void setProrr(Integer prorr) {
        this.prorr = prorr;
    }
    private Integer prorr;
    private Date fInscr;
    private Integer idTramite;
    private String dTramite;
    private Integer vigencia;
    private String dGarantia;
    private String gStatus;
    private String solicitante;
    private String sOriginal;
    private Integer idDeudor;
    private String nDeudor;
    private Integer idAcreedor;
    private String nAcreedor;
    private Integer idUsuario;
    private String nUsuario;
    private String textobusqueda;
    private String fechaInicio;
    private String fechaFin;

    public Integer getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(Integer idGarantia) {
        this.idGarantia = idGarantia;
    }

    public Integer getMovs() {
        return movs;
    }

    public void setMovs(Integer movs) {
        this.movs = movs;
    }

    public Integer getInscr() {
        return inscr;
    }

    public void setInscr(Integer inscr) {
        this.inscr = inscr;
    }

    public Integer getCert() {
        return cert;
    }

    public void setCert(Integer cert) {
        this.cert = cert;
    }

    public Integer getModif() {
        return modif;
    }

    public void setModif(Integer modif) {
        this.modif = modif;
    }

    public Integer getCancelado() {
        return cancelado;
    }

    public void setCancelado(Integer cancelado) {
        this.cancelado = cancelado;
    }

    public Integer getEjec() {
        return ejec;
    }

    public void setEjec(Integer ejec) {
        this.ejec = ejec;
    }

    public Date getfInscr() {
        return fInscr;
    }

    public void setfInscr(Date fInscr) {
        this.fInscr = fInscr;
    }

    public Integer getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public String getdTramite() {
        return dTramite;
    }

    public void setdTramite(String dTramite) {
        this.dTramite = dTramite;
    }

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public String getdGarantia() {
        return dGarantia;
    }

    public void setdGarantia(String dGarantia) {
        this.dGarantia = dGarantia;
    }

    public String getgStatus() {
        return gStatus;
    }

    public void setgStatus(String gStatus) {
        this.gStatus = gStatus;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getsOriginal() {
        return sOriginal;
    }

    public void setsOriginal(String sOriginal) {
        this.sOriginal = sOriginal;
    }

    public Integer getIdDeudor() {
        return idDeudor;
    }

    public void setIdDeudor(Integer idDeudor) {
        this.idDeudor = idDeudor;
    }

    public String getnDeudor() {
        return nDeudor;
    }

    public void setnDeudor(String nDeudor) {
        this.nDeudor = nDeudor;
    }

    public Integer getIdAcreedor() {
        return idAcreedor;
    }

    public void setIdAcreedor(Integer idAcreedor) {
        this.idAcreedor = idAcreedor;
    }

    public String getnAcreedor() {
        return nAcreedor;
    }

    public void setnAcreedor(String nAcreedor) {
        this.nAcreedor = nAcreedor;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getnUsuario() {
        return nUsuario;
    }

    public void setnUsuario(String nUsuario) {
        this.nUsuario = nUsuario;
    }

    public String getTextobusqueda() {
        return textobusqueda;
    }

    public void setTextobusqueda(String textobusqueda) {
        this.textobusqueda = textobusqueda;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
