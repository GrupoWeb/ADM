package mx.gob.se.rug.boleta.to;

import java.io.Serializable;
import java.util.List;

public class SeccionTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idSeccion;
	private String label;
	private Integer ordenSeccion;
	private List<CampoTO> campoTOs;

	
	
	public Integer getIdSeccion() {
		return idSeccion;
	}
	public void setIdSeccion(Integer idSeccion) {
		this.idSeccion = idSeccion;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getOrdenSeccion() {
		return ordenSeccion;
	}
	public void setOrdenSeccion(Integer ordenSeccion) {
		this.ordenSeccion = ordenSeccion;
	}
	public List<CampoTO> getCampoTOs() {
		return campoTOs;
	}
	public void setCampoTOs(List<CampoTO> campoTOs) {
		this.campoTOs = campoTOs;
		
	}
	
	
}
