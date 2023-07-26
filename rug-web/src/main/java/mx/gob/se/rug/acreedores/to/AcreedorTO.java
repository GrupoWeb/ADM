package mx.gob.se.rug.acreedores.to;

import mx.gob.se.rug.dto.Persona;

public class AcreedorTO extends Persona{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idAcreedor;
	private String razonSocial;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String rfc;
	private String folioMercantil;
	private String calle;
	private String calleNumero;
	private String calleNumeroInterior;
	private String codigoPostal;
	private Integer idColonia;
	private Integer idLocalidad;
	private String tipoPersona;
	private String idEstado;
	private String idMunicipio;
	private String curp;
	private Integer idNacionalidad;
	private Integer idTramitePendiente;
	private String documentocurp;
	private String telefono;
	private String correoElectronico;
	private String extencion;
	private String nombreCompleto;
	private String zonaPostal;
	private String poblacion;
	private String domicilioUno;
	private String domicilioDos;
	private Integer idPaisResidencia;
	private String tipo;
	private String nif;
	private Boolean acreedorInscribe;
	private Boolean afolExiste;
	
	/** campos nuevos */
	private String inscrita; // inscrita bajo el numero
	private String folioInscrito; //folio
	private String libroInscrito; //libro
	private String ubicacionInscrito; // Ubicada en
	private String edad; //Edad
	private String estadoCivil; //Estado civil
	private String profesion; //Profesion
	
	private Integer privilegioGrupos;
	private Integer privilegioModificar;
	
	public Boolean getAcreedorInscribe() {
		return acreedorInscribe;
	}
	public void setAcreedorInscribe(Boolean acreedorInscribe) {
		this.acreedorInscribe = acreedorInscribe;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getZonaPostal() {
		return zonaPostal;
	}
	public void setZonaPostal(String zonaPostal) {
		this.zonaPostal = zonaPostal;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
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
	public String getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	public String getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public Integer getIdColonia() {
		return idColonia;
	}
	public void setIdColonia(Integer idColonia) {
		this.idColonia = idColonia;
	}
	public Integer getIdLocalidad() {
		return idLocalidad;
	}
	public void setIdLocalidad(Integer idLocalidad) {
		this.idLocalidad = idLocalidad;
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
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getCalleNumero() {
		return calleNumero;
	}
	public void setCalleNumero(String calleNumero) {
		this.calleNumero = calleNumero;
	}
	public String getCalleNumeroInterior() {
		return calleNumeroInterior;
	}
	public void setCalleNumeroInterior(String calleNumeroInterior) {
		this.calleNumeroInterior = calleNumeroInterior;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	
	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}
	public String getFolioMercantil() {
		return folioMercantil;
	}
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	public String getTipoPersona() {
		return tipoPersona;
	}
	public void setIdAcreedor(String idAcreedor) {
		this.idAcreedor = idAcreedor;
	}
	public String getIdAcreedor() {
		return idAcreedor;
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
	public void setDocumentocurp(String documentocurp) {
		this.documentocurp = documentocurp;
	}
	public String getDocumentocurp() {
		return documentocurp;
	}
	public void setIdTramitePendiente(Integer idTramitePendiente) {
		this.idTramitePendiente = idTramitePendiente;
	}
	public Integer getIdTramitePendiente() {
		return idTramitePendiente;
	}
	public void setExtencion(String extencion) {
		this.extencion = extencion;
	}
	public String getExtencion() {
		return extencion;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setIdPaisResidencia(Integer idPaisResidencia) {
		this.idPaisResidencia = idPaisResidencia;
	}
	public Integer getIdPaisResidencia() {
		return idPaisResidencia;
	}
	public Boolean getAfolExiste() {
		return afolExiste;
	}
	public void setAfolExiste(Boolean afolExiste) {
		this.afolExiste = afolExiste;
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
	public Integer getPrivilegioGrupos() {
		return privilegioGrupos;
	}
	public void setPrivilegioGrupos(Integer privilegioGrupos) {
		this.privilegioGrupos = privilegioGrupos;
	}
	public Integer getPrivilegioModificar() {
		return privilegioModificar;
	}
	public void setPrivilegioModificar(Integer privilegioModificar) {
		this.privilegioModificar = privilegioModificar;
	}

	
}