package mx.gob.se.rug.dto;

/*
 * Persona.java        01/05/2009
 *
 * Copyright (c) 2009 Secretaria de Economia
 * Alfonso Reyes No. 30 Col. Hipodromo Condesa C.P. 06140,
 * Delegacion Cuauhtimoc, Mexico, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y de uso exclusivo de la
 * Secretaria de Economia.
 *
 */


import java.util.List;

import mx.gob.economia.dgi.framework.base.dto.AbstractBaseDTO;


/**
 * 
 * 
 * 
 * @version 0.1
 * @author Alfonso Esquivel
 * 
 */
@SuppressWarnings("serial")
public abstract class Persona extends AbstractBaseDTO {
	private Integer idPersona;
	private String idProcedencia;
	private String pais;
	private String rfc;

	private List<RolEmpresa> roles;
	private Domicilio domicilio;
	private DatosContacto datosContacto;
	
	
	public void setDomicilioInternacional(DomicilioInternacional domicilioInternacional){
		domicilio = domicilioInternacional;
	}
	
	public DomicilioInternacional getDomicilioInternacional(){
		if(domicilio instanceof DomicilioInternacional){
			return (DomicilioInternacional)domicilio;
		}
		return null;
	}
	
	public void setDomicilioNacional(DomicilioNacional domicilioNacional){
		domicilio = domicilioNacional;
	}
	
	public DomicilioNacional getDomicilioNacional(){
		if(domicilio instanceof DomicilioNacional){
			return (DomicilioNacional)domicilio;
		}
		return null;
	}
	
	/**
	 * @return the idPersona
	 */
	public Integer getIdPersona() {
		return idPersona;
	}

	/**
	 * @param idPersona
	 *            the idPersona to set
	 */
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}

	/**
	 * @return the idProcedencia
	 */
	public String getIdProcedencia() {
		return idProcedencia;
	}

	/**
	 * @param idProcedencia
	 *            the idProcedencia to set
	 */
	public void setIdProcedencia(String idProcedencia) {
		this.idProcedencia = idProcedencia;
	}

	/**
	 * @return the rfc
	 */
	public String getRfc() {
		return rfc;
	}

	/**
	 * @param rfc
	 *            the rfc to set
	 */
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	/**
	 * @return the tipoPersona
	 */

	/**
	 * @return the roles
	 */
	public List<RolEmpresa> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<RolEmpresa> roles) {
		this.roles = roles;
	}

	/**
	 * @return the domicilio
	 */
	public Domicilio getDomicilio() {
		return domicilio;
	}

	/**
	 * @param domicilio
	 *            the domicilio to set
	 */
	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	public void setDatosContacto(DatosContacto datosContacto) {
		this.datosContacto = datosContacto;
	}

	public DatosContacto getDatosContacto() {
		return datosContacto;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getPais() {
		return pais;
	}

}
