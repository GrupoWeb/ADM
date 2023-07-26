/*
 * PersonaMoral.java        01/05/2009
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140, 
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y de uso exclusivo de la
 * Secretaría de Economía.
 *
 */

package mx.gob.se.rug.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mx.gob.se.rug.constants.TipoPersona;

/**
 * 
 * 
 * 
 * @version 0.2
 * @author Alfonso Esquivel
 * 
 */
@SuppressWarnings("serial")
public class PersonaMoral extends Persona {

	private String denominacion;
	private Set<String> denominaciones;
	private String idEmpresa;
	private Date fechaConstitucion;
	private String idRegimenJuridico;
	private String exclusionExtranjeros;
	private int duracionSociedad;
	private boolean definido;
	private String numeroPermisoSRE;
	private String numeroExpedienteSRE;
	private String numeroFolioSRE;
	private Acreditacion acreditacion;
	private String tipoAdministracion;
	private Map<String, CapitalSocial> capitalSocial;
	private Set<Producto> productos;
	private ActividadEconomica actividadEconomica;
	private List<Domicilio> domicilios;
	private List<Persona> personas;
	private List<Tramite> tramites;
	private TipoPersonaMoral tipoPersonaMoral;
	private SolicitudAperturaEmpresa solicitud;
	private String defineComisarios;
	private List<PersonaFisica> personasFisicas;
	private DocumentPortal documentPortal;
	

	/**
	 * 
	 * @param idPersona
	 */


	public void setPersonasFisicas(List<PersonaFisica> personasFisicas){
		this.personasFisicas=personasFisicas;
	}
	
	public List<PersonaFisica> getPersonasFisicas(){
		return personasFisicas;
	}
	
	/**
	 * @return the denominacion
	 */
	public String getDenominacion() {
		return denominacion;
	}

	/**
	 * @param denominacion
	 *            the denominacion to set
	 */
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	/**
	 * @return the fechaConstitucion
	 */
	public Date getFechaConstitucion() {
		return fechaConstitucion;
	}

	/**
	 * @param fechaConstitucion
	 *            the fechaConstitucion to set
	 */
	public void setFechaConstitucion(Date fechaConstitucion) {
		this.fechaConstitucion = fechaConstitucion;
	}

	/**
	 * @return the exclusionExtranjeros
	 */
	public String getExclusionExtranjeros() {
		return exclusionExtranjeros;
	}

	/**
	 * @param exclusionExtranjeros
	 *            the exclusionExtranjeros to set
	 */
	public void setExclusionExtranjeros(String exclusionExtranjeros) {
		this.exclusionExtranjeros = exclusionExtranjeros;
	}

	/**
	 * @return the duracionSociedad
	 */
	public int getDuracionSociedad() {
		return duracionSociedad;
	}

	/**
	 * @param duracionSociedad
	 *            the duracionSociedad to set
	 */
	public void setDuracionSociedad(int duracionSociedad) {
		this.duracionSociedad = duracionSociedad;
	}

	public void setDefinido(boolean definido) {
		this.definido = definido;
	}

	public boolean isDefinido() {
		return definido;
	}

	public boolean getDefinido() {
		return definido;
	}

	/**
	 * @return
	 */
	public String getNumeroPermisoSRE() {
		return numeroPermisoSRE;
	}

	/**
	 * @param numeroPermisoSRE
	 */
	public void setNumeroPermisoSRE(String numeroPermisoSRE) {
		this.numeroPermisoSRE = numeroPermisoSRE;
	}

	/**
	 * @return
	 */
	public String getNumeroExpedienteSRE() {
		return numeroExpedienteSRE;
	}

	/**
	 * @param numeroExpedienteSRE
	 */
	public void setNumeroExpedienteSRE(String numeroExpedienteSRE) {
		this.numeroExpedienteSRE = numeroExpedienteSRE;
	}

	/**
	 * @return
	 */
	public String getNumeroFolioSRE() {
		return numeroFolioSRE;
	}

	/**
	 * @param numeroFolioSRE
	 */
	public void setNumeroFolioSRE(String numeroFolioSRE) {
		this.numeroFolioSRE = numeroFolioSRE;
	}

	/**
	 * @return
	 */
	public Acreditacion getAcreditacion() {
		return acreditacion;
	}

	/**
	 * @param acreditacion
	 */
	public void setAcreditacion(Acreditacion acreditacion) {
		this.acreditacion = acreditacion;
	}

	/**
	 * @return the tipoAdministracion
	 */
	public String getTipoAdministracion() {
		return tipoAdministracion;
	}

	/**
	 * @param tipoAdministracion
	 *            the tipoAdministracion to set
	 */
	public void setTipoAdministracion(String tipoAdministracion) {
		this.tipoAdministracion = tipoAdministracion;
	}

	/**
	 * @return the capitalSocial
	 */
	public Map<String, CapitalSocial> getCapitalSocial() {
		return capitalSocial;
	}

	/**
	 * @param capitalSocial
	 *            the capitalSocial to set
	 */
	public void setCapitalSocial(Map<String, CapitalSocial> capitalSocial) {
		this.capitalSocial = capitalSocial;
	}

	/**
	 * @return the productos
	 */
	public Set<Producto> getProductos() {
		return productos;
	}

	/**
	 * @param productos
	 *            the productos to set
	 */
	public void setProductos(Set<Producto> productos) {
		this.productos = productos;
	}

	/**
	 * @return the actividadEconomica
	 */
	public ActividadEconomica getActividadEconomica() {
		return actividadEconomica;
	}

	/**
	 * @param actividadEconomica
	 *            the actividadEconomica to set
	 */
	public void setActividadEconomica(ActividadEconomica actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	/**
	 * @return the domicilios
	 */
	public List<Domicilio> getDomicilios() {
		return domicilios;
	}

	/**
	 * @param domicilios
	 *            the domicilios to set
	 */
	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}

	/**
	 * @return the personas
	 */
	public List<Persona> getPersonas() {
		return personas;
	}

	/**
	 * @param personas
	 *            the personas to set
	 */
	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	/**
	 * @return the tramites
	 */
	public List<Tramite> getTramites() {
		return tramites;
	}

	/**
	 * @param tramites
	 *            the tramites to set
	 */
	public void setTramites(List<Tramite> tramites) {
		this.tramites = tramites;
	}

	/**
	 * @return the tipoPersonaMoral
	 */
	public TipoPersonaMoral getTipoPersonaMoral() {
		return tipoPersonaMoral;
	}

	/**
	 * @param tipoPersonaMoral
	 *            the tipoPersonaMoral to set
	 */
	public void setTipoPersonaMoral(TipoPersonaMoral tipoPersonaMoral) {
		this.tipoPersonaMoral = tipoPersonaMoral;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setSolicitud(SolicitudAperturaEmpresa solicitud) {
		this.solicitud = solicitud;
	}

	public SolicitudAperturaEmpresa getSolicitud() {
		return solicitud;
	}

	public void setIdRegimenJuridico(String idRegimenJuridico) {
		this.idRegimenJuridico = idRegimenJuridico;
	}

	public String getIdRegimenJuridico() {
		return idRegimenJuridico;
	}

	public void setDefineComisarios(String defineComisarios) {
		this.defineComisarios = defineComisarios;
	}

	public String getDefineComisarios() {
		return defineComisarios;
	}

	public void setDocumentPortal(DocumentPortal documentPortal) {
		this.documentPortal = documentPortal;
	}

	public DocumentPortal getDocumentPortal() {
		return documentPortal;
	}

	public void setDenominaciones(Set<String> denominaciones) {
		this.denominaciones = denominaciones;
	}

	public Set<String> getDenominaciones() {
		return denominaciones;
	}

}
