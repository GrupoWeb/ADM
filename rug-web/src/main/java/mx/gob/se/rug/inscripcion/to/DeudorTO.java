package mx.gob.se.rug.inscripcion.to;

import mx.gob.se.rug.dto.Persona;

public class DeudorTO extends Persona {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer idNacionalidad;
	
	private String razon;
	private String inscrita; // inscrita bajo el numero
	private String folioInscrito; //folio
	private String libroInscrito; //libro
	private String ubicacionInscrito; // Ubicada en
	
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String nombreCompleto;
	private String extendido;
	private String edad;
	private String estadoCivil;
	private String profesion;
	
	private String telefono;
	private String correo;
	
	private Integer idResidencia;	
	private String ubicacion;
	
	private String tipoPersona;
	private String curp;
	
	public String getRazon() {
		return razon;
	}
	public void setRazon(String razon) {
		this.razon = razon;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidoPaterno() {
		return apellidoPaterno;
	}
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}
	public String getApellidoMaterno() {
		return apellidoMaterno;
	}
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	public String getTipoPersona() {
		return tipoPersona;
	}
	public void setIdNacionalidad(Integer idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}
	public Integer getIdNacionalidad() {
		return idNacionalidad;
	}
	public void setCurp(String curp) {
		this.curp = curp;
	}
	public String getCurp() {
		return curp;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public String getExtendido() {
		return extendido;
	}
	public void setExtendido(String extendido) {
		this.extendido = extendido;
	}
	public String getEdad() {
		return edad;
	}
	public void setEdad(String edad) {
		this.edad = edad;
	}
	public String getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	public String getProfesion() {
		return profesion;
	}
	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public Integer getIdResidencia() {
		return idResidencia;
	}
	public void setIdResidencia(Integer idResidencia) {
		this.idResidencia = idResidencia;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getInscrita() {
		return inscrita;
	}
	public void setInscrita(String inscrita) {
		this.inscrita = inscrita;
	}
	public String getFolioInscrito() {
		return folioInscrito;
	}
	public void setFolioInscrito(String folioInscrito) {
		this.folioInscrito = folioInscrito;
	}
	public String getLibroInscrito() {
		return libroInscrito;
	}
	public void setLibroInscrito(String libroInscrito) {
		this.libroInscrito = libroInscrito;
	}
	public String getUbicacionInscrito() {
		return ubicacionInscrito;
	}
	public void setUbicacionInscrito(String ubicacionInscrito) {
		this.ubicacionInscrito = ubicacionInscrito;
	}
	
	

}
