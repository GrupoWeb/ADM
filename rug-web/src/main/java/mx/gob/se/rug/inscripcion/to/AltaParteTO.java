package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;

public class AltaParteTO implements Serializable{
	
	/**
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private Integer idTramite;
	private Integer idParte;
	private String domicilioUno;
	private String domicilioDos;
	private String poblacion;
	private String zonaPostal;
	private String claveLada;
	private String tipoPersona;
	private String razonSocial;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String folioMercantil;
	private String calle;
	private String numeroExterior;
	private String numeroInterior;
	private Integer idColonia;
	private Integer idPersona;
	private String hayDomicilio;
	private Integer idLocalidad;
	private String codigoPostal;
	private String curp;
	private Integer idNacionalidad;
	private Integer idUsuario;
	private String telefono;
	private String correoElectronico;
	private String rfc;
	private String extencion;
	private Integer idPaisResidencia;
	private String tipo;
	private Boolean acreedorInscribe;
	private String nif;
	
	/** campos nuevos */
	private String inscrita; // inscrita bajo el numero
	private String folioInscrito; //folio
	private String libroInscrito; //libro
	private String ubicacionInscrito; // Ubicada en
	private String edad; //Edad
	private String estadoCivil; //Estado civil
	private String profesion; //Profesion
	
	
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public Boolean getAcreedorInscribe() {
		return acreedorInscribe;
	}
	public void setAcreedorInscribe(Boolean acreedorInscribe) {
		this.acreedorInscribe = acreedorInscribe;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	
	
	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public Integer getIdParte() {
		return idParte;
	}
	public void setIdParte(Integer idParte) {
		this.idParte = idParte;
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
	public String getFolioMercantil() {
		return folioMercantil;
	}
	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumeroExterior() {
		return numeroExterior;
	}
	public void setNumeroExterior(String numeroExterior) {
		this.numeroExterior = numeroExterior;
	}
	public String getNumeroInterior() {
		return numeroInterior;
	}
	public void setNumeroInterior(String numeroInterior) {
		this.numeroInterior = numeroInterior;
	}
	public Integer getIdColonia() {
		return idColonia;
	}
	public void setIdColonia(Integer idColonia) {
		this.idColonia = idColonia;
	}
	public Integer getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	public void setIdLocalidad(Integer idLocalidad) {
		this.idLocalidad = idLocalidad;
	}
	public Integer getIdLocalidad() {
		return idLocalidad;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getRfc() {
		return rfc;
	}
	public void setHayDomicilio(String hayDomicilio) {
		this.hayDomicilio = hayDomicilio;
	}
	public String getHayDomicilio() {
		return hayDomicilio;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCurp(String curp) {
		this.curp = curp;
	}
	public String getCurp() {
		return curp;
	}
	public void setIdNacionalidad(Integer idNacionalidad) {
		this.idNacionalidad = idNacionalidad;
	}
	public Integer getIdNacionalidad() {
		return idNacionalidad;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setExtencion(String extencion) {
		this.extencion = extencion;
	}
	public String getExtencion() {
		return extencion;
	}
	public void setClaveLada(String claveLada) {
		this.claveLada = claveLada;
	}
	public String getClaveLada() {
		return claveLada;
	}
	public String getDomicilioUno() {
		return domicilioUno;
	}
	public void setDomicilioUno(String domicilioUno) {
		this.domicilioUno = domicilioUno;
	}
	public String getDomicilioDos() {
		return domicilioDos;
	}
	public void setDomicilioDos(String domicilioDos) {
		this.domicilioDos = domicilioDos;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	public String getZonaPostal() {
		return zonaPostal;
	}
	public void setZonaPostal(String zonaPostal) {
		this.zonaPostal = zonaPostal;
	}
	public void setIdPaisResidencia(Integer idPaisResidencia) {
		this.idPaisResidencia = idPaisResidencia;
	}
	public Integer getIdPaisResidencia() {
		return idPaisResidencia;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	
	
	
}
