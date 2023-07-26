package gt.gob.rgm.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the RUG_CAT_PERFILES database table.
 * 
 */
@Entity
@Table(name="RUG_CAT_PERFILES")
@NamedQuery(name="RugCatPerfiles.findAll", query="SELECT r FROM RugCatPerfiles r")
public class RugCatPerfiles implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_PERFIL")
	private long idPerfil;

	@Column(name="CVE_PERFIL")
	private String cvePerfil;

	@Column(name="DESC_PERFIL")
	private String descPerfil;

	@Column(name="SIT_PERFIL")
	private String sitPerfil;

	public RugCatPerfiles() {
	}

	public long getIdPerfil() {
		return this.idPerfil;
	}

	public void setIdPerfil(long idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getCvePerfil() {
		return this.cvePerfil;
	}

	public void setCvePerfil(String cvePerfil) {
		this.cvePerfil = cvePerfil;
	}

	public String getDescPerfil() {
		return this.descPerfil;
	}

	public void setDescPerfil(String descPerfil) {
		this.descPerfil = descPerfil;
	}

	public String getSitPerfil() {
		return this.sitPerfil;
	}

	public void setSitPerfil(String sitPerfil) {
		this.sitPerfil = sitPerfil;
	}

}