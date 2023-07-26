package gt.gob.rgm.adm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RugCatTextoFormId implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name="ID_TIPO_GARANTIA")
	private long idTipoGarantia;
	
	@Column(name="ID_PARTE")
	private long idParte;

	public RugCatTextoFormId() {
		super();
	}

	public long getIdTipoGarantia() {
		return idTipoGarantia;
	}

	public void setIdTipoGarantia(long idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
	}

	public long getIdParte() {
		return idParte;
	}

	public void setIdParte(long idParte) {
		this.idParte = idParte;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idParte ^ (idParte >>> 32));
		result = prime * result + (int) (idTipoGarantia ^ (idTipoGarantia >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RugCatTextoFormId other = (RugCatTextoFormId) obj;
		if (idParte != other.idParte)
			return false;
		if (idTipoGarantia != other.idTipoGarantia)
			return false;
		return true;
	}
	
}
