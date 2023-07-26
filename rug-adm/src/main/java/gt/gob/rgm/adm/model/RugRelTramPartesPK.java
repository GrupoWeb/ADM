package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RUG_REL_TRAM_PARTES database table.
 * 
 */
@Embeddable
public class RugRelTramPartesPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_TRAMITE")
	private Long idTramite;

	@Column(name="ID_PERSONA")
	private Long idPersona;

	@Column(name="ID_PARTE")
	private long idParte;

	public RugRelTramPartesPK() {
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
	public long getIdParte() {
		return this.idParte;
	}
	public void setIdParte(long idParte) {
		this.idParte = idParte;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RugRelTramPartesPK)) {
			return false;
		}
		RugRelTramPartesPK castOther = (RugRelTramPartesPK)other;
		return 
			(this.idTramite.longValue() == castOther.idTramite.longValue())
			&& (this.idPersona.longValue() == castOther.idPersona.longValue())
			&& (this.idParte == castOther.idParte);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idTramite ^ (this.idTramite >>> 32)));
		hash = hash * prime + ((int) (this.idPersona ^ (this.idPersona >>> 32)));
		hash = hash * prime + ((int) (this.idParte ^ (this.idParte >>> 32)));
		
		return hash;
	}
}