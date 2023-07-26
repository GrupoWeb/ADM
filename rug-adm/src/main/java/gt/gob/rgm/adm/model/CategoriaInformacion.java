package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the CATEGORIA_INFORMACION database table.
 * 
 */
@Entity
@Table(name="CATEGORIA_INFORMACION", schema="RUG_ANALYTICS")
@NamedQuery(name="CategoriaInformacion.findAll", query="SELECT c FROM CategoriaInformacion c")
public class CategoriaInformacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_categoria_informacion")
	@SequenceGenerator(name="seq_categoria_informacion", sequenceName="seq_categoria_informacion", allocationSize=1)
	@Column(name="CATEGORIA_INFORMACION_ID")
	private Integer categoriaInformacionId;

	private String nombre;

	private Integer excluir;

	public CategoriaInformacion() {
	}

	public Integer getCategoriaInformacionId() {
		return this.categoriaInformacionId;
	}

	public void setCategoriaInformacionId(Integer categoriaInformacionId) {
		this.categoriaInformacionId = categoriaInformacionId;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getExcluir() {
		return excluir;
	}

	public void setExcluir(Integer excluir) {
		this.excluir = excluir;
	}
}