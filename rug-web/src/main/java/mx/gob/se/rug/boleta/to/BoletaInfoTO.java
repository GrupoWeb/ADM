package mx.gob.se.rug.boleta.to;

import java.util.List;

public class BoletaInfoTO {

	private Integer idBoleta;
	private String tituloBoleta;
	private List<SeccionTO> seccionTOs;
	public Integer getIdBoleta() {
		return idBoleta;
	}
	public void setIdBoleta(Integer idBoleta) {
		this.idBoleta = idBoleta;
	}
	public String getTituloBoleta() {
		return tituloBoleta;
	}
	public void setTituloBoleta(String tituloBoleta) {
		this.tituloBoleta = tituloBoleta;
	}
	public List<SeccionTO> getSeccionTOs() {
		return seccionTOs;
	}
	public void setSeccionTOs(List<SeccionTO> seccionTOs) {
		this.seccionTOs = seccionTOs;
	}
	
	
	
}
