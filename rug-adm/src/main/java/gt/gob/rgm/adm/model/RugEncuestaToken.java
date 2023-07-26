package gt.gob.rgm.adm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="RUG_ENCUESTA_TOKEN")
@NamedQuery(name="RugEncuestaToken.findAll", query="SELECT b FROM RugEncuestaToken b")
public class RugEncuestaToken implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	String token;
	Integer estado;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	
	
}
