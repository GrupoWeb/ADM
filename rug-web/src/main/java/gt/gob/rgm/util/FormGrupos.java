package gt.gob.rgm.util;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

public class FormGrupos implements Serializable {
    private static final long serialVersionUID = 1L;

    public FormGrupos() {
    }

    @Column(name="DESC_GRUPO")
    private String descGrupo;

    @Temporal(TemporalType.DATE)
    @Column(name="FH_BAJA")
    private Date fhBaja;

    @Temporal(TemporalType.DATE)
    @Column(name="FH_CREACION")
    private Date fhCreacion;

    @Column(name="ID_ACREEDOR")
    private Long idAcreedor;

    @Column(name="ID_PERSONA_CREA")
    private Long idPersonaCrea;

    @Column(name="ID_USUARIO_BAJA")
    private Integer idUsuarioBaja;

    @Column(name="SIT_GRUPO")
    private String sitGrupo;

    public String getDescGrupo() {
        return descGrupo;
    }

    public void setDescGrupo(String descGrupo) {
        this.descGrupo = descGrupo;
    }

    public Date getFhBaja() {
        return fhBaja;
    }

    public void setFhBaja(Date fhBaja) {
        this.fhBaja = fhBaja;
    }

    public Date getFhCreacion() {
        return fhCreacion;
    }

    public void setFhCreacion(Date fhCreacion) {
        this.fhCreacion = fhCreacion;
    }

    public Long getIdAcreedor() {
        return idAcreedor;
    }

    public void setIdAcreedor(Long idAcreedor) {
        this.idAcreedor = idAcreedor;
    }

    public Long getIdPersonaCrea() {
        return idPersonaCrea;
    }

    public void setIdPersonaCrea(Long idPersonaCrea) {
        this.idPersonaCrea = idPersonaCrea;
    }

    public Integer getIdUsuarioBaja() {
        return idUsuarioBaja;
    }

    public void setIdUsuarioBaja(Integer idUsuarioBaja) {
        this.idUsuarioBaja = idUsuarioBaja;
    }

    public String getSitGrupo() {
        return sitGrupo;
    }

    public void setSitGrupo(String sitGrupo) {
        this.sitGrupo = sitGrupo;
    }
}
