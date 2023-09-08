package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class Deudo implements Serializable {
    
    private Long idPersona;
    private String Dpi;
    private String Rfc;
    private String nombrePersona;
    private String perJuridica;
    private Integer idNacionalidad;
    private String nomNacionalidad;
    private String email;
    private Integer idDomicilio;
    private String direccion;
    private Integer cGarantias;
    private Integer cVigentes;
    private Integer cnVigentes;
    private Integer cCanceladas;
    private String textobusqueda;
    private String idDeudor_b;
    
    private Integer dusuarios;

    public Integer getDusuarios() {
        return dusuarios;
    }

    public void setDusuarios(Integer dusuarios) {
        this.dusuarios = dusuarios;
    }
    
    

    public String getIdDeudor_b() {
        return idDeudor_b;
    }

    public void setIdDeudor_b(String idDeudor_b) {
        this.idDeudor_b = idDeudor_b;
    }
	
	
	public Deudo() {
	}
        
     public String getTextobusqueda() {
        return textobusqueda;
    }

    public void setTextobusqueda(String textobusqueda) {
        this.textobusqueda = textobusqueda;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public String getRfc() {
        return Rfc;
    }

    public void setRfc(String Rfc) {
        this.Rfc = Rfc;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getPerJuridica() {
        return perJuridica;
    }

    public void setPerJuridica(String perJuridica) {
        this.perJuridica = perJuridica;
    }

    public Integer getIdNacionalidad() {
        return idNacionalidad;
    }

    public void setIdNacionalidad(Integer idNacionalidad) {
        this.idNacionalidad = idNacionalidad;
    }

    public String getNomNacionalidad() {
        return nomNacionalidad;
    }

    public void setNomNacionalidad(String nomNacionalidad) {
        this.nomNacionalidad = nomNacionalidad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIdDomicilio() {
        return idDomicilio;
    }

    public void setIdDomicilio(Integer idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getcGarantias() {
        return cGarantias;
    }

    public void setcGarantias(Integer cGarantias) {
        this.cGarantias = cGarantias;
    }

    public Integer getcVigentes() {
        return cVigentes;
    }

    public void setcVigentes(Integer cVigentes) {
        this.cVigentes = cVigentes;
    }

    public Integer getCnVigentes() {
        return cnVigentes;
    }

    public void setCnVigentes(Integer cnVigentes) {
        this.cnVigentes = cnVigentes;
    }

    public Integer getcCanceladas() {
        return cCanceladas;
    }

    public void setcCanceladas(Integer cCanceladas) {
        this.cCanceladas = cCanceladas;
    }
    public String getDpi() {
        return Dpi;
    }

    public void setDpi(String Dpi) {
        this.Dpi = Dpi;
    }  
}
