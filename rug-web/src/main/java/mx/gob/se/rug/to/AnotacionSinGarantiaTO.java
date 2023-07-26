package mx.gob.se.rug.to;

import java.io.Serializable;

public class AnotacionSinGarantiaTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idTramite;
	private Integer idAnotacion;
	private Integer idUsuario;
	private String autoridad;
	private String tipoPersona;
	private String razonSocial;
	private String nomnbre;
	private String nombre;
	private String aPaterno;
	private String aMaterno;
	private String rfc;
	private String folioMercantil;
	private String anotacion;
	private Integer meses;
	private String descripcionTram;
	private String fechaStatus;
	private String fechaInscripcion;
	

	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public Integer getIdAnotacion() {
		return idAnotacion;
	}
	public void setIdAnotacion(Integer idAnotacion) {
		this.idAnotacion = idAnotacion;
	}
	public String getAutoridad() {
		return autoridad;
	}
	public void setAutoridad(String autoridad) {
		this.autoridad = autoridad;
	}
	public String getTipoPersona() {
		return tipoPersona;
	}
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getNomnbre() {
		return nomnbre;
	}
	public void setNomnbre(String nomnbre) {
		this.nomnbre = nomnbre;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getaPaterno() {
		return aPaterno;
	}
	public void setaPaterno(String aPaterno) {
		this.aPaterno = aPaterno;
	}
	public String getaMaterno() {
		return aMaterno;
	}
	public void setaMaterno(String aMaterno) {
		this.aMaterno = aMaterno;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getFolioMercantil() {
		return folioMercantil;
	}
	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}
	public String getAnotacion() {
		return anotacion;
	}
	public void setAnotacion(String anotacion) {
		this.anotacion = anotacion;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getMeses() {
		return meses;
	}
	public void setMeses(Integer meses) {
		this.meses = meses;
	}
	
	public String getDescripcionTram() {
		return descripcionTram;
	}
	
	public void setDescripcionTram(String descripcionTram ) {
		this.descripcionTram = descripcionTram;
		
	}
	public String getFechaStatus() {
		return fechaStatus;
	}
	public void setFechaStatus(String fechaStatus) {
		this.fechaStatus = fechaStatus;
	}
	public String getFechaInscripcion() {
		return fechaInscripcion;
	}
	public void setFechaInscripcion(String fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}
}
