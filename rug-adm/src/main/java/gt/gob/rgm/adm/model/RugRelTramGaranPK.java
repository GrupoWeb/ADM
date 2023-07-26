package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RUG_REL_TRAM_GARAN database table.
 * 
 */
@Embeddable
public class RugRelTramGaranPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_TRAMITE")
	private long idTramite;

	@Column(name="ID_GARANTIA")
	private long idGarantia;

	public RugRelTramGaranPK() {
	}
	public long getIdTramite() {
		return this.idTramite;
	}
	public void setIdTramite(long idTramite) {
		this.idTramite = idTramite;
	}
	public long getIdGarantia() {
		return this.idGarantia;
	}
	public void setIdGarantia(long idGarantia) {
		this.idGarantia = idGarantia;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RugRelTramGaranPK)) {
			return false;
		}
		RugRelTramGaranPK castOther = (RugRelTramGaranPK)other;
		return 
			(this.idTramite == castOther.idTramite)
			&& (this.idGarantia == castOther.idGarantia);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idTramite ^ (this.idTramite >>> 32)));
		hash = hash * prime + ((int) (this.idGarantia ^ (this.idGarantia >>> 32)));
		
		return hash;
	}
}