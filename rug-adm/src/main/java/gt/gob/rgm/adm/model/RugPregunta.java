package gt.gob.rgm.adm.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="RUG_PREGUNTA", schema="RUG")
@NamedQuery(name="RugPregunta.findAll", query="SELECT a FROM RugPregunta a")
public class RugPregunta implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="RUG_PREGUNTA_PREGUNTAID_GENERATOR", sequenceName="SEQ_PREGUNTA", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUG_PREGUNTA_PREGUNTAID_GENERATOR")
	@Column(name="ID_PREGUNTA")
	private Long preguntaId;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date fecha;
	
	@Column(name="DESC_PREGUNTA")
	private String descPregunta;
	
	@Column(name="TIPO_PREGUNTA")
	private Long tipoPregunta;

	public Long getPreguntaId() {
		return preguntaId;
	}

	public void setPreguntaId(Long preguntaId) {
		this.preguntaId = preguntaId;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescPregunta() {
		return descPregunta;
	}

	public void setDescPregunta(String descPregunta) {
		this.descPregunta = descPregunta;
	}

	public Long getTipoPregunta() {
		return tipoPregunta;
	}

	public void setTipoPregunta(Long tipoPregunta) {
		this.tipoPregunta = tipoPregunta;
	}
	
}
