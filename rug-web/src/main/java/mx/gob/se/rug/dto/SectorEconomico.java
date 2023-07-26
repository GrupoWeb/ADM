/*
 * SectorEconomico.java        01/05/2009
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
public class SectorEconomico extends AbstractBaseDTO {
	private int idClase;
	private int idSectorEconomico;
	private int idSubsectorEconomico;
	private int idRama;
	private int idSubrama;
	private String sectorEconomico;
	private String subsectorEconomico;
	private String rama;
	private String subRama;
	private String clase;
	private String sectorSubsector;
	private int porcentaje;
	private String observacion;
	private String tieneComentario;
	private String comentario;
	private String usoRestringido;
	private String idScian;
	private boolean seleccionada;
	private String idRegistroAe;

	/**
	 * @return the sectorEconomico
	 */
	public String getSectorEconomico() {
		return sectorEconomico;
	}

	/**
	 * @param sectorEconomico
	 *            the sectorEconomico to set
	 */
	public void setSectorEconomico(String sectorEconomico) {
		this.sectorEconomico = sectorEconomico;
	}

	/**
	 * @return the subsectorEconomico
	 */
	public String getSubsectorEconomico() {
		return subsectorEconomico;
	}

	/**
	 * @param subsectorEconomico
	 *            the subsectorEconomico to set
	 */
	public void setSubsectorEconomico(String subsectorEconomico) {
		this.subsectorEconomico = subsectorEconomico;
	}

	/**
	 * @return the rama
	 */
	public String getRama() {
		return rama;
	}

	/**
	 * @param rama
	 *            the rama to set
	 */
	public void setRama(String rama) {
		this.rama = rama;
	}

	/**
	 * @return the subRama
	 */
	public String getSubRama() {
		return subRama;
	}

	/**
	 * @param subRama
	 *            the subRama to set
	 */
	public void setSubRama(String subRama) {
		this.subRama = subRama;
	}

	/**
	 * @return the clase
	 */
	public String getClase() {
		return clase;
	}

	/**
	 * @param clase
	 *            the clase to set
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
	 * @return the idClase
	 */
	public int getIdClase() {
		return idClase;
	}

	/**
	 * @param idClase
	 *            the idClase to set
	 */
	public void setIdClase(int idClase) {
		this.idClase = idClase;
	}

	/**
	 * @return the idSectorEconomico
	 */
	public int getIdSectorEconomico() {
		return idSectorEconomico;
	}

	/**
	 * @param idSectorEconomico
	 *            the idSectorEconomico to set
	 */
	public void setIdSectorEconomico(int idSectorEconomico) {
		this.idSectorEconomico = idSectorEconomico;
	}

	/**
	 * @return the idSubsectorEconomico
	 */
	public int getIdSubsectorEconomico() {
		return idSubsectorEconomico;
	}

	/**
	 * @param idSubsectorEconomico
	 *            the idSubsectorEconomico to set
	 */
	public void setIdSubsectorEconomico(int idSubsectorEconomico) {
		this.idSubsectorEconomico = idSubsectorEconomico;
	}

	/**
	 * @return the idRama
	 */
	public int getIdRama() {
		return idRama;
	}

	/**
	 * @param idRama
	 *            the idRama to set
	 */
	public void setIdRama(int idRama) {
		this.idRama = idRama;
	}

	/**
	 * @return the idSubrama
	 */
	public int getIdSubrama() {
		return idSubrama;
	}

	/**
	 * @param idSubrama
	 *            the idSubrama to set
	 */
	public void setIdSubrama(int idSubrama) {
		this.idSubrama = idSubrama;
	}

	/**
	 * @return the sectorSubsector
	 */
	public String getSectorSubsector() {
		return sectorSubsector;
	}

	/**
	 * @param sectorSubsector
	 *            the sectorSubsector to set
	 */
	public void setSectorSubsector(String sectorSubsector) {
		this.sectorSubsector = sectorSubsector;
	}

	/**
	 * @return the procentaje
	 */
	public int getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @param procentaje
	 *            the procentaje to set
	 */
	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}

	/**
	 * @param observacion
	 *            the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * @return the tieneComentario
	 */
	public String getTieneComentario() {
		return tieneComentario;
	}

	/**
	 * @param tieneComentario
	 *            the tieneComentario to set
	 */
	public void setTieneComentario(String tieneComentario) {
		this.tieneComentario = tieneComentario;
	}

	/**
	 * @return the comentario
	 */
	public String getComentario() {
		return comentario;
	}

	/**
	 * @param comentario
	 *            the comentario to set
	 */
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setUsoRestringido(String usoRestringido) {
		this.usoRestringido = usoRestringido;
	}

	public String getUsoRestringido() {
		return usoRestringido;
	}

	public void setIdScian(String idScian) {
		this.idScian = idScian;
	}

	public String getIdScian() {
		return idScian;
	}

	public void setSeleccionada(boolean seleccionada) {
		this.seleccionada = seleccionada;
	}

	public boolean getSeleccionada() {
		return seleccionada;
	}

	public void setIdRegistroAe(String idRegistroAe) {
		this.idRegistroAe = idRegistroAe;
	}

	public String getIdRegistroAe() {
		return idRegistroAe;
	}

}
