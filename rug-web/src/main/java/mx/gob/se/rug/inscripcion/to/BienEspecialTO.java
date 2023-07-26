package mx.gob.se.rug.inscripcion.to;

public class BienEspecialTO {

	public Integer idTramite;
	public Integer idTramiteGarantia;
	public Integer tipoBien;
	public Integer tipoIdentificador;
	public String identificador;
	public String descripcion;
	public String serie;


	private String changeTipoBien(Integer tipoBien) {
		switch (tipoBien) {
			case 1: return "Vehiculo";
			case 2: return "Factura";
            case 4: return "Maquinaria";
            case 5: return "Veh\u00edculo de 2 y 3 ruedas";
            case 6: return "Derecho de Cr\u00e9dito";
			default: return "Otro";
		}
	}
	
	private String changeTipoId(Integer tipoId) {
		switch (tipoId) {
			case 1: return "Placa";
			case 2: return "VIN";
			case 3: return "No. Factura";
			default: return "No. Serie"; 
		}
	}
	
	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public String getTipoBien() {
		return changeTipoBien(tipoBien);
	}
	
	public Integer getTipoBienInt() {
		return tipoBien;
	}
	
	public void setTipoBien(Integer tipoBien) {
		this.tipoBien = tipoBien;
	}
	public String getTipoIdentificador() {
		return changeTipoId(tipoIdentificador);
	}
	public Integer getTipoIdentificadorInt() {
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
	public Integer getIdTramiteGarantia() {
		return idTramiteGarantia;
	}
	public void setIdTramiteGarantia(Integer idTramiteGarantia) {
		this.idTramiteGarantia = idTramiteGarantia;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}
}
