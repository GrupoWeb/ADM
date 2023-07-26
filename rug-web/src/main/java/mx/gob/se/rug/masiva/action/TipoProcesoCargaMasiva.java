package mx.gob.se.rug.masiva.action;

import java.io.Serializable;

public class TipoProcesoCargaMasiva implements Serializable{

	private static final long serialVersionUID = 1L;
	private String mensaje;
	private Integer id;

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
