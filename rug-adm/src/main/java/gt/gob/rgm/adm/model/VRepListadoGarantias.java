package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
/**
 * The persistent class for the V_REP_GARANTIAS database table.
 * 
 */

@Entity
@Table(name="V_REP_GARANTIAS")
@NamedQuery(name="VRepListadoGarantias.findAll", query="SELECT x FROM VRepListadoGarantias x")
public class VRepListadoGarantias implements Serializable {
	private static final long serialVersionUID = 1L;

        @Column(name="ID_GARANTIA")
	private Integer idGarantia;
        @Id
        @Column(name="MOVS")
	private Integer movs;

	@Column(name="INSCR")
	private Integer inscr;

	@Column(name="CERT")
	private Integer cert;

	@Column(name="MODIF")
	private Integer modif;
        
        @Column(name="CANCELADO")
	private Integer cancelado;

	@Column(name="EJEC")
	private Integer ejec;
        
        @Column(name="PRORROGA")
	private Integer prorr;
        
        @Column(name="FECHA_INSCR")
        @Temporal(javax.persistence.TemporalType.DATE)
	private Date fInscr;
        
        @Column(name="ID_TRAMITE")
	private Integer idTramite;
        
        @Column(name="DESCRIPCION_TRAMITE")
	private String dTramite;
        
        @Column(name="VIGENCIA")
	private Integer vigencia;
        
        @Column(name="DESC_GARANTIA")
	private String dGarantia;
        
        @Column(name="GARANTIA_STATUS")
	private String gStatus;
        
        @Column(name="SOLICITANTE")
	private String solicitante;
        
        @Column(name="SOLICITANTE_ORIGINAL")
	private String sOriginal;
        
        @Column(name="ID_DEUDOR")
	private Integer idDeudor;
        
        @Column(name="NOMBRE_DEUDOR")
	private String nDeudor;
        
        @Column(name="ID_ACREEDOR")
	private Integer idAcreedor;
        
        @Column(name="NOMBRE_ACREEDOR")
	private String nAcreedor;
        
        @Column(name="ID_USUARIO")
	private Integer idUsuario;
        
        @Column(name="NOMBRE_USUARIO")
	private String nUsuario;

      
	public VRepListadoGarantias() {
	}

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

   
    public String getnDeudor() {
        return nDeudor;
    }

    public void setnDeudor(String nDeudor) {
        this.nDeudor = nDeudor;
    }


    public String getnAcreedor() {
        return nAcreedor;
    }

    public void setnAcreedor(String nAcreedor) {
        this.nAcreedor = nAcreedor;
    }

    public Integer getIdDeudor() {
        return idDeudor;
    }

    public void setIdDeudor(Integer idDeudor) {
        this.idDeudor = idDeudor;
    }

    public Integer getIdAcreedor() {
        return idAcreedor;
    }

    public void setIdAcreedor(Integer idAcreedor) {
        this.idAcreedor = idAcreedor;
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

    public Integer getProrr() {
        return prorr;
    }

    public void setProrr(Integer prorr) {
        this.prorr = prorr;
    }
   
       
}
