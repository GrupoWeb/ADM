package mx.gob.se.rug.masiva.resultado.to;

import java.util.List;

import mx.gob.se.rug.masiva.to.Tramite;

public class CargaMasivaResultado {
	
	private ResultadoCargaMasiva resultadoCargaMasiva;
	private List<Tramite> tramitesCorrectos;
	private List<Tramite> tramitesIncorrectos;
	
	private Integer idArchivoResumen;
	private Integer idArchivoFirma;
	private Integer idFirmaMasiva;
	
	
	public ResultadoCargaMasiva getResultadoCargaMasiva() {
		return resultadoCargaMasiva;
	}
	public void setResultadoCargaMasiva(ResultadoCargaMasiva resultadoCargaMasiva) {
		this.resultadoCargaMasiva = resultadoCargaMasiva;
	}
	public List<Tramite> getTramitesCorrectos() {
		return tramitesCorrectos;
	}
	public void setTramitesCorrectos(List<Tramite> tramitesCorrectos) {
		this.tramitesCorrectos = tramitesCorrectos;
	}
	public List<Tramite> getTramitesIncorrectos() {
		return tramitesIncorrectos;
	}
	public void setTramitesIncorrectos(List<Tramite> tramitesIncorrectos) {
		this.tramitesIncorrectos = tramitesIncorrectos;
	}
	public Integer getIdArchivoResumen() {
		return idArchivoResumen;
	}
	public void setIdArchivoResumen(Integer idArchivoResumen) {
		this.idArchivoResumen = idArchivoResumen;
	}
	public Integer getIdArchivoFirma() {
		return idArchivoFirma;
	}
	public void setIdArchivoFirma(Integer idArchivoFirma) {
		this.idArchivoFirma = idArchivoFirma;
	}
	public Integer getIdFirmaMasiva() {
		return idFirmaMasiva;
	}
	public void setIdFirmaMasiva(Integer idFirmaMasiva) {
		this.idFirmaMasiva = idFirmaMasiva;
	}
	
	

}
