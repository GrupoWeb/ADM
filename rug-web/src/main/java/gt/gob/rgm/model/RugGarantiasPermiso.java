package gt.gob.rgm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RUG_GARANTIA_PERMISO")
public class RugGarantiasPermiso implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "RUG_GARANTIA_PERMISO_SEQ", sequenceName = "SEQ_GARANTIA_PERMISO" , allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RUG_GARANTIA_PERMISO_SEQ")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_GARANTIA")
    private Integer idGarantia;

    @Column(name = "ID_TRAMITE")
    private Integer idTramite;

    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "SIT")
    private String sit;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_AT")
    private Date creadoEn;

    @Temporal(TemporalType.DATE)
    @Column(name = "UPDATE_AT")
    private Date actualizadoEn;

    @Temporal(TemporalType.DATE)
    @Column(name = "DELETED_AT")
    private Date inhabilitadoEn;


    public RugGarantiasPermiso() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(Integer idGarantia) {
        this.idGarantia = idGarantia;
    }

    public Integer getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getSit() {
        return sit;
    }

    public void setSit(String sit) {
        this.sit = sit;
    }

    public Date getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Date creadoEn) {
        this.creadoEn = creadoEn;
    }

    public Date getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(Date actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }

    public Date getInhabilitadoEn() {
        return inhabilitadoEn;
    }

    public void setInhabilitadoEn(Date inhabilitadoEn) {
        this.inhabilitadoEn = inhabilitadoEn;
    }

    @Override
    public String toString() {
        return "RugGarantiasPermiso{" +
                "id=" + id +
                ", idGarantia=" + idGarantia +
                ", idTramite=" + idTramite +
                ", idUsuario=" + idUsuario +
                ", sit='" + sit + '\'' +
                ", creadoEn=" + creadoEn +
                ", actualizadoEn=" + actualizadoEn +
                ", inhabilitadoEn=" + inhabilitadoEn +
                '}';
    }
}
