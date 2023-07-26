package gt.gob.rgm.adm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RugDomiciliosExtHPK implements Serializable {
	@Column(name="ID_TRAMITE")
	private Long idTramite;
	
	@Column(name="ID_PERSONA")
	private Long idPersona;
	
	@Column(name="ID_PARTE")
	private Integer idParte;
	
	@Column(name="ID_DOMICILIO")
	private Long idDomicilio;
	
	public RugDomiciliosExtHPK() {
	}
	
	public Long getIdTramite() {
		return this.idTramite;
	}

	public void setIdTramite(Long idTramite) {
		this.idTramite = idTramite;
	}
	
	public Long getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	
	public Integer getIdParte() {
		return this.idParte;
	}

	public void setIdParte(Integer idParte) {
		this.idParte = idParte;
	}
	
	public Long getIdDomicilio() {
		return this.idDomicilio;
	}

	public void setIdDomicilio(Long idDomicilio) {
		this.idDomicilio = idDomicilio;
	}
	
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public int hashCode() {
		return super.hashCode();
	}
}
