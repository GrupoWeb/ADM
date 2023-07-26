package mx.gob.se.rug.detallegarantia.to;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class DetalleTO  implements Serializable{
	
		private static final long serialVersionUID = 1L;
		     
		private Integer idgarantia;
		private Integer idtipobien;
		private Integer idtipogarantia;
		private String monto;
		private Double limite;
		private String descbienes;
		private Date fechaCelebracionObligacion;
		private String acto;
		private Date fecacelebcontrato;
		private String otrosterminos;
		private String otroscontrato;
		private String otrosgarantia;
		private String tiposbienes;
		private String tipocontrato;
		private String tipogarantia;
		private Integer vigencia;
		private Integer relacionbien;
		private Integer  idtipocontrato;
		private Integer idcontrato;
		private String  cambio;
		private String  instrumento;
		private String  desmoneda;
		private Integer idmoneda;
		private Date fechaFinOb;
		private String nombrePersona;
		
		
		//ATRIBUTOS TEMPORALES
		private String descripcionGarantia;
		private Integer idTramite;
		private Integer idTipoTramite;
		private String descripcionTramite;
		private Date fechaModificacion;
		
		//Datos basa
		
		private String tipoContratobasa;
		private Date   fechainibasa;
		private Date   fechafinbasa;
		private String terminosbasa;
		private String noGarantiaPreviaOt;
		
		//Datos nuevos
		private String esPrioritaria;
		private String enRegistro;
		private String txtRegistro;
		
		             
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
		
		public String getActo() {
			return acto;
		}
		public void setActo(String acto) {
			this.acto = acto;
		}
		
		public String getOtrosterminos() {
			return otrosterminos;
		}
		public void setOtrosterminos(String otrosterminos) {
			this.otrosterminos = otrosterminos;
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
		public void setVigencia(Integer vigencia) {
			this.vigencia = vigencia;
		}
		public Integer getVigencia() {
			return vigencia;
		}
		public void setTipogarantia(String tipogarantia) {
			this.tipogarantia = tipogarantia;
		}
		public void setOtroscontrato(String otroscontrato) {
			this.otroscontrato = otroscontrato;
		}
		public String getOtroscontrato() {
			return otroscontrato;
		}
		public void setOtrosgarantia(String otrosgarantia) {
			this.otrosgarantia = otrosgarantia;
		}
		public String getOtrosgarantia() {
			return otrosgarantia;
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
		public void setIdcontrato(Integer idcontrato) {
			this.idcontrato = idcontrato;
		}
		public Integer getIdcontrato() {
			return idcontrato;
		}
		public Integer getIdTramite() {
			return idTramite;
		}
		public void setMonto(String monto) {
			this.monto = monto;
		}
		public String getMonto() {
			return monto;
		}
		public void setIdTramite(Integer idTramite) {
			this.idTramite = idTramite;
		}
		

		public void setCambio(String cambio) {
			this.cambio = cambio;
		}
		public String getCambio() {
			return cambio;
		}
		public void setInstrumento(String instrumento) {
			this.instrumento = instrumento;
		}
		public String getInstrumento() {
			return instrumento;
		}
		public void setDesmoneda(String desmoneda) {
			this.desmoneda = desmoneda;
		}
		public String getDesmoneda() {
			return desmoneda;
		}
		public void setIdmoneda(Integer idmoneda) {
			this.idmoneda = idmoneda;
		}
		public Integer getIdmoneda() {
			return idmoneda;
		}
		public void setIdtipogarantia(Integer idtipogarantia) {
			this.idtipogarantia = idtipogarantia;
		}
		public Integer getIdtipogarantia() {
			return idtipogarantia;
		}

		
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
		public void setTipoContratobasa(String tipoContratobasa) {
			this.tipoContratobasa = tipoContratobasa;
		}
		public String getTipoContratobasa() {
			return tipoContratobasa;
		}
		public void setFechainibasa(Date fechainibasa) {
			this.fechainibasa = fechainibasa;
		}
		public Date getFechainibasa() {
			return fechainibasa;
		}
		public void setFechafinbasa(Date fechafinbasa) {
			this.fechafinbasa = fechafinbasa;
		}
		public Date getFechafinbasa() {
			return fechafinbasa;
		}
		public void setTerminosbasa(String terminosbasa) {
			this.terminosbasa = terminosbasa;
		}
		public String getTerminosbasa() {
			return terminosbasa;
		}
		public Date getFechaCelebracionObligacion() {
			return fechaCelebracionObligacion;
		}
		public void setFechaCelebracionObligacion(Date fechaCelebracionObligacion) {
			this.fechaCelebracionObligacion = fechaCelebracionObligacion;
		}
		public Date getFecacelebcontrato() {
			return fecacelebcontrato;
		}
		public void setFecacelebcontrato(Date fecacelebcontrato) {
			this.fecacelebcontrato = fecacelebcontrato;
		}
		public Date getFechaFinOb() {
			return fechaFinOb;
		}
		public void setFechaFinOb(Date fechaFinOb) {
			this.fechaFinOb = fechaFinOb;
		}
		public String getNoGarantiaPreviaOt() {
			return noGarantiaPreviaOt;
		}
		public void setNoGarantiaPreviaOt(String noGarantiaPreviaOt) {
			this.noGarantiaPreviaOt = noGarantiaPreviaOt;
		}
		public String getEsPrioritaria() {
			return esPrioritaria;
		}
		public void setEsPrioritaria(String esPrioritaria) {
			this.esPrioritaria = esPrioritaria;
		}
		public String getEnRegistro() {
			return enRegistro;
		}
		public void setEnRegistro(String enRegistro) {
			this.enRegistro = enRegistro;
		}
		public String getTxtRegistro() {
			return txtRegistro;
		}
		public void setTxtRegistro(String txtRegistro) {
			this.txtRegistro = txtRegistro;
		}
		public String getNombrePersona() {
			return nombrePersona;
		}
		public void setNombrePersona(String nombrePersona) {
			this.nombrePersona = nombrePersona;
		}
}
