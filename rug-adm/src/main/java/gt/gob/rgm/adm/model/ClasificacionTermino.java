package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the CLASIFICACION_TERMINO database table.
 * 
 */
@Entity
@Table(name="CLASIFICACION_TERMINO", schema="RUG_ANALYTICS")
@NamedQueries({
	@NamedQuery(name="ClasificacionTermino.findAll", query="SELECT c FROM ClasificacionTermino c ORDER BY c.frecuencia DESC"),
	@NamedQuery(name="ClasificacionTermino.countAll", query="SELECT COUNT(c) FROM ClasificacionTermino c"),
	@NamedQuery(name="ClasificacionTermino.findNotClassified", query="SELECT c FROM ClasificacionTermino c WHERE c.categoriaId IS NULL ORDER BY c.frecuencia DESC"),
	@NamedQuery(name="ClasificacionTermino.countNotClassified", query="SELECT COUNT(c) FROM ClasificacionTermino c WHERE c.categoriaId IS NULL"),
	@NamedQuery(name="ClasificacionTermino.findExclude", query="SELECT c FROM ClasificacionTermino c WHERE c.categoriaInformacion.excluir = 1"),
	@NamedQuery(name="ClasificacionTermino.findNotApplied", query="SELECT c FROM ClasificacionTermino c WHERE c.aplicado = 0 AND c.categoriaInformacion.excluir = 1"),
	@NamedQuery(name="ClasificacionTermino.findByTermino", query="SELECT c FROM ClasificacionTermino c WHERE c.termino = :termino")
})
public class ClasificacionTermino implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_clasificacion_termino")
	@SequenceGenerator(name="seq_clasificacion_termino", sequenceName="seq_clasificacion_termino", allocationSize=1)
	@Column(name="CLASIFICACION_TERMINO_ID")
	private long clasificacionTerminoId;

	private Long frecuencia;
	
	private Double acumulado;
	
	private Integer aplicado;

	private String termino;
	
	@Column(name="CATEGORIA_INFORMACION_ID")
	private Integer categoriaId;

	@ManyToOne
	@JoinColumn(name="CATEGORIA_INFORMACION_ID", insertable=false, updatable=false)
	private CategoriaInformacion categoriaInformacion;

	public ClasificacionTermino() {
	}

	public long getClasificacionTerminoId() {
		return this.clasificacionTerminoId;
	}

	public void setClasificacionTerminoId(long clasificacionTerminoId) {
		this.clasificacionTerminoId = clasificacionTerminoId;
	}

	public Long getFrecuencia() {
		return this.frecuencia;
	}

	public void setFrecuencia(Long frecuencia) {
		this.frecuencia = frecuencia;
	}

	public Double getAcumulado() {
		return acumulado;
	}

	public void setAcumulado(Double acumulado) {
		this.acumulado = acumulado;
	}

	public Integer getAplicado() {
		return aplicado;
	}

	public void setAplicado(Integer aplicado) {
		this.aplicado = aplicado;
	}

	public String getTermino() {
		return this.termino;
	}

	public void setTermino(String termino) {
		this.termino = termino;
	}

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

	public CategoriaInformacion getCategoriaInformacion() {
		return this.categoriaInformacion;
	}

	public void setCategoriaInformacion(CategoriaInformacion categoriaInformacion) {
		this.categoriaInformacion = categoriaInformacion;
	}

}