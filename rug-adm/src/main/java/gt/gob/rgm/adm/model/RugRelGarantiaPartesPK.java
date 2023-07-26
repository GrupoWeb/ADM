package gt.gob.rgm.adm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RugRelGarantiaPartesPK implements Serializable {
	@Column(name="ID_GARANTIA")
	private Long idGarantia;

	@Column(name="ID_PARTE")
	private Integer idParte;

	@Column(name="ID_PERSONA")
	private Long idPersona;

	@Column(name="ID_RELACION")
	private Long idRelacion;
	
	public RugRelGarantiaPartesPK() {
	}

	public Long getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(Long idGarantia) {
		this.idGarantia = idGarantia;
	}

	public Integer getIdParte() {
		return idParte;
	}

	public void setIdParte(Integer idParte) {
		this.idParte = idParte;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Long getIdRelacion() {
		return idRelacion;
	}

	public void setIdRelacion(Long idRelacion) {
		this.idRelacion = idRelacion;
	}
	
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	public int hashCode() {
		return super.hashCode();
	}
}
