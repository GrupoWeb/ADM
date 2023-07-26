package mx.gob.se.rug.operaciones.to;

import java.io.Serializable;

public class CargaMasivaResumenTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private Integer idFirmaMasivaTemp;
	private String tipoTramiteFirma;
	private Integer buenas;
	private Integer malas;
	private String nombreArchivo;
	private String claveRastreo;
	
	
	
	public String getTipoTramiteFirma() {
		return tipoTramiteFirma;
	}
	public void setTipoTramiteFirma(String tipoTramiteFirma) {
		this.tipoTramiteFirma = tipoTramiteFirma;
	}
	public Integer getIdFirmaMasivaTemp() {
		return idFirmaMasivaTemp;
	}
	public void setIdFirmaMasivaTemp(Integer idFirmaMasivaTemp) {
		this.idFirmaMasivaTemp = idFirmaMasivaTemp;
	}
	public Integer getBuenas() {
		return buenas;
	}
	public void setBuenas(Integer buenas) {
		this.buenas = buenas;
	}
	public Integer getMalas() {
		return malas;
	}
	public void setMalas(Integer malas) {
		this.malas = malas;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getClaveRastreo() {
		return claveRastreo;
	}
	public void setClaveRastreo(String claveRastreo) {
		this.claveRastreo = claveRastreo;
	}
	
	
	
	
	
	

}
