package mx.gob.se.rug.boleta.to;

import java.util.Date;

public class BoletaCertificacionTO {
	private Integer idTramiteCert;
	private Integer idTramite;
	private Integer idGarantia;
	private Integer idTipoTramite;
	private Date fechaCert;
	
	public Integer getIdTramiteCert() {
		return idTramiteCert;
	}
	public void setIdTramiteCert(Integer idTramiteCert) {
		this.idTramiteCert = idTramiteCert;
	}
	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public Integer getIdGarantia() {
		return idGarantia;
	}
	public void setIdGarantia(Integer idGarantia) {
		this.idGarantia = idGarantia;
	}
	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}
	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	public Date getFechaCert() {
		return fechaCert;
	}
	public void setFechaCert(Date fechaCert) {
		this.fechaCert = fechaCert;
	}
	
	

}
