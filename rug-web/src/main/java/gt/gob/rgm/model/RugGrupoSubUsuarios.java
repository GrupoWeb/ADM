package gt.gob.rgm.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RUG_GRUPO_SUBUSUARIOS")
public class RugGrupoSubUsuarios implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GRUPO_SUBUSUARIO", sequenceName = "SEQ_GRUPO_SUBUSUARIO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRUPO_SUBUSUARIO")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_USUARIO")
    private long idUsuario;

    @Column(name = "ID_GRUPO")
    private Integer idGrupo;

    @Column(name = "ID_GRUPO_ANTERIOR")
    private Integer idGrupoAnterior;


    public RugGrupoSubUsuarios() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Integer getIdGrupoAnterior() {
        return idGrupoAnterior;
    }

    public void setIdGrupoAnterior(Integer idGrupoAnterior) {
        this.idGrupoAnterior = idGrupoAnterior;
    }
}
