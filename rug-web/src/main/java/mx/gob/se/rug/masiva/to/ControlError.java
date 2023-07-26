package mx.gob.se.rug.masiva.to;

import java.io.Serializable;

import mx.gob.se.rug.to.PlSql;

public class ControlError implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PlSql plSql;
	private Integer idInscripcion;
	private String claveRastreo = "N/A";
	public PlSql getPlSql() {
		return plSql;
	}
	public void setPlSql(PlSql plSql) {
		this.plSql = plSql;
	}
	public Integer getIdInscripcion() {
		return idInscripcion;
	}
	public void setIdInscripcion(Integer idInscripcion) {
		this.idInscripcion = idInscripcion;
	}
	public void setClaveRastreo(String claveRastreo) {
		this.claveRastreo = claveRastreo;
	}
	public String getClaveRastreo() {
		return claveRastreo;
	}
	
	

}
