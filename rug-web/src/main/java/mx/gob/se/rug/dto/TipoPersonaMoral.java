/*
 * TipoPersonaMoral.java        01/05/2009
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
public class TipoPersonaMoral extends AbstractBaseDTO {
	private int idTipoPersonaMoral;
	private String regimenJuridico;
	private String abreviacion;

	public TipoPersonaMoral() {
		super();
	}

	/**
	 * @param idTipoPersonaMoral
	 * @param regimenJuridico
	 * @param abreviacion
	 */
	public TipoPersonaMoral(int idTipoPersonaMoral, String regimenJuridico,
			String abreviacion) {
		super();
		this.idTipoPersonaMoral = idTipoPersonaMoral;
		this.regimenJuridico = regimenJuridico;
		this.abreviacion = abreviacion;
	}

	/**
	 * 
	 * @return the idTipoPersonaMoral
	 */
	public int getIdTipoPersonaMoral() {
		return idTipoPersonaMoral;
	}

	/**
	 * @param idTipoPersonaMoral
	 *            the idTipoPersonaMoral to set
	 */
	public void setIdTipoPersonaMoral(int idTipoPersonaMoral) {
		this.idTipoPersonaMoral = idTipoPersonaMoral;
	}

	/**
	 * @return the regimenJuridico
	 */
	public String getRegimenJuridico() {
		return regimenJuridico;
	}

	/**
	 * @param regimenJuridico
	 *            the regimenJuridico to set
	 */
	public void setRegimenJuridico(String regimenJuridico) {
		this.regimenJuridico = regimenJuridico;
	}

	/**
	 * @return the abreviacion
	 */
	public String getAbreviacion() {
		return abreviacion;
	}

	/**
	 * @param abreviacion
	 *            the abreviacion to set
	 */
	public void setAbreviacion(String abreviacion) {
		this.abreviacion = abreviacion;
	}

}
