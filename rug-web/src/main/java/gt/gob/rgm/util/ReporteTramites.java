package gt.gob.rgm.util;

import javax.enterprise.inject.Model;
import java.io.Serializable;

public class ReporteTramites implements Serializable {
    private static final long serialVersionUID = 1L;

    private String descripcion;
    private String fecha;
    private String garantia;
    private String persona;
    private String precio;

    public ReporteTramites() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
