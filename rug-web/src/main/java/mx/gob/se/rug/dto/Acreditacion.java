/*
 * Acreditacion.java        07/05/2009
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
public class Acreditacion extends AbstractBaseDTO {
	private int idAcreditacion;
	private String descripcion;
	private String folio;

	public Acreditacion() {
		super();
	}

	/**
	 * @param idAcreditacion
	 * @param descripcion
	 * @param folio
	 */
	public Acreditacion(int idAcreditacion, String descripcion, String folio) {
		this();
		this.idAcreditacion = idAcreditacion;
		this.descripcion = descripcion;
		this.folio = folio;
	}

	/**
	 * @return the idAcreditacion
	 */
	public int getIdAcreditacion() {
		return idAcreditacion;
	}

	/**
	 * @param idAcreditacion
	 *            the idAcreditacion to set
	 */
	public void setIdAcreditacion(int idAcreditacion) {
		this.idAcreditacion = idAcreditacion;
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
