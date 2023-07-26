/*
 * Identificacion.java        09/11/2009
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
public class Identificacion extends AbstractBaseDTO {
	private int idIdentificacion;
	private String descripcion;
	private String folio;


	public Identificacion() {
		super();
	}

	/**
	 * @param idAcreditacion
	 * @param descripcion
	 * @param folio
	 */
	public Identificacion(int idIdentificacion, String descripcion, String folio) {
		this();
		this.setIdIdentificacion(idIdentificacion);
		this.descripcion = descripcion;
		this.folio = folio;
	}

	public void setIdIdentificacion(int idIdentificacion) {
		this.idIdentificacion = idIdentificacion;
	}

	public int getIdIdentificacion() {
		return idIdentificacion;
	}


	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the folio
	 */
	public String getFolio() {
		return folio;
	}

	/**
	 * @param folio
	 *            the folio to set
	 */
	public void setFolio(String folio) {
		this.folio = folio;
	}

}
