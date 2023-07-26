package mx.gob.se.rug.masiva.to.string;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.gob.se.rug.boleta.to.Tramite;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.masiva.to.CargaMasiva;

public class CargaMasivaPreProcesed {

	private Integer totalTramites;
	private CargaMasiva cargaMasiva;
	private List<TramiteRes> tramiteIncorrectos;
	
	public CargaMasiva getCargaMasiva() {
		return cargaMasiva;
	}
	public void setCargaMasiva(CargaMasiva cargaMasiva) {
		this.cargaMasiva = cargaMasiva;
	}
	public List<TramiteRes> getTramiteIncorrectos() {
		return tramiteIncorrectos;
	}
	public void setTramiteIncorrectos(List<TramiteRes> tramiteIncorrectos) {
		this.tramiteIncorrectos = tramiteIncorrectos;
	}
	public Integer getTotalTramites() {
		return totalTramites;
	}
	public void setTotalTramites(Integer totalTramites) {
		this.totalTramites = totalTramites;
	}	
}
