package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the RUG_GARANTIAS_BIENES database table.
 * 
 */
@Entity
@Table(name="RUG_GARANTIAS_BIENES")
@NamedQueries({
	@NamedQuery(name="RugGarantiasBienes.findAll", query="SELECT r FROM RugGarantiasBienes r"),
	@NamedQuery(name="RugGarantiasBienes.findByIdentificador", query="SELECT r FROM RugGarantiasBienes r WHERE UPPER(r.identificador) LIKE :identificador")
})
public class RugGarantiasBienes implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RUG_GARANTIAS_BIENES_IDGARANBIEN_GENERATOR", sequenceName="SEQ_GARANTIAS_TEMP")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUG_GARANTIAS_BIENES_IDGARANBIEN_GENERATOR")
	@Column(name="ID_GARAN_BIEN")
	private long idGaranBien;

	@Column(name="DESCRIPCION_BIEN")
	private String descripcionBien;
	
	@JoinColumn(name="ID_TRAMITE", referencedColumnName="ID_TRAMITE", insertable=false, updatable=false)
	private Tramites tramite;

	@Column(name="ID_TRAMITE")
	private Long idTramite;

	private String identificador;

	@Column(name="TIPO_BIEN_ESPECIAL")
	private Integer tipoBienEspecial;

	@Column(name="TIPO_IDENTIFICADOR")
	private Integer tipoIdentificador;

	public RugGarantiasBienes() {
	}

	public long getIdGaranBien() {
		return this.idGaranBien;
	}

	public void setIdGaranBien(long idGaranBien) {
		this.idGaranBien = idGaranBien;
	}

	public String getDescripcionBien() {
		return this.descripcionBien;
	}

	public void setDescripcionBien(String descripcionBien) {
		this.descripcionBien = descripcionBien;
	}

	public Tramites getTramite() {
		return tramite;
	}

	public void setTramite(Tramites tramite) {
		this.tramite = tramite;
	}

	public Long getIdTramite() {
		return this.idTramite;
	}

	public void setIdTramite(Long idTramite) {
		this.idTramite = idTramite;
	}

	public String getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Integer getTipoBienEspecial() {
		return this.tipoBienEspecial;
	}

	public void setTipoBienEspecial(Integer tipoBienEspecial) {
		this.tipoBienEspecial = tipoBienEspecial;
	}

	public Integer getTipoIdentificador() {
		return this.tipoIdentificador;
	}

	public void setTipoIdentificador(Integer tipoIdentificador) {
		this.tipoIdentificador = tipoIdentificador;
	}

}