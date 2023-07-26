package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RUG_CAT_TEXTO_FORM database table.
 * 
 */
@Embeddable
public class RugCatTextoFormPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_TIPO_GARANTIA")
	private long idTipoGarantia;

	@Column(name="ID_PARTE")
	private long idParte;

	public RugCatTextoFormPK() {
	}
	public long getIdTipoGarantia() {
		return this.idTipoGarantia;
	}
	public void setIdTipoGarantia(long idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
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
		if (!(other instanceof RugCatTextoFormPK)) {
			return false;
		}
		RugCatTextoFormPK castOther = (RugCatTextoFormPK)other;
		return 
			(this.idTipoGarantia == castOther.idTipoGarantia)
			&& (this.idParte == castOther.idParte);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idTipoGarantia ^ (this.idTipoGarantia >>> 32)));
		hash = hash * prime + ((int) (this.idParte ^ (this.idParte >>> 32)));
		
		return hash;
	}
}