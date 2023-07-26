package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RUG_BOLETA_PDF database table.
 * 
 */
@Entity
@Table(name="RUG_BOLETA_PDF")
@NamedQuery(name="RugBoletaPdf.findAll", query="SELECT r FROM RugBoletaPdf r")
public class RugBoletaPdf implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RUG_BOLETA_PDF_ARCHIVOID_GENERATOR", sequenceName="SEQ_ARCHIVOPDF", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUG_BOLETA_PDF_ARCHIVOID_GENERATOR")
	@Column(name="ID_ARCHIVO")
	private long idArchivo;

	@Lob
	private byte[] archivo;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REG")
	private Date fechaReg;

	@Column(name="ID_PERSONA")
	private BigDecimal idPersona;

	@Column(name="ID_TRAMITE")
	private BigDecimal idTramite;

	@Column(name="PDF_KEY")
	private String pdfKey;

	@Column(name="STATUS_REG")
	private String statusReg;

	public RugBoletaPdf() {
	}

	public long getIdArchivo() {
		return this.idArchivo;
	}

	public void setIdArchivo(long idArchivo) {
		this.idArchivo = idArchivo;
	}

	public byte[] getArchivo() {
		return this.archivo;
	}

	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}

	public Date getFechaReg() {
		return this.fechaReg;
	}

	public void setFechaReg(Date fechaReg) {
		this.fechaReg = fechaReg;
	}

	public BigDecimal getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(BigDecimal idPersona) {
		this.idPersona = idPersona;
	}

	public BigDecimal getIdTramite() {
		return this.idTramite;
	}

	public void setIdTramite(BigDecimal idTramite) {
		this.idTramite = idTramite;
	}

	public String getPdfKey() {
		return this.pdfKey;
	}

	public void setPdfKey(String pdfKey) {
		this.pdfKey = pdfKey;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

}