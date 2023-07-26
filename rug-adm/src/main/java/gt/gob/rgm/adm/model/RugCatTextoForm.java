package gt.gob.rgm.adm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="RUG_CAT_TEXTO_FORM")
@NamedQuery(name="RugCatTextoForm.findAll", query="SELECT i FROM RugCatTextoForm i")
public class RugCatTextoForm implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private RugCatTextoFormPK rugCatTextoFormId;
	
	@Column(name="DESC_PARTE")
	private String descripcion;
	
	@Column(name="TEXTO")
	private String texto;
	
	public RugCatTextoForm() {		
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public RugCatTextoFormPK getRugCatTextoFormId() {
		return rugCatTextoFormId;
	}

	public void setRugCatTextoFormId(RugCatTextoFormPK rugCatTextoFormId) {
		this.rugCatTextoFormId = rugCatTextoFormId;
	}
	
}
