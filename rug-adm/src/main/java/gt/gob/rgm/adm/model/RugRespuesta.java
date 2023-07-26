package gt.gob.rgm.adm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="RUG_RESPUESTA", schema="RUG")
@NamedQuery(name="RugRespuesta.findAll", query="SELECT b FROM RugRespuesta b")
public class RugRespuesta implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_RESPUESTA")
	private Long idRespuesta;
	
	@Column(name="ID_PREGUNTA")
	private Long idPregunta;
	
	private String respuesta;
	
	private Date fecha;

	public Long getIdRespuesta() {
		return idRespuesta;
	}

	public void setIdRespuesta(Long idRespuesta) {
		this.idRespuesta = idRespuesta;
	}

	public Long getIdPregunta() {
		return idPregunta;
	}

	public void setIdPregunta(Long idPregunta) {
		this.idPregunta = idPregunta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}
