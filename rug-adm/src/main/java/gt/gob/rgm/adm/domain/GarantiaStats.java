package gt.gob.rgm.adm.domain;

import java.io.Serializable;

public class GarantiaStats implements Serializable {
	String fecha;
	String labelFecha;
	Long inscripciones;
	Long cancelaciones;
	Long certificaciones;
	Long modificaciones;
	Long renovaciones;
	Long consultas;
	Long ejecuciones;
	Long traslados;
	Long vinculaciones;
	Double variacionInscripciones;
	Double variacionCancelaciones;
	Double variacionCertificaciones;
	Double variacionModificaciones;
	Double variacionRenovaciones;
	Double variacionConsultas;
	Double variacionEjecuciones;
	Double variacionTraslados;
	Double variacionVinculaciones;

	/*
	1 - Inscripcion
	4 - Cancelacion
	5 - Certificacion
	7 - Modificacion
	9 - Renovacion vigencia
	11 - Consulta
	30 - Ejecucion
	31 - Traslado inscripcion
	77 - Vinculaciones*/
	
	public GarantiaStats() {
		setInscripciones(0L);
        setCancelaciones(0L);
        setCertificaciones(0L);
        setModificaciones(0L);
        setRenovaciones(0L);
        setConsultas(0L);
        setEjecuciones(0L);
        setTraslados(0L);
        setVinculaciones(0L);
        setVariacionInscripciones(0D);
        setVariacionCancelaciones(0D);
        setVariacionCertificaciones(0D);
        setVariacionModificaciones(0D);
        setVariacionRenovaciones(0D);
        setVariacionConsultas(0D);
        setVariacionEjecuciones(0D);
        setVariacionTraslados(0D);
        setVariacionVinculaciones(0D);
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

	public Long getInscripciones() {
		return inscripciones;
	}

	public void setInscripciones(Long inscripciones) {
		this.inscripciones = inscripciones;
	}

	public Long getCancelaciones() {
		return cancelaciones;
	}

	public void setCancelaciones(Long cancelaciones) {
		this.cancelaciones = cancelaciones;
	}

	public Long getCertificaciones() {
		return certificaciones;
	}

	public void setCertificaciones(Long certificaciones) {
		this.certificaciones = certificaciones;
	}

	public Long getModificaciones() {
		return modificaciones;
	}

	public void setModificaciones(Long modificaciones) {
		this.modificaciones = modificaciones;
	}

	public Long getRenovaciones() {
		return renovaciones;
	}

	public void setRenovaciones(Long renovaciones) {
		this.renovaciones = renovaciones;
	}

	public Long getConsultas() {
		return consultas;
	}

	public void setConsultas(Long consultas) {
		this.consultas = consultas;
	}

	public Long getEjecuciones() {
		return ejecuciones;
	}

	public void setEjecuciones(Long ejecuciones) {
		this.ejecuciones = ejecuciones;
	}

	public Long getTraslados() {
		return traslados;
	}

	public void setTraslados(Long traslados) {
		this.traslados = traslados;
	}

	public Long getVinculaciones() {
		return vinculaciones;
	}

	public void setVinculaciones(Long vinculaciones) {
		this.vinculaciones = vinculaciones;
	}

	public Double getVariacionInscripciones() {
		return variacionInscripciones;
	}

	public void setVariacionInscripciones(Double variacionInscripciones) {
		this.variacionInscripciones = variacionInscripciones;
	}

	public Double getVariacionCancelaciones() {
		return variacionCancelaciones;
	}

	public void setVariacionCancelaciones(Double variacionCancelaciones) {
		this.variacionCancelaciones = variacionCancelaciones;
	}

	public Double getVariacionCertificaciones() {
		return variacionCertificaciones;
	}

	public void setVariacionCertificaciones(Double variacionCertificaciones) {
		this.variacionCertificaciones = variacionCertificaciones;
	}

	public Double getVariacionModificaciones() {
		return variacionModificaciones;
	}

	public void setVariacionModificaciones(Double variacionModificaciones) {
		this.variacionModificaciones = variacionModificaciones;
	}

	public Double getVariacionRenovaciones() {
		return variacionRenovaciones;
	}

	public void setVariacionRenovaciones(Double variacionRenovaciones) {
		this.variacionRenovaciones = variacionRenovaciones;
	}

	public Double getVariacionConsultas() {
		return variacionConsultas;
	}

	public void setVariacionConsultas(Double variacionConsultas) {
		this.variacionConsultas = variacionConsultas;
	}

	public Double getVariacionEjecuciones() {
		return variacionEjecuciones;
	}

	public void setVariacionEjecuciones(Double variacionEjecuciones) {
		this.variacionEjecuciones = variacionEjecuciones;
	}

	public Double getVariacionTraslados() {
		return variacionTraslados;
	}

	public void setVariacionTraslados(Double variacionTraslados) {
		this.variacionTraslados = variacionTraslados;
	}

	public Double getVariacionVinculaciones() {
		return variacionVinculaciones;
	}

	public void setVariacionVinculaciones(Double variacionVinculaciones) {
		this.variacionVinculaciones = variacionVinculaciones;
	}

	@Override
	public boolean equals(Object obj) {
		if(this.getFecha() != null) {
			return ((GarantiaStats) obj).getFecha().equals(this.getFecha());
		}
		return super.equals(obj);
	}
}
