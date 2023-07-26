package gt.gob.rgm.adm.solr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersonaSolr implements Serializable {
	@Id
	@Column(name="ID_PERSONA")
	Long idPersona;
	
	String nombre;
	
	@Column(name="TIPO_PERSONA")
	String tipoPersona;
	
	@Column(name="RFC")
	String nit;
	
	@Column(name="CURP")
	String docid;
	
	@Column(name="E_MAIL")
	String email;
	
	public PersonaSolr() {
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
