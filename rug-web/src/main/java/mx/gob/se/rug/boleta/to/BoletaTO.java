package mx.gob.se.rug.boleta.to;

import java.io.Serializable;
import java.math.BigDecimal;

import mx.gob.se.rug.to.UsuarioTO;

public class BoletaTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int idgarantia;
	private int idtipobien;
	private int idtipogarantia;
	private BigDecimal monto;	
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
	private int vigencia;	
	private int  idtipocontrato;
	private int idtramite;
	private String tipotransaccion;
	private String fechainicio;
	private String nombreOtorgante;
	private String nombreAcreedor;
	private String folio;
	private String tipoPerOtorgante;
	private BigDecimal precio;
	private String fechacreacion;
	private int relacionbien;
	private String autoridad;
	private String anotacion;	
	private String curp;
	private Integer idcancelacion;
	private Integer idinscripcion;
	private UsuarioTO usuarioTO;
	private FirmaTO firmaTO;
	
	public int getIdgarantia() {
		return idgarantia;
	}
	public void setIdgarantia(int idgarantia) {
		this.idgarantia = idgarantia;
	}
	public int getIdtipobien() {
		return idtipobien;
	}
	public void setIdtipobien(int idtipobien) {
		this.idtipobien = idtipobien;
	}
	public int getIdtipogarantia() {
		return idtipogarantia;
	}
	public void setIdtipogarantia(int idtipogarantia) {
		this.idtipogarantia = idtipogarantia;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
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
	public int getVigencia() {
		return vigencia;
	}
	public void setVigencia(int vigencia) {
		this.vigencia = vigencia;
	}
	public int getIdtipocontrato() {
		return idtipocontrato;
	}
	public void setIdtipocontrato(int idtipocontrato) {
		this.idtipocontrato = idtipocontrato;
	}
	public String getTipotransaccion() {
		return tipotransaccion;
	}
	public void setTipotransaccion(String tipotransaccion) {
		this.tipotransaccion = tipotransaccion;
	}
	public String getFechainicio() {
		return fechainicio;
	}
	public void setFechainicio(String fechainicio) {
		this.fechainicio = fechainicio;
	}
	public String getNombreOtorgante() {
		return nombreOtorgante;
	}
	public void setNombreOtorgante(String nombreOtorgante) {
		this.nombreOtorgante = nombreOtorgante;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getTipoPerOtorgante() {
		return tipoPerOtorgante;
	}
	public void setTipoPerOtorgante(String tipoPerOtorgante) {
		this.tipoPerOtorgante = tipoPerOtorgante;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public String getFechacreacion() {
		return fechacreacion;
	}
	public void setFechacreacion(String fechacreacion) {
		this.fechacreacion = fechacreacion;
	}
	public int getRelacionbien() {
		return relacionbien;
	}
	public void setRelacionbien(int relacionbien) {
		this.relacionbien = relacionbien;
	}
	public void setIdtramite(int idtramite) {
		this.idtramite = idtramite;
	}
	public int getIdtramite() {
		return idtramite;
	}
	public void setAutoridad(String autoridad) {
		this.autoridad = autoridad;
	}
	public String getAutoridad() {
		return autoridad;
	}
	public void setAnotacion(String anotacion) {
		this.anotacion = anotacion;
	}
	public String getAnotacion() {
		return anotacion;
	}
	public void setCurp(String curp) {
		this.curp = curp;
	}
	public String getCurp() {
		return curp;
	}
	public void setIdcancelacion(Integer idcancelacion) {
		this.idcancelacion = idcancelacion;
	}
	public Integer getIdcancelacion() {
		return idcancelacion;
	}
	public void setIdinscripcion(Integer idinscripcion) {
		this.idinscripcion = idinscripcion;
	}
	public Integer getIdinscripcion() {
		return idinscripcion;
	}
	public String getNombreAcreedor() {
		return nombreAcreedor;
	}
	public void setNombreAcreedor(String nombreAcreedor) {
		this.nombreAcreedor = nombreAcreedor;
	}
	public UsuarioTO getUsuarioTO() {
		return usuarioTO;
	}
	public void setUsuarioTO(UsuarioTO usuarioTO) {
		this.usuarioTO = usuarioTO;
	}
	public FirmaTO getFirmaTO() {
		return firmaTO;
	}
	public void setFirmaTO(FirmaTO firmaTO) {
		this.firmaTO = firmaTO;
	}
	
	
	
}
