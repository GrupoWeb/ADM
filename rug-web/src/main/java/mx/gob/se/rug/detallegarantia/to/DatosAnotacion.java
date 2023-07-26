package mx.gob.se.rug.detallegarantia.to;

public class DatosAnotacion {
	
	private String personaAutoridadInstruye;
	private String contenidoResolucion;
	private String vigenciaAnotacion;
	private Integer tipoAsiento;
	
	public Integer getTipoAsiento() {
		return tipoAsiento;
	}
	public void setTipoAsiento(Integer tipoAsiento) {
		this.tipoAsiento = tipoAsiento;
	}
	public String getPersonaAutoridadInstruye() {
		return personaAutoridadInstruye;
	}
	public void setPersonaAutoridadInstruye(String personaAutoridadInstruye) {
		this.personaAutoridadInstruye = personaAutoridadInstruye;
	}
	public String getContenidoResolucion() {
		return contenidoResolucion;
	}
	public void setContenidoResolucion(String contenidoResolucion) {
		this.contenidoResolucion = contenidoResolucion;
	}
	public String getVigenciaAnotacion() {
		return vigenciaAnotacion;
	}
	public void setVigenciaAnotacion(String vigenciaAnotacion) {
		this.vigenciaAnotacion = vigenciaAnotacion;
	}

}
