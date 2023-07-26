package gt.gob.rgm.model;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RUG_REL_GRUPO_PRIVILEGIO")
public class RugRelGrupoPrivilegio implements Serializable {
    private static final long serialVersionUID = 1L;

    public RugRelGrupoPrivilegio() {
    }

    @Id
    @SequenceGenerator(name = "RUG_GRUPO_PRIVILEGIO", sequenceName = "SEQ_REL_GRUPO_PRIVILEGIO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RUG_GRUPO_PRIVILEGIO")
    @Column(name = "ID_RELACION")
    private long idRelacion;

    @Column(name="ID_GRUPO")
    private Long idGrupo;
    @Column(name="ID_PRIVILEGIO")
    private Long idPrivilegio;

    @Column(name = "SIT_RELACION")
    private String sitRelacion;

    @ManyToOne
    @JoinColumn(name = "ID_PRIVILEGIO", insertable=false, updatable=false)
    private RugPrivilegio privilegio;


    public RugPrivilegio getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(RugPrivilegio privilegio) {
        this.privilegio = privilegio;
    }

    public long getIdRelacion() {
        return idRelacion;
    }

    public void setIdRelacion(long idRelacion) {
        this.idRelacion = idRelacion;
    }

    public Long getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Long getIdPrivilegio() {
        return idPrivilegio;
    }

    public void setIdPrivilegio(Long idPrivilegio) {
        this.idPrivilegio = idPrivilegio;
    }

    public String getSitRelacion() {
        return sitRelacion;
    }

    public void setSitRelacion(String sitRelacion) {
        this.sitRelacion = sitRelacion;
    }
}
