package mx.gob.se.rug.busqueda.to;

import java.io.Serializable;

public class BusquedaTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String idTramite;
	private String idTipoTramite;
	private String fechaCreacion;
	private String descripcion;
	private String idPersona;
	private String nombre;
	private String folioMercantil;
	private String idGarantia;
	private String descGarantia;
	private String curpOtorgante;
	private String rfcOtorgante;
	private String noSerial;
	// search invoice and set
	private String invoice;
	private String set;
        private String nit;
        
        
        public String getNit() { /*orellana: busqueda por factura nit serie*/
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}


	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public String getNoSerial() {
		return noSerial;
	}
	public void setNoSerial(String noSerial) {
		this.noSerial = noSerial;
	}
	private Integer numReg;
	//Busqueda
	public String getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(String idTramite) {
		this.idTramite = idTramite;
	}
	
	public String getIdTipoTramite() {
		return idTipoTramite;
	}
	public void setIdTipoTramite(String idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFolioMercantil() {
		return folioMercantil;
	}
	public void setFolioMercantil(String folioMercantil) {
		this.folioMercantil = folioMercantil;
	}
	public String getIdGarantia() {
		return idGarantia;
	}
	public void setIdGarantia(String idGarantia) {
		this.idGarantia = idGarantia;
	}
	public String getDescGarantia() {
		return descGarantia;
	}
	public void setDescGarantia(String descGarantia) {
		this.descGarantia = descGarantia;
	}
	public String getCurpOtorgante() {
		return curpOtorgante;
	}
	public void setCurpOtorgante(String curpOtorgante) {
		this.curpOtorgante = curpOtorgante;
	}
	public Integer getNumReg() {
		return numReg;
	}
	public void setNumReg(Integer numReg) {
		this.numReg = numReg;
	}
	public String getRfcOtorgante() {
		return rfcOtorgante;
	}
	public void setRfcOtorgante(String rfcOtorgante) {
		this.rfcOtorgante = rfcOtorgante;
	}




}
