package gt.gob.rgm.adm.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "V_TRAMITES_REPORTE")
@NamedQuery(name="vTramitesPagados.findAll", query="SELECT v FROM VTramitesPagados v")
public class VTramitesPagados implements Serializable {
    private static final long serialVersionUID = 1L;

    public VTramitesPagados() {
    }

    @Id

    @Column(name="ID_TRAMITE")
    private String idTramite;

    @Column(name="ID_GARANTIA")
    private String idGarantia;

    @Column(name="DESCRIPCION")
    private String descripcion;

    @Column(name="PRECIO")
    private String precio;

    @Id
    @Column(name="ID_PERSONA_LOGIN")
    private Integer idPersonaLogin;

    @Column(name="FECHA")
    private String fecha;

    @Column(name="ANIO")
    private String anio;


    public String getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(String idTramite) {
        this.idTramite = idTramite;
    }

    public String getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(String idGarantia) {
        this.idGarantia = idGarantia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Integer getIdPersonaLogin() {
        return idPersonaLogin;
    }

    public void setIdPersonaLogin(Integer idPersonaLogin) {
        this.idPersonaLogin = idPersonaLogin;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }
}
