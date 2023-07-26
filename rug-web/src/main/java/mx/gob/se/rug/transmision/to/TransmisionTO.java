package mx.gob.se.rug.transmision.to;

import java.util.Date;

public class TransmisionTO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer idgarantia;
	private Integer idtipobien;
	private String idtipogarantia;
	private Double monto;
	private Double limite;
	private String descbienes;
	private String fecha_celeb;
	private String acto;
	private String fecacelebcontrato;
	private String otrosterminos;
	private String otroscontrato;
	private String otrosgarantia;
	private String tiposbienes;
	private String tipocontrato;
	private String tipogarantia;
	private Integer vigencia;
	private Integer relacionbien;
	private Integer idtipocontrato;
	
	//ATRIBUTOS TEMPORALES
	private String descripcionGarantia;
	private Integer idTipoTramite;
	private String descripcionTramite;
	private Date fechaModificacion;
	
	
	public Integer getIdgarantia() {
		return idgarantia;
	}
	public void setIdgarantia(Integer idgarantia) {
		this.idgarantia = idgarantia;
	}
	public Integer getIdtipobien() {
		return idtipobien;
	}
	public void setIdtipobien(Integer idtipobien) {
		this.idtipobien = idtipobien;
	}
	public String getIdtipogarantia() {
		return idtipogarantia;
	}
	public void setIdtipogarantia(String idtipogarantia) {
		this.idtipogarantia = idtipogarantia;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Double getLimite() {
		return limite;
	}
	public void setLimite(Double limite) {
		this.limite = limite;
	}
	public String getDescbienes() {
		return descbienes;
	}
	public void setDescbienes(String descbienes) {
		this.descbienes = descbienes;
	}
	public String getFecha_celeb() {
		return fecha_celeb;
	}
	public void setFecha_celeb(String fechaCeleb) {
		fecha_celeb = fechaCeleb;
	}
	public String getActo() {
		return acto;
	}
	public void setActo(String acto) {
		this.acto = acto;
	}
	public String getFecacelebcontrato() {
		return fecacelebcontrato;
	}
	public void setFecacelebcontrato(String fecacelebcontrato) {
		this.fecacelebcontrato = fecacelebcontrato;
	}
	public String getOtrosterminos() {
		return otrosterminos;
	}
	public void setOtrosterminos(String otrosterminos) {
		this.otrosterminos = otrosterminos;
	}
	public String getOtroscontrato() {
		return otroscontrato;
	}
	public void setOtroscontrato(String otroscontrato) {
		this.otroscontrato = otroscontrato;
	}
	public String getOtrosgarantia() {
		return otrosgarantia;
	}
	public void setOtrosgarantia(String otrosgarantia) {
		this.otrosgarantia = otrosgarantia;
	}
	public String getTiposbienes() {
		return tiposbienes;
	}
	public void setTiposbienes(String tiposbienes) {
		this.tiposbienes = tiposbienes;
	}
	public String getTipocontrato() {
		return tipocontrato;
	}
	public void setTipocontrato(String tipocontrato) {
		this.tipocontrato = tipocontrato;
	}
	public String getTipogarantia() {
		return tipogarantia;
	}
	public void setTipogarantia(String tipogarantia) {
		this.tipogarantia = tipogarantia;
	}
	public Integer getVigencia() {
		return vigencia;
	}
	public void setVigencia(Integer vigencia) {
		this.vigencia = vigencia;
	}
	public String getDescripcionGarantia() {
		return descripcionGarantia;
	}
	public void setDescripcionGarantia(String descripcionGarantia) {
		this.descripcionGarantia = descripcionGarantia;
	}
	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}
	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	public String getDescripcionTramite() {
		return descripcionTramite;
	}
	public void setDescripcionTramite(String descripcionTramite) {
		this.descripcionTramite = descripcionTramite;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public void setRelacionbien(Integer relacionbien) {
		this.relacionbien = relacionbien;
	}
	public Integer getRelacionbien() {
		return relacionbien;
	}
	public void setIdtipocontrato(Integer idtipocontrato) {
		this.idtipocontrato = idtipocontrato;
	}
	public Integer getIdtipocontrato() {
		return idtipocontrato;
	}

}
