package mx.gob.se.rug.certificacion.to;

import java.io.Serializable;
import java.util.List;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.to.TramiteTO;

public class CertificacionTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idTramite;
	private List<TramiteTO> listaTramites;
	private AcreedorTO acreedor;
	
	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public List<TramiteTO> getListaTramites() {
		return listaTramites;
	}
	public void setListaTramites(List<TramiteTO> listaTramites) {
		this.listaTramites = listaTramites;
	}
	public AcreedorTO getAcreedor() {
		return acreedor;
	}
	public void setAcreedor(AcreedorTO acreedor) {
		this.acreedor = acreedor;
	}
	
	
	
}
