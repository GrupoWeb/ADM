package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the TRAMITES database table.
 *
 */
@Entity
@Table(name = "TRAMITES")
@NamedQuery(name = "Tramites.findAll", query = "SELECT t FROM Tramites t")
public class Tramites implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_TRAMITE")
    private long idTramite;

//    @OneToOne
//    @JoinColumn(name = "ID_TRAMITE", insertable = false, updatable = false)
//    private RugGarantiasBienes idGaranBien;
    
    @Column(name = "B_CARGA_MASIVA")
    private Integer bCargaMasiva;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECH_PRE_INSCR")
    private Date fechPreInscr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INSCR")
    private Date fechaInscr;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_STATUS")
    private Date fechaStatus;

    @Column(name = "ID_PASO")
    private Integer idPaso;

    @Column(name = "ID_PERSONA")
    private Long idPersona;

    @Column(name = "ID_STATUS_TRAM")
    private Integer idStatusTram;

    @Column(name = "ID_TRAMITE_TEMP")
    private Long idTramiteTemp;

    @OneToOne
    @JoinColumn(name = "ID_TRAMITE_TEMP", insertable = false, updatable = false)
    private TramitesRugIncomp tramiteIncomp;

    //Join OneToMany Contratos
//    @OneToMany
//    @JoinColumn(name = "ID_TRAMITE_TEMP", insertable = false, updatable = false)
//    private RugContrato tramiteContrato;

    @Column(name = "STATUS_REG")
    private String statusReg;

    //bi-directional many-to-one association to RugGarantia
    /*@OneToOne
	@JoinColumn(name="ID_TRAMITE", referencedColumnName="ID_ULTIMO_TRAMITE", insertable=false, updatable=false)
	private RugGarantias garantia;*/
    //bi-directional many-to-one association to RugCatTipoTramite
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_TRAMITE")
    private RugCatTipoTramite tipoTramite;

    //@OneToMany(mappedBy="tramite", fetch=FetchType.LAZY)
    @OneToMany
    @JoinColumn(name = "ID_TRAMITE")
    private List<RugRelTramPartes> partes;
    
//    @ManyToOne
//    @JoinColumn(name = "ID_TRAMITE", insertable = false, updatable = false)
//    private RugGarantiasBienes Listabienes;

    
//    @OneToOne
//    @JoinColumn(name = "ID_TRAMITE", insertable = false, updatable = false)
//    private RugGarantiasBienes idGaranBien;

    @ManyToOne
    @JoinColumn(name = "ID_PERSONA", insertable = false, updatable = false)
    private RugSecuUsuario usuario;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAMITE", referencedColumnName = "ID_TRAMITE", insertable = false, updatable = false)
    private List<RugRelTramGaran> relGarantia;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAMITE_TEMP", referencedColumnName = "ID_TRAMITE_TEMP", insertable = false, updatable = false)
    private List<RugContrato> contrato;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAMITE", referencedColumnName = "ID_TRAMITE", insertable = false, updatable = false)
    private List<RugGarantiasBienes> ListBienes;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAMITE_CERT", referencedColumnName = "ID_TRAMITE", insertable = false, updatable = false)
    private List<RugCertificaciones> certificacion;

    public List<RugContrato> getContrato() {
        return contrato;
    }

    public void setContrato(List<RugContrato> contrato) {
        this.contrato = contrato;
    }


    public Tramites() {
    }
    
    

    public long getIdTramite() {
        return this.idTramite;
    }

    public void setIdTramite(long idTramite) {
        this.idTramite = idTramite;
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

    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public Long getIdTramiteTemp() {
        return this.idTramiteTemp;
    }

    public void setIdTramiteTemp(Long idTramiteTemp) {
        this.idTramiteTemp = idTramiteTemp;
    }

    public String getStatusReg() {
        return this.statusReg;
    }

    public TramitesRugIncomp getTramiteIncomp() {
        return tramiteIncomp;
    }

    public void setTramiteIncomp(TramitesRugIncomp tramiteIncomp) {
        this.tramiteIncomp = tramiteIncomp;
    }

    public void setStatusReg(String statusReg) {
        this.statusReg = statusReg;
    }

    /*public RugGarantias getGarantia() {
		return garantia;
	}

	public void setGarantia(RugGarantias garantia) {
		this.garantia = garantia;
	}*/
    public RugCatTipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(RugCatTipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public List<RugRelTramPartes> getPartes() {
        return this.partes;
    }

    public void setPartes(List<RugRelTramPartes> partes) {
        this.partes = partes;
    }

    /*public RugRelTramPartes addPartes(RugRelTramPartes partes) {
		getPartes().add(partes);
		partes.setTramite(this);

		return partes;
	}

	public RugRelTramPartes removePartes(RugRelTramPartes partes) {
		getPartes().remove(partes);
		partes.setTramite(null);

		return partes;
	}*/
    public RugSecuUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(RugSecuUsuario usuario) {
        this.usuario = usuario;
    }

    public List<RugRelTramGaran> getRelGarantia() {
        return relGarantia;
    }

    public void setRelGarantia(List<RugRelTramGaran> relGarantia) {
        this.relGarantia = relGarantia;
    }

    public List<RugCertificaciones> getCertificacion() {
        return certificacion;
    }

    public void setCertificacion(List<RugCertificaciones> certificacion) {
        this.certificacion = certificacion;
    }

//    /**
//     * @return the Listabienes
//     */
//    public RugGarantiasBienes getListabienes() {
//        return Listabienes;
//    }
//
//    /**
//     * @param Listabienes the Listabienes to set
//     */
//    public void setListabienes(RugGarantiasBienes Listabienes) {
//        this.Listabienes = Listabienes;
//    }

    /**
     * @return the ListBienes
     */
    public List<RugGarantiasBienes> getListBienes() {
        return ListBienes;
    }

    /**
     * @param ListBienes the ListBienes to set
     */
    public void setListBienes(List<RugGarantiasBienes> ListBienes) {
        this.ListBienes = ListBienes;
    }

   

    
}
