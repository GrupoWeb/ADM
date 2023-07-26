package gt.gob.rgm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="RUG_CAT_TIPO_TRAMITE")
public class RugCatTipoTramite implements Serializable {
	@Id
	@Column(name="ID_TIPO_TRAMITE")
	Integer idTipoTramite;
	String descripcion;
	Double precio;
	@Column(name="VIGENCIA_TRAM")
	Integer vigenciaTram;
	@Column(name="STATUS_REG")
	String statusReg;
	@Column(name="B_CARGA_MASIVA")
	Integer bCargaMasiva;
	
	public RugCatTipoTramite() {
	}

	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}

	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getVigenciaTram() {
		return vigenciaTram;
	}

	public void setVigenciaTram(Integer vigenciaTram) {
		this.vigenciaTram = vigenciaTram;
	}

	public String getStatusReg() {
		return statusReg;
	}

	public void setStatusReg(String statusReg) {
		this.statusReg = statusReg;
	}

	public Integer getbCargaMasiva() {
		return bCargaMasiva;
	}

	public void setbCargaMasiva(Integer bCargaMasiva) {
		this.bCargaMasiva = bCargaMasiva;
	}
}
