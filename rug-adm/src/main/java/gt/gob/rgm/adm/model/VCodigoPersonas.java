package gt.gob.rgm.adm.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="V_CODIGO_PERSONAS")
@NamedQueries(
        @NamedQuery(name="VCodigoPersona.findByUser", query = "SELECT t FROM VCodigoPersonas t WHERE t.codigo = UPPER(:Codigo)")
)
public class VCodigoPersonas implements Serializable {
    private static final long serialVersionUID = 1L;

    public VCodigoPersonas() {
    }

    @Column(name = "CODIGO")
    private String codigo;

    @Id
    @Column(name = "ID_PERSONA")
    private Integer idPersona;

    @Column(name = "RFC")
    private String rfc;

    @Column(name = "PER_JURIDICA")
    private String perJuridica;

    @Column(name = "CURP_DOC")
    private String curpDoc;


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getPerJuridica() {
        return perJuridica;
    }

    public void setPerJuridica(String perJuridica) {
        this.perJuridica = perJuridica;
    }

    public String getCurpDoc() {
        return curpDoc;
    }

    public void setCurpDoc(String curpDoc) {
        this.curpDoc = curpDoc;
    }
}
