package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class GuaranteeTransactionPart implements Serializable {
	private Long idTramite;
	private Long idGarantia;
	private Long idPersona;
	private String perJuridica;
	private Long idParte;
	
	public GuaranteeTransactionPart() {
	}

	public Long getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Long idTramite) {
		this.idTramite = idTramite;
	}

	public Long getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(Long idGarantia) {
		this.idGarantia = idGarantia;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getPerJuridica() {
		return perJuridica;
	}

	public void setPerJuridica(String perJuridica) {
		this.perJuridica = perJuridica;
	}

	public Long getIdParte() {
		return idParte;
	}

	public void setIdParte(Long idParte) {
		this.idParte = idParte;
	}
}
