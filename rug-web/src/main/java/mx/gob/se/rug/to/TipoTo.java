package mx.gob.se.rug.to;

import java.io.Serializable;

public class TipoTo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idTipo;
	private String desTipo;
	
	public TipoTo(){
		super();
	}
	
	public TipoTo(String pIdTipo, String pDesTipo) {
		this.idTipo = pIdTipo;
		this.desTipo = pDesTipo;
	}
	
	public String getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}
	public String getDesTipo() {
		return desTipo;
	}
	public void setDesTipo(String desTipo) {
		this.desTipo = desTipo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
