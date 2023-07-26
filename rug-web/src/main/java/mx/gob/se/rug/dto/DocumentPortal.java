package mx.gob.se.rug.dto;

import java.util.Date;

import mx.gob.economia.dgi.framework.document.dto.Document;

@SuppressWarnings("serial")
public class DocumentPortal extends Document{

	private String nombre;
	
	private String descripcion;
	
	private String dependencia;
	
	private String tipo;
	
	private String formato;
	
	private String claveEntidad;
	
	private String contentType;
	
	private String cedula;
	
	public DocumentPortal() {
		super("");
	}
	
	private String descripcionTipo;
	
	private String descripcionSituacion;
	
	private Date fechaRegistro;
	
	private String tramite;
	
	private String id;
	
	private String secuencia;
	
	private String descripcionDomicilio;
	
	
	public DocumentPortal(String pathName) {
		super(pathName);
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}


	public String getDependencia() {
		return dependencia;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getTipo() {
		return tipo;
	}


	public void setFormato(String formato) {
		this.formato = formato;
	}


	public String getFormato() {
		return formato;
	}


	public String getClaveEntidad() {
		return claveEntidad;
	}


	public void setClaveEntidad(String claveEntidad) {
		this.claveEntidad = claveEntidad;
	}


	public String getDescripcionTipo() {
		return descripcionTipo;
	}


	public void setDescripcionTipo(String descripcionTipo) {
		this.descripcionTipo = descripcionTipo;
	}


	public String getDescripcionSituacion() {
		return descripcionSituacion;
	}


	public void setDescripcionSituacion(String descripcionSituacion) {
		this.descripcionSituacion = descripcionSituacion;
	}


	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}


	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getNombre() {
		return nombre;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public String getContentType() {
		return contentType;
	}


	public void setCedula(String cedula) {
		this.cedula = cedula;
	}


	public String getCedula() {
		return cedula;
	}


	public void setTramite(String tramite) {
		this.tramite = tramite;
	}


	public String getTramite() {
		return tramite;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getId() {
		return id;
	}


	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}


	public String getSecuencia() {
		return secuencia;
	}


	public void setDescripcionDomicilio(String descripcionDomicilio) {
		this.descripcionDomicilio = descripcionDomicilio;
	}


	public String getDescripcionDomicilio() {
		return descripcionDomicilio;
	}

}
