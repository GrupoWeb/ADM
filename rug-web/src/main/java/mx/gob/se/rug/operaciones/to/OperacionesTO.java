package mx.gob.se.rug.operaciones.to;

import java.util.Date;
import java.util.List;

import mx.gob.se.rug.inscripcion.to.OtorganteTO;

public class OperacionesTO {
	private int idInscripcion;
	private Integer idTipoTramite;
	private String paso;
	private String tipoTransaccion;
	private Date fechaInicio;
	private Date fechaFin;
	private String numGarantia;	
	private String estatus;	
	private Date fechaCreacionRug;	
	private String descripcionGeneral;
	private String fechaOperacionInicio;
	private boolean esMenor;
	private String idGarantiaModificar;
	private Integer idTramiteTemporal;
	private List<OtorganteTO> otorgantes;
	private String usuario;
        
        private Integer idTipoGarantia;
        
        
        public Integer getIdTipoGarantia() {
		return idTipoGarantia;
	}
        
	public void setIdTipoGarantia(Integer idTipoGarantia) {
		this.idTipoGarantia = idTipoGarantia;
	}
        
	
	public String getDescripcionGeneral() {
		return descripcionGeneral;
	}
	public void setDescripcionGeneral(String descripcionGeneral) {
		this.descripcionGeneral = descripcionGeneral;
	}
	public int getIdInscripcion() {
		return idInscripcion;
	}
	public void setIdInscripcion(int idInscripcion) {
		this.idInscripcion = idInscripcion;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getNumGarantia() {
		return numGarantia;
	}
	public void setNumGarantia(String numGarantia) {
		this.numGarantia = numGarantia;
	}
	
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	public Date getFechaCreacionRug() {
		return fechaCreacionRug;
	}
	public void setFechaCreacionRug(Date fechaCreacionRug) {
		this.fechaCreacionRug = fechaCreacionRug;
	}
	
	public void setPaso(String paso) {
		this.paso = paso;
	}
	public String getPaso() {
		return paso;
	}
	public void setFechaOperacionInicio(String fechaOperacionInicio) {
		this.fechaOperacionInicio = fechaOperacionInicio;
	}
	public String getFechaOperacionInicio() {
		return fechaOperacionInicio;
	}
	/**
	 * @param esMenor the esMenor to set
	 */
	public void setEsMenor(boolean esMenor) {
		this.esMenor = esMenor;
	}
	/**
	 * @return the esMenor
	 */
	public boolean isEsMenor() {
		return esMenor;
	}
	/**
	 * @param idGarantiaModificar the idGarantiaModificar to set
	 */
	public void setIdGarantiaModificar(String idGarantiaModificar) {
		this.idGarantiaModificar = idGarantiaModificar;
	}
	/**
	 * @return the idGarantiaModificar
	 */
	public String getIdGarantiaModificar() {
		return idGarantiaModificar;
	}
	public void setIdTramiteTemporal(Integer idTramiteTemporal) {
		this.idTramiteTemporal = idTramiteTemporal;
	}
	public Integer getIdTramiteTemporal() {
		return idTramiteTemporal;
	}
	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}
	public void setOtorgantes(List<OtorganteTO> otorgantes) {
		this.otorgantes = otorgantes;
	}
	public List<OtorganteTO> getOtorgantes() {
		return otorgantes;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
