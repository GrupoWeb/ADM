package gt.gob.rgm.adm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the RUG_CAT_TIPO_TRAMITE database table.
 * 
 */
@Entity
@Table(name="RUG_CAT_TIPO_TRAMITE")
@NamedQuery(name="RugCatTipoTramite.findAll", query="SELECT r FROM RugCatTipoTramite r")
public class RugCatTipoTramite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_TIPO_TRAMITE")
	private Integer idTipoTramite;

	@Column(name="B_CARGA_MASIVA")
	private BigDecimal bCargaMasiva;

	private String descripcion;

	private double precio;

	@Column(name="STATUS_REG")
	private String statusReg;

	@Column(name="VIGENCIA_TRAM")
	private BigDecimal vigenciaTram;

	//bi-directional many-to-one association to Tramite
	@OneToMany(mappedBy="tipoTramite")
	private List<Tramites> tramites;

	public RugCatTipoTramite() {
	}

	public Integer getIdTipoTramite() {
		return this.idTipoTramite;
	}

	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public BigDecimal getBCargaMasiva() {
		return this.bCargaMasiva;
	}

	public void setBCargaMasiva(BigDecimal bCargaMasiva) {
		this.bCargaMasiva = bCargaMasiva;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return this.precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getStatusReg() {
		return this.statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

	public BigDecimal getVigenciaTram() {
		return this.vigenciaTram;
	}

	public void setVigenciaTram(BigDecimal vigenciaTram) {
		this.vigenciaTram = vigenciaTram;
	}

	public List<Tramites> getTramites() {
		return this.tramites;
	}

	public void setTramites(List<Tramites> tramites) {
		this.tramites = tramites;
	}

	public Tramites addTramite(Tramites tramite) {
		getTramites().add(tramite);
		tramite.setTipoTramite(this);

		return tramite;
	}

	public Tramites removeTramite(Tramites tramite) {
		getTramites().remove(tramite);
		tramite.setTipoTramite(null);

		return tramite;
	}

}