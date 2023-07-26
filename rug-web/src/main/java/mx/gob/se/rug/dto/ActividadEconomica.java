/*
 * ActividadEconomica.java        01/05/2009
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
public class ActividadEconomica extends AbstractBaseDTO {
	private int idGiroEconomico;
	private String giroEconomico;
	private String actividadesPublico;
	private String maquinaRegistradora;
	private List<SectorEconomico> sectoresEconomicos;

	/**
	 * @return the giroEconomico
	 */
	public String getGiroEconomico() {
		return giroEconomico;
	}

	/**
	 * @param giroEconomico
	 *            the giroEconomico to set
	 */
	public void setGiroEconomico(String giroEconomico) {
		this.giroEconomico = giroEconomico;
	}

	/**
	 * @return the actividadesPublico
	 */
	public String getActividadesPublico() {
		return actividadesPublico;
	}

	/**
	 * @param actividadesPublico
	 *            the actividadesPublico to set
	 */
	public void setActividadesPublico(String actividadesPublico) {
		this.actividadesPublico = actividadesPublico;
	}

	/**
	 * @return the maquinaRegistradora
	 */
	public String getMaquinaRegistradora() {
		return maquinaRegistradora;
	}

	/**
	 * @param maquinaRegistradora
	 *            the maquinaRegistradora to set
	 */
	public void setMaquinaRegistradora(String maquinaRegistradora) {
		this.maquinaRegistradora = maquinaRegistradora;
	}

	/**
	 * @return the sectoresEconomicos
	 */
	public List<SectorEconomico> getSectoresEconomicos() {
		return sectoresEconomicos;
	}

	/**
	 * @param sectoresEconomicos
	 *            the sectoresEconomicos to set
	 */
	public void setSectoresEconomicos(List<SectorEconomico> sectoresEconomicos) {
		this.sectoresEconomicos = sectoresEconomicos;
	}

	public void setIdGiroEconomico(int idGiroEconomico) {
		this.idGiroEconomico = idGiroEconomico;
	}

	public int getIdGiroEconomico() {
		return idGiroEconomico;
	}

}
