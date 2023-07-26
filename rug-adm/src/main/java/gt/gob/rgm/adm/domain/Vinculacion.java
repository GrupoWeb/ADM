package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class Vinculacion implements Serializable {
	Long solicitante;
	String causa;
	Long usuario;
	String operadaPor;
	Long homologadoId;
	String fecha;
	Long idGarantia;
	Long idPersonaMigracion;
	String solicitanteOriginal;
	Long idPersonaRug;
	String solicitanteNuevo;
	
	public Vinculacion() {
	}

	public Long getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Long solicitante) {
		this.solicitante = solicitante;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}

	public Long getHomologadoId() {
		return homologadoId;
	}

	public void setHomologadoId(Long homologadoId) {
		this.homologadoId = homologadoId;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Long getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(Long idGarantia) {
		this.idGarantia = idGarantia;
	}

	public Long getIdPersonaMigracion() {
		return idPersonaMigracion;
	}

	public void setIdPersonaMigracion(Long idPersonaMigracion) {
		this.idPersonaMigracion = idPersonaMigracion;
	}

	public Long getIdPersonaRug() {
		return idPersonaRug;
	}

	public void setIdPersonaRug(Long idPersonaRug) {
		this.idPersonaRug = idPersonaRug;
	}

	public String getOperadaPor() {
		return operadaPor;
	}

	public void setOperadaPor(String operadaPor) {
		this.operadaPor = operadaPor;
	}

	public String getSolicitanteOriginal() {
		return solicitanteOriginal;
	}

	public void setSolicitanteOriginal(String solicitanteOriginal) {
		this.solicitanteOriginal = solicitanteOriginal;
	}

	public String getSolicitanteNuevo() {
		return solicitanteNuevo;
	}

	public void setSolicitanteNuevo(String solicitanteNuevo) {
		this.solicitanteNuevo = solicitanteNuevo;
	}
}
