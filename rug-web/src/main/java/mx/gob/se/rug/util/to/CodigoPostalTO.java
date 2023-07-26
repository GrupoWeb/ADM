package mx.gob.se.rug.util.to;

import java.io.Serializable;
import java.util.List;

public class CodigoPostalTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String codigoPostal;
	private String cve_estado;
	private List<EstadoTO> estadoTOs;
	private String cve_municipio;
	private List<MunicipioTO> municipioTOs;
	private String cve_colonia;
	private List<ColoniaTO> coloniaTOs;
	private String cve_localidad;
	private List<LocalidadTO> localidadTOs;
	
	
	
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getCve_estado() {
		return cve_estado;
	}
	public void setCve_estado(String cveEstado) {
		cve_estado = cveEstado;
	}
	public List<EstadoTO> getEstadoTOs() {
		return estadoTOs;
	}
	public void setEstadoTOs(List<EstadoTO> estadoTOs) {
		this.estadoTOs = estadoTOs;
	}
	public String getCve_municipio() {
		return cve_municipio;
	}
	public void setCve_municipio(String cveMunicipio) {
		cve_municipio = cveMunicipio;
	}
	public List<MunicipioTO> getMunicipioTOs() {
		return municipioTOs;
	}
	public void setMunicipioTOs(List<MunicipioTO> municipioTOs) {
		this.municipioTOs = municipioTOs;
	}
	public String getCve_colonia() {
		return cve_colonia;
	}
	public void setCve_colonia(String cveColonia) {
		cve_colonia = cveColonia;
	}
	public List<ColoniaTO> getColoniaTOs() {
		return coloniaTOs;
	}
	public void setColoniaTOs(List<ColoniaTO> coloniaTOs) {
		this.coloniaTOs = coloniaTOs;
	}
	public String getCve_localidad() {
		return cve_localidad;
	}
	public void setCve_localidad(String cveLocalidad) {
		cve_localidad = cveLocalidad;
	}
	public List<LocalidadTO> getLocalidadTOs() {
		return localidadTOs;
	}
	public void setLocalidadTOs(List<LocalidadTO> localidadTOs) {
		this.localidadTOs = localidadTOs;
	}

	
}
