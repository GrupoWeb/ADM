/*
 * CapitalSocial.java        01/05/2009
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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

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
public class CapitalSocial extends AbstractBaseDTO {
	
    public static final String CAPITAL_VARIABLE = "Capital Variable";
    public static final String CAPITAL_FIJO = "Capital Fijo";
	private String tipoCapital;
	private BigDecimal monto;
	private BigDecimal numeroAcciones;
	private BigDecimal valorNominal;
	private float porcentaje;
	
	public CapitalSocial() {
		super();
	}
	
	public void setTipoCapital(String tipoCapital) {
		this.tipoCapital = tipoCapital;
	}

	public String getTipoCapital() {
		return tipoCapital;
	}

	/**
	 * @param numeroAcciones
	 * @param valorNominal
	 */
	public CapitalSocial(BigDecimal numeroAcciones, BigDecimal valorNominal) {
		this();
		this.numeroAcciones = numeroAcciones;
		this.valorNominal = valorNominal;
	}

	/**
	 * @return the monto
	 */
	public BigDecimal getMonto() {
		return monto;
	}

	/**
	 * @param monto
	 *            the monto to set
	 */
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	/**
	 * @return the numeroAcciones
	 */
	public BigDecimal getNumeroAcciones() {
		return numeroAcciones;
	}

	/**
	 * @param numeroAcciones
	 *            the numeroAcciones to set
	 */
	public void setNumeroAcciones(BigDecimal numeroAcciones) {
		this.numeroAcciones = numeroAcciones;
	}

	/**
	 * @return the valorNominal
	 */
	public BigDecimal getValorNominal() {
		return valorNominal;
	}

	/**
	 * @param valorNominal
	 *            the valorNominal to set
	 */
	public void setValorNominal(BigDecimal valorNominal) {
		this.valorNominal = valorNominal;
	}

	public void setPorcentaje(float porcentaje) {
		this.porcentaje = porcentaje;
	}

	public float getPorcentaje() {
		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		nf.setMaximumFractionDigits(2);
		nf.setGroupingUsed(false);
		nf.getRoundingMode();

		return Float.parseFloat((nf.format(porcentaje)));
	}
	
}
