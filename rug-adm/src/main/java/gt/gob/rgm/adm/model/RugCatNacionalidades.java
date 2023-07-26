package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the RUG_CAT_NACIONALIDADES database table.
 * 
 */
@Entity
@Table(name="RUG_CAT_NACIONALIDADES")
@NamedQueries({
	@NamedQuery(name="RugCatNacionalidades.findAll", query="SELECT r FROM RugCatNacionalidades r"),
	@NamedQuery(name="RugCatNacionalidades.findByStatus", query="SELECT r FROM RugCatNacionalidades r WHERE r.statusReg = :status ORDER BY r.descNacionalidad")
})
public class RugCatNacionalidades implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_NACIONALIDAD")
	private long idNacionalidad;

	@Column(name="CVE_PAIS")
	private String cvePais;

	@Column(name="DESC_NACIONALIDAD")
	private String descNacionalidad;

	@Column(name="STATUS_REG")
	private String statusReg;

	public RugCatNacionalidades() {
	}

	public long getIdNacionalidad() {
		return this.idNacionalidad;
	}

	public void setIdNacionalidad(long idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}

	public String getCvePais() {
		return this.cvePais;
	}

	public void setCvePais(String cvePais) {
		this.cvePais = cvePais;
	}

	public String getDescNacionalidad() {
		return this.descNacionalidad;
	}

	public void setDescNacionalidad(String descNacionalidad) {
		this.descNacionalidad = descNacionalidad;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

}