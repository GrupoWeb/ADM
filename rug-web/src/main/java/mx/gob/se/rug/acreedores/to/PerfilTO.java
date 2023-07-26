package mx.gob.se.rug.acreedores.to;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PerfilTO implements Serializable {
	
	private String id;
	private String descripcion;
	private String clave;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getClave() {
		return clave;
	}

	
}
