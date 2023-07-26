package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RUG_PERSONAS_H database table.
 * 
 */
@Embeddable
public class RugPersonasHPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_TRAMITE")
	private long idTramite;

	@Column(name="ID_PARTE")
	private long idParte;

	@Column(name="ID_PERSONA")
	private long idPersona;

	public RugPersonasHPK() {
	}
	public long getIdTramite() {
		return this.idTramite;
	}
	public void setIdTramite(long idTramite) {
		this.idTramite = idTramite;
	}
	public long getIdParte() {
		return this.idParte;
	}
	public void setIdParte(long idParte) {
		this.idParte = idParte;
	}
	public long getIdPersona() {
		return this.idPersona;
	}
	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RugPersonasHPK)) {
			return false;
		}
		RugPersonasHPK castOther = (RugPersonasHPK)other;
		return 
			(this.idTramite == castOther.idTramite)
			&& (this.idParte == castOther.idParte)
			&& (this.idPersona == castOther.idPersona);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idTramite ^ (this.idTramite >>> 32)));
		hash = hash * prime + ((int) (this.idParte ^ (this.idParte >>> 32)));
		hash = hash * prime + ((int) (this.idPersona ^ (this.idPersona >>> 32)));
		
		return hash;
	}
}