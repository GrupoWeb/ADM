package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RUG_REL_TRAM_INC_PARTES database table.
 * 
 */
@Embeddable
public class RugRelTramIncPartePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_TRAMITE_TEMP")
	private long idTramiteTemp;

	@Column(name="ID_PERSONA")
	private long idPersona;

	@Column(name="ID_PARTE")
	private long idParte;

	public RugRelTramIncPartePK() {
	}
	public long getIdTramiteTemp() {
		return this.idTramiteTemp;
	}
	public void setIdTramiteTemp(long idTramiteTemp) {
		this.idTramiteTemp = idTramiteTemp;
	}
	public long getIdPersona() {
		return this.idPersona;
	}
	public void setIdPersona(long idPersona) {
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
		if (!(other instanceof RugRelTramIncPartePK)) {
			return false;
		}
		RugRelTramIncPartePK castOther = (RugRelTramIncPartePK)other;
		return 
			(this.idTramiteTemp == castOther.idTramiteTemp)
			&& (this.idPersona == castOther.idPersona)
			&& (this.idParte == castOther.idParte);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idTramiteTemp ^ (this.idTramiteTemp >>> 32)));
		hash = hash * prime + ((int) (this.idPersona ^ (this.idPersona >>> 32)));
		hash = hash * prime + ((int) (this.idParte ^ (this.idParte >>> 32)));
		
		return hash;
	}
}