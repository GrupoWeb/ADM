package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the RUG_DOMICILIOS_EXT database table.
 * 
 */
@Entity
@Table(name="RUG_DOMICILIOS_EXT")
@NamedQuery(name="RugDomiciliosExt.findAll", query="SELECT r FROM RugDomiciliosExt r")
public class RugDomiciliosExt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ID_DOMICILIO_GENERATOR")
	@SequenceGenerator(name="ID_DOMICILIO_GENERATOR", sequenceName="SEQ_RUG_ID_DOMICILIO", allocationSize=1)
	@Column(name="ID_DOMICILIO")
	private Long idDomicilio;

	@Column(name="ID_PAIS_RESIDENCIA")
	private Integer idPaisResidencia;

	private String poblacion;

	@Column(name="UBICA_DOMICILIO_1")
	private String ubicaDomicilio1;

	@Column(name="UBICA_DOMICILIO_2")
	private String ubicaDomicilio2;

	@Column(name="ZONA_POSTAL")
	private String zonaPostal;

	public RugDomiciliosExt() {
	}

	public Long getIdDomicilio() {
		return this.idDomicilio;
	}

	public void setIdDomicilio(Long idDomicilio) {
		this.idDomicilio = idDomicilio;
	}

	public Integer getIdPaisResidencia() {
		return this.idPaisResidencia;
	}

	public void setIdPaisResidencia(Integer idPaisResidencia) {
		this.idPaisResidencia = idPaisResidencia;
	}

	public String getPoblacion() {
		return this.poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getUbicaDomicilio1() {
		return this.ubicaDomicilio1;
	}

	public void setUbicaDomicilio1(String ubicaDomicilio1) {
		this.ubicaDomicilio1 = ubicaDomicilio1;
	}

	public String getUbicaDomicilio2() {
		return this.ubicaDomicilio2;
	}

	public void setUbicaDomicilio2(String ubicaDomicilio2) {
		this.ubicaDomicilio2 = ubicaDomicilio2;
	}

	public String getZonaPostal() {
		return this.zonaPostal;
	}

	public void setZonaPostal(String zonaPostal) {
		this.zonaPostal = zonaPostal;
	}

}