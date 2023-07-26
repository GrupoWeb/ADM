package mx.gob.se.rug.masiva.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.resultado.to.TramiteRes;
import mx.gob.se.rug.to.PlSql;

public class ResCargaMasiva implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ResultadoCargaMasiva resultado;
	private List<PlSql> listaTramites;
	private List<PlSql> listaTramitesErroneos;
	private List<ControlError> listaErrores;
	private String regresa;
	private Integer idACreedorRepresentado;
	private Integer idArchivo;
	private Integer idArchivoXML;
	private Integer idTramiteNuevo;
	private List<TramiteRes> tramitesResultado;
	private String mensajeError;
	private ArchivoTO archivoTO;
	
	public String getMensajeError() {
		return mensajeError;
	}
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	public Integer getIdACreedorRepresentado() {
		return idACreedorRepresentado;
	}
	public void setIdACreedorRepresentado(Integer idACreedorRepresentado) {
		this.idACreedorRepresentado = idACreedorRepresentado;
	}
	public Integer getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Integer idArchivo) {
		this.idArchivo = idArchivo;
	}
	public String getRegresa() {
		return regresa;
	}
	public void setRegresa(String regresa) {
		this.regresa = regresa;
	}
	public ResultadoCargaMasiva getResultado() {
		return resultado == null ? new ResultadoCargaMasiva(): resultado ;
	}
	public void setResultado(ResultadoCargaMasiva resultado) {
		this.resultado = resultado;
	}
	public List<ControlError> getListaErrores() {
		return listaErrores == null ? new ArrayList<ControlError>(): listaErrores;
	}
	public void setListaErrores(List<ControlError> listaErrores) {
		this.listaErrores = listaErrores;
	}
	public List<TramiteRes> getTramitesResultado() {
		if (tramitesResultado == null){
			tramitesResultado = new ArrayList<TramiteRes>();
		}
		return tramitesResultado;
	}
	public void setTramitesResultado(List<TramiteRes> tramitesResultado) {
		this.tramitesResultado = tramitesResultado;
	}
	public Integer getIdTramiteNuevo() {
		return idTramiteNuevo;
	}
	public void setIdTramiteNuevo(Integer idTramiteNuevo) {
		this.idTramiteNuevo = idTramiteNuevo;
	}
	public Integer getIdArchivoXML() {
		return idArchivoXML;
	}
	public void setIdArchivoXML(Integer idArchivoXML) {
		this.idArchivoXML = idArchivoXML;
	}
	public List<PlSql> getListaTramites() {
		return listaTramites;
	}
	public void setListaTramites(List<PlSql> listaTramites) {
		this.listaTramites = listaTramites;
	}
	public List<PlSql> getListaTramitesErroneos() {
		return listaTramitesErroneos;
	}
	public void setListaTramitesErroneos(List<PlSql> listaTramitesErroneos) {
		this.listaTramitesErroneos = listaTramitesErroneos;
	}
	public ArchivoTO getArchivoTO() {
		return archivoTO;
	}
	public void setArchivoTO(ArchivoTO archivoTO) {
		this.archivoTO = archivoTO;
	}

}
