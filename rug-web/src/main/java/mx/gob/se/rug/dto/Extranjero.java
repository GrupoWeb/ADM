/*
 * Extranjero.java        11/05/2009
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
public class Extranjero extends AbstractBaseDTO implements Procedencia {
	private String nacionalidad;
	private String pais;
	private CalidadMigratoria calidadMigratoria;
	private String lugarNacimiento;

	/**
	 * @return the nacionalidad
	 */
	public String getNacionalidad() {
		return nacionalidad;
	}

	/**
	 * @param nacionalidad
	 *            the nacionalidad to set
	 */
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
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
	 * @return the calidadMigratoria
	 */
	public CalidadMigratoria getCalidadMigratoria() {
		return calidadMigratoria;
	}

	/**
	 * @param calidadMigratoria
	 *            the calidadMigratoria to set
	 */
	public void setCalidadMigratoria(CalidadMigratoria calidadMigratoria) {
		this.calidadMigratoria = calidadMigratoria;
	}

	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}

	public String getLugarNacimiento() {
		return lugarNacimiento;
	}

}
