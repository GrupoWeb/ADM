package mx.gob.se.rug.to;

import java.io.Serializable;

import mx.gob.se.rug.tipogarantia.to.TipoGarantiaTO;
/**
 * 
 * @author Adat Gonzalez Dorantes,Carmen Aburto Castañeda
 * @see GaranteTO
 * @see ContratoTO
 * @version 1.0
 * @category DataTransferObject
 */
public class GarantiaTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int idgarantia;
	private String foliomercantil;
	private String fechainscr;
	private String fechafingar;
	private TipoGarantiaTO tipogarantia;
	private String ruta;
	private String valorbienes;
	private String numerogarantia;
	private String descripcion;
	private String otrosterminos;
	private String status;
	private String tipobienes;
	private GaranteTO garante;
	private ContratoTO contrato;
	private String ubicacion;
	private String  vigencia;
        private int row;
	
	
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getVigencia() {
		return vigencia;
	}
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}
	public int getIdgarantia() {
		return idgarantia;
	}
	public void setIdgarantia(int idgarantia) {
		this.idgarantia = idgarantia;
	}
	public String getFoliomercantil() {
		return foliomercantil;
	}
	public void setFoliomercantil(String foliomercantil) {
		this.foliomercantil = foliomercantil;
	}
	public String getFechainscr() {
		return fechainscr;
	}
	public void setFechainscr(String fechainscr) {
		this.fechainscr = fechainscr;
	}
	public String getFechafingar() {
		return fechafingar;
	}
	public void setFechafingar(String fechafingar) {
		this.fechafingar = fechafingar;
	}
	public TipoGarantiaTO getTipogarantia() {
		return tipogarantia;
	}
	public void setTipogarantia(TipoGarantiaTO tipogarantia) {
		this.tipogarantia = tipogarantia;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getValorbienes() {
		return valorbienes;
	}
	public void setValorbienes(String valorbienes) {
		this.valorbienes = valorbienes;
	}
	public String getNumerogarantia() {
		return numerogarantia;
	}
	public void setNumerogarantia(String numerogarantia) {
		this.numerogarantia = numerogarantia;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getOtrosterminos() {
		return otrosterminos;
	}
	public void setOtrosterminos(String otrosterminos) {
		this.otrosterminos = otrosterminos;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTipobienes() {
		return tipobienes;
	}
	public void setTipobienes(String tipobienes) {
		this.tipobienes = tipobienes;
	}
	
	public GaranteTO getGarante() {
		return garante;
	}
	public void setGarante(GaranteTO garante) {
		this.garante = garante;
	}
	public ContratoTO getContrato() {
		return contrato;
	}
	public void setContrato(ContratoTO contrato) {
		this.contrato = contrato;
	}

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }
	
        
	
}
