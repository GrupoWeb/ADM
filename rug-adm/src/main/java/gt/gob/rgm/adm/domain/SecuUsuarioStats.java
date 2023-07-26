package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class SecuUsuarioStats implements Serializable {
	String fecha;
	String labelFecha;
	Long activos;
	Long pendientesActivacion;
	Long pendientesAprobacion;
	Long rechazados;
	Double variacionActivos;
	Double variacionPendientesActivacion;
	Double variacionPendientesAprobacion;
	Double variacionRechazados;

	public SecuUsuarioStats() {
		setActivos(0L);
        setPendientesActivacion(0L);
        setPendientesAprobacion(0L);
        setRechazados(0L);
        setVariacionActivos(0D);
        setVariacionPendientesActivacion(0D);
        setVariacionPendientesAprobacion(0D);
        setVariacionRechazados(0D);
	}
	
	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getLabelFecha() {
		return labelFecha;
	}

	public void setLabelFecha(String labelFecha) {
		this.labelFecha = labelFecha;
	}

	public Long getActivos() {
		return activos;
	}

	public void setActivos(Long activos) {
		this.activos = activos;
	}

	public Long getPendientesActivacion() {
		return pendientesActivacion;
	}

	public void setPendientesActivacion(Long pendientesActivacion) {
		this.pendientesActivacion = pendientesActivacion;
	}

	public Long getPendientesAprobacion() {
		return pendientesAprobacion;
	}

	public void setPendientesAprobacion(Long pendientesAprobacion) {
		this.pendientesAprobacion = pendientesAprobacion;
	}

	public Long getRechazados() {
		return rechazados;
	}

	public void setRechazados(Long rechazados) {
		this.rechazados = rechazados;
	}

	public Double getVariacionActivos() {
		return variacionActivos;
	}

	public void setVariacionActivos(Double variacionActivos) {
		this.variacionActivos = variacionActivos;
	}

	public Double getVariacionPendientesActivacion() {
		return variacionPendientesActivacion;
	}

	public void setVariacionPendientesActivacion(Double variacionPendientesActivacion) {
		this.variacionPendientesActivacion = variacionPendientesActivacion;
	}

	public Double getVariacionPendientesAprobacion() {
		return variacionPendientesAprobacion;
	}

	public void setVariacionPendientesAprobacion(Double variacionPendientesAprobacion) {
		this.variacionPendientesAprobacion = variacionPendientesAprobacion;
	}

	public Double getVariacionRechazados() {
		return variacionRechazados;
	}

	public void setVariacionRechazados(Double variacionRechazados) {
		this.variacionRechazados = variacionRechazados;
	}

	@Override
	public boolean equals(Object obj) {
		if(this.getFecha() != null) {
			return ((SecuUsuarioStats) obj).getFecha().equals(this.getFecha());
		}
		return super.equals(obj);
	}
}
