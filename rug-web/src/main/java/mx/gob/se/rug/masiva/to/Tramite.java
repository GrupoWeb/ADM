package mx.gob.se.rug.masiva.to;

import java.io.Serializable;

import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.to.UsuarioTO;

/**
 * 
 * @author Abraham Stalin Aguilar Valencia
 *
 */

public class Tramite implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer idTramite;
	private Integer idPaso;
	private Integer idTipoTramite;
	private Integer idAcreedor;
	private Integer idUsuario;
	private Integer idGarantia;
	private Integer idGarantiaPendiente;
	private String claveRastreo;
	private Integer idArchivo;
	private Integer consecutivo;
	private Integer idEstatus;
	private UsuarioTO usuario;
	private String descripcion;
	
	
	public TramiteRes getTramiteRes(){
		TramiteRes res= new TramiteRes();
		res.setClaveRastreo(claveRastreo);
		return res;
	}

	public Integer getIdGarantiaPendiente() {
		return idGarantiaPendiente;
	}
	public void setIdGarantiaPendiente(Integer idGarantiaPendiente) {
		this.idGarantiaPendiente = idGarantiaPendiente;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	public Integer getIdPaso() {
		return idPaso;
	}
	public void setIdPaso(Integer idPaso) {
		this.idPaso = idPaso;
	}
	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public Integer getIdTipoTramite() {
		return idTipoTramite;
	}
	public void setIdTipoTramite(Integer idTipoTramite) {
		this.idTipoTramite = idTipoTramite;
	}
	public Integer getIdAcreedor() {
		return idAcreedor;
	}
	public void setIdAcreedor(Integer idAcreedor) {
		this.idAcreedor = idAcreedor;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getClaveRastreo() {
		return claveRastreo;
	}
	public void setClaveRastreo(String claveRastreo) {
		this.claveRastreo = claveRastreo;
	}
	public Integer getIdGarantia() {
		return idGarantia;
	}
	public void setIdGarantia(Integer idGarantia) {
		this.idGarantia = idGarantia;
	}
	public Integer getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}
	public Integer getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Integer idArchivo) {
		this.idArchivo = idArchivo;
	}
	public Integer getIdEstatus() {
		return idEstatus;
	}
	public void setIdEstatus(Integer idEstatus) {
		this.idEstatus = idEstatus;
	}
	public UsuarioTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioTO usuario) {
		this.usuario = usuario;
	}
	
}
