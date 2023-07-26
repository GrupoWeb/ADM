package mx.gob.se.rug.job.to;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class jobAvisoTO {
	
	private Integer idJob;
	private String jobName;
	private String estado;
	private String periodicidad;
	private Date ultEjecucion;
	private Date proxEjecucion;
	private String descripcion;
	private List <String> listaEstado;
	
	public List<String> getListaEstado() {
		listaEstado = new ArrayList<String>();
		listaEstado.add("ACTIVO");
		listaEstado.add("INACTIVO");
		 return listaEstado;
	}
	
	public Integer getIdJob() {
		return idJob;
	}
	public void setIdJob(Integer idJob) {
		this.idJob = idJob;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getPeriodicidad() {
		return periodicidad;
	}
	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}
	public Date getUltEjecucion() {
		return ultEjecucion;
	}
	public void setUltEjecucion(Date ultEjecucion) {
		this.ultEjecucion = ultEjecucion;
	}
	public Date getProxEjecucion() {
		return proxEjecucion;
	}
	public void setProxEjecucion(Date proxEjecucion) {
		this.proxEjecucion = proxEjecucion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


}