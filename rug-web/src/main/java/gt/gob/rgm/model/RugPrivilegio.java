package gt.gob.rgm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RUG_PRIVILEGIOS")
public class RugPrivilegio implements Serializable {
    private static final long serialVersionUID = 1L;

    public RugPrivilegio() {
    }

    @Id
    @Column(name = "ID_PRIVILEGIO")
    private long idPrivilegio;

    @Column(name = "DESC_PRIVILEGIO")
    private String descripcion;

    @Column(name = "HTML")
    private String html;

    @Column(name = "SIT_PRIVILEGIO")
    private String sitPrivilegio;

    @Column(name = "ID_RECURSO")
    private Integer idRecurso;

    @Column(name = "ORDEN")
    private Integer orden;


    public long getIdPrivilegio() {
        return idPrivilegio;
    }

    public void setIdPrivilegio(long idPrivilegio) {
        this.idPrivilegio = idPrivilegio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getSitPrivilegio() {
        return sitPrivilegio;
    }

    public void setSitPrivilegio(String sitPrivilegio) {
        this.sitPrivilegio = sitPrivilegio;
    }

    public Integer getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Integer idRecurso) {
        this.idRecurso = idRecurso;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}
