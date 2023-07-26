package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_FIRMA_DOCTOS database table.
 * 
 */
@Entity
@Table(name="RUG_FIRMA_DOCTOS")
@NamedQuery(name="RugFirmaDoctos.findAll", query="SELECT r FROM RugFirmaDoctos r")
public class RugFirmaDoctos implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RUG_FIRMA_DOCTOS_IDFIRMA_GENERATOR", sequenceName="SEQ_RUG_CTRL_FIRMA", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUG_FIRMA_DOCTOS_IDFIRMA_GENERATOR")
	@Column(name="ID_FIRMA")
	private long idFirma;

	@Lob
	@Column(name="CERTIFICADO_CENTRAL_B64")
	private String certificadoCentralB64;

	@Lob
	@Column(name="CERTIFICADO_USUARIO_B64")
	private String certificadoUsuarioB64;

	@Lob
	@Column(name="CO_FIRMA_RUG")
	private String coFirmaRug;

	@Lob
	@Column(name="CO_SELLO")
	private String coSello;

	@Lob
	@Column(name="CO_USUAIRO")
	private String coUsuairo;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Lob
	@Column(name="FIRMA_RUG_B64")
	private String firmaRugB64;

	@Lob
	@Column(name="FIRMA_USUARIO_B64")
	private String firmaUsuarioB64;

	@Column(name="ID_TRAMITE_TEMP")
	private Long idTramiteTemp;

	@Column(name="ID_USUARIO_FIRMO")
	private Long idUsuarioFirmo;

	private String procesado;

	@Lob
	@Column(name="SELLO_TS_B64")
	private String selloTsB64;

	@Column(name="STATUS_REG")
	private String statusReg;

	@Lob
	@Column(name="XML_CO")
	private String xmlCo;

	public RugFirmaDoctos() {
	}

	public long getIdFirma() {
		return this.idFirma;
	}

	public void setIdFirma(long idFirma) {
		this.idFirma = idFirma;
	}

	public String getCertificadoCentralB64() {
		return this.certificadoCentralB64;
	}

	public void setCertificadoCentralB64(String certificadoCentralB64) {
		this.certificadoCentralB64 = certificadoCentralB64;
	}

	public String getCertificadoUsuarioB64() {
		return this.certificadoUsuarioB64;
	}

	public void setCertificadoUsuarioB64(String certificadoUsuarioB64) {
		this.certificadoUsuarioB64 = certificadoUsuarioB64;
	}

	public String getCoFirmaRug() {
		return this.coFirmaRug;
	}

	public void setCoFirmaRug(String coFirmaRug) {
		this.coFirmaRug = coFirmaRug;
	}

	public String getCoSello() {
		return this.coSello;
	}

	public void setCoSello(String coSello) {
		this.coSello = coSello;
	}

	public String getCoUsuairo() {
		return this.coUsuairo;
	}

	public void setCoUsuairo(String coUsuairo) {
		this.coUsuairo = coUsuairo;
	}

	public Date getFechaReg() {
		return this.fechaReg;
	}

	public void setFechaReg(Date fechaReg) {
		this.fechaReg = fechaReg;
	}

	public String getFirmaRugB64() {
		return this.firmaRugB64;
	}

	public void setFirmaRugB64(String firmaRugB64) {
		this.firmaRugB64 = firmaRugB64;
	}

	public String getFirmaUsuarioB64() {
		return this.firmaUsuarioB64;
	}

	public void setFirmaUsuarioB64(String firmaUsuarioB64) {
		this.firmaUsuarioB64 = firmaUsuarioB64;
	}

	public Long getIdTramiteTemp() {
		return this.idTramiteTemp;
	}

	public void setIdTramiteTemp(Long idTramiteTemp) {
		this.idTramiteTemp = idTramiteTemp;
	}

	public Long getIdUsuarioFirmo() {
		return this.idUsuarioFirmo;
	}

	public void setIdUsuarioFirmo(Long idUsuarioFirmo) {
		this.idUsuarioFirmo = idUsuarioFirmo;
	}

	public String getProcesado() {
		return this.procesado;
	}

	public void setProcesado(String procesado) {
		this.procesado = procesado;
	}

	public String getSelloTsB64() {
		return this.selloTsB64;
	}

	public void setSelloTsB64(String selloTsB64) {
		this.selloTsB64 = selloTsB64;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

	public String getXmlCo() {
		return this.xmlCo;
	}

	public void setXmlCo(String xmlCo) {
		this.xmlCo = xmlCo;
	}

}