package mx.gob.se.rug.inscripcion.to;

import java.io.Serializable;

public class TipoBienInscripcionTXT implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String contenido;

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getContenido() {
		return contenido;
	}
}
