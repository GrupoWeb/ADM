package mx.gob.se.rug.modificacion.to;

import java.util.Date;

public class ModificacionTO {
	private static final long serialVersionUID = 1L;

	private Integer idgarantia;
	private Integer idtramite;
	private Integer idpersona;
	private Integer idcontrato;
	private String modtipoacto;
	private String modotrosterminos;
	private String modmonto;
	private String modotrosgarantia;
	private String modtipobien;
	private String moddescripcion;
	private Integer idulttramite;
	private String modotroscontrato;
	private String tipogarantia;
	private Date fecacelebcontrato;
	private String tipocontrato;
	private String modterminos;
	private Integer idmoneda;
	private String noGarantiaPreviaOt;
	private Date fechainiob;
	private Date fechafinob;
	private Date modfechaceleb;
	private Date fechatermino;
	private String modinstrumento;
	private Date fechacelebgarantia;
	private String cambioBienesMonto;
	
	//Datos nuevos
	private String esPrioritaria;
	private String enRegistro;
	private String txtRegistro;

	public Date getFechainiob() {
		return fechainiob;
	}

	public void setFechainiob(Date fechainiob) {
		this.fechainiob = fechainiob;
	}

	public Integer getIdgarantia() {
		return idgarantia;
	}

	public void setIdgarantia(Integer idgarantia) {
		this.idgarantia = idgarantia;
	}

	public String getModtipoacto() {
		return modtipoacto;
	}

	public void setModtipoacto(String modtipoacto) {
		this.modtipoacto = modtipoacto;
	}

	public Date getModfechaceleb() {
		return modfechaceleb;
	}

	public void setModfechaceleb(Date modfechaceleb) {
		this.modfechaceleb = modfechaceleb;
	}

	public String getModotrosterminos() {
		return modotrosterminos;
	}

	public void setModotrosterminos(String modotrosterminos) {
		this.modotrosterminos = modotrosterminos;
	}

	public String getModotrosgarantia() {
		return modotrosgarantia;
	}

	public void setModotrosgarantia(String modotrosgarantia) {
		this.modotrosgarantia = modotrosgarantia;
	}

	public String getModtipobien() {
		return modtipobien;
	}

	public void setModtipobien(String modtipobien) {
		this.modtipobien = modtipobien;
	}

	public String getModdescripcion() {
		return moddescripcion;
	}

	public void setModdescripcion(String moddescripcion) {
		this.moddescripcion = moddescripcion;
	}

	public void setIdtramite(Integer idtramite) {
		this.idtramite = idtramite;
	}

	public Integer getIdtramite() {
		return idtramite;
	}

	public void setIdpersona(Integer idpersona) {
		this.idpersona = idpersona;
	}

	public Integer getIdpersona() {
		return idpersona;
	}

	public void setIdcontrato(Integer idcontrato) {
		this.idcontrato = idcontrato;
	}

	public Integer getIdcontrato() {
		return idcontrato;
	}

	public void setIdulttramite(Integer idulttramite) {
		this.idulttramite = idulttramite;
	}

	public Integer getIdulttramite() {
		return idulttramite;
	}

	public void setModotroscontrato(String modotroscontrato) {
		this.modotroscontrato = modotroscontrato;
	}

	public String getModotroscontrato() {
		return modotroscontrato;
	}

	public void setTipogarantia(String tipogarantia) {
		this.tipogarantia = tipogarantia;
	}

	public String getTipogarantia() {
		return tipogarantia;
	}

	public void setFecacelebcontrato(Date date) {
		this.fecacelebcontrato = date;
	}

	public Date getFecacelebcontrato() {
		return fecacelebcontrato;
	}

	public void setTipocontrato(String tipocontrato) {
		this.tipocontrato = tipocontrato;
	}

	public String getTipocontrato() {
		return tipocontrato;
	}

	public void setModterminos(String modterminos) {
		this.modterminos = modterminos;
	}

	public String getModterminos() {
		return modterminos;
	}

	public void setIdmoneda(Integer idmoneda) {
		this.idmoneda = idmoneda;
	}

	public Integer getIdmoneda() {
		return idmoneda;
	}

	public void setModmonto(String modmonto) {
		this.modmonto = modmonto;
	}

	public String getModmonto() {
		return modmonto;
	}

	public void setFechatermino(Date fechatermino) {
		this.fechatermino = fechatermino;
	}

	public Date getFechatermino() {
		return fechatermino;
	}

	public void setModinstrumento(String modinstrumento) {
		this.modinstrumento = modinstrumento;
	}

	public String getModinstrumento() {
		return modinstrumento;
	}

	public void setFechafinob(Date fechafinob) {
		this.fechafinob = fechafinob;
	}

	public Date getFechafinob() {
		return fechafinob;
	}

	public void setFechacelebgarantia(Date fechacelebgarantia) {
		this.fechacelebgarantia = fechacelebgarantia;
	}

	public Date getFechacelebgarantia() {
		return fechacelebgarantia;
	}

	public void setCambioBienesMonto(String cambioBienesMonto) {
		this.cambioBienesMonto = cambioBienesMonto;
	}

	public String getCambioBienesMonto() {
		return cambioBienesMonto;
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
}
