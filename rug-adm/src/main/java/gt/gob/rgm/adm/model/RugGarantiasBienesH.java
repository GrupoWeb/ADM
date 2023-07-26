package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the RUG_GARANTIAS_BIENES_H database table.
 * 
 */
@Entity
@Table(name="RUG_GARANTIAS_BIENES_H")
@NamedQuery(name="RugGarantiasBienesH.findAll", query="SELECT r FROM RugGarantiasBienesH r")
public class RugGarantiasBienesH implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_GARAN_BIEN")
	private long idGaranBien;

	@Column(name="DESCRIPCION_BIEN")
	private String descripcionBien;

	@Column(name="ID_TRAMITE")
	private BigDecimal idTramite;

	private String identificador;

	@Column(name="TIPO_BIEN_ESPECIAL")
	private BigDecimal tipoBienEspecial;

	@Column(name="TIPO_IDENTIFICADOR")
	private BigDecimal tipoIdentificador;

	@Column(name="SERIE")
	private String serie;

	public RugGarantiasBienesH() {
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

	public BigDecimal getIdTramite() {
		return this.idTramite;
	}

	public void setIdTramite(BigDecimal idTramite) {
		this.idTramite = idTramite;
	}

	public String getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public BigDecimal getTipoBienEspecial() {
		return this.tipoBienEspecial;
	}

	public void setTipoBienEspecial(BigDecimal tipoBienEspecial) {
		this.tipoBienEspecial = tipoBienEspecial;
	}

	public BigDecimal getTipoIdentificador() {
		return this.tipoIdentificador;
	}

	public void setTipoIdentificador(BigDecimal tipoIdentificador) {
		this.tipoIdentificador = tipoIdentificador;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}
}