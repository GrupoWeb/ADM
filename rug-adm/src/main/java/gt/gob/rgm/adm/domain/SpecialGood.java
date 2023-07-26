package gt.gob.rgm.adm.domain;

public class SpecialGood {
	private Integer idTramite;
	private Integer idTramiteGarantia;
	private Integer tipoBien;
	private Integer tipoIdentificador;
	private String identificador;
	private String descripcion;

	public SpecialGood() {
	}

	public Integer getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}

	public Integer getIdTramiteGarantia() {
		return idTramiteGarantia;
	}

	public void setIdTramiteGarantia(Integer idTramiteGarantia) {
		this.idTramiteGarantia = idTramiteGarantia;
	}

	public Integer getTipoBien() {
		return tipoBien;
	}

	public void setTipoBien(Integer tipoBien) {
		this.tipoBien = tipoBien;
	}

	public Integer getTipoIdentificador() {
		return tipoIdentificador;
	}

	public void setTipoIdentificador(Integer tipoIdentificador) {
		this.tipoIdentificador = tipoIdentificador;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
