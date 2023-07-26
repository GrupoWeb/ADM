package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class BoletaStats implements Serializable {
	String fecha;
	String labelFecha;
	Long aprobadas;
	Long pendientesAprobacion;
	Long rechazadas;
	Double variacionAprobadas;
	Double variacionPendientesAprobacion;
	Double variacionRechazadas;

	public BoletaStats() {
		setAprobadas(0L);
        setPendientesAprobacion(0L);
        setRechazadas(0L);
        setVariacionAprobadas(0D);
        setVariacionPendientesAprobacion(0D);
        setVariacionRechazadas(0D);
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

	public Long getAprobadas() {
		return aprobadas;
	}

	public void setAprobadas(Long aprobadas) {
		this.aprobadas = aprobadas;
	}

	public Long getPendientesAprobacion() {
		return pendientesAprobacion;
	}

	public void setPendientesAprobacion(Long pendientesAprobacion) {
		this.pendientesAprobacion = pendientesAprobacion;
	}

	public Long getRechazadas() {
		return rechazadas;
	}

	public void setRechazadas(Long rechazadas) {
		this.rechazadas = rechazadas;
	}

	public Double getVariacionAprobadas() {
		return variacionAprobadas;
	}

	public void setVariacionAprobadas(Double variacionAprobadas) {
		this.variacionAprobadas = variacionAprobadas;
	}

	public Double getVariacionPendientesAprobacion() {
		return variacionPendientesAprobacion;
	}

	public void setVariacionPendientesAprobacion(Double variacionPendientesAprobacion) {
		this.variacionPendientesAprobacion = variacionPendientesAprobacion;
	}

	public Double getVariacionRechazadas() {
		return variacionRechazadas;
	}

	public void setVariacionRechazadas(Double variacionRechazadas) {
		this.variacionRechazadas = variacionRechazadas;
	}

	@Override
	public boolean equals(Object obj) {
		if(this.getFecha() != null) {
			return ((BoletaStats) obj).getFecha().equals(this.getFecha());
		}
		return super.equals(obj);
	}
}
