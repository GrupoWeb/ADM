package gt.gob.rgm.util;

import java.io.Serializable;

public class FormPermisos implements Serializable {
    private static final long serialVersionUID = 1L;

    public FormPermisos() {}

    private Long idPersona;

    private Long idRelacion;

    private String sitRelacion;

    private String Nombre;

    private String isAdmin;

    private Long idOperacion;

    private Integer idUsuario;

    private Integer idGarantia;

    public Integer getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(Integer idGarantia) {
        this.idGarantia = idGarantia;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(Long idOperacion) {
        this.idOperacion = idOperacion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getSitRelacion() {
        return sitRelacion;
    }

    public void setSitRelacion(String sitRelacion) {
        this.sitRelacion = sitRelacion;
    }

    public Long getIdRelacion() {
        return idRelacion;
    }

    public void setIdRelacion(Long idRelacion) {
        this.idRelacion = idRelacion;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }
}
