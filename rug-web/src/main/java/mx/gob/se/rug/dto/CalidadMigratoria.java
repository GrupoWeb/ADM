/*
 * CalidadMigratoria.java        11/05/2009
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
public class CalidadMigratoria extends AbstractBaseDTO {
	private String formaMigratoria;
	private String folioFormaMigratoria;

	/**
	 * @return the formaMigratoria
	 */
	public String getFormaMigratoria() {
		return formaMigratoria;
	}

	/**
	 * @param formaMigratoria
	 *            the formaMigratoria to set
	 */
	public void setFormaMigratoria(String formaMigratoria) {
		this.formaMigratoria = formaMigratoria;
	}

	/**
	 * @return the folioFormaMigratoria
	 */
	public String getFolioFormaMigratoria() {
		return folioFormaMigratoria;
	}

	/**
	 * @param folioFormaMigratoria
	 *            the folioFormaMigratoria to set
	 */
	public void setFolioFormaMigratoria(String folioFormaMigratoria) {
		this.folioFormaMigratoria = folioFormaMigratoria;
	}

}
