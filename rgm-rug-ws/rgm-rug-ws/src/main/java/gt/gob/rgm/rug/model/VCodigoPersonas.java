package gt.gob.rgm.rug.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="V_CODIGO_PERSONAS")
@NamedQuery(name="VCodigoPersonas.findAll", query="SELECT r FROM VCodigoPersonas r")
public class VCodigoPersonas implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_PERSONA")
	private long idPersona;

	@Column(name="CODIGO")
	private String codigo;

	@Column(name="CURP_DOC")
	private String curpDoc;

	@Column(name="PER_JURIDICA")
	private String perJuridica;

	private String rfc;

	public VCodigoPersonas(){
		
	}

	public long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCurpDoc() {
		return curpDoc;
	}

	public void setCurpDoc(String curpDoc) {
		this.curpDoc = curpDoc;
	}

	public String getPerJuridica() {
		return perJuridica;
	}

	public void setPerJuridica(String perJuridica) {
		this.perJuridica = perJuridica;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	
	
}
