package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the RUG_DOMICILIOS_EXT_H database table.
 * 
 */
@Entity
@Table(name="RUG_DOMICILIOS_EXT_H")
@NamedQuery(name="RugDomiciliosExtH.findAll", query="SELECT r FROM RugDomiciliosExtH r")
public class RugDomiciliosExtH implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RugDomiciliosExtHPK id;

	@Column(name="ID_PAIS_RESIDENCIA")
	private Integer idPaisResidencia;

	private String poblacion;

	@Column(name="UBICA_DOMICILIO_1")
	private String ubicaDomicilio1;

	@Column(name="UBICA_DOMICILIO_2")
	private String ubicaDomicilio2;

	@Column(name="ZONA_POSTAL")
	private String zonaPostal;

	public RugDomiciliosExtH() {
	}

	public RugDomiciliosExtHPK getId() {
		return id;
	}

	public void setId(RugDomiciliosExtHPK id) {
		this.id = id;
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