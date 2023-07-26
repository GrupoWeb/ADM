package mx.gob.se.rug.dto;

/*
 * Domicilio.java        01/05/2009
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


import mx.gob.economia.dgi.framework.base.dto.AbstractBaseDTO;

/**
 * 
 * 
 * 
 * @version 0.1
 * @author Fabián Guerra
 * 
 */
@SuppressWarnings("serial")
public abstract class Domicilio extends AbstractBaseDTO {
	private String idDomicilio;
	private String pais;
	private String codigoPostal;
	private String tipoDomicilio;
	private String ubicacion;

	/**
	 * @return the idDomicilio
	 */
	public String getIdDomicilio() {
		return idDomicilio;
	}

	/**
	 * @param idDomicilio
	 *            the idDomicilio to set
	 */
	public void setIdDomicilio(String idDomicilio) {
		this.idDomicilio = idDomicilio;
	}

	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais
	 *            the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the codigoPostal
	 */
	public String getCodigoPostal() {
		return codigoPostal;
	}

	/**
	 * @param codigoPostal
	 *            the codigoPostal to set
	 */
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	/**
	 * @return the tipoDomicilio
	 */
	public String getTipoDomicilio() {
		return tipoDomicilio;
	}

	/**
	 * @param tipoDomicilio
	 *            the tipoDomicilio to set
	 */
	public void setTipoDomicilio(String tipoDomicilio) {
		this.tipoDomicilio = tipoDomicilio;
	}

	/**
	 * @return the ubicacion
	 */
	public String getUbicacion() {
		return ubicacion;
	}

	/**
	 * @param ubicacion
	 *            the ubicacion to set
	 */
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

}
