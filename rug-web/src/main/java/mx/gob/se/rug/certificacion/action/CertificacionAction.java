package mx.gob.se.rug.certificacion.action;

import mx.gob.se.rug.certificacion.to.CertificacionTO;
import mx.gob.se.rug.fwk.action.RugBaseAction;

public class CertificacionAction extends RugBaseAction {
	
	private static final long serialVersionUID = 1L;
	private Integer idTramite;
	private Integer idGarantia;
	private CertificacionTO certificacionTO;
	

	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}

	public Integer getIdTramite() {
		return idTramite;
	}

	public void setCertificacionTO(CertificacionTO certificacionTO) {
		this.certificacionTO = certificacionTO;
	}

	public CertificacionTO getCertificacionTO() {
		return certificacionTO;
	}
}
