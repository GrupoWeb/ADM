package gt.gob.rgm.adm.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="V_DETALLE_BOLETA_PARTES")
@NamedQuery(name="VDetalleBoletaPartes.findAll", query="SELECT v FROM VDetalleBoletaPartes v")
public class VDetalleBoletaPartes implements Serializable{

	private static final long serialVersionUID = 1L;

	private String curp;

	@Column(name="E_MAIL")
	private String eMail;

	private String extension;

	@Column(name="FOLIO_ELECTRONICO")
	private String folioElectronico;

	@Column(name="ID_PARTE")
	private BigDecimal idParte;

	@Id
	@Column(name="ID_PERSONA")
	private BigDecimal idPersona;

	@Column(name="ID_TRAMITE")
	private BigDecimal idTramite;

	@Column(name="NOMBRE_PARTE")
	private String nombre;

	@Column(name="PER_JURIDICA")
	private String perJuridica;

	private String rfc;

	private String telefono;

	private String domicilio;
	
	public VDetalleBoletaPartes() {
		
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFolioElectronico() {
		return folioElectronico;
	}

	public void setFolioElectronico(String folioElectronico) {
		this.folioElectronico = folioElectronico;
	}

	public BigDecimal getIdParte() {
		return idParte;
	}

	public void setIdParte(BigDecimal idParte) {
		this.idParte = idParte;
	}

	public BigDecimal getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(BigDecimal idPersona) {
		this.idPersona = idPersona;
	}

	public BigDecimal getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(BigDecimal idTramite) {
		this.idTramite = idTramite;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPerJuridica() {
		return perJuridica;
	}

	public void setPerJuridica(String perJuridica) {
		this.perJuridica = perJuridica;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	
}
