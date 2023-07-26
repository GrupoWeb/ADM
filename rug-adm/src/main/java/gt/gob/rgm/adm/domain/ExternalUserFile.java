package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class ExternalUserFile implements Serializable {
	private Long archivoId;
	private String tipo;
	private String url;
	
	public ExternalUserFile() {
	}

	public Long getArchivoId() {
		return archivoId;
	}

	public void setArchivoId(Long archivoId) {
		this.archivoId = archivoId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
