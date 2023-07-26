package mx.gob.se.rug.masiva.to;

import java.io.Serializable;

public class TipoTramiteCargaMasiva implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tramite;
	private Integer id;
			
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTramite() {
		return tramite;
	}
	public void setTramite(String tramite) {
		this.tramite = tramite;
	}
	
	
}