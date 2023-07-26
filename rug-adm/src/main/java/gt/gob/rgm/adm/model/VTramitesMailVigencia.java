package gt.gob.rgm.adm.model;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="V_TRAMITES_MAIL_VIGENCIA")
@NamedQuery(name="VTramitesMailVigencia.findAll", query="SELECT v FROM VTramitesMailVigencia v")
public class VTramitesMailVigencia implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name="ID_GARANTIA")
	private BigDecimal idGarantia;

	@Column(name="ID_TRAMITE_TEMP")
	private BigDecimal idTramiteTemp;

	@Id
	@Column(name="ID_TRAMITE")
	private BigDecimal idTramite;
	
	@Column(name="ID_TIPO_TRAMITE")
	private BigDecimal idTipoTramite;
	
	@Column(name="FECHA_TERM")
	private String fechaTerm;
	
	@Column(name="USUARIO_MAIL")
	private String usuarioMail;
	
	@Column(name="ACREEDOR")
	private String acreedor;
	
	@Column(name="ACREEDOR_MAIL")
	private String acreedorMail;
	
	@Column(name="ID_ANOTACION")
	private BigDecimal idAnotacion;
	
	public VTramitesMailVigencia() {
		
	}

	public BigDecimal getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(BigDecimal idGarantia) {
		this.idGarantia = idGarantia;
	}

	public BigDecimal getIdTramiteTemp() {
		return idTramiteTemp;
	}

	public void setIdTramiteTemp(BigDecimal idTramiteTemp) {
		this.idTramiteTemp = idTramiteTemp;
	}

	public BigDecimal getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(BigDecimal idTramite) {
		this.idTramite = idTramite;
	}

	public BigDecimal getIdTipoTramite() {
		return idTipoTramite;
	}

	public void setIdTipoTramite(BigDecimal idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public String getFechaTerm() {
		return fechaTerm;
	}

	public void setFechaTerm(String fechaTerm) {
		this.fechaTerm = fechaTerm;
	}

	public String getUsuarioMail() {
		return usuarioMail;
	}

	public void setUsuarioMail(String usuarioMail) {
		this.usuarioMail = usuarioMail;
	}

	public String getAcreedor() {
		return acreedor;
	}

	public void setAcreedor(String acreedor) {
		this.acreedor = acreedor;
	}

	public String getAcreedorMail() {
		return acreedorMail;
	}

	public void setAcreedorMail(String acreedorMail) {
		this.acreedorMail = acreedorMail;
	}

	public BigDecimal getIdAnotacion() {
		return idAnotacion;
	}

	public void setIdAnotacion(BigDecimal idAnotacion) {
		this.idAnotacion = idAnotacion;
	}
	
}
