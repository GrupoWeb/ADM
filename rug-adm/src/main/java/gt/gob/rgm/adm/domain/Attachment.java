package gt.gob.rgm.adm.domain;

import java.io.InputStream;
import java.io.Serializable;

import javax.mail.internet.MimeMultipart;

public class Attachment implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long adjuntoId;
	private Long incidenteId;
	private String tipo;
	private String descripcion;
	private InputStream adjunto;
	private String url;
	
	public Attachment() {
		super();
	}
	public Long getAdjuntoId() {
		return adjuntoId;
	}
	public void setAdjuntoId(Long adjuntoId) {
		this.adjuntoId = adjuntoId;
	}
	public Long getIncidenteId() {
		return incidenteId;
	}
	public void setIncidenteId(Long incidenteId) {
		this.incidenteId = incidenteId;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public InputStream getAdjunto() {
		return adjunto;
	}
	public void setAdjunto(InputStream adjunto) {
		this.adjunto = adjunto;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
