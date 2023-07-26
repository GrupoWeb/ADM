package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the REL_USU_ACREEDOR database table.
 * 
 */
@Embeddable
public class RelUsuAcreedorPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_USUARIO")
	private long idUsuario;

	@Column(name="ID_ACREEDOR")
	private long idAcreedor;

	public RelUsuAcreedorPK() {
	}
	public long getIdUsuario() {
		return this.idUsuario;
	}
	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public long getIdAcreedor() {
		return this.idAcreedor;
	}
	public void setIdAcreedor(long idAcreedor) {
		this.idAcreedor = idAcreedor;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RelUsuAcreedorPK)) {
			return false;
		}
		RelUsuAcreedorPK castOther = (RelUsuAcreedorPK)other;
		return 
			(this.idUsuario == castOther.idUsuario)
			&& (this.idAcreedor == castOther.idAcreedor);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idUsuario ^ (this.idUsuario >>> 32)));
		hash = hash * prime + ((int) (this.idAcreedor ^ (this.idAcreedor >>> 32)));
		
		return hash;
	}
}