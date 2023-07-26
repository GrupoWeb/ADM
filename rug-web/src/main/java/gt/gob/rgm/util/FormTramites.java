package gt.gob.rgm.util;

import mx.gob.se.rug.to.GarantiaTO;
import mx.gob.se.rug.to.PersonaTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

public class FormTramites implements Serializable {
    private static final long serialVersionUID = 1L;
    public FormTramites() {
    }

    private String fechaInicial;
    private String fechaFinal;
    private String nombre;
    private Integer idUsuario;

    private String anio;


    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
