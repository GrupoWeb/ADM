/*
 * GarantiaDTO.java        15/10/2010
 *
 * Copyright (c) 2009 Secretaría de Economía
 * Alfonso Reyes No. 30 Col. Hipódromo Condesa C.P. 06140, 
 * Delegación Cuauhtémoc, México, D.F.
 * Todos los Derechos Reservados.
 *
 * Este software es confidencial y contiene información perteneciente
 * a la Secretaría de Economía.
 * 
 */
package mx.gob.se.rug.garantia.to;

import java.io.Serializable;

public class ActoContratoTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tipoGarantia;
	private String fechaCelebracion;
	private String fechaSurgimientoDerecho;
	private String montoMaximo;
	private String tipoBienes;
	private String descripcion; //bienes
	private String otrosTerminos;
	private String valorDeBienes;
	private String ubicacionMueble;
	private String tipoBienesDesc;
	private String tipoGarantiaDesc;
	private String tipoMoneda;
	private String instrumentoPub;
	private Boolean cambiosBienesMonto;
	private Boolean noGarantiaPreviaOt;
	/** campos nuevos **/	
	private Boolean garantiaPrioritaria;	
	private Boolean cpRegistro;
	private String txtRegistro;
        
        /**corellana: leasing campo nuevo, monto_estimado**/
	private double monto_estimado;
        
        public double getMontoEstimado() {
		return monto_estimado;
	}
	public void setMontoEstimado(double montoEstimado) {
		this.monto_estimado = montoEstimado;
	}
        
        
	public String getInstrumentoPub() {
		return instrumentoPub;
	}
	public void setInstrumentoPub(String instrumentoPub) {
		this.instrumentoPub = instrumentoPub;
	}
	public Boolean getCambiosBienesMonto() {
		return cambiosBienesMonto;
	}
	public void setCambiosBienesMonto(Boolean cambiosBienesMonto) {
		this.cambiosBienesMonto = cambiosBienesMonto;
	}
	public Boolean getNoGarantiaPreviaOt() {
		return noGarantiaPreviaOt;
	}
	public void setNoGarantiaPreviaOt(Boolean noGarantiaPreviaOt) {
		this.noGarantiaPreviaOt = noGarantiaPreviaOt;
	}
	public String getTipoGarantia() {
		return tipoGarantia;
	}
	public void setTipoGarantia(String tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}
	public String getFechaCelebracion() {
		return fechaCelebracion;
	}
	public void setFechaCelebracion(String fechaCelebracion) {
		this.fechaCelebracion = fechaCelebracion;
	}
	public String getMontoMaximo() {
		return montoMaximo;
	}
	public void setMontoMaximo(String montoMaximo) {
		this.montoMaximo = montoMaximo;
	}
	public String getTipoBienes() {
		return tipoBienes;
	}
	public void setTipoBienes(String tipoBienes) {
		this.tipoBienes = tipoBienes;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getOtrosTerminos() {
		return otrosTerminos;
	}
	public void setOtrosTerminos(String otrosTerminos) {
		this.otrosTerminos = otrosTerminos;
	}
	public void setFechaSurgimientoDerecho(String fechaSurgimientoDerecho) {
		this.fechaSurgimientoDerecho = fechaSurgimientoDerecho;
	}
	public String getFechaSurgimientoDerecho() {
		return fechaSurgimientoDerecho;
	}
	public void setValorDeBienes(String valorDeBienes) {
		this.valorDeBienes = valorDeBienes;
	}
	public String getValorDeBienes() {
		return valorDeBienes;
	}
	public void setUbicacionMueble(String ubicacionMueble) {
		this.ubicacionMueble = ubicacionMueble;
	}
	public String getUbicacionMueble() {
		return ubicacionMueble;
	}
	public void setTipoBienesDesc(String tipoBienesDesc) {
		this.tipoBienesDesc = tipoBienesDesc;
	}
	public String getTipoBienesDesc() {
		return tipoBienesDesc;
	}
	public void setTipoGarantiaDesc(String tipoGarantiaDesc) {
		this.tipoGarantiaDesc = tipoGarantiaDesc;
	}
	public String getTipoGarantiaDesc() {
		return tipoGarantiaDesc;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public Boolean getGarantiaPrioritaria() {
		return garantiaPrioritaria;
	}
	public void setGarantiaPrioritaria(Boolean garantiaPrioritaria) {
		this.garantiaPrioritaria = garantiaPrioritaria;
	}	
	public String getTxtRegistro() {
		return txtRegistro;
	}
	public void setTxtRegistro(String txtRegistro) {
		this.txtRegistro = txtRegistro;
	}
	public Boolean getCpRegistro() {
		return cpRegistro;
	}
	public void setCpRegistro(Boolean cpRegistro) {
		this.cpRegistro = cpRegistro;
	}
	
	
}
