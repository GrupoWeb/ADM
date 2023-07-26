package mx.gob.se.rug.acreedores.to;

import java.io.Serializable;

public class TramitesMasivosTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombreArchivo;//NOMBRE_ARCHIVO
	private String nombre;//NOMBRE
	private String rfc;//RFC
	private Integer numCorrectos;//TOTAL_EXITO
	private Integer numErroneos;//TOTAL_NO_EXITO
	private Integer idPersona;//ID_USUARIO
	private Integer idFirmaMasiva;	//ID_FIRMA_MASIVA
	
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public Integer getNumCorrectos() {
		return numCorrectos;
	}
	public void setNumCorrectos(Integer numCorrectos) {
		this.numCorrectos = numCorrectos;
	}
	public Integer getNumErroneos() {
		return numErroneos;
	}
	public void setNumErroneos(Integer numErroneos) {
		this.numErroneos = numErroneos;
	}
	public Integer getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	public Integer getIdFirmaMasiva() {
		return idFirmaMasiva;
	}
	public void setIdFirmaMasiva(Integer idFirmaMasiva) {
		this.idFirmaMasiva = idFirmaMasiva;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
