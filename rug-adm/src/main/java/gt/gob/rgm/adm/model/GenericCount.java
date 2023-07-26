package gt.gob.rgm.adm.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GenericCount {
	@Id
	private Integer id;
	private String descripcion;
	private String subdescripcion;
	private Long total;
	private Double variacion;
	private Date fecha;
	@Column(name="str_fecha")
	private String strFecha;
	private String labelFecha;
	
	public GenericCount() {
	}
	
	public GenericCount(String descripcion, Long total) {
		super();
		this.descripcion = descripcion;
		this.total = total;
	}
	
	public GenericCount(String descripcion, String subdescripcion, Long total) {
		super();
		this.descripcion = descripcion;
		this.subdescripcion = subdescripcion;
		this.total = total;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public String getSubdescripcion() {
		return subdescripcion;
	}

	public void setSubdescripcion(String subdescripcion) {
		this.subdescripcion = subdescripcion;
	}

	public Double getVariacion() {
		return variacion;
	}

	public void setVariacion(Double variacion) {
		this.variacion = variacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getStrFecha() {
		return strFecha;
	}

	public void setStrFecha(String strFecha) {
		this.strFecha = strFecha;
		/*DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("YYYY-w");
		setFecha(java.sql.Date.valueOf(LocalDate.parse(strFecha, weekFormatter)));*/
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabelFecha() {
		return labelFecha;
	}

	public void setLabelFecha(String labelFecha) {
		this.labelFecha = labelFecha;
	}

	@Override
	public boolean equals(Object obj) {
		/*if(this.getFecha() != null) {
			return ((GenericCount) obj).getFecha().compareTo(this.getFecha()) == 0;
		}*/
		if(this.getStrFecha() != null) {
			return ((GenericCount) obj).getStrFecha().equals(this.getStrFecha());
		}
		return super.equals(obj);
	}
}
