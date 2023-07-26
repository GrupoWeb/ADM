package mx.gob.se.rug.acreedores.to;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class GrupoPerfilTO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String descPrivilegio;
	private Integer idPersonaPadre;
	private String claveUsuarioPadre;
	private String descripcion;
	private String idPersonaCrea;
	private Date fechaCreacion;
	private String situacion;
	private List<PerfilTO> perfiles;
	
	public GrupoPerfilTO() {
		super();
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public Integer getIdPersonaPadre() {
		return idPersonaPadre;
	}
	public void setIdPersonaPadre(Integer idPersonaPadre) {
		this.idPersonaPadre = idPersonaPadre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getIdPersonaCrea() {
		return idPersonaCrea;
	}
	public void setIdPersonaCrea(String idPersonaCrea) {
		this.idPersonaCrea = idPersonaCrea;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getSituacion() {
		return situacion;
	}
	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}
	public void setPerfiles(List<PerfilTO> perfiles) {
		this.perfiles = perfiles;
	}

	public List<PerfilTO> getPerfiles() {
		return perfiles;
	}
	public void setClaveUsuarioPadre(String claveUsuarioPadre) {
		this.claveUsuarioPadre = claveUsuarioPadre;
	}
	public String getClaveUsuarioPadre() {
		return claveUsuarioPadre;
	}
	public void setDescPrivilegio(String descPrivilegio) {
		this.descPrivilegio = descPrivilegio;
	}
	public String getDescPrivilegio() {
		return descPrivilegio;
	}

}
