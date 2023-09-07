package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the V_REP_LISTADO_DEUDORES database table.
 * 
 */
@Entity
@Table(name="V_REP_LISTADO_DEUDORES")
@NamedQuery(name="VRepListadoDeudores.findAll", query="SELECT b FROM VRepListadoDeudores b")
public class VRepListadoDeudores implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
        @Column(name="ID_PERSONA")
	private Long idPersona;
        
        @Column(name="DPI")
	private String Dpi;
        
        @Column(name="RFC")
	private String Rfc;

	@Column(name="NOMBRE_PERSONA")
	private String nombrePersona;

	@Column(name="PER_JURIDICA")
	private String perJuridica;

	@Column(name="ID_NACIONALIDAD")
	private Integer idNacionalidad;
        
        @Column(name="NOM_NACIONALIDAD")
	private String nomNacionalidad;

	@Column(name="E_MAIL")
	private String email;
        
        @Column(name="ID_DOMICILIO")
	private Integer idDomicilio;
        
        @Column(name="DIRECCION")
	private String direccion;
        
        @Column(name="CANTGARANTIAS")
	private Integer cGarantias;
        
        @Column(name="CANTVIGENTES")
	private Integer cVigentes;
        
        @Column(name="CANTNOVIGENTES")
	private Integer cnVigentes;
        
        @Column(name="CANTCANCELADAS")
	private Integer cCanceladas;

	public VRepListadoDeudores() {
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